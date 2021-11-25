package ro.allevo.whizzer.resources;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.persistence.StoredProcedureQuery;
import javax.servlet.http.HttpServletRequest;
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
import ro.allevo.fintpws.util.ModelUtils;
import ro.allevo.fintpws.util.Roles;
import ro.allevo.whizzer.model.CashFlowForecastEntity;


public class CashFlowForecastResources extends PagedCollection<CashFlowForecastEntity> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CashFlowForecastResources(UriInfo uriInfo, EntityManager entityManagerList) {
		super(uriInfo, 
				entityManagerList.createNamedQuery("CashFlowForecastEntity.findAll", CashFlowForecastEntity.class),
				entityManagerList.createNamedQuery("CashFlowForecastEntity.findTotal", Long.class),
				entityManagerList,
				CashFlowForecastEntity.class
				);
	}

	@Path("{id}")
	public CashFlowForecastResource getBICsResource(@PathParam("id") Integer id) {
		return new CashFlowForecastResource(getUriInfo(), getEntityManager(), id);
	}
	
	@Path("report")
	public CashFlowForecastGenDataSetsResource getcfforestProcedureCallResource() {
		String entityName = this.getUriInfo().getQueryParameters().getFirst("filter_entity_exact");
		// cfforecastdate must be passed with pattern = YYYY-MM-DD
		String cfforecastdate = this.getUriInfo().getQueryParameters().getFirst("filter_cfforecastdate_exact");
		LocalDate date = null;
		if(cfforecastdate != null && entityName != null) {
			date = LocalDate.parse(cfforecastdate);
			callStoredProcedure(entityName, date);
		}
		return new CashFlowForecastGenDataSetsResource(getUriInfo(), getEntityManager());
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.BALANCE_SHEET_VIEW})
	@JsonIgnore
	public PagedCollection<CashFlowForecastEntity> getCashFlowForecastsAsJson() {
		return this;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.BALANCE_SHEET_MODIFY})
	public Response postForm(CashFlowForecastEntity cashflowForecastEntity) {
		HttpServletRequest request = ModelUtils.getCurrentHttpRequest();
		cashflowForecastEntity.setUserid((Integer)request.getSession().getAttribute("user_id"));
		return post(cashflowForecastEntity);
	}
	
	private void callStoredProcedure(String entity, LocalDate date) {
		HashMap<String, Object> queryParams = new HashMap<>();
		queryParams.put("inentity", entity);
		queryParams.put("incfforecastdate", date);
		callNamedStoreProcedure("CashFlowForecastGenDataEntity.getcfforest", queryParams);
	}
	
	private void callNamedStoreProcedure(String nameOfProcedure, HashMap<String, Object> queryParams) {		
		EntityManager em = null;
		try {
			em = super.getEntityManager().getEntityManagerFactory().createEntityManager();
			StoredProcedureQuery query = em.createNamedStoredProcedureQuery(nameOfProcedure);	
			
			for(Map.Entry<String, Object> entry : queryParams.entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				query.setParameter(key , value);
			}
			
			em.getTransaction().begin();
			query.execute();
			em.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(em != null && em.isOpen()) {
				em.close();
			}		
		}
	}

}
