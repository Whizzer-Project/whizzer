package ro.allevo.whizzer.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BalanceSheetLoadHistory {
	
	@NotNull
	private Integer id;
	
	private String entity;
	
	private Timestamp insertdate;
	
	private BigDecimal omfp1;
	
	private BigDecimal omfp2;
	
	private BigDecimal omfp3;
	
	private BigDecimal omfp5;
	
	private BigDecimal omfp8;
	
	private BigDecimal omfp9;
	
	private BigDecimal omfp10;
	
	private BigDecimal omfp11;
	
	private BigDecimal omfp12;
	
	private BigDecimal omfp31;
	
	private BigDecimal omfp39;
	
	private BigDecimal omfp45;
	
	private BigDecimal omfp46;
	
	private BigDecimal omfp47;
	
	private BigDecimal omfp43;
	
	private BigDecimal omfp44;
	
	private BigDecimal omfp48;
	
	private BigDecimal omfp18;
	
	private BigDecimal omfp19;
	
	private BigDecimal omfp20;	
		
	private BigDecimal omfp15;
	
	private String source;

	private String username;

	private Integer year;


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

	public BigDecimal getOmfp10() {
		return this.omfp10;
	}

	public void setOmfp10(BigDecimal omfp10) {
		this.omfp10 = omfp10;
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

	public BigDecimal getOmfp20() {
		return this.omfp20;
	}

	public void setOmfp20(BigDecimal omfp20) {
		this.omfp20 = omfp20;
	}

	public BigDecimal getOmfp3() {
		return this.omfp3;
	}

	public void setOmfp3(BigDecimal omfp3) {
		this.omfp3 = omfp3;
	}

	public BigDecimal getOmfp31() {
		return this.omfp31;
	}

	public void setOmfp31(BigDecimal omfp31) {
		this.omfp31 = omfp31;
	}

	public BigDecimal getOmfp39() {
		return this.omfp39;
	}

	public void setOmfp39(BigDecimal omfp39) {
		this.omfp39 = omfp39;
	}

	public BigDecimal getOmfp43() {
		return this.omfp43;
	}

	public void setOmfp43(BigDecimal omfp43) {
		this.omfp43 = omfp43;
	}

	public BigDecimal getOmfp44() {
		return this.omfp44;
	}

	public void setOmfp44(BigDecimal omfp44) {
		this.omfp44 = omfp44;
	}

	public BigDecimal getOmfp45() {
		return this.omfp45;
	}

	public void setOmfp45(BigDecimal omfp45) {
		this.omfp45 = omfp45;
	}

	public BigDecimal getOmfp46() {
		return this.omfp46;
	}

	public void setOmfp46(BigDecimal omfp46) {
		this.omfp46 = omfp46;
	}

	public BigDecimal getOmfp47() {
		return this.omfp47;
	}

	public void setOmfp47(BigDecimal omfp47) {
		this.omfp47 = omfp47;
	}

	public BigDecimal getOmfp48() {
		return this.omfp48;
	}

	public void setOmfp48(BigDecimal omfp48) {
		this.omfp48 = omfp48;
	}

	public BigDecimal getOmfp5() {
		return this.omfp5;
	}

	public void setOmfp5(BigDecimal omfp5) {
		this.omfp5 = omfp5;
	}

	public BigDecimal getOmfp8() {
		return this.omfp8;
	}

	public void setOmfp8(BigDecimal omfp8) {
		this.omfp8 = omfp8;
	}

	public BigDecimal getOmfp9() {
		return this.omfp9;
	}

	public void setOmfp9(BigDecimal omfp9) {
		this.omfp9 = omfp9;
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
