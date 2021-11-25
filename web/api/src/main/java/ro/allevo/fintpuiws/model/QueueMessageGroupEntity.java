package ro.allevo.fintpuiws.model;

import javax.persistence.*;

import ro.allevo.fintpws.model.BaseEntity;


/**
 * The persistent class for the queuemessagegroups database table.
 * 
 */
@Entity
@Table(schema = "FINCFG", name="QUEUEMESSAGEGROUPS")

@NamedQueries({
	@NamedQuery(name = "QueueMessageGroupEntity.findAll", query = "select q from QueueMessageGroupEntity q"),
	@NamedQuery(name = "QueueMessageGroupEntity.findTotal", query = "select count(q.messagetype) from QueueMessageGroupEntity q") })
public class QueueMessageGroupEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private String messagetype;

	private String routingkeyword1;

	private String routingkeyword2;

	private String routingkeyword3;

	private String routingkeyword4;

	private String routingkeyword5;

	public QueueMessageGroupEntity() {
	}

	public String getMessagetype() {
		return this.messagetype;
	}

	public void setMessagetype(String messagetype) {
		this.messagetype = messagetype;
	}

	public String getRoutingkeyword1() {
		return this.routingkeyword1;
	}

	public void setRoutingkeyword1(String routingkeyword1) {
		this.routingkeyword1 = routingkeyword1;
	}

	public String getRoutingkeyword2() {
		return this.routingkeyword2;
	}

	public void setRoutingkeyword2(String routingkeyword2) {
		this.routingkeyword2 = routingkeyword2;
	}

	public String getRoutingkeyword3() {
		return this.routingkeyword3;
	}

	public void setRoutingkeyword3(String routingkeyword3) {
		this.routingkeyword3 = routingkeyword3;
	}

	public String getRoutingkeyword4() {
		return this.routingkeyword4;
	}

	public void setRoutingkeyword4(String routingkeyword4) {
		this.routingkeyword4 = routingkeyword4;
	}

	public String getRoutingkeyword5() {
		return this.routingkeyword5;
	}

	public void setRoutingkeyword5(String routingkeyword5) {
		this.routingkeyword5 = routingkeyword5;
	}

	@Override
	public String toString() {
		return "QueueMessageGroupEntity [messagetype=" + messagetype + ", routingkeyword1=" + routingkeyword1
				+ ", routingkeyword2=" + routingkeyword2 + ", routingkeyword3=" + routingkeyword3 + ", routingkeyword4="
				+ routingkeyword4 + ", routingkeyword5=" + routingkeyword5 + "]";
	}

	@Override
	public String getClassEvent() {
		return null;
	}

	@Override
	public String getMessage() {
		return null;
	}

}