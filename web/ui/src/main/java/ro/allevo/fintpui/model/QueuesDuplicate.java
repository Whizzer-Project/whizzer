package ro.allevo.fintpui.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class QueuesDuplicate {

	private String id;
	private String reference;
	private String feedback;
	private String sourcefilename;
	private String queuename;
	
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
	
	
}
