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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ro.allevo.fintpws.util.Invariants;
import ro.allevo.fintpws.util.annotations.URLId;


/**
 * The persistent class for the ROUTINGRULES database table.
 * 
 */
@Entity
@Table(schema = "FINCFG", name="ROUTINGRULES")
@NamedQueries({
	@NamedQuery(name = "RoutingRuleEntity.findById", query = "SELECT r FROM RoutingRuleEntity r "
			+ "WHERE r.id=:id"),
	@NamedQuery(name = "RoutingRuleEntity.findAll", query = "SELECT r FROM RoutingRuleEntity r ORDER BY r.queueEntity.name, r.sequence"),
	@NamedQuery(name = "RoutingRuleEntity.findTotal", query = "SELECT count(r.id) FROM RoutingRuleEntity r"),
	@NamedQuery(name = "RoutingRuleEntity.findAllRoutingSchema", query = "SELECT r FROM RoutingRuleEntity r " +
			"WHERE r.routingSchemaId=:schemaid ORDER BY r.sequence asc"),
	@NamedQuery(name = "RoutingRuleEntity.findTotalRoutingSchema", query = "SELECT count(r.id) FROM RoutingRuleEntity r "+
			"WHERE r.routingSchemaId=:schemaid"),
	@NamedQuery(name = "RoutingRuleEntity.findAllQueue", query = "SELECT r FROM RoutingRuleEntity r " +
			"WHERE r.queueId=:queueid ORDER BY r.sequence asc"),
	@NamedQuery(name = "RoutingRuleEntity.findTotalQueue", query = "SELECT count(r.id) FROM RoutingRuleEntity r "+
			"WHERE r.queueId=:queueid")
	
})
@Cacheable(false)
public class RoutingRuleEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="GuidGenerator")
	@TableGenerator(name="RRGuidGenerator", table="FINCFG.IDGENLIST",
	pkColumnName="TABCOLNAME", valueColumnName="IDVALUE",
	pkColumnValue="ROUTINGRULES_ID") 
	@Column(unique=true, nullable=false)
	@URLId
	private long id;

	@Column(nullable=false, length=500)
	private String action;

	@Column(length=70)
	private String description;

	@Column(length=500, name="functioncondition")
	private String functionCondition; 

	@Column(length=500, name="metadatacondition")
	private String metadataCondition;

	@Column(length=500, name="messagecondition")
	private String messageCondition;

	@Column(nullable=false, name="queueid")
	private long queueId;

	@Column(nullable=false, name="ruletype")
	private long ruleType;

	@Column(nullable=false, name="routingschemaid")
	private long routingSchemaId;

	@Column(nullable=false)
	private long sequence;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "QUEUEID")
	private QueueEntity queueEntity;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name="routingschemaid")
	private RoutingSchemaEntity routingSchemaEntity;

	public String getAction() {
		return this.action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getSequence() {
		return this.sequence;
	}

	public void setSequence(long sequence) {
		this.sequence = sequence;
	}

	@JsonIgnore
	public QueueEntity getQueueEntity() {
		return queueEntity;
	}

	@JsonIgnore
	public void setQueueEntity(QueueEntity queueEntity) {
		this.queueEntity = queueEntity;
		this.queueId = queueEntity.getId();
	}

	@JsonIgnore
	public RoutingSchemaEntity getRoutingSchemaEntity() {
		return routingSchemaEntity;
	}

	@JsonIgnore
	public void setRoutingSchemaEntity(RoutingSchemaEntity routingSchemaEntity) {
		this.routingSchemaEntity = routingSchemaEntity;
		this.routingSchemaId = routingSchemaEntity.getId();
	}

	public String getFunctionCondition() {
		return functionCondition;
	}

	public void setFunctionCondition(String functionCondition) {
		this.functionCondition = functionCondition;
	}

	public String getMetadataCondition() {
		return metadataCondition;
	}

	public void setMetadataCondition(String metadataCondition) {
		this.metadataCondition = metadataCondition;
	}

	public String getMessageCondition() {
		return messageCondition;
	}

	public void setMessageCondition(String messageCondition) {
		this.messageCondition = messageCondition;
	}

	public long getQueueId() {
		return queueId;
	}

	public void setQueueId(long queueId) {
		this.queueId = queueId;
	}

	public long getRuleType() {
		return ruleType;
	}

	public void setRuleType(long ruleType) {
		this.ruleType = ruleType;
	}

	public long getRoutingSchemaId() {
		return routingSchemaId;
	}

	public void setRoutingSchemaId(long routingSchemaId) {
		this.routingSchemaId = routingSchemaId;
	}

	public long getId() {
		return id;
	}
	
	public String getQueueName() {
		return queueEntity.getName();
	}

	@Override
	public String toString() {
		return "RoutingRuleEntity [id=" + id + ", action=" + action + ", description=" + description
				+ ", functionCondition=" + functionCondition + ", metadataCondition=" + metadataCondition
				+ ", messageCondition=" + messageCondition + ", queueId=" + queueId + ", ruleType=" + ruleType
				+ ", routingSchemaId=" + routingSchemaId + ", sequence=" + sequence + ", queueEntity=" + queueEntity
				+ ", routingSchemaEntity=" + routingSchemaEntity + "]";
	}

	@Override
	public String getClassEvent() {
		return Invariants.configClassEvents.MANAGE.toString();
	}
	
	@Override
	public String getMessage() {
		return "routing rule";
	}

}
