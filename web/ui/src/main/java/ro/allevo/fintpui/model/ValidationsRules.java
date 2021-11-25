package ro.allevo.fintpui.model;

import java.util.LinkedHashMap;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ValidationsRules {
	
	@NotNull
	private Integer id;
	
	@NotNull
	@Size(min=1, max=50)
	private String name;
	
	private String type;
	private String algorithm;
	private String txField;
	private String txType;
	private LinkedHashMap<String, Object> conditions;	
	private Integer userId;
	private Integer configId;
	
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAlgorithm() {
		return algorithm;
	}
	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
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
	public Integer getConfigId() {
		return configId;
	}
	public void setConfigId(Integer configId) {
		this.configId = configId;
	}
	@JsonGetter("rowid")
	public Integer getRowid() {
		return id;
	}
}
