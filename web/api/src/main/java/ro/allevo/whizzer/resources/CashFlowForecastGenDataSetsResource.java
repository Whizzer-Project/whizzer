package ro.allevo.whizzer.resources;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ro.allevo.fintpws.resources.PagedCollection;
import ro.allevo.fintpws.util.Roles;
import ro.allevo.whizzer.model.CashFlowForecastGenDataEntity;

public class CashFlowForecastGenDataSetsResource extends PagedCollection<CashFlowForecastGenDataEntity>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public CashFlowForecastGenDataSetsResource(UriInfo uriInfo, EntityManager entityManagerData) {
		super(uriInfo, 
				entityManagerData.createNamedQuery("CashFlowForecastGenDataEntity.findAll", CashFlowForecastGenDataEntity.class),
				entityManagerData.createNamedQuery("CashFlowForecastGenDataEntity.findTotal", Long.class),
				entityManagerData,
				CashFlowForecastGenDataEntity.class
		);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@JsonIgnore
	@RolesAllowed({Roles.BALANCE_SHEET_VIEW})
	public PagedCollection<CashFlowForecastGenDataEntity> getCashFlowForecastGenDataAsJson() {
		return this;
	}
	
	@Path("{id}")
	public CashFlowForecastGenDataResource getBalanceSheetResource(@PathParam("id") Integer id) {
		return new CashFlowForecastGenDataResource(getUriInfo(), getEntityManager(), id);
	}
	
}
