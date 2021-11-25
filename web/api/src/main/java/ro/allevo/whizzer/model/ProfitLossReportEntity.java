package ro.allevo.whizzer.model;

import java.math.BigDecimal;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import ro.allevo.fintpws.model.BaseEntity;
import ro.allevo.fintpws.util.annotations.URLId;

@Entity
@Table(schema = "FINDATA", name="REPPROFITANDLOSS")
@NamedQueries({
	@NamedQuery(name = "ProfitLossReportEntity.findAll", query = "select b from ProfitLossReportEntity b"),
	@NamedQuery(name = "ProfitLossReportEntity.findTotal", query = "select count(b.entity) from ProfitLossReportEntity b")
})
@Cacheable(false)
public class ProfitLossReportEntity extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(unique=true, nullable=false, length=8, updatable=false)
	@URLId
	private String id;
	@Column(nullable=false, length=17)
	private String entity;
	@Column(nullable=false, length=17)
	private Integer year;
	@Column(nullable=false, length=17)
	private BigDecimal omfp3;
	@Column(nullable=false, length=17)
	private BigDecimal omfp2;
	@Column(nullable=false, length=17)
	private BigDecimal omfp1;
	@Column(nullable=false, length=17)
	private BigDecimal omfp8;
	@Column(nullable=false, length=17)
	private BigDecimal omfp11;
	@Column(nullable=false, length=17)
	private BigDecimal omfp12;
	@Column(nullable=false, length=17)
	private BigDecimal omfp15;
	@Column(nullable=false, length=17)
	private BigDecimal omfp16;
	@Column(nullable=false, length=17)
	private BigDecimal omfp17;
	@Column(nullable=false, length=17)
	private BigDecimal omfp18;
	@Column(nullable=false, length=17)
	private BigDecimal omfp19;
	@Column(nullable=false, length=17)
	private BigDecimal omfp21;
	@Column(nullable=false, length=17)
	private BigDecimal omfp24;
	@Column(nullable=false, length=17)
	private BigDecimal omfp30;
	@Column(nullable=false, length=17)
	private BigDecimal omfp40;
	@Column(nullable=true, length=17)
	private BigDecimal omfp41;
	@Column(nullable=true, length=17)
	private BigDecimal omfp42;
	@Column(nullable=false, length=17)
	private BigDecimal omfp45;
	@Column(nullable=false, length=17)
	private BigDecimal omfp48;
	@Column(nullable=false, length=17)
	private BigDecimal omfp50;
	@Column(nullable=false, length=17)
	private BigDecimal omfp54;
	@Column(nullable=false, length=17)
	private BigDecimal omfp56;
	@Column(nullable=false, length=17)
	private BigDecimal omfp57;
	@Column(nullable=true, length=17)
	private BigDecimal omfp58;
	@Column(nullable=true, length=17)
	private BigDecimal omfp59;
	@Column(nullable=false, length=17)
	private BigDecimal omfp60;
	@Column(nullable=false, length=17)
	private BigDecimal omfp61;
	@Column(nullable=false, length=17)
	private BigDecimal omfp62;
	@Column(nullable=true, length=17)
	private BigDecimal omfp63;
	@Column(nullable=true, length=17)
	private BigDecimal omfp64;
	@Column(nullable=true, length=17)
	private BigDecimal omfp67;
	@Column(nullable=true, length=17)
	private BigDecimal omfp68;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ProfitLossReportEntity() {
		super();
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public BigDecimal getOmfp3() {
		return omfp3;
	}

	public void setOmfp3(BigDecimal omfp3) {
		this.omfp3 = omfp3;
	}

	public BigDecimal getOmfp2() {
		return omfp2;
	}

	public void setOmfp2(BigDecimal omfp2) {
		this.omfp2 = omfp2;
	}

	public BigDecimal getOmfp1() {
		return omfp1;
	}

	public void setOmfp1(BigDecimal omfp1) {
		this.omfp1 = omfp1;
	}

	public BigDecimal getOmfp8() {
		return omfp8;
	}

	public void setOmfp8(BigDecimal omfp8) {
		this.omfp8 = omfp8;
	}

	public BigDecimal getOmfp11() {
		return omfp11;
	}

	public void setOmfp11(BigDecimal omfp11) {
		this.omfp11 = omfp11;
	}

	public BigDecimal getOmfp12() {
		return omfp12;
	}

	public void setOmfp12(BigDecimal omfp12) {
		this.omfp12 = omfp12;
	}

	public BigDecimal getOmfp15() {
		return omfp15;
	}

	public void setOmfp15(BigDecimal omfp15) {
		this.omfp15 = omfp15;
	}

	public BigDecimal getOmfp16() {
		return omfp16;
	}

	public void setOmfp16(BigDecimal omfp16) {
		this.omfp16 = omfp16;
	}

	public BigDecimal getOmfp17() {
		return omfp17;
	}

	public void setOmfp17(BigDecimal omfp17) {
		this.omfp17 = omfp17;
	}

	public BigDecimal getOmfp18() {
		return omfp18;
	}

	public void setOmfp18(BigDecimal omfp18) {
		this.omfp18 = omfp18;
	}

	public BigDecimal getOmfp19() {
		return omfp19;
	}

	public void setOmfp19(BigDecimal omfp19) {
		this.omfp19 = omfp19;
	}

	public BigDecimal getOmfp21() {
		return omfp21;
	}

	public void setOmfp21(BigDecimal omfp21) {
		this.omfp21 = omfp21;
	}

	public BigDecimal getOmfp24() {
		return omfp24;
	}

	public void setOmfp24(BigDecimal omfp24) {
		this.omfp24 = omfp24;
	}

	public BigDecimal getOmfp30() {
		return omfp30;
	}

	public void setOmfp30(BigDecimal omfp30) {
		this.omfp30 = omfp30;
	}

	public BigDecimal getOmfp40() {
		return omfp40;
	}

	public void setOmfp40(BigDecimal omfp40) {
		this.omfp40 = omfp40;
	}

	public BigDecimal getOmfp41() {
		return omfp41;
	}

	public void setOmfp41(BigDecimal omfp41) {
		this.omfp41 = omfp41;
	}

	public BigDecimal getOmfp42() {
		return omfp42;
	}

	public void setOmfp42(BigDecimal omfp42) {
		this.omfp42 = omfp42;
	}

	public BigDecimal getOmfp45() {
		return omfp45;
	}

	public void setOmfp45(BigDecimal omfp45) {
		this.omfp45 = omfp45;
	}

	public BigDecimal getOmfp48() {
		return omfp48;
	}

	public void setOmfp48(BigDecimal omfp48) {
		this.omfp48 = omfp48;
	}

	public BigDecimal getOmfp50() {
		return omfp50;
	}

	public void setOmfp50(BigDecimal omfp50) {
		this.omfp50 = omfp50;
	}

	public BigDecimal getOmfp54() {
		return omfp54;
	}

	public void setOmfp54(BigDecimal omfp54) {
		this.omfp54 = omfp54;
	}

	public BigDecimal getOmfp56() {
		return omfp56;
	}

	public void setOmfp56(BigDecimal omfp56) {
		this.omfp56 = omfp56;
	}

	public BigDecimal getOmfp57() {
		return omfp57;
	}

	public void setOmfp57(BigDecimal omfp57) {
		this.omfp57 = omfp57;
	}

	public BigDecimal getOmfp58() {
		return omfp58;
	}

	public void setOmfp58(BigDecimal omfp58) {
		this.omfp58 = omfp58;
	}

	public BigDecimal getOmfp59() {
		return omfp59;
	}

	public void setOmfp59(BigDecimal omfp59) {
		this.omfp59 = omfp59;
	}

	public BigDecimal getOmfp60() {
		return omfp60;
	}

	public void setOmfp60(BigDecimal omfp60) {
		this.omfp60 = omfp60;
	}

	public BigDecimal getOmfp61() {
		return omfp61;
	}

	public void setOmfp61(BigDecimal omfp61) {
		this.omfp61 = omfp61;
	}

	public BigDecimal getOmfp62() {
		return omfp62;
	}

	public void setOmfp62(BigDecimal omfp62) {
		this.omfp62 = omfp62;
	}

	public BigDecimal getOmfp63() {
		return omfp63;
	}

	public void setOmfp63(BigDecimal omfp63) {
		this.omfp63 = omfp63;
	}

	public BigDecimal getOmfp64() {
		return omfp64;
	}

	public void setOmfp64(BigDecimal omfp64) {
		this.omfp64 = omfp64;
	}

	public BigDecimal getOmfp67() {
		return omfp67;
	}

	public void setOmfp67(BigDecimal omfp67) {
		this.omfp67 = omfp67;
	}

	public BigDecimal getOmfp68() {
		return omfp68;
	}

	public void setOmfp68(BigDecimal omfp68) {
		this.omfp68 = omfp68;
	}



	@Override
	public String toString() {
		return "ProfitLossReportEntity [entity=" + entity + ", year=" + year + ", omfp3=" + omfp3 + ", omfp2=" + omfp2
				+ ", omfp1=" + omfp1 + ", omfp8=" + omfp8 + ", omfp11=" + omfp11 + ", omfp12=" + omfp12 + ", omfp15="
				+ omfp15 + ", omfp16=" + omfp16 + ", omfp17=" + omfp17 + ", omfp18=" + omfp18 + ", omfp19=" + omfp19
				+ ", omfp21=" + omfp21 + ", omfp24=" + omfp24 + ", omfp30=" + omfp30 + ", omfp40=" + omfp40
				+ ", omfp41=" + omfp41 + ", omfp42=" + omfp42 + ", omfp45=" + omfp45 + ", omfp48=" + omfp48
				+ ", omfp50=" + omfp50 + ", omfp54=" + omfp54 + ", omfp56=" + omfp56 + ", omfp57=" + omfp57
				+ ", omfp58=" + omfp58 + ", omfp59=" + omfp59 + ", omfp60=" + omfp60 + ", omfp61=" + omfp61
				+ ", omfp62=" + omfp62 + ", omfp63=" + omfp63 + ", omfp64=" + omfp64 + ", omfp67=" + omfp67
				+ ", omfp68=" + omfp68 + "]";
	}

	@Override
	public String getClassEvent() {
		return null;
	}

	@Override
	public String getMessage() {
		return null;
	}

}
