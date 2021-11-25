package ro.allevo.fintpui.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Role {
	private int id;
	@Size(min=1)
	private String name;
	private int userDefined = 1;
	private List<UserDefinedRole> userDefinedRoles;
	
	@Size(min=1)
	private List<String> messageTypes;
	@Size(min=1)
	private List<String> internalEntities;
	
	private List<String> actions;
	private Boolean listOfEntities;
	
	public Boolean getListOfEntities() {
		return listOfEntities;
	}
	public void setListOfEntities(Boolean listOfEntities) {
		this.listOfEntities = listOfEntities;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getUserDefined() {
		return userDefined;
	}
	public List<UserDefinedRole> getUserDefinedRoles() {
		userDefinedRoles = new ArrayList<>();
		
		for (String mt : messageTypes)
			for (String ie : internalEntities)
				userDefinedRoles.add(new UserDefinedRole(mt, ie));
		
		return userDefinedRoles;
	}
	public void setUserDefinedRoles(List<UserDefinedRole> userDefinedRoles) {
		this.userDefinedRoles = userDefinedRoles;
		HashSet<String> mt = new HashSet<>();
		HashSet<String> ie = new HashSet<>();
		
		for (UserDefinedRole role : userDefinedRoles) {
			mt.add(role.getMessageType());
			ie.add(role.getInternalEntityName());
		}
		
		messageTypes = new ArrayList<>(mt);
		internalEntities = new ArrayList<>(ie);
	}
	public List<String> getMessageTypes() {
		return messageTypes;
	}
	public void setMessageTypes(List<String> messageTypes) {
		this.messageTypes = messageTypes;
	}
	public List<String> getInternalEntities() {
		return internalEntities;
	}
	public void setInternalEntities(List<String> internalEntities) {
		this.internalEntities = internalEntities;
	}
	
	public List<String> getActions() {
		return actions;
	}
	
}
