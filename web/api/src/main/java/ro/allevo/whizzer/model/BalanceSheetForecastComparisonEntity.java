package ro.allevo.whizzer.model;

import java.io.Serializable;
import javax.persistence.*;

import ro.allevo.fintpws.model.BaseEntity;
import ro.allevo.fintpws.util.Invariants;

import java.math.BigDecimal;


/**
 * The persistent class for the BalanceSheetForecastComparisonEntity database table.
 * 
 */
@Entity
@Table(schema = "findata", name="bsforecastcomparison")
@NamedQueries({
	@NamedQuery(name = "BalanceSheetForecastComparisonEntity.findAll", query = "select b from BalanceSheetForecastComparisonEntity b"),
	@NamedQuery(name = "BalanceSheetForecastComparisonEntity.findTotal", query = "select count(b.omfp12) from BalanceSheetForecastComparisonEntity b")})
@Cacheable(false)
public class BalanceSheetForecastComparisonEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String entity;

	private BigDecimal omfp12;

	private BigDecimal omfp12diff;

	private BigDecimal omfp12f;

	private BigDecimal omfp15;

	private BigDecimal omfp15diff;

	private BigDecimal omfp15f;

	private BigDecimal omfp16;

	private BigDecimal omfp16diff;

	private BigDecimal omfp16f;

	private BigDecimal omfp2;

	private BigDecimal omfp21;

	private BigDecimal omfp21diff;

	private BigDecimal omfp21f;

	private BigDecimal omfp2diff;

	private BigDecimal omfp2f;

	private BigDecimal omfp3;

	private BigDecimal omfp30;

	private BigDecimal omfp30diff;

	private BigDecimal omfp30f;

	private BigDecimal omfp3diff;

	private BigDecimal omfp3f;

	private BigDecimal omfp40;

	private BigDecimal omfp40diff;

	private BigDecimal omfp40f;

	private BigDecimal omfp50;

	private BigDecimal omfp50diff;

	private BigDecimal omfp50f;

	private BigDecimal omfp57;

	private BigDecimal omfp57diff;

	private BigDecimal omfp57f;

	private BigDecimal omfp60;

	private BigDecimal omfp60diff;

	private BigDecimal omfp60f;

	private BigDecimal omfp61;

	private BigDecimal omfp61diff;

	private BigDecimal omfp61f;

	private BigDecimal omfp62;

	private BigDecimal omfp62diff;

	private BigDecimal omfp62f;

	private BigDecimal omfp63;

	private BigDecimal omfp63diff;

	private BigDecimal omfp63f;

	private Integer year;

	public BalanceSheetForecastComparisonEntity() {
	}

	public String getEntity() {
		return this.entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public BigDecimal getOmfp12() {
		return this.omfp12;
	}

	public void setOmfp12(BigDecimal omfp12) {
		this.omfp12 = omfp12;
	}

	public BigDecimal getOmfp12diff() {
		return this.omfp12diff;
	}

	public void setOmfp12diff(BigDecimal omfp12diff) {
		this.omfp12diff = omfp12diff;
	}

	public BigDecimal getOmfp12f() {
		return this.omfp12f;
	}

	public void setOmfp12f(BigDecimal omfp12f) {
		this.omfp12f = omfp12f;
	}

	public BigDecimal getOmfp15() {
		return this.omfp15;
	}

	public void setOmfp15(BigDecimal omfp15) {
		this.omfp15 = omfp15;
	}

	public BigDecimal getOmfp15diff() {
		return this.omfp15diff;
	}

	public void setOmfp15diff(BigDecimal omfp15diff) {
		this.omfp15diff = omfp15diff;
	}

	public BigDecimal getOmfp15f() {
		return this.omfp15f;
	}

	public void setOmfp15f(BigDecimal omfp15f) {
		this.omfp15f = omfp15f;
	}

	public BigDecimal getOmfp16() {
		return this.omfp16;
	}

	public void setOmfp16(BigDecimal omfp16) {
		this.omfp16 = omfp16;
	}

	public BigDecimal getOmfp16diff() {
		return this.omfp16diff;
	}

	public void setOmfp16diff(BigDecimal omfp16diff) {
		this.omfp16diff = omfp16diff;
	}

	public BigDecimal getOmfp16f() {
		return this.omfp16f;
	}

	public void setOmfp16f(BigDecimal omfp16f) {
		this.omfp16f = omfp16f;
	}

	public BigDecimal getOmfp2() {
		return this.omfp2;
	}

	public void setOmfp2(BigDecimal omfp2) {
		this.omfp2 = omfp2;
	}

	public BigDecimal getOmfp21() {
		return this.omfp21;
	}

	public void setOmfp21(BigDecimal omfp21) {
		this.omfp21 = omfp21;
	}

	public BigDecimal getOmfp21diff() {
		return this.omfp21diff;
	}

	public void setOmfp21diff(BigDecimal omfp21diff) {
		this.omfp21diff = omfp21diff;
	}

	public BigDecimal getOmfp21f() {
		return this.omfp21f;
	}

	public void setOmfp21f(BigDecimal omfp21f) {
		this.omfp21f = omfp21f;
	}

	public BigDecimal getOmfp2diff() {
		return this.omfp2diff;
	}

	public void setOmfp2diff(BigDecimal omfp2diff) {
		this.omfp2diff = omfp2diff;
	}

	public BigDecimal getOmfp2f() {
		return this.omfp2f;
	}

	public void setOmfp2f(BigDecimal omfp2f) {
		this.omfp2f = omfp2f;
	}

	public BigDecimal getOmfp3() {
		return this.omfp3;
	}

	public void setOmfp3(BigDecimal omfp3) {
		this.omfp3 = omfp3;
	}

	public BigDecimal getOmfp30() {
		return this.omfp30;
	}

	public void setOmfp30(BigDecimal omfp30) {
		this.omfp30 = omfp30;
	}

	public BigDecimal getOmfp30diff() {
		return this.omfp30diff;
	}

	public void setOmfp30diff(BigDecimal omfp30diff) {
		this.omfp30diff = omfp30diff;
	}

	public BigDecimal getOmfp30f() {
		return this.omfp30f;
	}

	public void setOmfp30f(BigDecimal omfp30f) {
		this.omfp30f = omfp30f;
	}

	public BigDecimal getOmfp3diff() {
		return this.omfp3diff;
	}

	public void setOmfp3diff(BigDecimal omfp3diff) {
		this.omfp3diff = omfp3diff;
	}

	public BigDecimal getOmfp3f() {
		return this.omfp3f;
	}

	public void setOmfp3f(BigDecimal omfp3f) {
		this.omfp3f = omfp3f;
	}

	public BigDecimal getOmfp40() {
		return this.omfp40;
	}

	public void setOmfp40(BigDecimal omfp40) {
		this.omfp40 = omfp40;
	}

	public BigDecimal getOmfp40diff() {
		return this.omfp40diff;
	}

	public void setOmfp40diff(BigDecimal omfp40diff) {
		this.omfp40diff = omfp40diff;
	}

	public BigDecimal getOmfp40f() {
		return this.omfp40f;
	}

	public void setOmfp40f(BigDecimal omfp40f) {
		this.omfp40f = omfp40f;
	}

	public BigDecimal getOmfp50() {
		return this.omfp50;
	}

	public void setOmfp50(BigDecimal omfp50) {
		this.omfp50 = omfp50;
	}

	public BigDecimal getOmfp50diff() {
		return this.omfp50diff;
	}

	public void setOmfp50diff(BigDecimal omfp50diff) {
		this.omfp50diff = omfp50diff;
	}

	public BigDecimal getOmfp50f() {
		return this.omfp50f;
	}

	public void setOmfp50f(BigDecimal omfp50f) {
		this.omfp50f = omfp50f;
	}

	public BigDecimal getOmfp57() {
		return this.omfp57;
	}

	public void setOmfp57(BigDecimal omfp57) {
		this.omfp57 = omfp57;
	}

	public BigDecimal getOmfp57diff() {
		return this.omfp57diff;
	}

	public void setOmfp57diff(BigDecimal omfp57diff) {
		this.omfp57diff = omfp57diff;
	}

	public BigDecimal getOmfp57f() {
		return this.omfp57f;
	}

	public void setOmfp57f(BigDecimal omfp57f) {
		this.omfp57f = omfp57f;
	}

	public BigDecimal getOmfp60() {
		return this.omfp60;
	}

	public void setOmfp60(BigDecimal omfp60) {
		this.omfp60 = omfp60;
	}

	public BigDecimal getOmfp60diff() {
		return this.omfp60diff;
	}

	public void setOmfp60diff(BigDecimal omfp60diff) {
		this.omfp60diff = omfp60diff;
	}

	public BigDecimal getOmfp60f() {
		return this.omfp60f;
	}

	public void setOmfp60f(BigDecimal omfp60f) {
		this.omfp60f = omfp60f;
	}

	public BigDecimal getOmfp61() {
		return this.omfp61;
	}

	public void setOmfp61(BigDecimal omfp61) {
		this.omfp61 = omfp61;
	}

	public BigDecimal getOmfp61diff() {
		return this.omfp61diff;
	}

	public void setOmfp61diff(BigDecimal omfp61diff) {
		this.omfp61diff = omfp61diff;
	}

	public BigDecimal getOmfp61f() {
		return this.omfp61f;
	}

	public void setOmfp61f(BigDecimal omfp61f) {
		this.omfp61f = omfp61f;
	}

	public BigDecimal getOmfp62() {
		return this.omfp62;
	}

	public void setOmfp62(BigDecimal omfp62) {
		this.omfp62 = omfp62;
	}

	public BigDecimal getOmfp62diff() {
		return this.omfp62diff;
	}

	public void setOmfp62diff(BigDecimal omfp62diff) {
		this.omfp62diff = omfp62diff;
	}

	public BigDecimal getOmfp62f() {
		return this.omfp62f;
	}

	public void setOmfp62f(BigDecimal omfp62f) {
		this.omfp62f = omfp62f;
	}

	public BigDecimal getOmfp63() {
		return this.omfp63;
	}

	public void setOmfp63(BigDecimal omfp63) {
		this.omfp63 = omfp63;
	}

	public BigDecimal getOmfp63diff() {
		return this.omfp63diff;
	}

	public void setOmfp63diff(BigDecimal omfp63diff) {
		this.omfp63diff = omfp63diff;
	}

	public BigDecimal getOmfp63f() {
		return this.omfp63f;
	}

	public void setOmfp63f(BigDecimal omfp63f) {
		this.omfp63f = omfp63f;
	}

	public Integer getYear() {
		return this.year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	@Override
	public String toString() {
		return "BalanceSheetForecastComparisonEntity [entity=" + entity + ", omfp12=" + omfp12 + ", omfp12diff="
				+ omfp12diff + ", omfp12f=" + omfp12f + ", omfp15=" + omfp15 + ", omfp15diff=" + omfp15diff
				+ ", omfp15f=" + omfp15f + ", omfp16=" + omfp16 + ", omfp16diff=" + omfp16diff + ", omfp16f=" + omfp16f
				+ ", omfp2=" + omfp2 + ", omfp21=" + omfp21 + ", omfp21diff=" + omfp21diff + ", omfp21f=" + omfp21f
				+ ", omfp2diff=" + omfp2diff + ", omfp2f=" + omfp2f + ", omfp3=" + omfp3 + ", omfp30=" + omfp30
				+ ", omfp30diff=" + omfp30diff + ", omfp30f=" + omfp30f + ", omfp3diff=" + omfp3diff + ", omfp3f="
				+ omfp3f + ", omfp40=" + omfp40 + ", omfp40diff=" + omfp40diff + ", omfp40f=" + omfp40f + ", omfp50="
				+ omfp50 + ", omfp50diff=" + omfp50diff + ", omfp50f=" + omfp50f + ", omfp57=" + omfp57
				+ ", omfp57diff=" + omfp57diff + ", omfp57f=" + omfp57f + ", omfp60=" + omfp60 + ", omfp60diff="
				+ omfp60diff + ", omfp60f=" + omfp60f + ", omfp61=" + omfp61 + ", omfp61diff=" + omfp61diff
				+ ", omfp61f=" + omfp61f + ", omfp62=" + omfp62 + ", omfp62diff=" + omfp62diff + ", omfp62f=" + omfp62f
				+ ", omfp63=" + omfp63 + ", omfp63diff=" + omfp63diff + ", omfp63f=" + omfp63f + ", year=" + year + "]";
	}

	@Override
	public String getClassEvent() {
		return Invariants.whizzerClassEvents.BSMODULE.toString();
	}

	@Override
	public String getMessage() {
		return "balance sheet forecast comparison";
	}

}