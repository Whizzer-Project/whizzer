package ro.allevo.whizzer.model;

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
@Table(schema = "FINCFG", name="CONFIGBSANDPLREPO")
@NamedQueries({
	@NamedQuery(name = "ConfigBsandPlEntity.findAll", query = "select b from ConfigBsandPlEntity b"),
	@NamedQuery(name = "ConfigBsandPlEntity.findTotal", query = "select count(b.name) from ConfigBsandPlEntity b")
})
@Cacheable(false)
public class ConfigBsandPlEntity extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(unique=true, nullable=false, length=8, updatable=false)
	@URLId
	private Integer id;
	
	private String label;
	@Column(nullable=false)
	
	private String name;
	@Column(nullable=false)
	private String mandatory;
	@Column(nullable=false)
	private String reportingcategory;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMandatory() {
		return mandatory;
	}

	public void setMandatory(String mandatory) {
		this.mandatory = mandatory;
	}

	public String getReportingcategory() {
		return reportingcategory;
	}

	public void setReportingcategory(String reportingcategory) {
		this.reportingcategory = reportingcategory;
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
