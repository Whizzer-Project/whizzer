package ro.allevo.fintpws.resources;

import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import ro.allevo.fintpws.model.BusinessAreaEntity;
import ro.allevo.fintpws.util.enums.BusinessAreaToViews;

public class BusinessAreaResource extends BaseResource<BusinessAreaEntity> {
	
	public BusinessAreaResource(UriInfo uriInfo, EntityManager entityManager, String businessArea) {
		super(BusinessAreaEntity.class, uriInfo, entityManager,
			entityManager.createNamedQuery("BusinessAreaEntity.findById", BusinessAreaEntity.class)
				.setParameter("id", businessArea)
				.setFirstResult(0).setMaxResults(1)//use query to limit number of results
			);
	}

	@Path("messages")
	public MessagesByBusinessAreaResource getMessages() {
		BusinessAreaToViews businessArea = BusinessAreaToViews.get(getEntityManager(), get().getName());
		
		return new MessagesByBusinessAreaResource(getUriInfo(), businessArea, true);
	}
	
	@Path("message-types")
	public MessageTypesResource getMessageTypes() {
		return new MessageTypesResource(getUriInfo(), getEntityManager(), get());
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public BusinessAreaEntity getEntity() {
		return get();
	}
}
