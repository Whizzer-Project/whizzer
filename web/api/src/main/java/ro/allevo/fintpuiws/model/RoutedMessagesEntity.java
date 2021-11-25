package ro.allevo.fintpuiws.model;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ro.allevo.fintpws.model.BaseEntity;
import ro.allevo.fintpws.util.Invariants;

import java.sql.Timestamp;


/**
 * The persistent class for the routedmessages database table.
 * 
 */
@Entity
@Table(name="routedmessages")
@NamedQueries({
	@NamedQuery(name="RoutedMessagesEntity.findAll", query="SELECT r FROM RoutedMessagesEntity r"),
	@NamedQuery(name="RoutedMessagesEntity.findById", query="SELECT r FROM RoutedMessagesEntity r where r.id=:id")
})
@Cacheable(false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoutedMessagesEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name = "ack")
	private Integer ack;

	@Column(name = "amount")
	private String amount;

	@Column(name = "correlationid")
	private String correlationid;

	@Column(name = "currentqueue")
	private Integer currentqueue;

	@Column(name = "entity")
	private String entity;

	@Column(name = "insertdate")
	private Timestamp insertdate;

	@Column(name = "ioidentifier")
	private String ioidentifier;

	@Column(name = "messagetype")
	private String messagetype;

	@Column(name = "paymentid")
	private String paymentid;

	@Column(name = "receiver")
	private String receiver;

	@Column(name = "reference")
	private String reference;

	@Column(name = "requestorservice")
	private String requestorservice;

	@Column(name = "responderservice")
	private String responderservice;

	@Column(name = "sender")
	private String sender;

	@Column(name = "updatedate")
	private Timestamp updatedate;

	@Column(name = "userid")
	private Integer userid;

	public RoutedMessagesEntity() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getAck() {
		return this.ack;
	}

	public void setAck(Integer ack) {
		this.ack = ack;
	}

	public String getAmount() {
		return this.amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getCorrelationid() {
		return this.correlationid;
	}

	public void setCorrelationid(String correlationid) {
		this.correlationid = correlationid;
	}

	public Integer getCurrentqueue() {
		return this.currentqueue;
	}

	public void setCurrentqueue(Integer currentqueue) {
		this.currentqueue = currentqueue;
	}

	public String getEntity() {
		return this.entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public Timestamp getInsertdate() {
		return this.insertdate;
	}

	public void setInsertdate(Timestamp insertdate) {
		this.insertdate = insertdate;
	}

	public String getIoidentifier() {
		return this.ioidentifier;
	}

	public void setIoidentifier(String ioidentifier) {
		this.ioidentifier = ioidentifier;
	}

	public String getMessagetype() {
		return this.messagetype;
	}

	public void setMessagetype(String messagetype) {
		this.messagetype = messagetype;
	}

	public String getPaymentid() {
		return this.paymentid;
	}

	public void setPaymentid(String paymentid) {
		this.paymentid = paymentid;
	}

	public String getReceiver() {
		return this.receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getReference() {
		return this.reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getRequestorservice() {
		return this.requestorservice;
	}

	public void setRequestorservice(String requestorservice) {
		this.requestorservice = requestorservice;
	}

	public String getResponderservice() {
		return this.responderservice;
	}

	public void setResponderservice(String responderservice) {
		this.responderservice = responderservice;
	}

	public String getSender() {
		return this.sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public Timestamp getUpdatedate() {
		return this.updatedate;
	}

	public void setUpdatedate(Timestamp updatedate) {
		this.updatedate = updatedate;
	}

	public Integer getUserid() {
		return this.userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	@Override
	public String toString() {
		return "RoutedMessagesEntity [id=" + id + ", ack=" + ack + ", amount=" + amount + ", correlationid="
				+ correlationid + ", currentqueue=" + currentqueue + ", entity=" + entity + ", insertdate=" + insertdate
				+ ", ioidentifier=" + ioidentifier + ", messagetype=" + messagetype + ", paymentid=" + paymentid
				+ ", receiver=" + receiver + ", reference=" + reference + ", requestorservice=" + requestorservice
				+ ", responderservice=" + responderservice + ", sender=" + sender + ", updatedate=" + updatedate
				+ ", userid=" + userid + "]";
	}

	@Override
	public String getClassEvent() {
		return Invariants.routingClassEvents.MANAGE.toString();
	}
	
	@Override
	public String getMessage() {
		return "routed messages";
	}

}