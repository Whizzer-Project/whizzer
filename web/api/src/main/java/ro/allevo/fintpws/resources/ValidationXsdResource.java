package ro.allevo.fintpws.resources;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import ro.allevo.fintpws.model.TxTemplatesConfigEntity;
import ro.allevo.fintpws.model.TxTemplatesSmallConfigEntity;
import ro.allevo.fintpws.util.Roles;

public class ValidationXsdResource extends BaseResource<TxTemplatesSmallConfigEntity>{

	public ValidationXsdResource(UriInfo uriInfo, EntityManager entityManager, Integer templateId) {
		super(TxTemplatesSmallConfigEntity.class, uriInfo, entityManager, templateId);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.PAYMENT_TEMPLATE_CONFIGURATION_VIEW})
	public Response getTemplateSmallConfig() {
		return Response.ok(get()).build();
	}
	
}
