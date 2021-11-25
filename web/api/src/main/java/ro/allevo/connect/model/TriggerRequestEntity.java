package ro.allevo.connect.model;

import org.quartz.Trigger.TriggerState;

public class TriggerRequestEntity {
	
	private String group;
	private String name;
	private String cronExpression;
	private TriggerState triggerState;
	private String jobName;
	private String action;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCronExpression() {
		return cronExpression;
	}
	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public TriggerState getTriggerState() {
		return triggerState;
	}
	public void setTriggerState(TriggerState triggerState) {
		this.triggerState = triggerState;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	
//	public enum TriggerState { NONE, NORMAL, PAUSED, COMPLETE, ERROR, BLOCKED }
}
