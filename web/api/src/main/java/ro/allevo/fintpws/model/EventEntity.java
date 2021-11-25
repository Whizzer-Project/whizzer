package ro.allevo.fintpws.model;

import java.sql.Timestamp;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(schema = "FINDATA", name="repevents")

@NamedQueries({
	@NamedQuery(name = "EventEntity.findAll", query = "select e from EventEntity e"),
	@NamedQuery(name = "EventEntity.findTotal", query = "select count(e.guid) from EventEntity e"),
	@NamedQuery(name = "EventEntity.findById", query = "select e from EventEntity e where trim(e.guid)=:id") })

@Cacheable(false)
public class EventEntity  extends BaseEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	private String guid;	
	

	private String clasS;
	private Timestamp eventdate;
	private String correlationid;	
	private String username;		
	private String message;
	private String additionalinfo;
	private String innerexception;
	private String type;
	private String service;
	
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
	public String getClasS() {
		return clasS;
	}
	public void setClasS(String clasS) {
		this.clasS = clasS;
	}
	public Timestamp getEventdate() {
		return eventdate;
	}
	public void setEventdate(Timestamp eventdate) {
		this.eventdate = eventdate;
	}
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}	

	public String getCorrelationid() {
		return correlationid;
	}
	public void setCorrelationid(String correlationid) {
		this.correlationid = correlationid;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String toString() {
		return "EventEntity [guid=" + guid + ", clasS=" + clasS + ", eventdate=" + eventdate + ", correlationid="
				+ correlationid + ", username=" + username + ", message=" + message + ", additionalinfo="
				+ additionalinfo + ", innerexception=" + innerexception + "]";
	}
	@Override
	public String getClassEvent() {
		return null;
	}
	
}
