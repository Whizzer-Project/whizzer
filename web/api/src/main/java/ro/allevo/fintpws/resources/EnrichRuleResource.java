package ro.allevo.fintpws.resources;

import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import ro.allevo.fintpws.model.EnrichRulesEntity;

public class EnrichRuleResource extends BaseResource<EnrichRulesEntity> {

	public EnrichRuleResource(UriInfo uriInfo, EntityManager entityManager, Integer id) {
		super(EnrichRulesEntity.class, uriInfo, entityManager, id);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public EnrichRulesEntity getEnrichEntity() {
		return get();
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateEnrichEntity(EnrichRulesEntity enrichEntity) {
		return put(enrichEntity);
	}
	
	@DELETE
	public Response deleteEnrichEntity() {
		return delete();
	}

}
