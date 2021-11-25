package ro.allevo.whizzer.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.cache.annotation.Cacheable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class BalanceSheetForecast implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;

	private String entity;

	private Integer forecast;

	private Integer historicalbs;

	private BigDecimal omfp12;

	private BigDecimal omfp12f;

	private BigDecimal omfp15;

	private BigDecimal omfp15f;

	private BigDecimal omfp16;

	private BigDecimal omfp16f;

	private BigDecimal omfp2;

	private BigDecimal omfp21;

	private BigDecimal omfp21f;

	private BigDecimal omfp2f;

	private BigDecimal omfp3;

	private BigDecimal omfp30;

	private BigDecimal omfp30f;

	private BigDecimal omfp3f;

	private BigDecimal omfp40;

	private BigDecimal omfp40f;

	private BigDecimal omfp50;

	private BigDecimal omfp50f;

	private BigDecimal omfp57;

	private BigDecimal omfp57f;

	private BigDecimal omfp60;

	private BigDecimal omfp60f;

	private BigDecimal omfp61;

	private BigDecimal omfp61f;

	private BigDecimal omfp62;

	private BigDecimal omfp62f;

	private BigDecimal omfp63;

	private BigDecimal omfp63f;

	private Integer realisedbs;

	private String status;

	private Integer userid;

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

	public Integer getForecast() {
		return this.forecast;
	}

	public void setForecast(Integer forecast) {
		this.forecast = forecast;
	}

	public Integer getHistoricalbs() {
		return this.historicalbs;
	}

	public void setHistoricalbs(Integer historicalbs) {
		this.historicalbs = historicalbs;
	}

	public BigDecimal getOmfp12() {
		return this.omfp12;
	}

	public void setOmfp12(BigDecimal omfp12) {
		this.omfp12 = omfp12;
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

	public BigDecimal getOmfp21f() {
		return this.omfp21f;
	}

	public void setOmfp21f(BigDecimal omfp21f) {
		this.omfp21f = omfp21f;
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

	public BigDecimal getOmfp30f() {
		return this.omfp30f;
	}

	public void setOmfp30f(BigDecimal omfp30f) {
		this.omfp30f = omfp30f;
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

	public BigDecimal getOmfp63f() {
		return this.omfp63f;
	}

	public void setOmfp63f(BigDecimal omfp63f) {
		this.omfp63f = omfp63f;
	}

	public Integer getRealisedbs() {
		return this.realisedbs;
	}

	public void setRealisedbs(Integer realisedbs) {
		this.realisedbs = realisedbs;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getUserid() {
		return this.userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	@Override
	public String toString() {
		return "BalanceSheetForecastEntity [id=" + id + ", entity=" + entity + ", forecast=" + forecast
				+ ", historicalbs=" + historicalbs + ", omfp12=" + omfp12 + ", omfp12f=" + omfp12f + ", omfp15="
				+ omfp15 + ", omfp15f=" + omfp15f + ", omfp16=" + omfp16 + ", omfp16f=" + omfp16f + ", omfp2=" + omfp2
				+ ", omfp21=" + omfp21 + ", omfp21f=" + omfp21f + ", omfp2f=" + omfp2f + ", omfp3=" + omfp3
				+ ", omfp30=" + omfp30 + ", omfp30f=" + omfp30f + ", omfp3f=" + omfp3f + ", omfp40=" + omfp40
				+ ", omfp40f=" + omfp40f + ", omfp50=" + omfp50 + ", omfp50f=" + omfp50f + ", omfp57=" + omfp57
				+ ", omfp57f=" + omfp57f + ", omfp60=" + omfp60 + ", omfp60f=" + omfp60f + ", omfp61=" + omfp61
				+ ", omfp61f=" + omfp61f + ", omfp62=" + omfp62 + ", omfp62f=" + omfp62f + ", omfp63=" + omfp63
				+ ", omfp63f=" + omfp63f + ", realisedbs=" + realisedbs + ", status=" + status + ", userid=" + userid
				+ "]";
	}


}