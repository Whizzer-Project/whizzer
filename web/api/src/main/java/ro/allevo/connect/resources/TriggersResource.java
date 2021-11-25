package ro.allevo.connect.resources;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
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

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ro.allevo.connect.model.TriggerRequestEntity;
import ro.allevo.connect.utils.Utils;
import ro.allevo.fintpws.util.JsonResponseWrapper;
import ro.allevo.fintpws.util.Roles;

public class TriggersResource extends QuartzPagedApiCollection<TriggerRequestEntity> {
	
	private static final long serialVersionUID = 1L;
	
	private Scheduler scheduler;
	private String jobGroup;
	private String jobName;
	private EntityManager entityManagerConnect;
	
	private TriggersResource() {
		this.scheduler = Utils.getScheduler();
	}
	
	public TriggersResource(EntityManager entityManagerConnect) {
		this();
		this.entityManagerConnect = entityManagerConnect;
	}
	
	public TriggersResource(String jobGroup, String jobName) {
		this();
		this.jobGroup = jobGroup;
		this.jobName = jobName;
	}
	
	@SuppressWarnings("unchecked")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@JsonIgnore
	@RolesAllowed({Roles.API_INTERFACE_MODIFY})
	public QuartzPagedApiCollection<TriggerRequestEntity> getTriggers(@Context UriInfo uriInfo) throws SchedulerException {

		List<TriggerRequestEntity> triggersEntity = new ArrayList<>();
		
		if(jobName != null) {
			List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(JobKey.jobKey(jobName, jobGroup));
			
			for(Trigger trigger : triggers) {
				triggersEntity.add(TriggerResource.getEntity(trigger));
			}
			
		}else {
			// enumerate each trigger group
			for(String group: scheduler.getTriggerGroupNames()) {
			    // enumerate each trigger in group
			    for(TriggerKey triggerKey : scheduler.getTriggerKeys(GroupMatcher.groupEquals(group))) {
			    	triggersEntity.add(TriggerResource.getEntity(scheduler.getTrigger(triggerKey)));
			    }
			}
		}

		List<TriggerRequestEntity> filteredTriggers = getFilteredList(uriInfo, triggersEntity);
		if (null != filteredTriggers)
			setTotal(filteredTriggers.size());		
		//getValueOfQueryParameter is mainly used for http://localhost:8086/connect-api/jobs?sort_name=asc&total&page=1&page_size=10
		setItems(getPage(filteredTriggers, getValueOfQueryParameter(uriInfo, "page"), getValueOfQueryParameter(uriInfo, "page_size")));	
		
		return this;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.API_INTERFACE_MODIFY})
	public Response createTrigger(TriggerRequestEntity triggerRequest, @PathParam(value = "jobName" ) String jobName, 
			@PathParam(value = "jobGroup" ) String jobGroup) {
		
		try {
			JobDetail job = scheduler.getJobDetail(JobKey.jobKey(jobName, jobGroup));
			CronTrigger trigger = TriggerBuilder.newTrigger()
					.withIdentity(triggerRequest.getName(), triggerRequest.getGroup()).startNow()
					.withSchedule(CronScheduleBuilder.cronSchedule(triggerRequest.getCronExpression()))
					.forJob(job).build();
			
			scheduler.scheduleJob(trigger);
		}catch (SchedulerException e){
			e.printStackTrace();
			return JsonResponseWrapper.getResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}
		return JsonResponseWrapper.getResponse(Response.Status.CREATED, "Trigger created");
	}
	
	@Path("{triggerGroup}/{triggerName}")
	@RolesAllowed({Roles.API_INTERFACE_MODIFY})
	public TriggerResource getTrigger(@PathParam(value = "triggerGroup") String triggerGroup, @PathParam(value = "triggerName" ) String triggerName) {
		return new TriggerResource(triggerGroup, triggerName);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{triggerGroup}")
	@RolesAllowed({Roles.API_INTERFACE_MODIFY})
	public List<TriggerRequestEntity> getTriggersByGroupName(@PathParam(value = "triggerGroup" ) String triggerGroup) throws SchedulerException {
		List<TriggerRequestEntity> triggers = new LinkedList<TriggerRequestEntity>();
	    for (TriggerKey triggerKey : scheduler.getTriggerKeys(GroupMatcher.triggerGroupEquals(triggerGroup))) {
	    	triggers.add(TriggerResource.getEntity(scheduler.getTrigger(triggerKey)));
    	}
		return triggers;
	}
}
