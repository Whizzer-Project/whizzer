package ro.allevo.fintpws.model;

import java.io.Serializable;


public class MessageCriterionPK implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer criterionId;
	private String templateName;
	
	public MessageCriterionPK() {
		
	}

	/**
	 * @param criterionId
	 * @param templateName
	 */
	public MessageCriterionPK(Integer criterionId, String templateName) {
		this.criterionId = criterionId;
		this.templateName = templateName;
	}
}
