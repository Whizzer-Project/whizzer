package ro.allevo.fintpui.model;

import java.util.LinkedHashMap;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TemplateConfigDetailed {

	private Integer id;
	@NotNull
    @Size(min=1,max=35,message = "must not be empty")
	private String fieldlabel;
	private String fieldvalue;
	private Integer fieldvisibility;
	private String fieldxpath;
	private Boolean mandatory;
	private String pattern;
	private Integer fieldid;
	private String fieldtype;
	private TemplateConfig txtemplatesconfig;
	//private Integer configid;
	private TemplateConfigOptions txtemplatesconfigoption;
	//private TemplateDetailed[] txtemplatesdetaileds;
	private Boolean editable;
	private String busslist;
	private String busslistfield;
	private LinkedHashMap<String, Object> conditions;
	private Boolean visible;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getFieldlabel() {
		return fieldlabel;
	}
	public void setFieldlabel(String fieldlabel) {
		this.fieldlabel = fieldlabel;
	}
//	public Integer getConfigid() {
//		return configid;
//	}
//	public void setConfigid(Integer configid) {
//		this.configid = configid;
//	}
	public String getFieldvalue() {
		return fieldvalue;
	}
	public void setFieldvalue(String fieldvalue) {
		this.fieldvalue = fieldvalue;
	}
	public Integer getFieldvisibility() {
		return fieldvisibility;
	}
	public void setFieldvisibility(Integer fieldvisibility) {
		this.fieldvisibility = fieldvisibility;
	}
	public String getFieldxpath() {
		return fieldxpath;
	}
	public void setFieldxpath(String fieldxpath) {
		this.fieldxpath = fieldxpath;
	}
	public TemplateConfigOptions getTxtemplatesconfigoption() {
		return txtemplatesconfigoption;
	}
	public void setTxtemplatesconfigoption(TemplateConfigOptions txtemplatesconfigoption) {
		this.txtemplatesconfigoption = txtemplatesconfigoption;
	}
//	@Override
//	public String toString() {
//		String txtemplatesconfigoptionId ="";
//		if (null != this.txtemplatesconfigoption) {
//			txtemplatesconfigoptionId  += this.txtemplatesconfigoption.getId();
//		}
//		return "TemplateConfigDetailed [id=" + id + ", fieldid=" + fieldid + ", configid=" + configid + ", fieldxpath="
//				+ fieldxpath + ", fieldlabel=" + fieldlabel + ", optionId=" + txtemplatesconfigoptionId + "]";
//	}
	
	public Boolean getMandatory() {
		return mandatory;
	}
	public Integer getFieldid() {
		return fieldid;
	}
	public void setFieldid(Integer fieldid) {
		this.fieldid = fieldid;
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
	public TemplateConfig getTxtemplatesconfig() {
		return txtemplatesconfig;
	}
	public void setTxtemplatesconfig(TemplateConfig txtemplatesconfig) {
		this.txtemplatesconfig = txtemplatesconfig;
	}
//	public TemplateDetailed[] getTxtemplatesdetaileds() {
//		return txtemplatesdetaileds;
//	}
//	public void setTxtemplatesdetaileds(TemplateDetailed[] txtemplatesdetaileds) {
//		this.txtemplatesdetaileds = txtemplatesdetaileds;
//	}
	public Boolean getEditable() {
		return editable;
	}
	public void setEditable(Boolean editable) {
		this.editable = editable;
	}
	public String getFieldtype() {
		return fieldtype;
	}
	public void setFieldtype(String fieldtype) {
		this.fieldtype = fieldtype;
	}
	public String getBusslist() {
		return busslist;
	}
	public void setBusslist(String busslist) {
		this.busslist = busslist;
	}
	public String getBusslistfield() {
		return busslistfield;
	}
	public void setBusslistfield(String busslistfield) {
		this.busslistfield = busslistfield;
	}
	public LinkedHashMap<String, Object> getConditions() {
		return conditions;
	}
	public void setConditions(LinkedHashMap<String, Object> conditions) {
		this.conditions = conditions;
	}
	
	@JsonGetter("rowid")
	public String getRowid() {
		return ""+id;
	}
	public Boolean getVisible() {
		return visible;
	}
	public void setVisible(Boolean visible) {
		this.visible = visible;
	}
//	public void setVisible(Integer visible) {
//		this.visible = visible==1?true:false;
//	}
//	public void setVisible(String visible) {
//		this.visible = visible=="true"?true:false;
//	}
}
