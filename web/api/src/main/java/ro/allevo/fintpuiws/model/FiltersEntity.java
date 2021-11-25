package ro.allevo.fintpuiws.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(schema = "FINCFG", name = "reporterfilterfields")
@NamedQueries({
	@NamedQuery(name = "FiltersEntity.findByBusinessAreaAndUserName", query = "SELECT f FROM FiltersEntity f"
			+ " where f.businessArea=:businessarea and f.userName=:user")
	})
public class FiltersEntity {
	
	@Id
	private long id;
	
	@Column(name = "businessarea")
	private String businessArea;
	
	@Column(name ="user")
	private String userName;
	
	private String label;
	
	private String param;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getBusinessArea() {
		return businessArea;
	}

	public void setBusinessArea(String businessArea) {
		this.businessArea = businessArea;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}
	

}
