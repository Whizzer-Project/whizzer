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
import ro.allevo.whizzer.model.BalanceSheetEntity;

public class BalanceSheetResource extends BaseResource<BalanceSheetEntity>{
		
	public BalanceSheetResource(UriInfo uriInfo, EntityManager emd, Integer id) {
		super(BalanceSheetEntity.class, uriInfo, emd, id);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.BALANCE_SHEET_VIEW})
	public BalanceSheetEntity getBalanceSheet() {
		return get();
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.BALANCE_SHEET_MODIFY})
	public Response updateBalanceSheet(BalanceSheetEntity entity) {
		return put(entity);
	}

}
