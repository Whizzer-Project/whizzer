/*
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

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ro.allevo.fintpws.util.Invariants;
import ro.allevo.fintpws.util.annotations.URLId;


/**
 * The persistent class for the SERVICEMAPS database table.
 * 
 */
@Entity
@Table(schema="FINCFG", name="SERVICEMAPS")
@NamedQueries({
	@NamedQuery(name = "ServiceMapEntity.findById", query = "SELECT t FROM ServiceMapEntity t "
			+ "WHERE t.id = :id"),
	@NamedQuery(name = "ServiceMapEntity.findAll", query = "SELECT t FROM ServiceMapEntity t order by t.name asc"),
	@NamedQuery(name = "ServiceMapEntity.findTotal", query = "SELECT count(t.name) FROM ServiceMapEntity t")
})
@Cacheable(false)
public class ServiceMapEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	@Id 
	@Column(nullable=false, precision=10)
	@URLId
	private long id;

	@Column(length=50, name="delayednotificationqueue")
	private String delayedNotificationQueue;

	@Column(name="duplicatecheck")
	private long duplicateCheck;

	@Column(length=50, name="duplicatemap")
	private String duplicateMap;

	@Column(length=50, name="duplicatenotificationqueue")
	private String duplicateNotificationQueue;

	@Column(length=50, name="duplicatequeue")
	private String duplicateQueue;

	@Column(length=300, name="exitpoint")
	private String exitPoint;

	@Column(unique=true,nullable=false, length=30, name="name")
	private String name;

	@Column(nullable=false, precision=10, name="heartbeatinterval")
	private long heartbeatInterval;

	@Column(precision=5, name="ioidentifier")
	private long ioIdentifier;

	@Column(name="lastheartbeat")
	private Timestamp lastHeartbeat;

	@Column(precision=10, name="lastsessionid")
	private long lastSessionId;

	@Column(length=255)
	private String partner;


	@Column(precision=10, name="servicetype")
	private long serviceType;

	@Column(length=26, name="sessionid")
	private String sessionId;

	@Column(nullable=false, precision=10)
	private long status;

	@Column(length=255)
	private String version;
	
	@OneToMany
	@JoinColumn(name="service")
	private List<StatusEntity> statusEntityList;
	
	
	public long getId() {
		return id;
	}

	public String getDelayedNotificationQueue() {
		return delayedNotificationQueue;
	}

	public long getDuplicateCheck() {
		return duplicateCheck;
	}

	public String getDuplicateMap() {
		return duplicateMap;
	}

	public String getDuplicateNotificationQueue() {
		return duplicateNotificationQueue;
	}

	public String getDuplicateQueue() {
		return duplicateQueue;
	}

	public String getExitPoint() {
		return exitPoint;
	}

	public String getName() {
		return name;
	}

	public long getHeartbeatInterval() {
		return heartbeatInterval;
	}

	public long getIoIdentifier() {
		return ioIdentifier;
	}

	public Timestamp getLastHeartbeat() {
		return lastHeartbeat;
	}

	public long getLastSessionId() {
		return lastSessionId;
	}

	public String getPartner() {
		return partner;
	}

	public long getServiceType() {
		return serviceType;
	}

	public String getSessionId() {
		return sessionId;
	}

	public long getStatus() {
		return status;
	}

	public String getVersion() {
		return version;
	}

	@Override
	public String toString() {
		return name;
	}

	public void setDelayedNotificationQueue(String delayedNotificationQueue) {
		this.delayedNotificationQueue = delayedNotificationQueue;
	}

	public void setDuplicateCheck(long duplicateCheck) {
		this.duplicateCheck = duplicateCheck;
	}

	public void setDuplicateMap(String duplicateMap) {
		this.duplicateMap = duplicateMap;
	}

	public void setDuplicateNotificationQueue(String duplicateNotificationQueue) {
		this.duplicateNotificationQueue = duplicateNotificationQueue;
	}

	public void setDuplicateQueue(String duplicateQueue) {
		this.duplicateQueue = duplicateQueue;
	}

	public void setExitPoint(String exitPoint) {
		this.exitPoint = exitPoint;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setHeartbeatInterval(long heartbeatInterval) {
		this.heartbeatInterval = heartbeatInterval;
	}

	public void setIoIdentifier(long ioIdentifier) {
		this.ioIdentifier = ioIdentifier;
	}

	public void setLastHeartbeat(Timestamp lastHeartbeat) {
		this.lastHeartbeat = lastHeartbeat;
	}

	public void setLastSessionId(long lastSessionId) {
		this.lastSessionId = lastSessionId;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public void setServiceType(long serviceType) {
		this.serviceType = serviceType;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public void setStatus(long status) {
		this.status = status;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@JsonIgnore
	public List<StatusEntity> getStatusEntityList() {
		return statusEntityList;
	}

	public void setStatusEntityList(List<StatusEntity> statusEntityList) {
		this.statusEntityList = statusEntityList;
	}

	@Override
	public String getClassEvent() {
		return Invariants.configClassEvents.MANAGE.toString();
	}
	
	@Override
	public String getMessage() {
		return "services";
	}
}
