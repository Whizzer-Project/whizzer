package ro.allevo.fintpuiws.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import ro.allevo.fintpws.model.BaseEntity;

@Entity
public class QueuesDuplicateEntity extends BaseEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1564315155116355242L;
	@Id
	String id;
	String reference;
	String feedback;
	String sourcefilename;
	String queuename;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public String getFeedback() {
		return feedback;
	}
	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}
	public String getSourcefilename() {
		return sourcefilename;
	}
	public void setSourcefilename(String sourcefilename) {
		this.sourcefilename = sourcefilename;
	}
	public String getQueuename() {
		return queuename;
	}
	public void setQueuename(String queuename) {
		this.queuename = queuename;
	}
	@Override
	public String toString() {
		return "QueuesDuplicateEntity [id=" + id + ", queueName=" + queuename + ", feedback=" + feedback + ", reference=" + reference
				+ "]";
	}
	@Override
	public String getClassEvent() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
