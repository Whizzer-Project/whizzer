package ro.allevo.fintpui.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RepreconInvVsStatement {
	
	@NotNull
	@Size(max = 7)
	private String matchType;  
	
	@Size(max = 35)
	private String stmtReference;

	@Size(max = 140)
	private String stmtName;
	
	private Double stmtAmount;
	
	@Size(max = 3)
	private String stmtCurrency;
	
	@Size(max = 35)
	private String stmtAccountnumber;
	
	@Size(max = 500)
	private String stmtRemittanceinfo;
	
	@Size(max = 18)
	private String stmtstatementNumber;
	
	@Size(max = 10)
	private String stmtValuedate;
  
	@Size(max = 30)
	private String stmtCorrelationid;
	
	@Size(max = 35)
	private String invReference;
	
	@Size(max = 35)
	private String invCdtaccount;

	@Size(max = 35)
	private String invCdtcustomername;
	
	private Double invAmount;
	
	@Size(max = 3)
	private String invCurrency;
    
	@Size(max = 70)
	private String invDbtcustomername;
	
	@Size(max = 70)
	private String invMaturitydate;
	
	@Size(max = 35)
	private String invInvoiceno;

	@Size(max = 4)
	private String invInvoiceserial;
	
	@Size(max = 30)
	private String invCorrelationid;

	public String getMatchType() {
		return matchType;
	}

	public void setMatchType(String matchType) {
		this.matchType = matchType;
	}

	public String getStmtReference() {
		return stmtReference;
	}

	public void setStmtReference(String stmtReference) {
		this.stmtReference = stmtReference;
	}

	public String getStmtName() {
		return stmtName;
	}

	public void setStmtName(String stmtName) {
		this.stmtName = stmtName;
	}

	public Double getStmtAmount() {
		return stmtAmount;
	}

	public void setStmtAmount(Double stmtAmount) {
		this.stmtAmount = stmtAmount;
	}

	public String getStmtCurrency() {
		return stmtCurrency;
	}

	public void setStmtCurrency(String stmtCurrency) {
		this.stmtCurrency = stmtCurrency;
	}

	public String getStmtAccountnumber() {
		return stmtAccountnumber;
	}

	public void setStmtAccountnumber(String stmtAccountnumber) {
		this.stmtAccountnumber = stmtAccountnumber;
	}

	public String getStmtRemittanceinfo() {
		return stmtRemittanceinfo;
	}

	public void setStmtRemittanceinfo(String stmtRemittanceinfo) {
		this.stmtRemittanceinfo = stmtRemittanceinfo;
	}

	public String getStmtstatementNumber() {
		return stmtstatementNumber;
	}

	public void setStmtstatementNumber(String stmtstatementNumber) {
		this.stmtstatementNumber = stmtstatementNumber;
	}

	public String getStmtValuedate() {
		return stmtValuedate;
	}

	public void setStmtValuedate(String stmtValuedate) {
		this.stmtValuedate = stmtValuedate;
	}

	public String getStmtCorrelationid() {
		return stmtCorrelationid;
	}

	public void setStmtCorrelationid(String stmtCorrelationid) {
		this.stmtCorrelationid = stmtCorrelationid;
	}

	public String getInvReference() {
		return invReference;
	}

	public void setInvReference(String invReference) {
		this.invReference = invReference;
	}

	public String getInvCdtaccount() {
		return invCdtaccount;
	}

	public void setInvCdtaccount(String invCdtaccount) {
		this.invCdtaccount = invCdtaccount;
	}

	public String getInvCdtcustomername() {
		return invCdtcustomername;
	}

	public void setInvCdtcustomername(String invCdtcustomername) {
		this.invCdtcustomername = invCdtcustomername;
	}

	public Double getInvAmount() {
		return invAmount;
	}

	public void setInvAmount(Double invAmount) {
		this.invAmount = invAmount;
	}

	public String getInvCurrency() {
		return invCurrency;
	}

	public void setInvCurrency(String invCurrency) {
		this.invCurrency = invCurrency;
	}

	public String getInvDbtcustomername() {
		return invDbtcustomername;
	}

	public void setInvDbtcustomername(String invDbtcustomername) {
		this.invDbtcustomername = invDbtcustomername;
	}

	public String getInvMaturitydate() {
		return invMaturitydate;
	}

	public void setInvMaturitydate(String invMaturitydate) {
		this.invMaturitydate = invMaturitydate;
	}

	public String getInvInvoiceno() {
		return invInvoiceno;
	}

	public void setInvInvoiceno(String invInvoiceno) {
		this.invInvoiceno = invInvoiceno;
	}

	public String getInvInvoiceserial() {
		return invInvoiceserial;
	}

	public void setInvInvoiceserial(String invInvoiceserial) {
		this.invInvoiceserial = invInvoiceserial;
	}

	public String getInvCorrelationid() {
		return invCorrelationid;
	}

	public void setInvCorrelationid(String invCorrelationid) {
		this.invCorrelationid = invCorrelationid;
	}
	

}
