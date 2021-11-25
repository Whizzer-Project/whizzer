package ro.allevo.whizzer.resources;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import ro.allevo.fintpws.resources.BaseResource;
import ro.allevo.fintpws.util.ModelUtils;
import ro.allevo.fintpws.util.Roles;
import ro.allevo.whizzer.model.CashFlowForecastEntity;


public class CashFlowForecastResource extends BaseResource<CashFlowForecastEntity> {

	public CashFlowForecastResource(UriInfo uriInfo, EntityManager entityManagerData, Integer id) {
		super(CashFlowForecastEntity.class, uriInfo, entityManagerData, id);	
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.BALANCE_SHEET_VIEW})
	public CashFlowForecastEntity getCashFlowForecast() {
		return get();
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.BALANCE_SHEET_MODIFY})
	public Response updateCashFlowForecast(CashFlowForecastEntity entity) {
		HttpServletRequest request = ModelUtils.getCurrentHttpRequest();
		entity.setUserid((Integer)request.getSession().getAttribute("user_id"));
		return put(entity);
	}

	@DELETE
	@RolesAllowed({Roles.BALANCE_SHEET_MODIFY})
	public Response deleteCashFlowForecast() {
		return delete();
	}
	
}