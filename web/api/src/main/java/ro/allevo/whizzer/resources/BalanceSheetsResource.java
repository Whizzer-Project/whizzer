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
import ro.allevo.whizzer.model.BalanceSheetEntity;

public class BalanceSheetsResource extends PagedCollection<BalanceSheetEntity>{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BalanceSheetsResource(UriInfo uriInfo, EntityManager entityManagerData) {
		super(uriInfo, 
				entityManagerData.createNamedQuery("BalanceSheetEntity.findAll", BalanceSheetEntity.class),
				entityManagerData.createNamedQuery("BalanceSheetEntity.findTotal", Long.class),
				entityManagerData,
				BalanceSheetEntity.class
		);
	}
	
	@Path("{id}")
	public BalanceSheetResource getBalanceSheetResource(@PathParam("id") Integer id) {		
		return new BalanceSheetResource(getUriInfo(), getEntityManager(), id);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@JsonIgnore
	@RolesAllowed({Roles.BALANCE_SHEET_VIEW})
	public PagedCollection<BalanceSheetEntity> getBalanceSheetsAsJson() {
		return this;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.BALANCE_SHEET_MODIFY})
	public Response postForm(BalanceSheetEntity balanceSheetEntity) {
		return post(balanceSheetEntity);
	}

}
