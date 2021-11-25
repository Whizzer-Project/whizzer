package ro.allevo.fintpui.model;

import java.util.LinkedHashMap;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EnrichRules {
	
	@NotNull
	private Integer id;
	@NotNull
	@Size(min=1, max=50)
	private String name;
	private String txField;
	private String bussList;
	private String bussListField;
	private String txType;
	private LinkedHashMap<String, Object> conditions;
	private Integer userId;
	private TemplateConfig txtemplatesconfig;
	private Boolean mandatory;
	private TemplateConfigOptions txtemplatesconfigoption;
	//private Integer optionId;
	private String pattern;
	
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
	public String getTxField() {
		return txField;
	}
	public void setTxField(String txField) {
		this.txField = txField;
	}
	public String getBussList() {
		return bussList;
	}
	public void setBussList(String bussList) {
		this.bussList = bussList;
	}
	public String getBussListField() {
		return bussListField;
	}
	public void setBussListField(String bussListField) {
		this.bussListField = bussListField;
	}
	public String getTxType() {
		return txType;
	}
	public void setTxType(String txType) {
		this.txType = txType;
	}
	public LinkedHashMap<String, Object> getConditions() {
		return conditions;
	}
	public void setConditions(LinkedHashMap<String, Object> conditions) {
		this.conditions = conditions;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	@JsonGetter("rowid")
	public Integer getRowid() {
		return id;
	}
	public TemplateConfig getTxtemplatesconfig() {
		return txtemplatesconfig;
	}
	public void setTxtemplatesconfig(TemplateConfig txtemplatesconfig) {
		this.txtemplatesconfig = txtemplatesconfig;
	}
	public Boolean getMandatory() {
		return mandatory;
	}
	public void setMandatory(Boolean mandatory) {
		this.mandatory = mandatory;
	}
	public String getPattern() {
		return pattern;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	public TemplateConfigOptions getTxtemplatesconfigoption() {
		return txtemplatesconfigoption;
	}
	public void setTxtemplatesconfigoption(TemplateConfigOptions txtemplatesconfigoption) {
		this.txtemplatesconfigoption = txtemplatesconfigoption;
	}
}
