package ro.allevo.connect.model;

import ro.allevo.fintpws.model.BaseEntity;
import ro.allevo.fintpws.util.Invariants;

public class ConsentsResponseEntity extends BaseEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String consentStatus;
	private String consentId;
	private Links _links;
	
	public String getConsentStatus() {
		return consentStatus;
	}
	public void setConsentStatus(String consentStatus) {
		this.consentStatus = consentStatus;
	}
	public String getConsentId() {
		return consentId;
	}
	public void setConsentId(String consentId) {
		this.consentId = consentId;
	}
	public Links get_links() {
		return _links;
	}
	public void set_links(Links _links) {
		this._links = _links;
	}
	@Override
	public String toString() {
		return "ConsentsResponseEntity [id=" + consentId + ", consentStatus=" + consentStatus + ",]";
	}
	@Override
	public String getClassEvent() {
		return Invariants.configClassEvents.MANAGE.toString();
	}
	
	@Override
	public String getMessage() {
		return "consent response";
	}
}


