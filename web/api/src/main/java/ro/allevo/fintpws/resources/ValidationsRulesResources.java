package ro.allevo.fintpws.resources;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ro.allevo.fintpws.model.ValidationsRulesEntity;
import ro.allevo.fintpws.util.Roles;

public class ValidationsRulesResources extends PagedCollection<ValidationsRulesEntity> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ValidationsRulesResources(UriInfo uriInfo, EntityManager emc) {
		super(uriInfo,
				emc.createNamedQuery("ValidationsRulesEntity.findAll", ValidationsRulesEntity.class),
				emc.createNamedQuery("ValidationsRulesEntity.findTotal", Long.class),
				emc,
				ValidationsRulesEntity.class);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@JsonIgnore
	public PagedCollection<ValidationsRulesEntity> getValidationsRulesAsJson() {
		return this;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.PAYMENT_TEMPLATE_CONFIGURATION_MODIFY})
	public Response postValidationRule(ValidationsRulesEntity validationsRulesEntity) {
		return post(validationsRulesEntity);
	}
	
	@Path(value= "{id}")
	public ValidationsRuleResources getValidationsRuleById(@PathParam("id") Integer id) {
		return new ValidationsRuleResources(getUriInfo(), getEntityManager(), id);
	}
}
