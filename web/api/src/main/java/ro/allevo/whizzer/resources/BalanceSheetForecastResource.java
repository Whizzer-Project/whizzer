package ro.allevo.whizzer.resources;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import ro.allevo.fintpws.resources.BaseResource;
import ro.allevo.fintpws.util.Roles;
import ro.allevo.whizzer.model.BalanceSheetForecastEntity;


public class BalanceSheetForecastResource extends BaseResource<BalanceSheetForecastEntity> {

	public BalanceSheetForecastResource(UriInfo uriInfo, EntityManager entityManagerData, Integer id) {
		super(BalanceSheetForecastEntity.class, uriInfo, entityManagerData, id);	
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.BALANCE_SHEET_VIEW})
	public BalanceSheetForecastEntity getBalanceSheetForecast() {
		return get();
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.BALANCE_SHEET_MODIFY})
	public Response updateBalanceSheetForecast(BalanceSheetForecastEntity entity) {
		return put(entity);
	}

	@DELETE
	@RolesAllowed({Roles.BALANCE_SHEET_MODIFY})
	public Response deleteBalanceSheetForecast() {
		return delete();
	}
	
}