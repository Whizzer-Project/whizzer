package ro.allevo.connect.resources;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.core.Response;

import org.quartz.Scheduler;

import com.fasterxml.jackson.databind.ObjectMapper;

import ro.allevo.connect.model.SchedulerEntity;
import ro.allevo.connect.utils.Utils;

public class SchedulerResource {	

	@PUT
	public Response updateScheduler(String toggle) {
		// toggle is a json string - it has only a value: true (pause the scheduler) or false (starts/resumes the scheduler)
		
		ObjectMapper objMp = new ObjectMapper();
		boolean inStandbyMode = false;
		try {
			SchedulerEntity schedulerEntity = objMp.readValue(toggle, SchedulerEntity.class);
			inStandbyMode = schedulerEntity.inStandbyMode();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(inStandbyMode) {
			return Utils.stopScheduler();
		}else {
			return Utils.startScheduler();
		}
	}	
	
	@GET
	public Scheduler getScheduler(){
		return Utils.getScheduler();
	}
}
