package ro.allevo.connect.model;

import org.quartz.Trigger.TriggerState;

public class TriggerStateEntity {
	
	private String id;
	private TriggerState triggerState;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public TriggerState getTriggerState() {
		return triggerState;
	}
	public void setTriggerState(TriggerState triggerState) {
		this.triggerState = triggerState;
	}
	
	

}
