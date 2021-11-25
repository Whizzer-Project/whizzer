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
 * The persistent class for the plloadhistory database table.
 * 
 */

@Entity
@Table(schema = "FINDATA", name="PLLOADHISTORY")
@NamedQueries({
	@NamedQuery(name = "ProfitLossEntity.findAll", query = "select p from ProfitLossEntity p"),
	@NamedQuery(name = "ProfitLossEntity.findTotal", query = "select count(p.id) from ProfitLossEntity p"),
	@NamedQuery(name = "ProfitLossEntity.findById", query = "select p from ProfitLossEntity p where p.id=:id") })
@Cacheable(false)
public class ProfitLossEntity extends BaseEntity {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="ProfitLossEntityIdGenerator")
	@TableGenerator(name="ProfitLossEntityIdGenerator", table="FINCFG.IDGENLIST",
	pkColumnName="TABCOLNAME", valueColumnName="IDVALUE",
	pkColumnValue="PLLOADHISTORY_ID") 
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
	private BigDecimal omfp11;
	@Column
	private BigDecimal omfp12;
	@Column
	private BigDecimal omfp15;
	@Column
	private BigDecimal omfp16;
	@Column
	private BigDecimal omfp17;
	@Column
	private BigDecimal omfp18;
	@Column
	private BigDecimal omfp19;
	@Column
	private BigDecimal omfp2;
	@Column
	private BigDecimal omfp21;
	@Column
	private BigDecimal omfp24;
	@Column
	private BigDecimal omfp3;
	@Column
	private BigDecimal omfp30;
	@Column
	private BigDecimal omfp40;
	@Column
	private BigDecimal omfp41;
	@Column
	private BigDecimal omfp42;
	@Column
	private BigDecimal omfp45;
	@Column
	private BigDecimal omfp48;
	@Column
	private BigDecimal omfp50;
	@Column
	private BigDecimal omfp54;
	@Column
	private BigDecimal omfp56;
	@Column
	private BigDecimal omfp57;
	@Column
	private BigDecimal omfp58;
	@Column
	private BigDecimal omfp59;
	@Column
	private BigDecimal omfp60;
	@Column
	private BigDecimal omfp61;
	@Column
	private BigDecimal omfp62;
	@Column
	private BigDecimal omfp63;
	@Column
	private BigDecimal omfp64;
	@Column
	private BigDecimal omfp67;
	@Column
	private BigDecimal omfp68;
	@Column
	private BigDecimal omfp8;
	@Column
	private String source;
	@Column
	private String username;
	@Column
	private Integer year;
	@Column
	private String level;
	
	public ProfitLossEntity() {
	}

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

	@Override
	public String toString() {
		return "ProfitLossEntity [entity=" + entity + ", insertdate=" + insertdate + ", omfp1=" + omfp1 + ", omfp11="
				+ omfp11 + ", omfp12=" + omfp12 + ", omfp15=" + omfp15 + ", omfp16=" + omfp16 + ", omfp17=" + omfp17
				+ ", omfp18=" + omfp18 + ", omfp19=" + omfp19 + ", omfp2=" + omfp2 + ", omfp21=" + omfp21 + ", omfp24="
				+ omfp24 + ", omfp3=" + omfp3 + ", omfp30=" + omfp30 + ", omfp40=" + omfp40 + ", omfp41=" + omfp41
				+ ", omfp42=" + omfp42 + ", omfp45=" + omfp45 + ", omfp48=" + omfp48 + ", omfp50=" + omfp50
				+ ", omfp54=" + omfp54 + ", omfp56=" + omfp56 + ", omfp57=" + omfp57 + ", omfp58=" + omfp58
				+ ", omfp59=" + omfp59 + ", omfp60=" + omfp60 + ", omfp61=" + omfp61 + ", omfp62=" + omfp62
				+ ", omfp63=" + omfp63 + ", omfp64=" + omfp64 + ", omfp67=" + omfp67 + ", omfp68=" + omfp68 + ", omfp8="
				+ omfp8 + ", source=" + source + ", username=" + username + ", year=" + year + "]";
	}

	@Override
	public String getClassEvent() {
		return Invariants.whizzerClassEvents.BSMODULE.toString();
	}

	@Override
	public String getMessage() {
		return "profit & loss";
	}

}
