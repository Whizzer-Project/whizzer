package ro.allevo.whizzer.resources;

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

import ro.allevo.fintpws.resources.PagedCollection;
import ro.allevo.fintpws.util.Roles;
import ro.allevo.whizzer.model.BalanceSheetForecastComparisonEntity;

public class BalanceSheetComparisonResources extends PagedCollection<BalanceSheetForecastComparisonEntity>{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BalanceSheetComparisonResources(UriInfo uriInfo, EntityManager entityManagerData) {
		super(uriInfo, 
				entityManagerData.createNamedQuery("BalanceSheetForecastComparisonEntity.findAll", BalanceSheetForecastComparisonEntity.class),
				entityManagerData.createNamedQuery("BalanceSheetForecastComparisonEntity.findTotal", Long.class),
				entityManagerData,
				BalanceSheetForecastComparisonEntity.class
		);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@JsonIgnore
	@RolesAllowed({Roles.BALANCE_SHEET_VIEW})
	public PagedCollection<BalanceSheetForecastComparisonEntity> getBalanceSheetsAsJson() {
		return this;
	}

}
