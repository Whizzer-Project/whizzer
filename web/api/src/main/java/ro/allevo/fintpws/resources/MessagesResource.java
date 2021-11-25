/*
 * FinTP - Financial Transactions Processing Application
 * Copyright (C) 2013 Business Information Systems (Allevo) S.R.L.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>
 * or contact Allevo at : 031281 Bucuresti, 23C Calea Vitan, Romania,
 * phone +40212554577, office@allevo.ro <mailto:office@allevo.ro>, www.allevo.ro.
 */

package ro.allevo.fintpws.resources;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ro.allevo.fintpws.model.EntryQueueEntity;
import ro.allevo.fintpws.model.QueueEntity;
import ro.allevo.fintpws.model.RoutedMessageEntity;
import ro.allevo.fintpws.util.URIFilter;
import ro.allevo.fintpws.util.URIFilter.URIFilterType;
import ro.allevo.fintpws.util.enums.MessageTypeToViews;

/**
 * Resource class implementing /messages path methods and acting as
 * /messages/{id} sub-resource locator.
 * 
 * @author horia
 * @version $Revision: 1.0 $
 */
public class MessagesResource extends PagedCollection<RoutedMessageEntity> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8690995620534091765L;
	private QueueEntity queueEntity = null;
	private UriInfo uriInfo;
	private EntityManager entityManagerData;
	public MessagesResource(UriInfo uriInfo, EntityManager entityManagerData, QueueEntity queueEntity) {
		super(uriInfo, 
				entityManagerData.createNamedQuery("RoutedMessageEntity.findAllQueue", RoutedMessageEntity.class)
					.setParameter("queuename", queueEntity.getName()),
				entityManagerData.createNamedQuery("RoutedMessageEntity.findTotalQueue", Long.class)
					.setParameter("queuename", queueEntity.getName()),
				entityManagerData,
				RoutedMessageEntity.class
				);
		
		this.queueEntity = queueEntity;
	}
	public MessagesResource(UriInfo uriInfo, EntityManager entityManagerData) {
		super(uriInfo, entityManagerData, RoutedMessageEntity.class);
		this.entityManagerData = entityManagerData;
		this.uriInfo = uriInfo;
	}

	/**
	 * Returns a message sub-resource with id messageId
	 * 
	 * @param messageGuid
	 *            String
	 * @return MessageResource The message sub-resource
	 */
	@Path("{messageId}")
	public MessageResource getMessage(@PathParam("messageId") String messageGuid) {
		messageGuid = Jsoup.clean(messageGuid, Whitelist.none());
		return new MessageResource(getUriInfo(), getEntityManager(), messageGuid);
	}
	
	@Path("entry-queue/{messageId}")
	public EntryQueueResource getEntryQueue(@PathParam("messageId") String messageGuid) {
		return new EntryQueueResource(getUriInfo(), getEntityManager(), messageGuid);
	}
	
	
	@Path("by-correlation-id/{id}")
	public MessageResource getMessageByCorrelationId(@PathParam("id") String correlationId) {
		correlationId = Jsoup.clean(correlationId, Whitelist.none());
		return MessageResource.getByCorrelationId(getUriInfo(), entityManagerData, correlationId);
	}
	
	@Path("by-message-type/{messageType}")
	public MessagesByMessageTypeResource getMessagesByMessageType(@PathParam("messageType") String messageTypeValue) {
		messageTypeValue = Jsoup.clean(messageTypeValue, Whitelist.none());
		
		MessageTypeToViews messageType = MessageTypeToViews.get(getEntityManager(), messageTypeValue);
		
		if (null != queueEntity)
			return new MessagesByMessageTypeResource(getUriInfo(), getEntityManager(), 
					messageType, new URIFilter[] {new URIFilter("queuename", URIFilterType.FILTER_TYPE_EXACT, queueEntity.getName())});
		
		return new MessagesByMessageTypeResource(getUriInfo(), getEntityManager(), messageType);
	}

	/**
	 * GET method : returns an application/json formatted list of messages
	 * 
	 * @return JSONObject The list of messages
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@JsonIgnore
	public PagedCollection<RoutedMessageEntity> getMessagesAsJson(@Context HttpServletRequest request, @QueryParam("type") String type) {
		setRequest(request);
		return new PagedCollection<>(uriInfo,
				entityManagerData.createNamedQuery("RoutedMessageEntity.findAll", RoutedMessageEntity.class),
				entityManagerData.createNamedQuery("RoutedMessageEntity.findTotal", Long.class),
				entityManagerData,
				RoutedMessageEntity.class
				);
	}

	/**
	 * POST method : creates a message
	 * 
	 * @param jsonEntity
	 *            JSONObject The message to be created
	 * @return Response The URI of the newly created message * @throws
	 *         JSONException
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postForm(EntryQueueEntity entity) {
		//insert in EntryQueueEntity
		return post(entity, true);
	}
}
