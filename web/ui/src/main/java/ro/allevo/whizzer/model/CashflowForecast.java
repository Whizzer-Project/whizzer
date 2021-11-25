package ro.allevo.whizzer.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * The persistent class for the cfforecast database table.
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CashflowForecast implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;

	private String entity;

	private String informationtype;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date issuedate;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date maturitydate;
	@NotNull(message = "Suma operatiune (Operation amount) required")
	private BigDecimal operationamount;

	private String operationcurrency;

	private String operationiban;

	private String operationtype;

	private String opretionno;

	private String opretionsubtype;

	private String payersuppliername;

	private Integer paymentduedate;

	private String revenueexpensetype;

	private Integer userid;

	public CashflowForecast() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEntity() {
		return this.entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public String getInformationtype() {
		return this.informationtype;
	}

	public void setInformationtype(String informationtype) {
		this.informationtype = informationtype;
	}

	public Date getIssuedate() {
		return this.issuedate;
	}

	public void setIssuedate(Date issuedate) {
		this.issuedate = issuedate;
	}

	public Date getMaturitydate() {
		return this.maturitydate;
	}

	public void setMaturitydate(Date maturitydate) {
		this.maturitydate = maturitydate;
	}

	public BigDecimal getOperationamount() {
		return this.operationamount;
	}

	public void setOperationamount(BigDecimal operationamount) {
		this.operationamount = operationamount;
	}

	public String getOperationcurrency() {
		return this.operationcurrency;
	}

	public void setOperationcurrency(String operationcurrency) {
		this.operationcurrency = operationcurrency;
	}

	public String getOperationiban() {
		return this.operationiban;
	}

	public void setOperationiban(String operationiban) {
		this.operationiban = operationiban;
	}

	public String getOperationtype() {
		return this.operationtype;
	}

	public void setOperationtype(String operationtype) {
		this.operationtype = operationtype;
	}

	public String getOpretionno() {
		return this.opretionno;
	}

	public void setOpretionno(String opretionno) {
		this.opretionno = opretionno;
	}

	public String getOpretionsubtype() {
		return this.opretionsubtype;
	}

	public void setOpretionsubtype(String opretionsubtype) {
		this.opretionsubtype = opretionsubtype;
	}

	public String getPayersuppliername() {
		return this.payersuppliername;
	}

	public void setPayersuppliername(String payersuppliername) {
		this.payersuppliername = payersuppliername;
	}

	
	public Integer getPaymentduedate() {
		return paymentduedate;
	}

	public void setPaymentduedate(Integer paymentduedate) {
		this.paymentduedate = paymentduedate;
	}

	public String getRevenueexpensetype() {
		return this.revenueexpensetype;
	}

	public void setRevenueexpensetype(String revenueexpensetype) {
		this.revenueexpensetype = revenueexpensetype;
	}

	public Integer getUserid() {
		return this.userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	@Override
	public String toString() {
		return "CashFlowForecastingEntity [id=" + id + ", entity=" + entity + ", informationtype=" + informationtype
				+ ", issuedate=" + issuedate + ", maturitydate=" + maturitydate + ", operationamount=" + operationamount
				+ ", operationcurrency=" + operationcurrency + ", operationiban=" + operationiban + ", operationtype="
				+ operationtype + ", opretionno=" + opretionno + ", opretionsubtype=" + opretionsubtype
				+ ", payersuppliername=" + payersuppliername + ", paymentduedate=" + paymentduedate
				+ ", revenueexpensetype=" + revenueexpensetype + ", userid=" + userid + "]";
	}
	
	@JsonGetter("rowid")
	public Integer getRowid() {
		return id;
	}

}