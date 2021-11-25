package ro.allevo.whizzer.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProfitLossLoadHistory {
	
	@NotNull
	private Integer id;
	
	private String entity;

	private Timestamp insertdate;

	private BigDecimal omfp3;
	
	private BigDecimal omfp2;

	private BigDecimal omfp1;

	private BigDecimal omfp8;

	private BigDecimal omfp11;

	private BigDecimal omfp12;

	private BigDecimal omfp15;

	private BigDecimal omfp16;

	private BigDecimal omfp17;

	private BigDecimal omfp18;

	private BigDecimal omfp19;

	private BigDecimal omfp21;

	private BigDecimal omfp24;

	private BigDecimal omfp30;

	private BigDecimal omfp40;

	private BigDecimal omfp41;

	private BigDecimal omfp42;

	private BigDecimal omfp45;

	private BigDecimal omfp48;

	private BigDecimal omfp50;

	private BigDecimal omfp54;

	private BigDecimal omfp56;

	private BigDecimal omfp57;

	private BigDecimal omfp58;

	private BigDecimal omfp59;

	private BigDecimal omfp60;

	private BigDecimal omfp61;

	private BigDecimal omfp62;

	private BigDecimal omfp63;

	private BigDecimal omfp64;

	private BigDecimal omfp67;

	private BigDecimal omfp68;

	private String source;

	private String username;

	private Integer year;

	private String level;
	
	
	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getEntity() {
		return this.entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public Timestamp getInsertdate() {
		return this.insertdate;
	}

	public void setInsertdate(Timestamp insertdate) {
		this.insertdate = insertdate;
	}

	public BigDecimal getOmfp1() {
		return this.omfp1;
	}

	public void setOmfp1(BigDecimal omfp1) {
		this.omfp1 = omfp1;
	}

	public BigDecimal getOmfp11() {
		return this.omfp11;
	}

	public void setOmfp11(BigDecimal omfp11) {
		this.omfp11 = omfp11;
	}

	public BigDecimal getOmfp12() {
		return this.omfp12;
	}

	public void setOmfp12(BigDecimal omfp12) {
		this.omfp12 = omfp12;
	}

	public BigDecimal getOmfp15() {
		return this.omfp15;
	}

	public void setOmfp15(BigDecimal omfp15) {
		this.omfp15 = omfp15;
	}

	public BigDecimal getOmfp16() {
		return this.omfp16;
	}

	public void setOmfp16(BigDecimal omfp16) {
		this.omfp16 = omfp16;
	}

	public BigDecimal getOmfp17() {
		return this.omfp17;
	}

	public void setOmfp17(BigDecimal omfp17) {
		this.omfp17 = omfp17;
	}

	public BigDecimal getOmfp18() {
		return this.omfp18;
	}

	public void setOmfp18(BigDecimal omfp18) {
		this.omfp18 = omfp18;
	}

	public BigDecimal getOmfp19() {
		return this.omfp19;
	}

	public void setOmfp19(BigDecimal omfp19) {
		this.omfp19 = omfp19;
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

	public BigDecimal getOmfp24() {
		return this.omfp24;
	}

	public void setOmfp24(BigDecimal omfp24) {
		this.omfp24 = omfp24;
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

	public BigDecimal getOmfp40() {
		return this.omfp40;
	}

	public void setOmfp40(BigDecimal omfp40) {
		this.omfp40 = omfp40;
	}

	public BigDecimal getOmfp41() {
		return this.omfp41;
	}

	public void setOmfp41(BigDecimal omfp41) {
		this.omfp41 = omfp41;
	}

	public BigDecimal getOmfp42() {
		return this.omfp42;
	}

	public void setOmfp42(BigDecimal omfp42) {
		this.omfp42 = omfp42;
	}

	public BigDecimal getOmfp45() {
		return this.omfp45;
	}

	public void setOmfp45(BigDecimal omfp45) {
		this.omfp45 = omfp45;
	}

	public BigDecimal getOmfp48() {
		return this.omfp48;
	}

	public void setOmfp48(BigDecimal omfp48) {
		this.omfp48 = omfp48;
	}

	public BigDecimal getOmfp50() {
		return this.omfp50;
	}

	public void setOmfp50(BigDecimal omfp50) {
		this.omfp50 = omfp50;
	}

	public BigDecimal getOmfp54() {
		return this.omfp54;
	}

	public void setOmfp54(BigDecimal omfp54) {
		this.omfp54 = omfp54;
	}

	public BigDecimal getOmfp56() {
		return this.omfp56;
	}

	public void setOmfp56(BigDecimal omfp56) {
		this.omfp56 = omfp56;
	}

	public BigDecimal getOmfp57() {
		return this.omfp57;
	}

	public void setOmfp57(BigDecimal omfp57) {
		this.omfp57 = omfp57;
	}

	public BigDecimal getOmfp58() {
		return this.omfp58;
	}

	public void setOmfp58(BigDecimal omfp58) {
		this.omfp58 = omfp58;
	}

	public BigDecimal getOmfp59() {
		return this.omfp59;
	}

	public void setOmfp59(BigDecimal omfp59) {
		this.omfp59 = omfp59;
	}

	public BigDecimal getOmfp60() {
		return this.omfp60;
	}

	public void setOmfp60(BigDecimal omfp60) {
		this.omfp60 = omfp60;
	}

	public BigDecimal getOmfp61() {
		return this.omfp61;
	}

	public void setOmfp61(BigDecimal omfp61) {
		this.omfp61 = omfp61;
	}

	public BigDecimal getOmfp62() {
		return this.omfp62;
	}

	public void setOmfp62(BigDecimal omfp62) {
		this.omfp62 = omfp62;
	}

	public BigDecimal getOmfp63() {
		return this.omfp63;
	}

	public void setOmfp63(BigDecimal omfp63) {
		this.omfp63 = omfp63;
	}

	public BigDecimal getOmfp64() {
		return this.omfp64;
	}

	public void setOmfp64(BigDecimal omfp64) {
		this.omfp64 = omfp64;
	}

	public BigDecimal getOmfp67() {
		return this.omfp67;
	}

	public void setOmfp67(BigDecimal omfp67) {
		this.omfp67 = omfp67;
	}

	public BigDecimal getOmfp68() {
		return this.omfp68;
	}

	public void setOmfp68(BigDecimal omfp68) {
		this.omfp68 = omfp68;
	}

	public BigDecimal getOmfp8() {
		return this.omfp8;
	}

	public void setOmfp8(BigDecimal omfp8) {
		this.omfp8 = omfp8;
	}

	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getYear() {
		return this.year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
