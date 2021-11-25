/*
* FinTP - Financial Transactions Processing Application
* Copyright (C) 2013 Business Information Systems (Allevo) S.R.L.
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>
* or contact Allevo at : 031281 Bucuresti, 23C Calea Vitan, Romania,
* phone +40212554577, office@allevo.ro <mailto:office@allevo.ro>, www.allevo.ro.
*/

package ro.allevo.fintpws.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import ro.allevo.fintpws.util.Invariants;


/**
 * The persistent class for the msgtypes database table.
 * 
 */
@Entity
@Table(schema = "FINCFG", name = "MESSAGETYPES")
@NamedQueries({ 
	@NamedQuery(name = "MsgTypeListEntity.findById", query = "select m from MsgTypeListEntity m where m.messageType = :id"),
	@NamedQuery(name = "MsgTypeListEntity.findByBusinessArea", query = "select m from MsgTypeListEntity m where m.businessArea = :businessarea"),
	@NamedQuery(name = "MsgTypeListEntity.findAll", query = "select m from MsgTypeListEntity m order by m.friendlyName asc"),
	@NamedQuery(name = "MsgTypeListEntity.findTotal", query = "select count(m) from MsgTypeListEntity m"),
	})
public class MsgTypeListEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	
	@Column(length = 100, nullable = false, name="businessarea")
	private String businessArea;

	@Column(name="displayorder")
	private Integer displayOrder;
	
	@Column(length = 50, nullable = false, name="friendlyname")
	private String friendlyName;

	@Column(length = 50, name = "messagetype")
	@Id
	private String messageType;
	
	@Column(length = 35, name="parentmessagetype")
	private String parentMessageType;

	@Column(name="reportingstorage")
	private String reportingStorage;
	
	@Column(length = 35, nullable = false)
	private String storage;
	
	public String getFriendlyName() {
		return this.friendlyName;
	}

	public void setFriendlyName(String friendlyname) {
		this.friendlyName = friendlyname;
	}

	public String getStorage() {
		return this.storage;
	}

	public void setStorage(String storage) {
		this.storage = storage;
	}

	public String getBusinessArea() {
		return businessArea;
	}

	public void setBusinessArea(String businessArea) {
		this.businessArea = businessArea;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getParentMessageType() {
		return parentMessageType;
	}

	public void setParentMessageType(String parentMessageType) {
		this.parentMessageType = parentMessageType;
	}

	public String getReportingStorage() {
		return reportingStorage;
	}

	public void setReportingStorage(String reportingStorage) {
		this.reportingStorage = reportingStorage;
	}

	@Override
	public String toString() {
		return "MsgTypeListEntity [businessArea=" + businessArea + ", displayOrder=" + displayOrder + ", friendlyName="
				+ friendlyName + ", messageType=" + messageType + ", parentMessageType=" + parentMessageType
				+ ", reportingStorage=" + reportingStorage + ", storage=" + storage + "]";
	}

	@Override
	public String getClassEvent() {
		return Invariants.configClassEvents.MANAGE.toString();
	}
	
	@Override
	public String getMessage() {
		return "entry in list message type";
	}

}