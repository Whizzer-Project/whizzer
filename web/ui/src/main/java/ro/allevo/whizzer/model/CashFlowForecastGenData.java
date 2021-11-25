package ro.allevo.whizzer.model;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CashFlowForecastGenData {
	private Integer id;

	private BigDecimal amt;

	private Date cfforecastdate;

	private String entity;

	private String indicator;

	private String operationcurrency;

	private String operationiban;

	public CashFlowForecastGenData() {
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

}