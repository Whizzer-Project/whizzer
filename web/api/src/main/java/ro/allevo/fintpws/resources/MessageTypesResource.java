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
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ro.allevo.fintpws.model.BusinessAreaEntity;
import ro.allevo.fintpws.model.MsgTypeListEntity;
import ro.allevo.fintpws.model.QueueEntity;

/**
 * @author costi
 * @version $Revision: 1.0 $
 */
public class MessageTypesResource extends PagedCollection<MsgTypeListEntity> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7055952938448301859L;

	public MessageTypesResource(UriInfo uriInfo, EntityManager entityManagerData,
			BusinessAreaEntity businessAreaEntity) {
		super(uriInfo,
				entityManagerData.createNamedQuery("MsgTypeListEntity.findByBusinessArea", MsgTypeListEntity.class)
						.setParameter("businessarea", businessAreaEntity.getName()),
				null, entityManagerData, MsgTypeListEntity.class);

		disablePaging();
	}

	public MessageTypesResource(UriInfo uriInfo, EntityManager entityManagerData, QueueEntity queueEntity) {
		super(uriInfo,
				entityManagerData
						.createNamedQuery("EntryQueueEntity.findDistinctMessagesQueue", MsgTypeListEntity.class)
						.setParameter("queuename", queueEntity.getName()),
				entityManagerData.createNamedQuery("EntryQueueEntity.findTotalDistinctMessagesQueue", Long.class)
						.setParameter("queuename", queueEntity.getName()),
				entityManagerData, MsgTypeListEntity.class);
	}

	public MessageTypesResource(UriInfo uriInfo, EntityManager entityManagerConfig) {
		super(uriInfo, entityManagerConfig.createNamedQuery("MsgTypeListEntity.findAll", MsgTypeListEntity.class),
				entityManagerConfig.createNamedQuery("MsgTypeListEntity.findTotal", Long.class), entityManagerConfig,
				MsgTypeListEntity.class);
	}

	/**
	 * GET method : returns an application/json formatted list of messages
	 * 
	 * @return JSONObject The list of messages
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@JsonIgnore
	public PagedCollection<MsgTypeListEntity> getMessageTypes() {
		// authorization
		/*
		 * if (!RolesUtils.hasReadAuthorityOnQueue(queueEntity)) { throw new
		 * AccessDeniedException("forbidden"); }
		 */

		return this;
	}

	@Path("{messageType}")
	public MessageTypeResource getByName(@PathParam("messageType") String messageType) {
		return new MessageTypeResource(getUriInfo(), getEntityManager(), messageType);
	}

	/**
	 * Returns the resource formatted as json
	 * 
	 * @return JSONObject * @throws JSONException
	 */
	/*
	 * @SuppressWarnings("unchecked") public JSONObject asJson() throws
	 * JSONException { final JSONObject messageTypesAsJson =
	 * pagedCollection.asJson(); final JSONArray jsonArray = new JSONArray(); //
	 * fill data
	 * 
	 * List<MsgTypeListEntity> items = (List<MsgTypeListEntity>)
	 * pagedCollection.getItems();
	 * 
	 * TypedQuery<MsgTypeListEntity> query = entityManagerData
	 * .createNamedQuery("EntryQueueEntity.findMessagesTypesQueue",
	 * MsgTypeListEntity.class).setParameter("queuename", queueEntity.getName());
	 * List<MsgTypeListEntity> results = query.getResultList();
	 * 
	 * for (MsgTypeListEntity messagetype : items) { JSONObject msgtypeasJson =
	 * pagedCollection.asJson(); msgtypeasJson.put("messagetype",
	 * messagetype.getMessagetype()); msgtypeasJson.put("friendlyname",
	 * messagetype.getFriendlyname()); msgtypeasJson.put("storage",
	 * messagetype.getStorage()); msgtypeasJson.put("businessarea",
	 * messagetype.getBusinessarea()); msgtypeasJson.put("reportingstorage",
	 * messagetype.getReportingstorage()); msgtypeasJson.put("parentmsgtype",
	 * messagetype.getParentmsgtype());
	 * 
	 * jsonArray.put(msgtypeasJson); } // mark messages without type as undefined if
	 * (results.contains(null)) { JSONObject nullEntity = pagedCollection.asJson();
	 * nullEntity.put("messagetype", "undefined"); jsonArray.put(nullEntity);
	 * 
	 * } messageTypesAsJson.put("messagetypes", jsonArray);
	 * 
	 * return messageTypesAsJson; }
	 */

}
