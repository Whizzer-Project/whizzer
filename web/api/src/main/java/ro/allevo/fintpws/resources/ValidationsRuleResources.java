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

import ro.allevo.fintpws.model.ValidationsRulesEntity;

public class ValidationsRuleResources extends BaseResource<ValidationsRulesEntity> {
	
	public ValidationsRuleResources(UriInfo uriInfo, EntityManager entityManager, Integer id) {
		super(ValidationsRulesEntity.class, uriInfo, entityManager, id);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ValidationsRulesEntity getValidationsRulesEntity() {
		return get();
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateValidationsRulesEntity(ValidationsRulesEntity validationsRulesEntity) {
		return put(validationsRulesEntity);
	}
	
	@DELETE
	public Response deleteValidationsRulesEntity() {
		return delete();
	}

}
