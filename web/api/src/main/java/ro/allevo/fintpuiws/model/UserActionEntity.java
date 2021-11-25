package ro.allevo.fintpuiws.model;

import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ro.allevo.fintpws.model.BaseEntity;
import ro.allevo.fintpws.util.Invariants;

@Entity
@Table(schema = "FINCFG", name = "USERACTIONS")
@Cacheable(false)
public class UserActionEntity extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private long id;
	
	private String name;
	
	private String label;
	
	@Column(name = "currentmessage")
	private int currentMessage;
	
	@Column(name = "selectedmessage")
	private int selectedMessage;
	
	@Column(name = "groupmessage")
	private int groupMessage;
	
	private int priority;
	
	private String location;
	
	@Column(name = "codeselection")
	private int codeSelection;
	
	private String function;

	@Column(name = "detailsinput")
	private int detailsInput;
	
	@OneToMany(targetEntity = UserActionCodeEntity.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "useractionid", referencedColumnName = "id")
	private List<UserActionCodeEntity> userActionCodeEntity;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getDetailsInput() {
		return detailsInput;
	}

	public void setDetailsInput(int detailsInput) {
		this.detailsInput = detailsInput;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getCurrentMessage() {
		return currentMessage;
	}

	public void setCurrentMessage(int currentMessage) {
		this.currentMessage = currentMessage;
	}

	public int getSelectedMessage() {
		return selectedMessage;
	}

	public void setSelectedMessage(int selectedMessage) {
		this.selectedMessage = selectedMessage;
	}

	public int getGroupMessage() {
		return groupMessage;
	}

	public void setGroupMessage(int groupMessage) {
		this.groupMessage = groupMessage;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getCodeSelection() {
		return codeSelection;
	}

	public void setCodeSelection(int codeSelection) {
		this.codeSelection = codeSelection;
	}

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}
	
	public List<UserActionCodeEntity> getUserActionCodeEntity() {
		return userActionCodeEntity;
	}

	@Override
	public String toString() {
		return "UserActionEntity [id=" + id + ", name=" + name + ", label=" + label + ", currentMessage="
				+ currentMessage + ", selectedMessage=" + selectedMessage + ", groupMessage=" + groupMessage
				+ ", priority=" + priority + ", location=" + location + ", codeSelection=" + codeSelection
				+ ", function=" + function + ", detailsInput=" + detailsInput + ", userActionCodeEntity="
				+ userActionCodeEntity + "]";
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
