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

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ro.allevo.fintpws.util.Invariants;
import ro.allevo.fintpws.util.annotations.URLId;

/**
 * The persistent class for the ENTRYQUEUE database table.
 * 
 */
@Entity
@Table(schema = "FINDATA", name = "ENTRYQUEUE")
/*
 * @NamedNativeQueries(value = { @NamedNativeQuery(name =
 * "EntryQueueEntity.findDistinctMessagesQueueNQ", query =
 * "SELECT DISTINCT t0.MESSAGETYPE AS a1, t0.BUSINESSAREA AS a2," +
 * "t0.DISPLAYORDER AS a3, t0.FRIENDLYNAME AS a4, t0.MTID AS a5, t0.PARENTMSGTYPE AS a6,"
 * +"t0.REPORTINGSTORAGE AS a7, t0.STORAGE AS a8" +"FROM FINDATA.ENTRYQUEUE t2"
 * +
 * "LEFT OUTER JOIN FINDATA.ROUTEDMESSAGES t1 ON (t1.CORRELATIONID = t2.CORRELATIONID)"
 * +
 * "left join FINCFG.MSGTYPES t0 on ((t2.QUEUENAME = :queuename) AND (t1.MSGTYPE = t0.MESSAGETYPE))"
 * ) })
 */
@NamedQueries({
		// @NamedQuery(name = "EntryQueueEntity.test", query =
		// " SELECT DISTINCT t0 FROM EntryQueueEntity t2 LEFT OUTER JOIN RoutedMessageEntity t1 ON (t1.CORRELATIONID = t2.CORRELATIONID) left join MsgTypeListEntity t0 on ((t2.QUEUENAME = :queuename) AND (t1.MSGTYPE = t0.MESSAGETYPE))"),
		@NamedQuery(name = "EntryQueueEntity.findAllQueue", query = "SELECT b FROM EntryQueueEntity b "
				+ " left join b.routedMessage r"
				+ " where b.queueName=:queuename order by b.id "),
		@NamedQuery(name = "EntryQueueEntity.findById", query = "SELECT b FROM EntryQueueEntity b "
				+ "WHERE b.id = :id"),
		@NamedQuery(name = "EntryQueueEntity.findTotalQueue", query = "SELECT count(b) FROM EntryQueueEntity b "
				+ "WHERE b.queueName=:queuename"),
		@NamedQuery(name = "EntryQueueEntity.findMessageTypesQueue", query = "SELECT distinct r.messageType FROM EntryQueueEntity b  left join b.routedMessage r where b.queueName= :queuename "),
		//@NamedQuery(name = "EntryQueueEntity.findTotalMessageTypesQueue", query = "SELECT count(distinct r.messageType) FROM EntryQueueEntity b  left join b.routedMessage r where b.queueName= :queuename "),

		@NamedQuery(name = "EntryQueueEntity.findDistinctMessagesQueue",
        query = "        SELECT distinct m FROM EntryQueueEntity eq "
                + "        LEFT OUTER JOIN eq.routedMessage r on (r.correlationId = eq.correlationId)"
                + "        left outer join MsgTypeListEntity m on (r.messageType = m.messageType)"
                + "        WHERE (eq.queueName = :queuename)"),
		@NamedQuery(name = "EntryQueueEntity.findTotalDistinctMessagesQueue", query = "SELECT count(distinct r.messageType)   FROM EntryQueueEntity b "
				+ " left join b.routedMessage r where b.queueName= :queuename"),
		@NamedQuery(name = "EntryQueueEntity.findGroupMessagesQueue", query = "SELECT distinct r.messageType FROM EntryQueueEntity b "
				+ " join b.routedMessage r where b.queueName= :queuename") })
@Cacheable(false)
public class EntryQueueEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique = true, nullable = false, length = 30)
	private String id;

	@Column(length = 35, name="batchid")
	private String batchId;

	@Column(unique = true, nullable = false, length = 30, name="correlationid")
	@URLId
	private String correlationId;

	@Column(length = 40)
	private String feedback;

	@Column(nullable = false, name="holdstatus")
	private long holdStatus;

	@Lob()
	@Column(nullable = false)
	private String payload;

	private long priority;

	@Column(nullable = false, length = 30, name="requestorservice")
	private String requestorService;

	@Column(nullable = false, length = 30, name="requesttype")
	private String requestType;

	@Column(length = 30, name="responderservice")
	private String responderService;

	@Column(nullable = false)
	private long sequence;

	@Column(length = 30, name="sessionid")
	private String sessionId;

	@Transient
	private long status;

	@Column(length = 35, name="queuename")
	private String queueName;
	
	@OneToOne(targetEntity = RoutedMessageEntity.class, cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinColumn(name = "CORRELATIONID", referencedColumnName = "CORRELATIONID", insertable = false, updatable = false)
	private RoutedMessageEntity routedMessage; //only from RoutedMessageEntity

	public EntryQueueEntity() {
	}

	public String getFeedback() {
		return this.feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

	public String getPayload() {
		return this.payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public long getPriority() {
		return this.priority;
	}

	public void setPriority(long priority) {
		this.priority = priority;
	}

	public long getSequence() {
		return this.sequence;
	}

	public void setSequence(long sequence) {
		this.sequence = sequence;
	}

	public long getStatus() {
		return this.status;
	}

	public void setStatus(long status) {
		this.status = status;
	}

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public String getCorrelationId() {
		return correlationId;
	}

	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}

	public long getHoldStatus() {
		return holdStatus;
	}

	public void setHoldStatus(long holdStatus) {
		this.holdStatus = holdStatus;
	}

	public String getRequestorService() {
		return requestorService;
	}

	public void setRequestorService(String requestorService) {
		this.requestorService = requestorService;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getResponderService() {
		return responderService;
	}

	public void setResponderService(String responderService) {
		this.responderService = responderService;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	@JsonIgnore
	public RoutedMessageEntity getRoutedMessage() {
		return routedMessage;
	}

	@JsonIgnore
	public void setRoutedMessage(RoutedMessageEntity routedMessage) {
		this.routedMessage = routedMessage;
	}

	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		return "EntryQueueEntity [id=" + id + ", batchId=" + batchId + ", correlationId=" + correlationId
				+ ", feedback=" + feedback + ", holdStatus=" + holdStatus + ", priority="
				+ priority + ", requestorService=" + requestorService + ", requestType=" + requestType
				+ ", responderService=" + responderService + ", sequence=" + sequence + ", sessionId=" + sessionId
				+ ", status=" + status + ", queueName=" + queueName + ", routedMessage=" + routedMessage + "]";
	}

	@Override
	public String getClassEvent() {
		return Invariants.queueClassEvents.MANAGE.toString();
	}

	@Override
	public String getMessage() {
		return "entry queue";
	}
}