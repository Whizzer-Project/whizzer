package ro.allevo.whizzer.resources;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ro.allevo.fintpws.resources.PagedCollection;
import ro.allevo.fintpws.security.RolesUtils;
import ro.allevo.fintpws.util.Roles;
import ro.allevo.whizzer.model.BalanceSheetReportEntity;


public class BalanceSheetReportResource extends PagedCollection<BalanceSheetReportEntity> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EntityManager entityManagerData;
	private UriInfo uriInfo;
	
	public BalanceSheetReportResource(UriInfo uriInfo, EntityManager entityManagerData) {
		super(uriInfo, entityManagerData, BalanceSheetReportEntity.class);
		this.uriInfo = uriInfo;
		this.entityManagerData = entityManagerData;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.BALANCE_SHEET_VIEW})
	@JsonIgnore
	public PagedCollection<BalanceSheetReportEntity> getBalanceSheetAsJson(@Context HttpServletRequest request) {
		RolesUtils utils = new RolesUtils();
		return new PagedCollection<BalanceSheetReportEntity>(uriInfo,
				entityManagerData.createNamedQuery("BalanceSheetReportEntity.findAll", BalanceSheetReportEntity.class), 
				entityManagerData.createNamedQuery("BalanceSheetReportEntity.findTotal", Long.class),
				entityManagerData,
				BalanceSheetReportEntity.class,
				utils.getFilters(request, "whizzer", this.entityManagerData, 6)
				);
	}
	
}
