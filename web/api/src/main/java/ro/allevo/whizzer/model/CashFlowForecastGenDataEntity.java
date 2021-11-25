package ro.allevo.whizzer.model;

import javax.persistence.*;

import ro.allevo.fintpws.model.BaseEntity;
import ro.allevo.fintpws.util.Invariants;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;


/**
 * The persistent class for the cfforecastgendata database table.
 * 
 */
@Entity
@Table(schema = "FINDATA", name="CFFORECASTGENDATA")
@NamedStoredProcedureQueries({
	@NamedStoredProcedureQuery(
		name = "CashFlowForecastGenDataEntity.getcfforest",
		procedureName = "findata.getcfforest",
		parameters = {
				@StoredProcedureParameter(name = "inentity", type = String.class, mode = ParameterMode.IN),
				@StoredProcedureParameter(name = "incfforecastdate", type = LocalDate.class, mode = ParameterMode.IN),
				@StoredProcedureParameter(name = "outretcursor", type = void.class, mode = ParameterMode.REF_CURSOR),
		}
	)
})
@NamedQueries({
	@NamedQuery(name="CashFlowForecastGenDataEntity.findAll", query="SELECT c FROM CashFlowForecastGenDataEntity c"),
	@NamedQuery(name = "CashFlowForecastGenDataEntity.findTotal", query = "select count(c.id) from CashFlowForecastGenDataEntity c"),
	@NamedQuery(name = "CashFlowForecastGenDataEntity.findById", query = "select c from CashFlowForecastGenDataEntity c where c.id=:id")	
	})
public class CashFlowForecastGenDataEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false, updatable=false)
	private Integer id;

	private BigDecimal amt;

	@Temporal(TemporalType.DATE)
	private Date cfforecastdate;

	private String entity;

	private String indicator;

	private String operationcurrency;

	private String operationiban;

	public CashFlowForecastGenDataEntity() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public BigDecimal getAmt() {
		return this.amt;
	}

	public void setAmt(BigDecimal amt) {
		this.amt = amt;
	}

	public Date getCfforecastdate() {
		return this.cfforecastdate;
	}

	public void setCfforecastdate(Date cfforecastdate) {
		this.cfforecastdate = cfforecastdate;
	}

	public String getEntity() {
		return this.entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public String getIndicator() {
		return this.indicator;
	}

	public void setIndicator(String indicator) {
		this.indicator = indicator;
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

	@Override
	public String toString() {
		return "CashFlowForecastingGenDataEntity [id=" + id + ", amt=" + amt + ", cfforecastdate=" + cfforecastdate
				+ ", entity=" + entity + ", indicator=" + indicator + ", operationcurrency=" + operationcurrency
				+ ", operationiban=" + operationiban + "]";
	}

	@Override
	public String getClassEvent() {
		return Invariants.whizzerClassEvents.BSMODULE.toString();
	}

	@Override
	public String getMessage() {
		return null;
	}

}