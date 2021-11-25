package ro.allevo.whizzer.model;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;

import ro.allevo.fintpws.model.BaseEntity;
import ro.allevo.fintpws.util.Invariants;
import ro.allevo.fintpws.util.annotations.URLId;

import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the cfcomparison database table.
 * 
 */
@Entity
@Table(schema = "findata", name="cfcomparison")
@NamedQueries({
	@NamedQuery(name = "CashFlowComparisonEntity.findAll", query = "select b from CashFlowComparisonEntity b"),
	@NamedQuery(name = "CashFlowComparisonEntity.findTotal", query = "select count(b.id) from CashFlowComparisonEntity b"),
	@NamedQuery(name = "CashFlowComparisonEntity.findById", query = "select b from CashFlowComparisonEntity b where b.id=:id")})
@Cacheable(false)
public class CashFlowComparisonEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="CashFlowComparisonEntityGenerator")
	@TableGenerator(name="CashFlowComparisonEntityGenerator", table="FINCFG.IDGENLIST",
	pkColumnName="TABCOLNAME", valueColumnName="IDVALUE",
	pkColumnValue="CFFORECASTGENDATA_ID") 
	@Column(unique=true, nullable=false, updatable=false, name="id")
	@URLId
	private Integer id;
	@Column
	private BigDecimal accountbalanceamount;
	@Column
	private String accountbalancecurrency;
	//@Column
	@Temporal(TemporalType.DATE)
	@JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
	private Date accountbalancedate;
	@Column
	private String balanceiban;
	@Column
	private String entity;
	@Column
	private BigDecimal exchangerate;
	@Column
	private BigDecimal ronaccountbalanceamount;
	@Column
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

	public Date getAccountbalancedate() {
		return this.accountbalancedate;
	}

	public void setAccountbalancedate(Date accountbalancedate) {
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


	
	@Override
	public String toString() {
		return "CashFlowComparisonEntity [id=" + id + ", accountbalanceamount=" + accountbalanceamount
				+ ", accountbalancecurrency=" + accountbalancecurrency + ", accountbalancedate=" + accountbalancedate
				+ ", balanceiban=" + balanceiban + ", entity=" + entity + ", exchangerate=" + exchangerate
				+ ", ronaccountbalanceamount=" + ronaccountbalanceamount + ", userid=" + userid + "]";
	}

	@Override
	public String getClassEvent() {
		return Invariants.whizzerClassEvents.BSMODULE.toString();
	}

	@Override
	public String getMessage() {
		return "cash flow comparison";
	}

}