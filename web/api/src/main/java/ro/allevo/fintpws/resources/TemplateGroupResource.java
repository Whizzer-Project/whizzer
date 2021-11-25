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

import ro.allevo.fintpws.model.TxTemplatesGroupsEntity;

public class TemplateGroupResource extends BaseResource<TxTemplatesGroupsEntity> {

	public TemplateGroupResource(UriInfo uriInfo, EntityManager entityManager, Integer groupId) {
		super(TxTemplatesGroupsEntity.class, uriInfo, entityManager, groupId);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public TxTemplatesGroupsEntity getTemplateGroup() {
		return get();
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateTemplateGroup(TxTemplatesGroupsEntity templateGroup) {
		return put(templateGroup);
	}
	
	@DELETE
	public Response deleteTemplateGroup() {
		return delete();
	}
}
