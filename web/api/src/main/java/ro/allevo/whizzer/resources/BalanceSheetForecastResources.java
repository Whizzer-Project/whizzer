package ro.allevo.whizzer.resources;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ro.allevo.fintpws.resources.PagedCollection;
import ro.allevo.fintpws.security.RolesUtils;
import ro.allevo.fintpws.util.Roles;
import ro.allevo.whizzer.model.BalanceSheetForecastEntity;

public class BalanceSheetForecastResources extends PagedCollection<BalanceSheetForecastEntity> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EntityManager entityManagerData;
	private UriInfo uriInfo;
	
	public BalanceSheetForecastResources(UriInfo uriInfo, EntityManager entityManagerData) {
		super(uriInfo, entityManagerData, BalanceSheetForecastEntity.class);
		this.uriInfo = uriInfo;
		this.entityManagerData = entityManagerData;
	}

	@Path("{id}")
	public BalanceSheetForecastResource getBICsResource(@PathParam("id") Integer id) {
		return new BalanceSheetForecastResource(getUriInfo(), getEntityManager(), id);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.BALANCE_SHEET_MODIFY})
	@JsonIgnore
	public PagedCollection<BalanceSheetForecastEntity> getBalanceSheetForecastAsJson(@Context HttpServletRequest request) {
		RolesUtils utils = new RolesUtils();
		return new PagedCollection<BalanceSheetForecastEntity>(uriInfo,
				entityManagerData.createNamedQuery("BalanceSheetForecastEntity.findAll", BalanceSheetForecastEntity.class), 
				entityManagerData.createNamedQuery("BalanceSheetForecastEntity.findTotal", Long.class),
				entityManagerData,
				BalanceSheetForecastEntity.class,
				utils.getFilters(request, "whizzer", this.entityManagerData, 6)
				);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.BALANCE_SHEET_MODIFY})
	public Response postForm(BalanceSheetForecastEntity balanceSheetForecastEntity) {
		return post(balanceSheetForecastEntity);
	}

}