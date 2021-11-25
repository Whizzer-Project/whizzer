package ro.allevo.whizzer.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PKIEntity{
	
	private Integer id;
	
	private BigDecimal emplcostsinturnover;
	private BigDecimal profitrate;
	private BigDecimal debitratio;
	private BigDecimal currentratio;
	private BigDecimal roe;
	private BigDecimal assetrurnoverratio;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public BigDecimal getEmplcostsinturnover() {
		return emplcostsinturnover;
	}
	public void setEmplcostsinturnover(BigDecimal emplcostsinturnover) {
		this.emplcostsinturnover = emplcostsinturnover;
	}
	public BigDecimal getProfitrate() {
		return profitrate;
	}
	public void setProfitrate(BigDecimal profitrate) {
		this.profitrate = profitrate;
	}
	public BigDecimal getDebitratio() {
		return debitratio;
	}
	public void setDebitratio(BigDecimal debitratio) {
		this.debitratio = debitratio;
	}
	public BigDecimal getCurrentratio() {
		return currentratio;
	}
	public void setCurrentratio(BigDecimal currentratio) {
		this.currentratio = currentratio;
	}
	public BigDecimal getRoe() {
		return roe;
	}
	public void setRoe(BigDecimal roe) {
		this.roe = roe;
	}
	public BigDecimal getAssetrurnoverratio() {
		return assetrurnoverratio;
	}
	public void setAssetrurnoverratio(BigDecimal assetrurnoverratio) {
		this.assetrurnoverratio = assetrurnoverratio;
	}
	
}
