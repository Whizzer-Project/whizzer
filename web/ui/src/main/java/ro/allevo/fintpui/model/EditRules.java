package ro.allevo.fintpui.model;

import java.util.LinkedHashMap;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EditRules {
	
	@NotNull
	private Integer id;
	
	@NotNull
	@Size(min=1, max=50)
	private String name;
	
	private String bussList;
	private String bussListField;
    private LinkedHashMap<String, Object> conditions;
	private Boolean mandatory;
	private String txField;
	private String txType;	
	private Integer configId;
	private Integer userId;
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
	public LinkedHashMap<String, Object> getConditions() {
		return conditions;
	}
	public void setConditions(LinkedHashMap<String, Object> conditions) {
		this.conditions = conditions;
	}
	public Boolean getMandatory() {
		return mandatory;
	}
	public void setMandatory(Boolean mandatory) {
		this.mandatory = mandatory;
	}
	public String getTxField() {
		return txField;
	}
	public void setTxField(String txField) {
		this.txField = txField;
	}
	public String getTxType() {
		return txType;
	}
	public void setTxType(String txType) {
		this.txType = txType;
	}
	public Integer getConfigId() {
		return configId;
	}
	public void setConfigId(Integer configId) {
		this.configId = configId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getPattern() {
		return pattern;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	@JsonGetter("rowid")
	public Integer getRowid() {
		return id;
	}
}
