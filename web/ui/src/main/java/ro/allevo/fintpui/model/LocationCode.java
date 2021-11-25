package ro.allevo.fintpui.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationCode{
	
    @Size(min=1,max=100)
	private String entityName;
	@NotNull
	@Size(max=35)
	private String code;
	@NotNull
	@Size(max=35)
	private String name;	
	@Size(max=1)	
	private String defaultValue;
	
	@NotNull
	private long id;

	@JsonGetter("entityName")
	public String getEntityName() {
		return entityName;
	}
	@JsonSetter("entityName")
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	@JsonGetter("rowid")
	public long getRowid() {
		return id;
	}
	
}
