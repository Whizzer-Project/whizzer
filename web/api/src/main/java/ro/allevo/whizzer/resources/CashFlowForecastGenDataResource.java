package ro.allevo.whizzer.resources;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import ro.allevo.fintpws.resources.BaseResource;
import ro.allevo.fintpws.util.Roles;
import ro.allevo.whizzer.model.CashFlowForecastGenDataEntity;

public class CashFlowForecastGenDataResource extends BaseResource<CashFlowForecastGenDataEntity> {

	public CashFlowForecastGenDataResource(UriInfo uriInfo, EntityManager entityManagerData, Integer entityUniqueId) {
		super(CashFlowForecastGenDataEntity.class, uriInfo, entityManagerData, entityUniqueId);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.BALANCE_SHEET_VIEW})
	public CashFlowForecastGenDataEntity getBalanceSheet() {
		return get();
	}

}
