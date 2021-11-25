package ro.allevo.fintpws.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import ro.allevo.fintpws.util.Invariants;

@Entity
@IdClass(MessageCriterionPK.class)
@Table(schema = "FINCFG", name="reportmessagecriteriatemplates")
@NamedQueries({
@NamedQuery(name="ReportMessageCriteriaTemplatesEntity.FindAllByTemplateAndBusinessArea", query="Select t from ReportMessageCriteriaTemplatesEntity t "
		+ "where t.messagetypebusinessarea=:businessArea and t.templateName=:template and t.userid=:userId"),
@NamedQuery(name="ReportMessageCriteriaTemplatesEntity.FindAllByBusinessArea", query="Select t from ReportMessageCriteriaTemplatesEntity t "
		+ "where t.messagetypebusinessarea=:businessArea and t.userid=:userId"),
@NamedQuery(name="ReportMessageCriteriaTemplatesEntity.deleteAllForIds", query="Delete from ReportMessageCriteriaTemplatesEntity d where "
		+ "d.messagetypebusinessarea=:businessArea and d.templateName=:template and d.userid=:userId")
})
public class ReportMessageCriteriaTemplatesEntity extends BaseEntity{
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="templatename")
	private String templateName;
	
	@Id
	@Column(name="criterionid")
	private Integer criterionId;
	
	@Column
	private String value;
	
	@Column
	private Integer userid;
	
	@Column
	private String messagetypebusinessarea;
		
	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public Integer getCriterionId() {
		return criterionId;
	}

	public void setCriterionId(Integer criteionId) {
		this.criterionId = criteionId;
	}

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

	@Override
	public String toString() {
		return "ReportMessageCriteriaTemplatesEntity [templateName=" + templateName + ", criterionId=" + criterionId
				+ ", value=" + value + ", userid=" + userid + ", messagetypebusinessarea=" + messagetypebusinessarea
				+ "]";
	}

	@Override
	public String getClassEvent() {
		return Invariants.configClassEvents.MANAGE.toString();
	}
	
	@Override
	public String getMessage() {
		return "";
		//return "Transaction report template.";
	}
}
