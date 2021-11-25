package ro.allevo.tracker.resources;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import ro.allevo.fintpws.util.Roles;
import ro.allevo.tracker.model.ComponentPerformanceEntity;

public class ComponentResource extends TraceResource {

	public ComponentResource(EntityManager entityManager) {
		super(entityManager);
	}
	
	@GET
	@Path("{timestamp}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<ComponentPerformanceEntity> get(@PathParam("timestamp") String timestamp,
			@DefaultValue("") @QueryParam("name") String name,
			@DefaultValue("") @QueryParam("thread") String thread) {
		String[] formattedDate = timestamp.split(" ");
		
		TypedQuery<ComponentPerformanceEntity> query = getEntityManager().createNamedQuery("ComponentPerformanceEntity.findByTimestamp", ComponentPerformanceEntity.class)
				.setParameter("date", formattedDate[0])
				.setParameter("time", formattedDate[1])
				.setParameter("name", name)
				.setParameter("thread", thread);
		
		
		List<ComponentPerformanceEntity> result = query.getResultList();
		
		return result;
	}
	
	@GET
	@Path("timestamps")
	@Produces(MediaType.APPLICATION_JSON)
	public List<ComponentPerformanceEntity> getIntervals(@QueryParam("date") Date date) {
		TypedQuery<String> query = getEntityManager().createNamedQuery("ComponentPerformanceEntity.findTimestampsByDate", String.class)
				.setParameter("date", date);//yyyy-mm-dd
		
		List<String> timestamps = query.getResultList();
		
		List<ComponentPerformanceEntity> result = new ArrayList<ComponentPerformanceEntity>() {{
			for (String time : timestamps)
				add(new ComponentPerformanceEntity() {{
					setDate(date);
					setTime(time);
				}});
		}};
		
		return result;
	}
	
	@GET
	@Path("trace")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.TRACKER_VIEW})
	public List<ComponentPerformanceEntity> trace(@Context HttpServletRequest request) {
		Integer userId = (Integer)request.getSession().getAttribute("user_id");
		
		EntityManager em = startTrace(userId);
		
		em.getTransaction().begin();
		//collect
		StoredProcedureQuery procedure = em.createStoredProcedureQuery("fintrack.collectcomponentperformance")
				.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN)
				.registerStoredProcedureParameter(2, String.class, ParameterMode.OUT)
				.setParameter(1, userId);
		procedure.execute();
		String timestamp = (String)procedure.getOutputParameterValue(2);
		
		em.getTransaction().commit();
		
		em.close();
		
		return get(timestamp, "", "");
	}

}
