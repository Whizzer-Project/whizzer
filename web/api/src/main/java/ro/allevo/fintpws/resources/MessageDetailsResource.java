package ro.allevo.fintpws.resources;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import ro.allevo.fintpws.util.enums.MessageTypeToViews;

public class MessageDetailsResource extends BaseResource {

	public MessageDetailsResource(UriInfo uriInfo, EntityManager entityManagerData, String messageId, MessageTypeToViews messageType) {
		super(uriInfo, entityManagerData, messageType, messageId);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Object getMessage(@Context HttpServletRequest request) {
		setRequest(request);
		
		return getObject();
	}
}
