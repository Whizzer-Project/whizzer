package ro.allevo.connect.model;

import ro.allevo.fintpws.model.BaseEntity;
import ro.allevo.fintpws.util.Invariants;

public class AccountEntity extends BaseEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String resourceId;
	private String iban;
	private String currency;
	private String product;
	private String cashAccountType;
	private String name;
	
	public String getResourceId() {
		return resourceId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	public String getIban() {
		return iban;
	}
	public void setIban(String iban) {
		this.iban = iban;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getCashAccountType() {
		return cashAccountType;
	}
	public void setCashAccountType(String cashAccountType) {
		this.cashAccountType = cashAccountType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "AccountEntity [resourceId=" + resourceId + ", iban=" + iban + ", currency=" + currency + ", product="
				+ product + ", cashAccountType=" + cashAccountType + ", name=" + name + "]";
	}
	@Override
	public String getClassEvent() {
		return Invariants.connectClassEvents.MANAGE.toString();
	}
	
	@Override
	public String getMessage() {
		return "account";
	}
	
	
	/*private String resourceId;
	private String iban;
	private String bban;
	private String msisdn;
	private String currency;
	private String name;
	private String product;
	private String cashAccountType;
	private String status;
	private String bic;
	private String linkedAccounts;
	private String usage;
	private String details;
	private List<BalanceEntity> balances;
	
	public String getResourceId() {
		return resourceId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
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
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getCashAccountType() {
		return cashAccountType;
	}
	public void setCashAccountType(String cashAccountType) {
		this.cashAccountType = cashAccountType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getBic() {
		return bic;
	}
	public void setBic(String bic) {
		this.bic = bic;
	}
	public String getLinkedAccounts() {
		return linkedAccounts;
	}
	public void setLinkedAccounts(String linkedAccounts) {
		this.linkedAccounts = linkedAccounts;
	}
	public String getUsage() {
		return usage;
	}
	public void setUsage(String usage) {
		this.usage = usage;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public List<BalanceEntity> getBalances() {
		return balances;
	}
	public void setBalances(List<BalanceEntity> balances) {
		this.balances = balances;
	}*/
	
}
