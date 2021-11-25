package ro.allevo.connect.utils;

import javax.ws.rs.core.Response;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

import ro.allevo.fintpws.util.JsonResponseWrapper;

public class Utils {
	
	private static Scheduler scheduler; 
	private static SchedulerFactory schedulerFactory;
	
	public static Scheduler getScheduler() {
		
		if(schedulerFactory == null) {
			schedulerFactory = new StdSchedulerFactory();
		}

		if(scheduler == null) {
			try {
				scheduler = schedulerFactory.getScheduler();
			} catch (SchedulerException e) {
				e.printStackTrace();
			}
		}
		return scheduler;
	}

	public static Response startScheduler() {
		try {
			getScheduler().start();
			getScheduler().resumeAll();
		}catch (SchedulerException e){
			e.printStackTrace();
			return JsonResponseWrapper.getResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}
		return JsonResponseWrapper.getResponse(Response.Status.OK, "scheduler started");
	}

	public static Response stopScheduler() {
		try {
			getScheduler().standby(); // pauseAll() ar trebui folosit mai degraba
		}catch (SchedulerException e){
			e.printStackTrace();
			return JsonResponseWrapper.getResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}
		return JsonResponseWrapper.getResponse(Response.Status.OK, "scheduler shutdown");
	}
}
