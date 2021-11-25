package ro.allevo.whizzer.model;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CashflowComparison{

	private Integer id;

	@NotNull(message = "Account balance required")
	private BigDecimal accountbalanceamount;

	private String accountbalancecurrency;

	private String accountbalancedate;

	private String balanceiban;

	private String entity;
	
	//@NotNull(message = "Exchange rate required")
	private BigDecimal exchangerate;

	private BigDecimal ronaccountbalanceamount;

	private Integer userid;

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public BigDecimal getAccountbalanceamount() {
		return this.accountbalanceamount;
	}

	public void setAccountbalanceamount(BigDecimal accountbalanceamount) {
		this.accountbalanceamount = accountbalanceamount;
	}

	public String getAccountbalancecurrency() {
		return this.accountbalancecurrency;
	}

	public void setAccountbalancecurrency(String accountbalancecurrency) {
		this.accountbalancecurrency = accountbalancecurrency;
	}

	public String getAccountbalancedate() {
		return accountbalancedate;
	}

	public void setAccountbalancedate(String accountbalancedate) {
		this.accountbalancedate = accountbalancedate;
	}

	public String getBalanceiban() {
		return this.balanceiban;
	}

	public void setBalanceiban(String balanceiban) {
		this.balanceiban = balanceiban;
	}

	public String getEntity() {
		return this.entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public BigDecimal getExchangerate() {
		return this.exchangerate;
	}

	public void setExchangerate(BigDecimal exchangerate) {
		this.exchangerate = exchangerate;
	}

	public BigDecimal getRonaccountbalanceamount() {
		return this.ronaccountbalanceamount;
	}

	public void setRonaccountbalanceamount(BigDecimal ronaccountbalanceamount) {
		this.ronaccountbalanceamount = ronaccountbalanceamount;
	}

	public Integer getUserid() {
		return this.userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	@JsonGetter("rowid")
	public Integer getRowid() {
		return id;
	}

}