package ro.allevo.fintpuiws.model;

import javax.persistence.*;

import ro.allevo.fintpws.model.BaseEntity;


/**
 * The persistent class for the queuemessagegroupsfilter database table.
 * 
 */
@Entity
@Table(schema = "FINCFG", name="QUEUEMESSAGEGROUPSFILTER")

@NamedQueries({
	@NamedQuery(name = "QueueMessageGroupsFilterEntity.findAll", query = "select q from QueueMessageGroupsFilterEntity q"),
	@NamedQuery(name = "QueueMessageGroupsFilterEntity.findTotal", query = "select count(q.id) from QueueMessageGroupsFilterEntity q") })
public class QueueMessageGroupsFilterEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(unique=true, nullable=false)
	private Integer id;

	private String datasource;

	private String label;

	private Integer level;

	private String messagetype;

	private String routingkeyword;

	private String type;

	public QueueMessageGroupsFilterEntity() {
	}

	public String getDatasource() {
		return this.datasource;
	}

	public void setDatasource(String datasource) {
		this.datasource = datasource;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Integer getLevel() {
		return this.level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getMessagetype() {
		return this.messagetype;
	}

	public void setMessagetype(String messagetype) {
		this.messagetype = messagetype;
	}

	public String getRoutingkeyword() {
		return this.routingkeyword;
	}

	public void setRoutingkeyword(String routingkeyword) {
		this.routingkeyword = routingkeyword;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "QueueMessageGroupsFilterEntity [datasource=" + datasource + ", id=" + id + ", label=" + label
				+ ", level=" + level + ", messagetype=" + messagetype + ", routingkeyword=" + routingkeyword + ", type="
				+ type + "]";
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
