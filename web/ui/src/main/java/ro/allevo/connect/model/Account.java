package ro.allevo.connect.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Account {

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
		this.iban = null!=iban?iban.trim():iban;
	}
	public String getCurrency() {
		return null!=currency?currency.trim():currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getProduct() {
		return null!=product?product.trim():product;
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
}
