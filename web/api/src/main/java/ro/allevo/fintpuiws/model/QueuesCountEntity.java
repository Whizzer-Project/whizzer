package ro.allevo.fintpuiws.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import ro.allevo.fintpws.model.BaseEntity;
import ro.allevo.fintpws.util.Invariants;

@Entity
public class QueuesCountEntity extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "id")
	private int id;
	@Column(name = "queuename")
	private String queueName;
	@Column(name = "nooftx")
	private long noOfTx;
	private String label;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getQueueName() {
		return queueName;
	}
	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}
	public long getNoOfTx() {
		return noOfTx;
	}
	public void setNoOfTx(long noOfTx) {
		this.noOfTx = noOfTx;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	@Override
	public String toString() {
		return "QueuesCountEntity [id=" + id + ", queueName=" + queueName + ", noOfTx=" + noOfTx + ", label=" + label
				+ "]";
	}
	@Override
	public String getClassEvent() {
		return Invariants.queueClassEvents.MANAGE.toString();
	}
	
	@Override
	public String getMessage() {
		return "queues count";
	}
	
}
