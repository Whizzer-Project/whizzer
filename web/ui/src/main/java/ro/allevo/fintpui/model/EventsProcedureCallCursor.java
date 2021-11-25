package ro.allevo.fintpui.model;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EventsProcedureCallCursor {

	private String guid;	
	private String correlationid;	
	private String reference;
	private Timestamp updatedate;	
	private String username;		
	private String message;
	private String additionalinfo;
	private String innerexception;
	private String type;
	private String service;
	private Timestamp eventdate;
	
	public Timestamp getEventdate() {
		return eventdate;
	}
	public void setEventdate(Timestamp eventdate) {
		this.eventdate = eventdate;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}	
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public Timestamp getUpdatedate() {
		return updatedate;
	}
	public void setUpdatedate(Timestamp updatedate) {
		this.updatedate = updatedate;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getCorrelationid() {
		return correlationid;
	}
	public void setCorrelationid(String correlationid) {
		this.correlationid = correlationid;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getAdditionalinfo() {
		return additionalinfo;
	}
	public void setAdditionalinfo(String additionalinfo) {
		this.additionalinfo = additionalinfo;
	}
	public String getInnerexception() {
		return innerexception;
	}
	public void setInnerexception(String innerexception) {
		this.innerexception = innerexception;
	}
}
