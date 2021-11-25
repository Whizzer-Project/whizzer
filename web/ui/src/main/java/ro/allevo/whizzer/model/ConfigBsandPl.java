package ro.allevo.whizzer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ConfigBsandPl {


	private Integer id;
	private String name;
	private String label;
	private String mandatory;
	private String reportingcategory;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
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

}
