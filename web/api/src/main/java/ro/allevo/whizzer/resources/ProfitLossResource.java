package ro.allevo.whizzer.resources;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import ro.allevo.fintpws.resources.BaseResource;
import ro.allevo.fintpws.util.Roles;
import ro.allevo.whizzer.model.ProfitLossEntity;

public class ProfitLossResource extends BaseResource<ProfitLossEntity> {
		
	public ProfitLossResource(UriInfo uriInfo, EntityManager emd, Integer id) {
		super(ProfitLossEntity.class, uriInfo, emd, id);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.BALANCE_SHEET_VIEW})
	public ProfitLossEntity getProfitLoss() {
		return get();
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.BALANCE_SHEET_MODIFY})
	public Response updateProfitLoss(ProfitLossEntity entity) {
		return put(entity);
	}

}
