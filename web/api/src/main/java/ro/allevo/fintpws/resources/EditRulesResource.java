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

import ro.allevo.fintpws.model.EditRulesEntity;

public class EditRulesResource extends PagedCollection<EditRulesEntity>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EditRulesResource(UriInfo uriInfo, EntityManager emc) {
		super(uriInfo,
				emc.createNamedQuery("EditRulesEntity.findAll", EditRulesEntity.class),
				emc.createNamedQuery("EditRulesEntity.findTotal", Long.class),
				emc,
				EditRulesEntity.class);
	}
	
	public EditRulesResource(UriInfo uriInfo, EntityManager emc, String msgType) {
		super(uriInfo, 
				emc.createNamedQuery("EditRulesEntity.findByTxType", EditRulesEntity.class)
				.setParameter("message_type", msgType), 
				null, 
				emc, 
				EditRulesEntity.class);
	}

	@Path(value= "{id}")
	public EditRuleResource getEditRuleById(@PathParam("id") Integer id) {
		return new EditRuleResource(getUriInfo(), getEntityManager(), id);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@JsonIgnore
	public PagedCollection<EditRulesEntity> getEditRulesAsJson() {
		return this;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	//@RolesAllowed({Roles.BANKS_LIST_MODIFY})
	public Response postEditRule(EditRulesEntity editRulesEntity) {
		return post(editRulesEntity);
	}

}
