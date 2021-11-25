package ro.allevo.connect.model;

public class AccessEntity {
	
	
	private String availableAccounts;

	public String getAvailableAccounts() {
		return availableAccounts;
	}

	public void setAvailableAccounts(String availableAccounts) {
		this.availableAccounts = availableAccounts;
	}
	
	/*private List<AccessFiledEntity> accounts;
	private List<AccessFiledEntity> balances;
	private List<AccessFiledEntity> transactions;
	
	public List<AccessFiledEntity> getAccounts() {
		return accounts;
	}
	public void setAccounts(List<AccessFiledEntity> accounts) {
		this.accounts = accounts;
	}
	public List<AccessFiledEntity> getBalances() {
		return balances;
	}
	public void setBalances(List<AccessFiledEntity> balances) {
		this.balances = balances;
	}
	public List<AccessFiledEntity> getTransactions() {
		return transactions;
	}
	public void setTransactions(List<AccessFiledEntity> transactions) {
		this.transactions = transactions;
	}*/
}


class AccessFiledEntity {
	private String iban;
	private String bban;
	private String pan;
	private String maskedPan;
	private String currency;
	private String msisdn;

	public String getIban() {
		return iban;
	}
	public void setIban(String iban) {
		this.iban = iban;
	}
	public String getBban() {
		return bban;
	}
	public void setBban(String bban) {
		this.bban = bban;
	}
	public String getPan() {
		return pan;
	}
	public void setPan(String pan) {
		this.pan = pan;
	}
	public String getMaskedPan() {
		return maskedPan;
	}
	public void setMaskedPan(String maskedPan) {
		this.maskedPan = maskedPan;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}


}