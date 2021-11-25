package ro.allevo.fintpui.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
public class BudgetCode{
	
	@NotNull
	private long id;
	@NotNull
	@Size(max=35)
	private String code;
	@NotNull
	@Size(max=35)
	private String name;

	
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
