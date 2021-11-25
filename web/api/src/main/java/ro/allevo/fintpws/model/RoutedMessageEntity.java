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

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ro.allevo.fintpws.util.Invariants;
import ro.allevo.fintpws.util.annotations.URLId;

/**
 * The persistent class for the ROUTEDMESSAGES database table.
 * 
 */
@Entity
@Table(schema = "FINDATA", name = "ROUTEDMESSAGES")
@NamedQueries({
		@NamedQuery(name = "RoutedMessageEntity.findById", query = "SELECT b FROM RoutedMessageEntity b "
				+ "WHERE b.id = :id"),
		@NamedQuery(name = "RoutedMessageEntity.findByCorrelId", query = "SELECT b FROM RoutedMessageEntity b "
				+ "WHERE trim(b.correlationId) = :id"),
		
		@NamedQuery(name = "RoutedMessageEntity.findAll", query = "SELECT b FROM RoutedMessageEntity b order by b.insertDate, b.id"),
		@NamedQuery(name = "RoutedMessageEntity.findTotal", query = "SELECT count(b.id) FROM RoutedMessageEntity b"),
		
		@NamedQuery(name = "RoutedMessageEntity.findAllQueue", query = "SELECT b FROM RoutedMessageEntity b join b.entryQueue e "
				+ "where e.queueName=:queuename order by b.insertDate, b.id "),
		@NamedQuery(name = "RoutedMessageEntity.findTotalQueue", query = "SELECT count(b.id) FROM RoutedMessageEntity b join b.entryQueue e"
				+ " join b.entryQueue a where e.queueName=:queuename ")
})
//By default EclipseLink enables a shared object cache to cache objects read from the database to avoid repeated 
//database access. If the database is changed directly through JDBC, or by another application or server, 
//the objects in the shared cache will be stale.
@Cacheable(false)
public class RoutedMessageEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique = true, nullable = false, length = 30)
	@URLId
	private String id;

	@Transient
	private long ack;

	@Column(unique = true, length = 30, name="correlationid")
	private String correlationId;

	@Column(name="currentqueue")
	private long currentQueue;

	@Column(name="insertdate")
	private Timestamp insertDate;

	@Column(length = 50, name="messagetype")
	private String messageType;

	@Column(length = 35)
	private String receiver;

	@Transient
	private String receiverApp;

	@Column(length = 35)
	private String sender;

	@Transient
	private String senderApp;

	@Column(length = 35, nullable=false)
	private String reference;

	//@Transient
	@Column(name="userid")
	private long userId;
	
	private String amount;
	
	private String paymentid;

	
	// bi-directional one-to-one association to EntryQueueEntity
	// EAGER Fetch  get results in one query ( parent and child )
	// LAZY Fetch  get results as sub-query .

	

	@OneToOne(targetEntity = EntryQueueEntity.class, cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinColumn(name = "CORRELATIONID", referencedColumnName = "CORRELATIONID", insertable = false, updatable = false)
	private EntryQueueEntity entryQueue;

	@OneToMany(targetEntity = StatusEntity.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "CORRELATIONID", referencedColumnName = "CORRELATIONID")
	private List<StatusEntity> status; //events
	
	
	@OneToOne(targetEntity = MsgTypeListEntity.class, cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinColumn(name = "messagetype", referencedColumnName = "messagetype", insertable = false, updatable = false)
	private MsgTypeListEntity messageTypeEntity;
	
	@JsonIgnore
	public List<StatusEntity> getStatus() {
		return status;
	}

	@JsonIgnore
	public void setStatus(List<StatusEntity> status) {
		this.status = status;
	}

	public long getAck() {
		return this.ack;
	}

	public void setAck(long ack) {
		this.ack = ack;
	}

	public String getReceiver() {
		return this.receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getSender() {
		return this.sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getCorrelationId() {
		return correlationId;
	}

	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}

	public long getCurrentQueue() {
		return currentQueue;
	}

	public void setCurrentQueue(long currentQueue) {
		this.currentQueue = currentQueue;
	}

	public Timestamp getInsertDate() {
		return insertDate;
	}

	public void setInsertDate(Timestamp insertDate) {
		this.insertDate = insertDate;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getReceiverApp() {
		return receiverApp;
	}

	public void setReceiverApp(String receiverApp) {
		this.receiverApp = receiverApp;
	}

	public String getSenderApp() {
		return senderApp;
	}

	public void setSenderApp(String senderApp) {
		this.senderApp = senderApp;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getPaymentid() {
		return paymentid;
	}

	public void setPaymentid(String paymentid) {
		this.paymentid = paymentid;
	}

	@JsonIgnore
	public EntryQueueEntity getEntryQueue() {
		return entryQueue;
	}

	@JsonIgnore
	public void setEntryQueue(EntryQueueEntity entryQueue) {
		this.entryQueue = entryQueue;
	}

	@JsonIgnore
	public MsgTypeListEntity getMessageTypeEntity() {
		return messageTypeEntity;
	}

	@JsonIgnore
	public void setMessageTypeEntity(MsgTypeListEntity messageTypeEntity) {
		this.messageTypeEntity = messageTypeEntity;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "RoutedMessageEntity [id=" + id + ", ack=" + ack + ", correlationId=" + correlationId + ", currentQueue="
				+ currentQueue + ", insertDate=" + insertDate + ", messageType=" + messageType + ", receiver="
				+ receiver + ", receiverApp=" + receiverApp + ", sender=" + sender + ", senderApp=" + senderApp
				+ ", reference=" + reference + ", userId=" + userId + ", amount=" + amount + ", paymentid=" + paymentid
				+ ", entryQueue=" + entryQueue + ", status=" + status + ", messageTypeEntity=" + messageTypeEntity
				+ "]";
	}

	@Override
	public String getClassEvent() {
		return Invariants.messageClassEvents.MOVE.toString();
	}
	
	@Override
	public String getMessage() {
		return "routed message";
	}

}
