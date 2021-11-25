package ro.allevo.fintpws.resources;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import ro.allevo.fintpws.model.TxTemplatesConfigEntity;
import ro.allevo.fintpws.util.Roles;

public class TemplateConfigResource extends BaseResource<TxTemplatesConfigEntity>{

	public TemplateConfigResource(UriInfo uriInfo, EntityManager entityManager, Integer templateId) {
		super(TxTemplatesConfigEntity.class, uriInfo, entityManager, templateId);
	}

	@GET
	@SuppressWarnings("resource")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.PAYMENT_TEMPLATE_CONFIGURATION_VIEW})
	public Response getTemplateConfig(@QueryParam("validationxsd") String validationxsd) {
		if (null == validationxsd) {
			return Response.ok(get()).build();
		}else {
			ValidationXsdResource validation = new ValidationXsdResource(getUriInfo(), getEntityManager(), get().getId());
			return validation.getTemplateSmallConfig();
		}
		
	}
	
	@Path("validationxsd")
	public ValidationXsdResource getValidation() {
		return new ValidationXsdResource(getUriInfo(), getEntityManager(), get().getId());
	}
	
	@Path("fields")
	public TemplatesConfigDetailedsResource getTemplateConfigDetailed() {
		return new TemplatesConfigDetailedsResource(getUriInfo(), getEntityManager(), get());
	}
	
	@PUT
	@RolesAllowed({Roles.PAYMENT_TEMPLATE_CONFIGURATION_MODIFY})
	@Consumes(MediaType.APPLICATION_JSON)
	public void postTemplateConfig(TxTemplatesConfigEntity templateConfigEntity){
		put(templateConfigEntity);
	}
	
}
