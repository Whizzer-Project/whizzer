package ro.allevo.whizzer.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import ro.allevo.fintpws.model.BaseEntity;
import ro.allevo.fintpws.util.Invariants;
import ro.allevo.fintpws.util.annotations.URLId;

/**
 * The persistent class for the bsloadhistory database table.
 * 
 */
@Entity
@Table(schema = "FINDATA", name="BSLOADHISTORY")
@NamedQueries({
	@NamedQuery(name = "BalanceSheetEntity.findAll", query = "select b from BalanceSheetEntity b"),
	@NamedQuery(name = "BalanceSheetEntity.findTotal", query = "select count(b.id) from BalanceSheetEntity b"),
	@NamedQuery(name = "BalanceSheetEntity.findById", query = "select b from BalanceSheetEntity b where b.id=:id")})
@Cacheable(false)
public class BalanceSheetEntity extends BaseEntity {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="BalanceSheetEntityIdGenerator")
	@TableGenerator(name="BalanceSheetEntityIdGenerator", table="FINCFG.IDGENLIST",
	pkColumnName="TABCOLNAME", valueColumnName="IDVALUE",
	pkColumnValue="BSLOADHISTORY_ID") 
	@Column(unique=true, nullable=false, updatable=false, name="id")
	@URLId
	private Integer id;
	
	@Column
	private String entity;
	@Column
	private Timestamp insertdate;
	@Column
	private BigDecimal omfp1;
	@Column
	private BigDecimal omfp10;
	@Column
	private BigDecimal omfp11;
	@Column
	private BigDecimal omfp12;
	@Column
	private BigDecimal omfp15;
	@Column
	private BigDecimal omfp18;
	@Column
	private BigDecimal omfp19;
	@Column
	private BigDecimal omfp2;
	@Column
	private BigDecimal omfp20;
	@Column
	private BigDecimal omfp3;
	@Column
	private BigDecimal omfp31;
	@Column
	private BigDecimal omfp39;
	@Column
	private BigDecimal omfp43;
	@Column
	private BigDecimal omfp44;
	@Column
	private BigDecimal omfp45;
	@Column
	private BigDecimal omfp46;
	@Column
	private BigDecimal omfp47;
	@Column
	private BigDecimal omfp48;
	@Column
	private BigDecimal omfp5;
	@Column
	private BigDecimal omfp8;
	@Column
	private BigDecimal omfp9;
	@Column
	private String source;
	@Column
	private String username;
	@Column
	private Integer year;

	public BalanceSheetEntity() {
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

	@Override
	public String toString() {
		return "BalanceSheetEntity [entity=" + entity + ", insertdate=" + insertdate + ", omfp1=" + omfp1 + ", omfp10="
				+ omfp10 + ", omfp11=" + omfp11 + ", omfp12=" + omfp12 + ", omfp15=" + omfp15 + ", omfp18=" + omfp18
				+ ", omfp19=" + omfp19 + ", omfp2=" + omfp2 + ", omfp20=" + omfp20 + ", omfp3=" + omfp3 + ", omfp31="
				+ omfp31 + ", omfp39=" + omfp39 + ", omfp43=" + omfp43 + ", omfp44=" + omfp44 + ", omfp45=" + omfp45
				+ ", omfp46=" + omfp46 + ", omfp47=" + omfp47 + ", omfp48=" + omfp48 + ", omfp5=" + omfp5 + ", omfp8="
				+ omfp8 + ", omfp9=" + omfp9 + ", source=" + source + ", username=" + username + ", year=" + year + "]";
	}

	@Override
	public String getClassEvent() {
		return Invariants.whizzerClassEvents.BSMODULE.toString();
	}

	@Override
	public String getMessage() {
		return "balance sheet";
	}

}
