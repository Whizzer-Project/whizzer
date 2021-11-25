package ro.allevo.fintpui.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Status {
	
	private String additionalinfo;
	private String clasS;
	private String correlationid;
	private Timestamp eventdate;
	private String guid;
	private String innerexception;
	private Timestamp insertdate;
	private String machine;
	private String message;
	private BigDecimal service;
	private Integer serviceMap;
	private String sessionid;
	private String type;
	private Integer userid;
	
	public void setAdditionalinfo(String additionalinfo) {
		this.additionalinfo = additionalinfo;
	}
	public void setClasS(String clasS) {
		this.clasS = clasS;
	}
	public void setCorrelationid(String correlationid) {
		this.correlationid = correlationid;
	}
	public void setEventdate(Timestamp eventdate) {
		this.eventdate = eventdate;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public void setInnerexception(String innerexception) {
		this.innerexception = innerexception;
	}
	public void setInsertdate(Timestamp insertdate) {
		this.insertdate = insertdate;
	}
	public void setMachine(String machine) {
		this.machine = machine;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public void setService(BigDecimal service) {
		this.service = service;
	}
	public void setServiceMap(Integer serviceMap) {
		this.serviceMap = serviceMap;
	}
	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	public String getAdditionalinfo() {
		return additionalinfo;
	}
	public String getClasS() {
		return clasS;
	}
	public String getCorrelationid() {
		return correlationid;
	}
	public Timestamp getEventdate() {
		return eventdate;
	}
	public String getGuid() {
		return guid;
	}
	public String getInnerexception() {
		return innerexception;
	}
	public Timestamp getInsertdate() {
		return insertdate;
	}
	public String getMachine() {
		return machine;
	}
	public String getMessage() {
		return message;
	}
	public BigDecimal getService() {
		return service;
	}
	public Integer getServiceMap() {
		return serviceMap;
	}
	public String getSessionid() {
		return sessionid;
	}
	public String getType() {
		return type;
	}
	public Integer getUserid() {
		return userid;
	}
	
}
