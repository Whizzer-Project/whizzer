package ro.allevo.fintpuiws.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import ro.allevo.fintpws.model.BaseEntity;
import ro.allevo.fintpws.util.Invariants;

@Entity
@Table(schema = "FINCFG", name = "USERACTIONMAPS")
@NamedQueries({
	@NamedQuery(name = "UserActionMapEntity.findActionsForSelection", query = "SELECT ua FROM UserActionMapEntity u JOIN u.userActionEntity ua "
			+ "WHERE ua.selectedMessage=1 and u.messageType = :messageType and u.queueTypeId = :queueTypeId"),
	@NamedQuery(name = "UserActionMapEntity.findActionsForGroup", query = "SELECT ua FROM UserActionMapEntity u JOIN u.userActionEntity ua "
			+ "WHERE ua.groupMessage=1 and u.messageType = :messageType and u.queueTypeId = :queueTypeId")
	})
public class UserActionMapEntity extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private long id;
	
	@Column(name = "queuetypeid")
	private long queueTypeId;
	
	@Column(name = "useractionid")
	private long userActionId;
	
	@Column(name = "messagetype")
	private String messageType;

	@OneToOne(fetch = FetchType.EAGER)
	@PrimaryKeyJoinColumn(name = "useractionid")
	private UserActionEntity userActionEntity;

	public long getId() {
		return id;
	}

	public long getQueueTypeId() {
		return queueTypeId;
	}

	public long getUserActionId() {
		return userActionId;
	}

	public String getMessageType() {
		return messageType;
	}

	public UserActionEntity getUserActionEntity() {
		return userActionEntity;
	}

	@Override
	public String toString() {
		return "UserActionMapEntity [id=" + id + ", queueTypeId=" + queueTypeId + ", userActionId=" + userActionId
				+ ", messageType=" + messageType + ", userActionEntity=" + userActionEntity + "]";
	}

	@Override
	public String getClassEvent() {
		return Invariants.userClassEvents.MANAGE.toString();
	}
	
	@Override
	public String getMessage() {
		return "user action";
	}
}
