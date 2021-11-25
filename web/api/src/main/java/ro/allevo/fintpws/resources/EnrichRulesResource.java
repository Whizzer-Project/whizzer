package ro.allevo.fintpws.resources;

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

import ro.allevo.fintpws.model.EnrichRulesEntity;

public class EnrichRulesResource extends PagedCollection<EnrichRulesEntity>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EnrichRulesResource(UriInfo uriInfo, EntityManager emc) {
		super(uriInfo,
				emc.createNamedQuery("EnrichRulesEntity.findAll", EnrichRulesEntity.class),
				emc.createNamedQuery("EnrichRulesEntity.findTotal", Long.class),
				emc,
				EnrichRulesEntity.class);
	}
	
	@Path(value= "{id}")
	public EnrichRuleResource getEnrichById(@PathParam("id") Integer id) {
		return new EnrichRuleResource(getUriInfo(), getEntityManager(), id);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@JsonIgnore
	public PagedCollection<EnrichRulesEntity> getEnrichAsJson() {
		return this;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	//@RolesAllowed({Roles.BANKS_LIST_MODIFY})
	public Response postEnrich(EnrichRulesEntity enrichEntity) {
		return post(enrichEntity);
	}


}
