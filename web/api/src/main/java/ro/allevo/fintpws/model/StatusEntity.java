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

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import ro.allevo.fintpws.util.Invariants;
import ro.allevo.fintpws.util.annotations.URLId;

/**
 * The persistent class for the STATUS database table.
 * 
 */
@Entity
@Table(schema = "FINDATA", name = "STATUS")
@NamedQueries({
		@NamedQuery(name = "StatusEntity.findById", query = "SELECT s FROM StatusEntity s "
				+ "WHERE trim(s.guid) = :id"),
		@NamedQuery(name = "StatusEntity.findByCorrelationId", query = "SELECT s FROM StatusEntity s "
				+ "WHERE trim(s.correlationid) = :correlationid order by s.insertdate asc, s.guid"),
		@NamedQuery(name = "StatusEntity.findAll", query = "SELECT s FROM StatusEntity s order by s.insertdate asc, s.guid"),
		@NamedQuery(name = "StatusEntity.findTotalByCorrelationId", query = "SELECT count(s.guid) FROM StatusEntity s "
				+ "WHERE trim(s.correlationid) = :correlationid"),
		@NamedQuery(name = "StatusEntity.findTotal", query = "SELECT count(s.guid) FROM StatusEntity s") })
@Cacheable(false)
public class StatusEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Column(length = 3000)
	private String additionalinfo;

	@Column(name = "CLASS", length = 20)
	//@Transient
	private String clasS;

	@Column(nullable = false, length = 30)
	private String correlationid;

	@Column(nullable = false)
	private Timestamp eventdate;

	@Id
	@GeneratedValue(generator = "StatusGuidGenerator")
	@TableGenerator(name = "StatusGuidGenerator", table = "FINCFG.IDGENLIST", pkColumnName = "TABCOLNAME", valueColumnName = "IDVALUE", pkColumnValue = "STATUS_GUID")
	@Column(unique = true, nullable = false, length = 30)
	@URLId
	private String guid;

	@Column(length = 3500)
	private String innerexception;

	@Column(nullable = false)
	private Timestamp insertdate;

	@Column(nullable = false, length = 30)
	private String machine;

	@Column(nullable = false, length = 256)
	private String message;

	@Column(nullable = false, precision = 10)
	private BigDecimal service;

	@ManyToOne(cascade = CascadeType.PERSIST , fetch = FetchType.LAZY)
	@JoinColumn(name="service",insertable=false, updatable=false)
	private ServiceMapEntity serviceMap;
		
	@Column(length = 30)
	//@Transient
	private String sessionid;

	@Column(nullable = false, length = 20)
	private String type;
	
	
	//TODO verificare daca exista userid; daca nu exista, se comenteaza unde avem erori
//	@Transient
	private Integer userid;
	
	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public String getAdditionalinfo() {
		return this.additionalinfo;
	}

	public void setAdditionalinfo(String additionalinfo) {
		this.additionalinfo = additionalinfo;
	}

	public String getCorrelationid() {
		return this.correlationid;
	}

	public void setCorrelationid(String correlationid) {
		this.correlationid = correlationid;
	}

	public Timestamp getEventdate() {
		return this.eventdate;
	}

	public void setEventdate(Timestamp eventdate) {
		this.eventdate = eventdate;
	}

	public String getGuid() {
		return this.guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getInnerexception() {
		return this.innerexception;
	}

	public void setInnerexception(String innerexception) {
		this.innerexception = innerexception;
	}

	public Timestamp getInsertdate() {
		return this.insertdate;
	}

	public void setInsertdate(Timestamp insertdate) {
		this.insertdate = insertdate;
	}

	public String getMachine() {
		return this.machine;
	}

	public void setMachine(String machine) {
		this.machine = machine;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public BigDecimal getService() {
		return this.service;
	}

	public void setService(BigDecimal service) {
		this.service = service;
	}

	public String getSessionid() {
		return this.sessionid;
	}

	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getClasS() {
		return clasS;
	}

	public void setClasS(String clasS) {
		this.clasS = clasS;
	}

	public ServiceMapEntity getServiceMap() {
		return serviceMap;
	}

	public void setServiceMap(ServiceMapEntity serviceMapEntity) {
		this.serviceMap = serviceMapEntity;
	}

	@Override
	public String toString() {
		return "StatusEntity [additionalinfo=" + additionalinfo + ", clasS=" + clasS + ", correlationid="
				+ correlationid + ", eventdate=" + eventdate + ", guid=" + guid + ", innerexception=" + innerexception
				+ ", insertdate=" + insertdate + ", machine=" + machine + ", message=" + message + ", service="
				+ service + ", serviceMap=" + serviceMap + ", sessionid=" + sessionid + ", type=" + type + ", userid="
				+ userid + "]";
	}

	@Override
	public String getClassEvent() {
		return Invariants.notificationsClassEvents.NOTIFICATIONS.toString();
	}
	
}
