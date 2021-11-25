package ro.allevo.fintpws.resources;

import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import ro.allevo.fintpws.model.EntryQueueEntity;

public class EntryQueueResource extends BaseResource<EntryQueueEntity> {

	public EntryQueueResource(UriInfo uriInfo, EntityManager entityManager, String correlationId) {
		super(EntryQueueEntity.class, uriInfo, entityManager, correlationId);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public EntryQueueEntity getEntryQueue() {
		return get();
	}

}
