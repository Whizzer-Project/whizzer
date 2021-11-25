package ro.allevo.fintpws.resources;

import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import ro.allevo.fintpws.model.MsgTypeListEntity;

public class MessageTypeResource extends BaseResource<MsgTypeListEntity> {

	public MessageTypeResource(UriInfo uriInfo, EntityManager entityManager, String messageType) {
		super(MsgTypeListEntity.class, uriInfo, entityManager, messageType);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public MsgTypeListEntity getMessageType() {
		return get();
	}

}
