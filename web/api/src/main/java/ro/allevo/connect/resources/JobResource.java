package ro.allevo.connect.resources;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

import ro.allevo.connect.model.JobEntity;
import ro.allevo.connect.utils.Utils;
import ro.allevo.fintpws.util.JsonResponseWrapper;
import ro.allevo.fintpws.util.Roles;

public class JobResource{

	private Scheduler scheduler;
	private String group;
	private String name;
	
	public JobResource(String group, String name) {
		this.group = group;
		this.name = name;
		this.scheduler = Utils.getScheduler();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.API_INTERFACE_MODIFY})
	public JobEntity getJob() throws SchedulerException {
		return getEntity(scheduler, group, name);
	}
	
	public static JobEntity getEntity(Scheduler scheduler, String group, String name) throws SchedulerException {
		JobDetail job = scheduler.getJobDetail(JobKey.jobKey(name, group));
		JobKey jobKey = job.getKey(); 
		JobEntity jobEntity = new JobEntity();
		jobEntity.setGroup(jobKey.getGroup());
		jobEntity.setName(jobKey.getName());
		jobEntity.setDetails(job.getDescription());
		
		Map<String, String> params = new HashMap<String, String>();
		if(job.getJobDataMap()!=null) {
			for (Entry<String, Object> mapElement : job.getJobDataMap().entrySet()) { 
				params.put(mapElement.getKey(), mapElement.getValue().toString());
	        } 
		}
		jobEntity.setParams(params);
		return jobEntity;
	}
	
	@SuppressWarnings("unchecked")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.API_INTERFACE_MODIFY})
	public Response updateJob(JobEntity entity) {
		String action = entity.getAction();
		try {
//			retrieve the existing triggers first	
			JobKey jobKey = JobKey.jobKey(entity.getName(), entity.getGroup());
			List<Trigger> existingJobTriggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
			
			Set<Trigger> triggersForJob = new HashSet<Trigger>();
			triggersForJob.addAll(existingJobTriggers);
			
			JobDetail oldJobDetail = scheduler.getJobDetail(JobKey.jobKey(entity.getName(), entity.getGroup()));
			JobDataMap oldJobDataMap = oldJobDetail.getJobDataMap();
			
			JobDetail newJobDetail = JobBuilder.newJob(oldJobDetail.getJobClass()).requestRecovery().storeDurably(true)
										.withDescription(entity.getDetails()).withIdentity(entity.getName(), entity.getGroup()).build();
			
			JobDataMap newJobDataMap = newJobDetail.getJobDataMap();
		
			if(entity.getParams()!=null) {
				for (Entry<String, String> mapElement : entity.getParams().entrySet()) { 
					newJobDataMap.put(mapElement.getKey(), mapElement.getValue());
		        } 
			}
			
			if(!oldJobDataMap.isEmpty()) {
				for(Entry<String, Object> oldMapElement : oldJobDataMap.entrySet()) {
					newJobDataMap.putIfAbsent(oldMapElement.getKey(), oldMapElement.getValue());
				}
			}
			
			scheduler.scheduleJob(newJobDetail, triggersForJob, true);
			
			if(action.equals("start")) {
				scheduler.start();
			}else if(action.equals("stop")) {
				scheduler.pauseJob(jobKey);
			}
			
		} catch (SchedulerException e) {
			e.printStackTrace();
			return JsonResponseWrapper.getResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}

		return JsonResponseWrapper.getResponse(Response.Status.ACCEPTED, "Job updated");
	}

	@DELETE
	@RolesAllowed({Roles.API_INTERFACE_MODIFY})
	public Response deleteJob() {
		try {
			scheduler.deleteJob(JobKey.jobKey(name, group));
		} catch (SchedulerException e) {
			e.printStackTrace();
			return JsonResponseWrapper.getResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}
		return JsonResponseWrapper.getResponse(Response.Status.ACCEPTED, "Job deleted");
	}

	@Path("triggers")
	@RolesAllowed({Roles.API_INTERFACE_MODIFY})
	public TriggersResource getTriggers() {
		 return new TriggersResource(group, name);
	}		
}
