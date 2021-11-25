package ro.allevo.whizzer.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;

import ro.allevo.fintpws.model.BaseEntity;
import ro.allevo.fintpws.util.annotations.URLId;

@Entity
@Table(schema = "FINDATA", name="getbskpis")

public class PKIEntity extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column()
	@URLId
	private BigDecimal id;
	
	@Column(nullable=false, length=17)
	private BigDecimal emplcostsinturnover;
	@Column(nullable=false, length=17)
	private BigDecimal profitrate;
	@Column(nullable=false, length=17)
	private BigDecimal debitratio;
	@Column(nullable=false, length=17)
	private BigDecimal currentratio;
	@Column(nullable=false, length=17)
	private BigDecimal roe;
	@Column(nullable=false, length=17)
	private BigDecimal assetrurnoverratio;
	@Column(nullable=false, length=17)
	private BigDecimal id_emplcostsinturnover;
	@Column(nullable=false, length=17)
	private BigDecimal id_profitrate;
	@Column(nullable=false, length=17)
	private BigDecimal id_debitratio;
	@Column(nullable=false, length=17)
	private BigDecimal id_currentratio;
	@Column(nullable=false, length=17)
	private BigDecimal id_roe;
	@Column(nullable=false, length=17)
	private BigDecimal id_assetrurnoverratio;
	
//	public BigDecimal getId() {
//		return id;
//	}
//	public void setId(BigDecimal id) {
//		this.id = id;
//	}
	
	public BigDecimal getId_emplcostsinturnover() {
		return id_emplcostsinturnover;
	}
	public void setId_emplcostsinturnover(BigDecimal id_emplcostsinturnover) {
		this.id_emplcostsinturnover = id_emplcostsinturnover;
	}
	public BigDecimal getId_profitrate() {
		return id_profitrate;
	}
	public void setId_profitrate(BigDecimal id_profitrate) {
		this.id_profitrate = id_profitrate;
	}
	public BigDecimal getId_debitratio() {
		return id_debitratio;
	}
	public void setId_debitratio(BigDecimal id_debitratio) {
		this.id_debitratio = id_debitratio;
	}
	public BigDecimal getId_currentratio() {
		return id_currentratio;
	}
	public void setId_currentratio(BigDecimal id_currentratio) {
		this.id_currentratio = id_currentratio;
	}
	public BigDecimal getId_roe() {
		return id_roe;
	}
	public void setId_roe(BigDecimal id_roe) {
		this.id_roe = id_roe;
	}
	public BigDecimal getId_assetrurnoverratio() {
		return id_assetrurnoverratio;
	}
	public void setId_assetrurnoverratio(BigDecimal id_assetrurnoverratio) {
		this.id_assetrurnoverratio = id_assetrurnoverratio;
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
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getClassEvent() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
