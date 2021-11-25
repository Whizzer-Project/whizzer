package ro.allevo.whizzer.model;

import java.io.Serializable;
import javax.persistence.*;

import ro.allevo.fintpws.model.BaseEntity;
import ro.allevo.fintpws.util.Invariants;
import ro.allevo.fintpws.util.annotations.URLId;

import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the cfforecast database table.
 * 
 */
@Entity
@Table(schema = "findata", name="cfforecast")
@NamedQueries({
	@NamedQuery(name="CashFlowForecastEntity.findAll", query="SELECT c FROM CashFlowForecastEntity c"),
	@NamedQuery(name="CashFlowForecastEntity.findTotal", query="SELECT count(c) FROM CashFlowForecastEntity c"),
	@NamedQuery(name="CashFlowForecastEntity.findById", query = "select c from CashFlowForecastEntity c where c.id=:id")
})

public class CashFlowForecastEntity  extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="CashFlowForecastEntityGenerator")
	@TableGenerator(name="CashFlowForecastEntityGenerator", table="FINCFG.IDGENLIST",
	pkColumnName="TABCOLNAME", valueColumnName="IDVALUE",
	pkColumnValue="CASHFLOWFORECAST_ID") 
	@Column(unique=true, nullable=false, updatable=false, name="id")
	@URLId
	private Integer id;

	private String entity;
	@Column
	private String informationtype;
	@Column
	@Temporal(TemporalType.DATE)
	private Date issuedate;
	@Column
	@Temporal(TemporalType.DATE)
	private Date maturitydate;
	@Column
	private BigDecimal operationamount;
	@Column
	private String operationcurrency;
	@Column
	private String operationiban;
	@Column
	private String operationtype;
	@Column
	private String opretionsubtype;
	@Column
	private String payersuppliername;
	@Column
	private Integer paymentduedate;
	@Column
	private String revenueexpensetype;
	@Column
	private Integer userid;

	public CashFlowForecastEntity() {
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

	public static long getSerialversionuid() {
		return serialVersionUID;
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
				+ operationtype + ", opretionsubtype=" + opretionsubtype
				+ ", payersuppliername=" + payersuppliername + ", paymentduedate=" + paymentduedate
				+ ", revenueexpensetype=" + revenueexpensetype + ", userid=" + userid + "]";
	}

	@Override
	public String getClassEvent() {
		return Invariants.whizzerClassEvents.BSMODULE.toString();
	}

	@Override
	public String getMessage() {
		return "cash flow forecast";
	}

}