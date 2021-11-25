package ro.allevo.whizzer.resources;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.StoredProcedureQuery;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ro.allevo.fintpws.util.Roles;
import ro.allevo.whizzer.model.CashFlowComparisonGenDataEntity;


public class CashFlowComparisonReportResources {

	private EntityManager entityManager;
	
	public CashFlowComparisonReportResources(UriInfo uriInfo, EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	
	@SuppressWarnings("unchecked")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.BALANCE_SHEET_VIEW})
	@JsonIgnore
	public List<CashFlowComparisonGenDataEntity> callStoredProcedure(@QueryParam("entityName") String entityName, @QueryParam("date") String date) throws ParseException {
		EntityManager em = entityManager.getEntityManagerFactory().createEntityManager();
		em.getTransaction().begin();
		
		StoredProcedureQuery procedure = em.createStoredProcedureQuery("findata.getcfcomparison", CashFlowComparisonGenDataEntity.class)
				.registerStoredProcedureParameter("inentity", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("incfforecastdate", Date.class, ParameterMode.IN)
				.registerStoredProcedureParameter("outretcursor", void.class, ParameterMode.REF_CURSOR)
				.setParameter("inentity", entityName)
				.setParameter("incfforecastdate", new SimpleDateFormat("yyyy-MM-dd").parse(date)  );
		
		List<CashFlowComparisonGenDataEntity> cashflowComparison = procedure.getResultList();
		em.getTransaction().commit();
		em.close();

		return cashflowComparison;
	}
	

}
