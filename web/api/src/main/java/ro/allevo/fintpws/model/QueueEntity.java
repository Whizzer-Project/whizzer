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

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import ro.allevo.fintpws.util.Invariants;
import ro.allevo.fintpws.util.annotations.MergeEntity;
import ro.allevo.fintpws.util.annotations.URLId;

/**
 * The persistent class for the QUEUES database table.
 * 
 */
@Entity
@Table(schema = "FINCFG", name = "QUEUES")
@NamedQueries({
		@NamedQuery(name = "QueueEntity.findAll", query = "select q from QueueEntity q order by q.name asc"),
		@NamedQuery(name = "QueueEntity.findTotal", query = "select count(q.name) from QueueEntity q"),
		@NamedQuery(name = "QueueEntity.findById", query = "select q from QueueEntity q where q.id=:id"),
		@NamedQuery(name = "QueueEntity.findByName", query = "select q from QueueEntity q where q.name=:id")
		 })
@Cacheable(false)
public class QueueEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="GuidGenerator")
	@TableGenerator(name="GuidGenerator", table="FINCFG.IDGENLIST",
		pkColumnName="TABCOLNAME", valueColumnName="IDVALUE",
		pkColumnValue="QUEUES_ID")
	@Column(unique = true, nullable = false)
	@URLId
	private long id;

	@Column(name = "maxtrxonbatch")
	private long maxTrxOnBatch;

	@Column(length = 50)
	private String label;
	
	@Column(name = "exitpoint")
	private long exitPoint;

	@Column(length = 100)
	private String description;

	@Column(nullable = false, name="holdstatus")
	private long holdStatus;

	@Column(nullable = false)
	private long priority;
	
	@Column(nullable = false, length = 50)
	private String name;

	@Column(name="queuetypeid", nullable = false)
	private long queueTypeId;
	
	//@Column(name="category",length = 100)
	private String category;
		
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "EXITPOINT")
	private ServiceMapEntity serviceMapEntity;
	
	@OneToMany(targetEntity = QueueMoveMapsEntity.class, cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "SOURCEQUEUEID", referencedColumnName="ID")
	@MergeEntity
	private List<QueueMoveMapsEntity> queueMoveMapsEntity;
	
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public long getPriority() {
		return priority;
	}

	public void setPriority(long priority) {
		this.priority = priority;
	}

	public long getId() {
		return id;
	}

	public long getMaxTrxOnBatch() {
		return maxTrxOnBatch;
	}

	public void setMaxTrxOnBatch(long maxTrxOnBatch) {
		this.maxTrxOnBatch = maxTrxOnBatch;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public long getExitPoint() {
		return exitPoint;
	}
	
	public String getExitPointName() {
		if (null != serviceMapEntity)
			return serviceMapEntity.getName();
		
		return null;
	}

	public void setExitPoint(long exitPoint) {
		this.exitPoint = exitPoint;
	}

	public long getHoldStatus() {
		return holdStatus;
	}

	public void setHoldStatus(long holdStatus) {
		this.holdStatus = holdStatus;
	}

	public long getQueueTypeId() {
		return queueTypeId;
	}

	public void setQueueTypeId(long queueTypeId) {
		this.queueTypeId = queueTypeId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public List<QueueMoveMapsEntity> getQueueMoveMapsEntity() {
		return queueMoveMapsEntity;
	}

	public void setQueueMoveMapsEntity(List<QueueMoveMapsEntity> queueMoveMapsEntity) {
		this.queueMoveMapsEntity = queueMoveMapsEntity;
	}

	@Override
	public String toString() {
		return "QueueEntity [id=" + id + ", maxTrxOnBatch=" + maxTrxOnBatch + ", label=" + label + ", exitPoint="
				+ exitPoint + ", description=" + description + ", holdStatus=" + holdStatus + ", priority=" + priority
				+ ", name=" + name + ", queueTypeId=" + queueTypeId + ", category=" + category + ", serviceMapEntity="
				+ serviceMapEntity + "]";
	}

	@Override
	public String getClassEvent() {
		return Invariants.configClassEvents.MANAGE.toString();
	}
	
	@Override
	public String getMessage() {
		return "queue";
	}
}
