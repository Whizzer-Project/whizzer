package ro.allevo.fintpuiws.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import ro.allevo.fintpws.model.BaseEntity;
import ro.allevo.fintpws.util.Invariants;

@Entity
@Table(schema = "FINCFG", name = "USERACTIONCODES")
public class UserActionCodeEntity extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private long id;
	
	@Column(name="useractionid")
	private long userActionId;
	
	private String code;
	
	private String label;
	
	private String description;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserActionId() {
		return userActionId;
	}

	public void setUserActionId(long userActionId) {
		this.userActionId = userActionId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "UserActionCodeEntity [id=" + id + ", userActionId=" + userActionId + ", code=" + code + ", label="
				+ label + ", description=" + description + "]";
	}

	@Override
	public String getClassEvent() {
		return Invariants.userClassEvents.MANAGE.toString();
	}
	
	@Override
	public String getMessage() {
		return "user action code";
	}
	
}
