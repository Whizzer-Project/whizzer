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
@Table(schema = "FINDATA", name="REPRECONPAYMENTVSSTATEMENT")
@Cacheable(false)
@NamedQueries({
	@NamedQuery(name = "RepreconPaymentVsStatementEntity.findAll", query = "select b from RepreconPaymentVsStatementEntity b"),
	@NamedQuery(name = "RepreconPaymentVsStatementEntity.findTotal", query = "select count(b.id) from RepreconPaymentVsStatementEntity b") })
	

public class RepreconPaymentVsStatementEntity extends BaseEntity {
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
	
	@Column(length=35, name = "pymtmessagetype")
	private String pymtMessagetype;
	
	public String getStmtstatementNumber() {
		return stmtstatementNumber;
	}

	public void setStmtstatementNumber(String stmtstatementNumber) {
		this.stmtstatementNumber = stmtstatementNumber;
	}

	@Column(length=35, name = "pymtendtoendid")
	private String pymtEndtoendid;

	@Column(length=70, name = "pymtdbtcustomername")
	private String pymtDbtcustomername;
	
	@Column(precision=17, scale=2, name = "pymtamount")
	private Double pymtAmount;
	
	@Column(length=3, name = "pymtcurrency")
	private String pymtCurrency;
    
	@Column(length=35, name = "pymtdbtaccount")
	private String pymtDbtaccount;
	
	@Column(length=140, name = "pymtremittanceinfo")
	private String pymtRemittanceinfo;
	
	@Column(length=10, name = "pymtvaluedate")
	private String pymtValuedate;

	@Column(length=70, name = "pymtcdtcustomername")
	private String pymtCdtcustomername;
	
	@Column(length=35, name = "pymtcdtaccount")
	private String pymtCdtaccount;
	
	@Column(length=30, name = "pymtcorrelationid")
	private String pymtCorrelationid;

	
	public RepreconPaymentVsStatementEntity() {
		
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

	public String getPymtMessagetype() {
		return pymtMessagetype;
	}

	public void setPymtMessagetype(String pymtMessagetype) {
		this.pymtMessagetype = pymtMessagetype;
	}

	public String getPymtEndtoendid() {
		return pymtEndtoendid;
	}

	public void setPymtEndtoendid(String pymtEndtoendid) {
		this.pymtEndtoendid = pymtEndtoendid;
	}

	public String getPymtDbtcustomername() {
		return pymtDbtcustomername;
	}

	public void setPymtDbtcustomername(String pymtDbtcustomername) {
		this.pymtDbtcustomername = pymtDbtcustomername;
	}

	public Double getPymtAmount() {
		return pymtAmount;
	}

	public void setPymtAmount(Double pymtAmount) {
		this.pymtAmount = pymtAmount;
	}

	public String getPymtCurrency() {
		return pymtCurrency;
	}

	public void setPymtCurrency(String pymtCurrency) {
		this.pymtCurrency = pymtCurrency;
	}

	public String getPymtDbtaccount() {
		return pymtDbtaccount;
	}

	public void setPymtDbtaccount(String pymtDbtaccount) {
		this.pymtDbtaccount = pymtDbtaccount;
	}

	public String getPymtRemittanceinfo() {
		return pymtRemittanceinfo;
	}

	public void setPymtRemittanceinfo(String pymtRemittanceinfo) {
		this.pymtRemittanceinfo = pymtRemittanceinfo;
	}

	public String getPymtValuedate() {
		return pymtValuedate;
	}

	public void setPymtValuedate(String pymtValuedate) {
		this.pymtValuedate = pymtValuedate;
	}

	public String getPymtCdtcustomername() {
		return pymtCdtcustomername;
	}

	public void setPymtCdtcustomername(String pymtCdtcustomername) {
		this.pymtCdtcustomername = pymtCdtcustomername;
	}

	public String getPymtCdtaccount() {
		return pymtCdtaccount;
	}

	public void setPymtCdtaccount(String pymtCdtaccount) {
		this.pymtCdtaccount = pymtCdtaccount;
	}

	public String getPymtCorrelationid() {
		return pymtCorrelationid;
	}

	public void setPymtCorrelationid(String pymtCorrelationid) {
		this.pymtCorrelationid = pymtCorrelationid;
	}

	public String getMatchType() {
		return matchType;
	}

	public void setMatchType(String matchType) {
		this.matchType = matchType;
	}

	@Override
	public String toString() {
		return "ReconciliationEntity [id=" + id + ", matchType=" + matchType + ", stmtReference="
				+ stmtReference + ", stmtName=" + stmtName + ", stmtAmount=" + stmtAmount + ", stmtCurrency="
				+ stmtCurrency + ", stmtAccountnumber=" + stmtAccountnumber + ", stmtRemittanceinfo="
				+ stmtRemittanceinfo + ", stmtValuedate=" + stmtValuedate + ", stmtCorrelationid=" + stmtCorrelationid
				+ ", stmtstatementNumber=" + stmtstatementNumber + ", pymtMessagetype=" + pymtMessagetype
				+ ", pymtEndtoendid=" + pymtEndtoendid + ", pymtDbtcustomername=" + pymtDbtcustomername
				+ ", pymtAmount=" + pymtAmount + ", pymtCurrency=" + pymtCurrency + ", pymtDbtaccount=" + pymtDbtaccount
				+ ", pymtRemittanceinfo=" + pymtRemittanceinfo + ", pymtValuedate=" + pymtValuedate
				+ ", pymtCdtcustomername=" + pymtCdtcustomername + ", pymtCdtaccount=" + pymtCdtaccount
				+ ", pymtCorrelationid=" + pymtCorrelationid + "]";
	}

	@Override
	public String getClassEvent() {
		return Invariants.configClassEvents.MANAGE.toString();
	}

	@Override
	public String getMessage() {
		return "reports reconciliation debit transactions vs payment transactions";
	}
	
}
