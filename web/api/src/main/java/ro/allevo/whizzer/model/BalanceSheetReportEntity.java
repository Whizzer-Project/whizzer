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
@Table(schema = "FINDATA", name="REPBALANCESHEET")
@NamedQueries({
	@NamedQuery(name = "BalanceSheetReportEntity.findAll", query = "select b from BalanceSheetReportEntity b"),
	@NamedQuery(name = "BalanceSheetReportEntity.findTotal", query = "select count(b.entity) from BalanceSheetReportEntity b")
})
@Cacheable(false)
public class BalanceSheetReportEntity extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(unique=true, nullable=false, length=8, updatable=false)
	@URLId
	private BigDecimal id;
	@Column(nullable=false, length=17)
	private String entity;
	@Column(nullable=false, length=17)
	private Integer year;

	@Column(nullable=false, length=17)
	private BigDecimal omfp1;
	@Column(nullable=false, length=17)
	private BigDecimal omfp2;
	@Column(nullable=false, length=17)
	private BigDecimal omfp3;
	@Column(nullable=false, length=17)
	private BigDecimal omfp5;
	@Column(nullable=false, length=17)
	private BigDecimal omfp8;
	@Column(nullable=false, length=17)
	private BigDecimal  omfp9;
	@Column(nullable=false, length=17)
	private BigDecimal omfp10;
	@Column(nullable=false, length=17)
	private BigDecimal omfp11;
	@Column(nullable=false, length=17)
	private BigDecimal omfp12;
	@Column(nullable=false, length=17)
	private BigDecimal  omfp31;
	@Column(nullable=false, length=17)
	private BigDecimal omfp39;
	@Column(nullable=false, length=17)
	private BigDecimal omfp45;
	@Column(nullable=false, length=17)
	private BigDecimal omfp46;
	@Column(nullable=false, length=17)
	private BigDecimal omfp47;
	@Column(nullable=false, length=17)
	private BigDecimal omfp43;
	@Column(nullable=false, length=17)
	private BigDecimal omfp44;
	@Column(nullable=false, length=17)
	private BigDecimal omfp48;
	@Column(nullable=false, length=17)
	private BigDecimal omfp18;
	@Column(nullable=false, length=17)
	private BigDecimal omfp19;
	@Column(nullable=false, length=17)
	private BigDecimal omfp20;
	@Column(nullable=false, length=17)
	private BigDecimal omfp15;
	@Column(nullable=false, length=17)
	private BigDecimal totalact;
	@Column(nullable=false, length=17)
	private BigDecimal totalactimob;
	@Column(nullable=false, length=17)
	private BigDecimal totalcapstr;
	@Column(nullable=false, length=17)
	private BigDecimal totalpas;


	public BigDecimal getId() {
		return id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
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

	public BigDecimal getOmfp1() {
		return omfp1;
	}

	public void setOmfp1(BigDecimal omfp1) {
		this.omfp1 = omfp1;
	}

	public BigDecimal getOmfp2() {
		return omfp2;
	}

	public void setOmfp2(BigDecimal omfp2) {
		this.omfp2 = omfp2;
	}

	public BigDecimal getOmfp3() {
		return omfp3;
	}

	public void setOmfp3(BigDecimal omfp3) {
		this.omfp3 = omfp3;
	}

	public BigDecimal getOmfp5() {
		return omfp5;
	}

	public void setOmfp5(BigDecimal omfp5) {
		this.omfp5 = omfp5;
	}

	public BigDecimal getOmfp8() {
		return omfp8;
	}

	public void setOmfp8(BigDecimal omfp8) {
		this.omfp8 = omfp8;
	}

	public BigDecimal getOmfp9() {
		return omfp9;
	}

	public void setOmfp9(BigDecimal omfp9) {
		this.omfp9 = omfp9;
	}

	public BigDecimal getOmfp10() {
		return omfp10;
	}

	public void setOmfp10(BigDecimal omfp10) {
		this.omfp10 = omfp10;
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

	public BigDecimal getOmfp31() {
		return omfp31;
	}

	public void setOmfp31(BigDecimal omfp31) {
		this.omfp31 = omfp31;
	}

	public BigDecimal getOmfp39() {
		return omfp39;
	}

	public void setOmfp39(BigDecimal omfp39) {
		this.omfp39 = omfp39;
	}

	public BigDecimal getOmfp45() {
		return omfp45;
	}

	public void setOmfp45(BigDecimal omfp45) {
		this.omfp45 = omfp45;
	}

	public BigDecimal getOmfp46() {
		return omfp46;
	}

	public void setOmfp46(BigDecimal omfp46) {
		this.omfp46 = omfp46;
	}

	public BigDecimal getOmfp47() {
		return omfp47;
	}

	public void setOmfp47(BigDecimal omfp47) {
		this.omfp47 = omfp47;
	}

	public BigDecimal getOmfp43() {
		return omfp43;
	}

	public void setOmfp43(BigDecimal omfp43) {
		this.omfp43 = omfp43;
	}

	public BigDecimal getOmfp44() {
		return omfp44;
	}

	public void setOmfp44(BigDecimal omfp44) {
		this.omfp44 = omfp44;
	}

	public BigDecimal getOmfp48() {
		return omfp48;
	}

	public void setOmfp48(BigDecimal omfp48) {
		this.omfp48 = omfp48;
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

	public BigDecimal getOmfp20() {
		return omfp20;
	}

	public void setOmfp20(BigDecimal omfp20) {
		this.omfp20 = omfp20;
	}

	public BigDecimal getOmfp15() {
		return omfp15;
	}

	public void setOmfp15(BigDecimal omfp15) {
		this.omfp15 = omfp15;
	}

	public BigDecimal getTotalact() {
		return totalact;
	}

	public void setTotalact(BigDecimal totalact) {
		this.totalact = totalact;
	}

	public BigDecimal getTotalactimob() {
		return totalactimob;
	}

	public void setTotalactimob(BigDecimal totalactimob) {
		this.totalactimob = totalactimob;
	}

	public BigDecimal getTotalcapstr() {
		return totalcapstr;
	}

	public void setTotalcapstr(BigDecimal totalcapstr) {
		this.totalcapstr = totalcapstr;
	}

	public BigDecimal getTotalpas() {
		return totalpas;
	}

	public void setTotalpas(BigDecimal totalpas) {
		this.totalpas = totalpas;
	}

	@Override
	public String getClassEvent() {
		return null;
	}

	@Override
	public String getMessage() {
		return null;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

}
