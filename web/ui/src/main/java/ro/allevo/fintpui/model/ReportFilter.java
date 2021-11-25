package ro.allevo.fintpui.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportFilter {
	
	Integer criterionId;
	String value;
	Integer userid;
	String messagetypebusinessarea;
	String templateName;
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Integer getUserid() {
		return userid;
	}
	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	public String getMessagetypebusinessarea() {
		return messagetypebusinessarea;
	}
	public void setMessagetypebusinessarea(String messagetypebusinessarea) {
		this.messagetypebusinessarea = messagetypebusinessarea;
	}
	public Integer getCriterionId() {
		return criterionId;
	}
	public void setCriterionId(Integer criterionId) {
		this.criterionId = criterionId;
	}
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	@Override
	public String toString() {
		return "ReportMessageCriteriaTemplatesEntity [templateName=" + templateName + ", criterionId=" + criterionId
				+ ", value=" + value + ", userid=" + userid + ", messagetypebusinessarea=" + messagetypebusinessarea
				+ "]";
	}
}
