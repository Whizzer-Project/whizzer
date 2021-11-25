package ro.allevo.fintpws.resources;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

public class BusinessAreasResource {

	private final UriInfo uriInfo;
	
	private final EntityManager entityManager;
	
	public BusinessAreasResource(UriInfo uriInfo, EntityManager entityManagerConfig) {
		this.uriInfo = uriInfo;
		this.entityManager = entityManagerConfig;
	}
	
	@Path("{name}")
	public BusinessAreaResource getByName(@PathParam("name") String businessArea) {
		return new BusinessAreaResource(uriInfo, entityManager, businessArea);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<String> getBusinessAreas() {
		TypedQuery<String> query = entityManager.createNamedQuery("BusinessAreaEntity.findAll", String.class);
		
		return query.getResultList();
	}

}
