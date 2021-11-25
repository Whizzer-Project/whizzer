package ro.allevo.whizzer.resources;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ro.allevo.fintpws.resources.PagedCollection;
import ro.allevo.fintpws.util.Roles;
import ro.allevo.whizzer.model.ProfitLossEntity;

public class ProfitLossesResource extends PagedCollection<ProfitLossEntity>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EntityManager entityManager;
	
	public ProfitLossesResource(UriInfo uriInfo, EntityManager entityManagerData) {
		super(uriInfo, 
				entityManagerData.createNamedQuery("ProfitLossEntity.findAll", ProfitLossEntity.class),
				entityManagerData.createNamedQuery("ProfitLossEntity.findTotal", Long.class),
				entityManagerData,
				ProfitLossEntity.class
		);
		this.entityManager = entityManagerData;
	}
	
	@Path("{id}")
	public ProfitLossResource getProfitLossResource(@PathParam("id") Integer id) {		
		return new ProfitLossResource(getUriInfo(), getEntityManager(), id);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.BALANCE_SHEET_VIEW})
	@JsonIgnore
	public PagedCollection<ProfitLossEntity> getProfitLossesAsJson() {
		return this;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.BALANCE_SHEET_VIEW})
	public Response postForm(ProfitLossEntity profitLossEntity) {
		return post(profitLossEntity);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.BALANCE_SHEET_VIEW})
	@JsonIgnore
	@Path("compute-bs-forecast")
	public String computebsforecast(@QueryParam("entity") String entity,
			@QueryParam("historicalbs") Integer historicalbs,
			@QueryParam("realisedbs") Integer realisedbs,
			@QueryParam("forecast") Integer forecast,
			@Context HttpServletRequest request) {
		Integer userId = (Integer) request.getSession().getAttribute("user_id");
		EntityManager em = entityManager.getEntityManagerFactory().createEntityManager();
		em.getTransaction().begin();
		StoredProcedureQuery procedure = em.createStoredProcedureQuery("findata.computebsforecast")
				.registerStoredProcedureParameter("inentity", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("inrealisebsyear", Integer.class, ParameterMode.IN)
				.registerStoredProcedureParameter("inhistoricalbsyear", Integer.class, ParameterMode.IN)
				.registerStoredProcedureParameter("inforecastbsyear", Integer.class, ParameterMode.IN)
				.registerStoredProcedureParameter("inuserid", Integer.class, ParameterMode.IN)
				.setParameter("inentity", entity)
				.setParameter("inrealisebsyear", realisedbs)
				.setParameter("inhistoricalbsyear", historicalbs)
				.setParameter("inforecastbsyear", forecast)
				.setParameter("inuserid", userId);
		procedure.execute();
		
		em.getTransaction().commit();
		em.close();

		return null;
	}
}
