package ro.allevo.connect.resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.security.RolesAllowed;
import javax.jms.ConnectionFactory;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
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

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.matchers.GroupMatcher;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ro.allevo.connect.model.AuthorizationServersEntity;
import ro.allevo.connect.model.JobEntity;
import ro.allevo.connect.model.JobTypes;
import ro.allevo.connect.schedulers.AccountSatementJob;
import ro.allevo.connect.schedulers.TestJob;
import ro.allevo.connect.utils.Utils;
import ro.allevo.fintpws.model.InternalAccountEntity;
import ro.allevo.fintpws.util.JsonResponseWrapper;
import ro.allevo.fintpws.util.Roles;

public class JobsResource extends QuartzPagedApiCollection<JobEntity>{

	private static final long serialVersionUID = 1L;
	
	private static Scheduler scheduler;
	private EntityManager entityManagerConnect;
	private EntityManager entityManagerList;
	private ConnectionFactory connectionFactory;
	private String amqStatement;
	private String apiUrl;
	
	public JobsResource(EntityManager entityManagerConnect, EntityManager entityManagerList, ConnectionFactory connectionFactory, String amqStatement, String apiUrl) {
		this.entityManagerConnect = entityManagerConnect;
		this.entityManagerList = entityManagerList;
		this.connectionFactory = connectionFactory;
		this.amqStatement = amqStatement;
		this.apiUrl = apiUrl;
		scheduler = Utils.getScheduler();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@JsonIgnore
	@RolesAllowed({Roles.API_INTERFACE_MODIFY})
	public QuartzPagedApiCollection<JobEntity> getJobs(@Context UriInfo uriInfo) throws SchedulerException {

		List<JobEntity> jobs = new LinkedList<>();
		for (String groupName : scheduler.getJobGroupNames()) {
			for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
			 	jobs.add(JobResource.getEntity(scheduler, groupName, jobKey.getName()));
		    }
		}
		
		List<JobEntity> filteredJobs = getFilteredList(uriInfo, jobs);
		setTotal(filteredJobs.size());		
		//getValueOfQueryParameter is mainly used for http://localhost:8086/connect-api/jobs?sort_name=asc&total&page=1&page_size=10
		setItems(getPage(filteredJobs, getValueOfQueryParameter(uriInfo, "page"), getValueOfQueryParameter(uriInfo, "page_size")));		

		return this;
	}	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{jobGroup}")
	@RolesAllowed({Roles.API_INTERFACE_MODIFY})
	public List<JobEntity> getJobsByGroupName(@PathParam(value = "jobGroup" ) String groupName) throws SchedulerException {
		List<JobEntity> jobs = new LinkedList<>();
	    for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
	 		jobs.add(JobResource.getEntity(scheduler, groupName, jobKey.getName()));
    	}
		return jobs;
	}
	
	
	@Path("{jobGroup}/{jobName}")
	@RolesAllowed({Roles.API_INTERFACE_MODIFY})
	public JobResource getJob(@PathParam(value = "jobGroup" ) String groupName, @PathParam("jobName") String name) {
		return new JobResource(groupName, name);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.API_INTERFACE_MODIFY})
	public Response createJob(JobEntity jobRequest) {
		try {
			Class<? extends Job> _class = null; 
			if(!EnumUtils.isValidEnum(JobTypes.class, jobRequest.getGroup())) {
				return JsonResponseWrapper.getResponse(Response.Status.BAD_REQUEST, "Invalid job type");
			}
			JobTypes jobType = JobTypes.valueOf(jobRequest.getGroup()) ;
			Map<String,Object> customJobData  = new HashMap<>();
			switch(jobType) {
				case PSD2:
					initPsd2Job(customJobData, jobRequest);
					_class = AccountSatementJob.class;
					break;
				case TEST_TYPE:  _class = TestJob.class; break;
				default:  break;
			}

			JobDetail jobDetail = JobBuilder.newJob(_class).requestRecovery().storeDurably(true) 
					.withDescription(jobRequest.getDetails()).withIdentity(jobRequest.getName(), jobRequest.getGroup()).build();
			
			JobDataMap jdm = jobDetail.getJobDataMap();
			jdm.putAll(customJobData);
			
			switch(jobType) {
				case PSD2:
					jdm.put("connectionFactory", connectionFactory);
					jdm.put("amqStatement", amqStatement);
					break;
				case TEST_TYPE:  break;
				default:  break;
			}
			
			if(jobRequest.getParams()!=null) {
				for (Entry<String, String> mapElement : jobRequest.getParams().entrySet()) { 
		            jdm.put(mapElement.getKey(), mapElement.getValue());
		        } 
			}
			scheduler.addJob(jobDetail, true);
			
		} catch (SchedulerException e) {
			e.printStackTrace();
			return JsonResponseWrapper.getResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}
		return JsonResponseWrapper.getResponse(Response.Status.CREATED, "Job created");
	}
	
	private void initPsd2Job(Map<String,Object> customJobData, JobEntity jobRequest) {
//		jobRequest.getName() = bic-entityname -> findByBIC - pass only bic from name
		TypedQuery<AuthorizationServersEntity> queryAuth = entityManagerConnect.createNamedQuery("AuthorizationServersEntity.findByBIC", AuthorizationServersEntity.class).setParameter("bic", StringUtils.substringBefore(jobRequest.getName(), "-"));
		AuthorizationServersEntity auth = queryAuth.getSingleResult();	
		TypedQuery<InternalAccountEntity> internalAccount = entityManagerList.createNamedQuery("InternalAccountEntity.findByBIC", InternalAccountEntity.class).setParameter("bic", StringUtils.substringBefore(jobRequest.getName(), "-"));
		List<InternalAccountEntity> accounts = internalAccount.getResultList();
		customJobData.put("url", auth.getRedirectUri());
		customJobData.put("apiUrl", apiUrl);
		List<String> resourceIds = new ArrayList<>();
		for(InternalAccountEntity account:accounts) {
			String resourceId = account.getResourceId();
			if(resourceId != null) {
				resourceIds.add(account.getResourceId());
			}	
		}
		customJobData.put("resourceIds", resourceIds);
	}
}
