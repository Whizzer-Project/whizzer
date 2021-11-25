package ro.allevo.connect.resources;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;

import ro.allevo.connect.model.TriggerRequestEntity;
import ro.allevo.connect.utils.Utils;
import ro.allevo.fintpws.util.JsonResponseWrapper;
import ro.allevo.fintpws.util.Roles;

public class TriggerResource {
	
	private String triggerGroup;
	private String triggerName;
	private Scheduler scheduler;
	
	public TriggerResource(String triggerGroup, String triggerName) {
		this.triggerGroup = triggerGroup;
		this.triggerName = triggerName;
		this.scheduler = Utils.getScheduler();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed(Roles.API_INTERFACE_MODIFY)
	public TriggerRequestEntity getTrigger() throws SchedulerException {
		Trigger trigger = scheduler.getTrigger(TriggerKey.triggerKey(triggerName, triggerGroup));
		return getEntity(trigger);
	}
	
	public static TriggerRequestEntity getEntity(Trigger trigger) throws SchedulerException {
		
		TriggerRequestEntity triggerEntity = new TriggerRequestEntity();
		triggerEntity.setGroup(trigger.getKey().getGroup());
		triggerEntity.setName(trigger.getKey().getName());
		triggerEntity.setJobName(trigger.getJobKey().getName());

		triggerEntity.setTriggerState(Utils.getScheduler().getTriggerState(trigger.getKey()));

		if(trigger instanceof CronTrigger) {
			CronTrigger cronTrigger = (CronTrigger)trigger;
			triggerEntity.setCronExpression(cronTrigger.getCronExpression());
		}
		return triggerEntity;
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed(Roles.API_INTERFACE_MODIFY)
	public Response updateTrigger(TriggerRequestEntity triggerRequestEntity) {
		// triggerRequestEntity imi iau action (start sau stop) - si dau pauseTrigg sau starttrigg
		String action = triggerRequestEntity.getAction();
		try {
			// retrieve the trigger  , triggerRequestEntity.getGroup()
			CronTrigger oldTrigger = (CronTrigger) scheduler.getTrigger(TriggerKey.triggerKey(triggerName, triggerGroup));

			// obtain a builder that would produce the trigger ----- oldTrigger.getTriggerBuilder()
			// update the schedule associated with the builder, and build the new trigger		
			CronTrigger newTrigger = (CronTrigger) oldTrigger.getTriggerBuilder()
					.withIdentity(triggerRequestEntity.getName(), triggerRequestEntity.getGroup()).startNow()
					.withSchedule(CronScheduleBuilder.cronSchedule(triggerRequestEntity.getCronExpression()))
					.build();


			scheduler.rescheduleJob(oldTrigger.getKey(), newTrigger);

			if("start".equals(action)) {
				scheduler.start();
			}else if("stop".equals(action)) {
				scheduler.pauseTrigger(newTrigger.getKey());
			}
			
		}catch (SchedulerException e){
			e.printStackTrace();
			return JsonResponseWrapper.getResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}
		return JsonResponseWrapper.getResponse(Response.Status.CREATED, "Trigger updated");
	}
	
	@DELETE
	@RolesAllowed(Roles.API_INTERFACE_MODIFY)
	public Response deleteTrigger() {
		try {
			//If the related job does not have any other triggers, and the job is not durable, then the job will also be deleted.
			scheduler.unscheduleJob(TriggerKey.triggerKey(triggerName, triggerGroup));
		} catch (SchedulerException e) {
			e.printStackTrace();
			return JsonResponseWrapper.getResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}
		return JsonResponseWrapper.getResponse(Response.Status.ACCEPTED, "Job deleted");
	}	
}

