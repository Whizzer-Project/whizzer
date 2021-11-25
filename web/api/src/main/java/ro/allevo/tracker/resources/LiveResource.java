package ro.allevo.tracker.resources;

import java.sql.Date;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import ro.allevo.fintpws.util.Roles;
import ro.allevo.tracker.model.LivePerformanceEntity;

public class LiveResource extends TraceResource {
	
	public LiveResource(UriInfo uriInfo, EntityManager entityManager) {
		super(entityManager);
	}
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public LivePerformanceEntity get(@PathParam("id") int id) {
		TypedQuery<LivePerformanceEntity> query = getEntityManager().createNamedQuery("LivePerformanceEntity.findById", LivePerformanceEntity.class)
				.setParameter("id", id);
		
		LivePerformanceEntity result = query.getSingleResult();
		
		return result;
	}
	
	@GET
	@Path("intervals")
	@Produces(MediaType.APPLICATION_JSON)
	public List<LivePerformanceEntity> getIntervals(@QueryParam("date") Date date) {
		TypedQuery<LivePerformanceEntity> query = getEntityManager().createNamedQuery("LivePerformanceEntity.findIntervalsByDate", LivePerformanceEntity.class)
				.setParameter("date", date);//yyyy-mm-dd
		
		List<LivePerformanceEntity> intervals = query.getResultList();
		
		return intervals;
	}
	
	@GET
	@Path("trace")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.TRACKER_VIEW})
	public LivePerformanceEntity trace(@Context HttpServletRequest request, @QueryParam("traceTime") Integer traceTime) {
		Integer userId = (Integer)request.getSession().getAttribute("user_id");
		
		EntityManager em = startTrace(userId);
		
		em.getTransaction().begin();
		//collect
		StoredProcedureQuery procedure = em.createStoredProcedureQuery("fintrack.collectrjliveperformance")
				.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN)
				.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN)
				.registerStoredProcedureParameter(3, Integer.class, ParameterMode.OUT)
				.setParameter(1, traceTime)
				.setParameter(2, userId);
		procedure.execute();
		Integer rowId = (Integer)procedure.getOutputParameterValue(3);
		em.getTransaction().commit();
		
		em.close();
		
		return get(rowId);
	}

	
}
