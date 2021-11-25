package ro.allevo.fintpws.model;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import ro.allevo.fintpws.util.Invariants;
import ro.allevo.fintpws.util.annotations.URLId;


@Entity
@Table(schema = "FINDATA", name="REPRECONINVSUPPVSSTATEMENT")
@Cacheable(false)
@NamedQueries({
	@NamedQuery(name = "RepreconInvSuppVsStatementEntity.findAll", query = "select b from RepreconInvSuppVsStatementEntity b"),
	@NamedQuery(name = "RepreconInvSuppVsStatementEntity.findTotal", query = "select count(b.id) from RepreconInvSuppVsStatementEntity b") })
	

public class RepreconInvSuppVsStatementEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator="AidGenerator")
	@Column(unique=true, nullable=false, updatable=false)
	@URLId
	private Long id;
	
	
	@Column(nullable=false, length=7, name = "matchtype")
	private String matchType;  
	
	@Column(length=35, name = "stmtreference")
	private String stmtReference;

	@Column(length=140, name = "stmtname")
	private String stmtName;
	
	@Column(precision=17, scale=2, name = "stmtamount")
	private Double stmtAmount;
	
	@Column(length=3, name = "stmtCurrency")
	private String stmtCurrency;
	
	@Column(length=35, name = "stmtaccountnumber")
	private String stmtAccountnumber;
	
	@Column(length=500, name = "stmtremittanceinfo")
	private String stmtRemittanceinfo;
	
	@Column(length=10, name = "stmtvaluedate")
	private String stmtValuedate;
  
	@Column(length=30, name = "stmtcorrelationid")
	private String stmtCorrelationid;
	
	@Column(length=18, name = "stmtstatementnumber")
	private String stmtstatementNumber;

	@Column(length=35, name = "invreference")
	private String invReference;

	@Column(precision=17, scale=2, name = "invamount")
	private Double invAmount;
	
	@Column(length=3, name = "invcurrency")
	private String invCurrency;
	
	@Column(length=70, name = "invdbtcustomername")
	private String invDbtcustomername;
	
	@Column(length=70, name = "invmaturitydate")
	private String invMaturitydate;
	
	@Column(length=35, name = "invcdtaccount")
	private String invCdtaccount;
	
	@Column(length=35, name = "invcdtcustomername")
	private String invCdtcustomername;
	
	@Column(length=35, name = "invinvoiceno")
	private String invInvoiceno;
	
	@Column(length=4, name = "invinvoiceserial")
	private String invInvoiceserial;
	
	@Column(length=30, name = "invcorrelationid")
	private String invCorrelationid;

	
	public RepreconInvSuppVsStatementEntity() {
		
	}
	
	public String getStmtstatementNumber() {
		return stmtstatementNumber;
	}

	public void setStmtstatementNumber(String stmtstatementNumber) {
		this.stmtstatementNumber = stmtstatementNumber;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getInvReference() {
		return invReference;
	}

	public void setInvReference(String invReference) {
		this.invReference = invReference;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getMatchType() {
		return matchType;
	}

	public void setMatchType(String matchType) {
		this.matchType = matchType;
	}

	@Override
	public String toString() {
		return "RepreconInvSuppVsStatementEntity [id=" + id + ", matchType=" + matchType + ", stmtReference="
				+ stmtReference + ", stmtName=" + stmtName + ", stmtAmount=" + stmtAmount + ", stmtCurrency="
				+ stmtCurrency + ", stmtAccountnumber=" + stmtAccountnumber + ", stmtRemittanceinfo="
				+ stmtRemittanceinfo + ", stmtValuedate=" + stmtValuedate + ", stmtCorrelationid=" + stmtCorrelationid
				+ ", stmtstatementNumber=" + stmtstatementNumber + ", invReference=" + invReference + ", invAmount="
				+ invAmount + ", invCurrency=" + invCurrency + ", invDbtcustomername=" + invDbtcustomername
				+ ", invMaturitydate=" + invMaturitydate + ", invCdtaccount=" + invCdtaccount + ", invCdtcustomername="
				+ invCdtcustomername + ", invInvoiceno=" + invInvoiceno + ", invInvoiceserial=" + invInvoiceserial
				+ ", invCorrelationid=" + invCorrelationid + "]";
	}

	@Override
	public String getClassEvent() {
		return Invariants.routingClassEvents.MANAGE.toString();
	}
	
	@Override
	public String getMessage() {
		return "reports reconciliation debit transactions vs supplier invoices";
	}
}

