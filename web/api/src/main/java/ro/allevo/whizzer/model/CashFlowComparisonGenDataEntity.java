package ro.allevo.whizzer.model;

import java.io.Serializable;
import javax.persistence.*;

import ro.allevo.fintpws.model.BaseEntity;
import ro.allevo.fintpws.util.Invariants;

import java.math.BigDecimal;
import java.util.Date;


//@Entity
//@Table(schema = "findata", name="cfforecast")
//@NamedQueries({
//	@NamedQuery(name="CashFlowComparisonGenDataEntity.findAll", query="SELECT c FROM CashFlowComparisonGenDataEntity c"),
//	@NamedQuery(name="CashFlowComparisonGenDataEntity.findTotal", query="SELECT count(c) FROM CashFlowComparisonGenDataEntity c"),
//	@NamedQuery(name="CashFlowComparisonGenDataEntity.findById", query = "select c from CashFlowComparisonGenDataEntity c where c.id=:id")
//})

@Entity
@NamedStoredProcedureQueries({
	@NamedStoredProcedureQuery(
		name = "CashFlowComparisonGenDataEntity.getcfcomparison",
		procedureName = "findata.getcfcomparison",
		parameters = {
				@StoredProcedureParameter(name = "inentity", type = String.class, mode = ParameterMode.IN),
				@StoredProcedureParameter(name = "incfforecastdate", type = Date.class, mode = ParameterMode.IN),
				@StoredProcedureParameter(name = "outretcursor", type = void.class, mode = ParameterMode.REF_CURSOR),
		}
	)
})
public class CashFlowComparisonGenDataEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	
	
	@Id
	private Integer id;
	
	private String indicator;
	private String iban;
	
	private BigDecimal  amount;
	private String  currency;
	@Temporal(TemporalType.DATE)
	private Date accountdate;
	
	
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIndicator() {
		return indicator;
	}

	public void setIndicator(String indicator) {
		this.indicator = indicator;
	}

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Date getAccountdate() {
		return accountdate;
	}

	public void setAccountdate(Date accountdate) {
		this.accountdate = accountdate;
	}



	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

}