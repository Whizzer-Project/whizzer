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

import ro.allevo.fintpws.model.EditRulesEntity;

public class EditRuleResource extends BaseResource<EditRulesEntity>{

	public EditRuleResource(UriInfo uriInfo, EntityManager entityManager, Integer id) {
		super(EditRulesEntity.class, uriInfo, entityManager, id);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public EditRulesEntity getEditRulesEntity() {
		return get();
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateEditRulesEntity(EditRulesEntity editRulesEntity) {
		return put(editRulesEntity);
	}

	@DELETE
	public Response deleteEditRulesEntity() {
		return delete();
	}
}
