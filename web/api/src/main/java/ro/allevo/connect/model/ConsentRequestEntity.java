package ro.allevo.connect.model;

public class ConsentRequestEntity {
	
	private Boolean recurringIndicator;
	private String validUntil;
	private Integer frequencyPerDay;
	private Boolean combinedServiceIndicator;
	private AccessEntity access;

	public Boolean getRecurringIndicator() {
		return recurringIndicator;
	}
	public void setRecurringIndicator(Boolean recurringIndicator) {
		this.recurringIndicator = recurringIndicator;
	}
	
	public Integer getFrequencyPerDay() {
		return frequencyPerDay;
	}
	
	public AccessEntity getAccess() {
		return access;
	}
	public void setAccess(AccessEntity access) {
		this.access = access;
	}
	public String getValidUntil() {
		return validUntil;
	}
	public void setValidUntil(String validUntil) {
		this.validUntil = validUntil;
	}
	public void setFrequencyPerDay(Integer frequencyPerDay) {
		this.frequencyPerDay = frequencyPerDay;
	}
	public Boolean getCombinedServiceIndicator() {
		return combinedServiceIndicator;
	}
	public void setCombinedServiceIndicator(Boolean combinedServiceIndicator) {
		this.combinedServiceIndicator = combinedServiceIndicator;
	}
}
