package ro.allevo.fintpuiws.model;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class FinTPAccessLists {
	private String schema;
	private String name;
	private String apiServiceName;
	private String[] columns;
	
	public FinTPAccessLists() {}
	
	public FinTPAccessLists(String name, String schema, String apiServiceName, String[] columns) {
		super();
		this.name = name;
		this.apiServiceName = apiServiceName;
		this.columns = columns;
		this.schema = schema;
	}
	public FinTPAccessLists(String name, String apiServiceName, String[] columns) {
		super();
		this.name = name;
		this.apiServiceName = apiServiceName;
		this.columns = columns;
		this.schema = "finlist";
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTable() {
		return apiServiceName;
	}
	public void setTable(String tableName) {
		this.apiServiceName = tableName;
	}
	public String getSchema() {
		return schema;
	}
	public void setSchema(String schema) {
		this.schema = schema;
	}
	public String[] getColumns() {
		return columns;
	}
	@JsonIgnore
	public String getStringColumns() {
		return Arrays.toString(columns);
	}
	public void setColumns(String ...columns) {
		this.columns = columns;
	}
	@JsonIgnore
	public String getFormattedData() {
		return this.name +":"+this.schema+"."+this.apiServiceName+"."+this.getStringColumns();
	}
}
