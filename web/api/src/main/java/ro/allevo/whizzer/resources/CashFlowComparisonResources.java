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
import ro.allevo.whizzer.model.CashFlowComparisonEntity;


public class CashFlowComparisonResources extends PagedCollection<CashFlowComparisonEntity> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CashFlowComparisonResources(UriInfo uriInfo, EntityManager entityManagerData) {
		super(uriInfo, 
				entityManagerData.createNamedQuery("CashFlowComparisonEntity.findAll", CashFlowComparisonEntity.class),
				entityManagerData.createNamedQuery("CashFlowComparisonEntity.findTotal", Long.class),
				entityManagerData,
				CashFlowComparisonEntity.class
				);
	}

	@Path("{id}")
	public CashFlowComparisonResource getBICsResource(@PathParam("id") Integer id) {
		return new CashFlowComparisonResource(getUriInfo(), getEntityManager(), id);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.BALANCE_SHEET_MODIFY})
	@JsonIgnore
	public PagedCollection<CashFlowComparisonEntity> getCashFlowComparisonsAsJson() {
		return this;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.BALANCE_SHEET_MODIFY})
	public Response postForm(CashFlowComparisonEntity cashflowComparisonEntity) {
		return post(cashflowComparisonEntity);
	}

}
