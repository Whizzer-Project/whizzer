package ro.allevo.fintpuiws.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class TransactionHeaderEntity {

	@Id
	private String routingKeywordName;
	
	private String label;
	
	private String contentType;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getRoutingKeywordName() {
		return routingKeywordName;
	}

	public void setRoutingKeywordName(String routingKeywordName) {
		this.routingKeywordName = routingKeywordName;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}	 
}
