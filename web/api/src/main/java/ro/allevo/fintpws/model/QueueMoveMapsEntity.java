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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import ro.allevo.fintpws.util.Invariants;
import ro.allevo.fintpws.util.annotations.MergeEntity;
import ro.allevo.fintpws.util.annotations.URLId;

@Entity
@Table(schema = "FINCFG", name="QUEUEMOVEMAPS")

@NamedQueries({
	@NamedQuery(name = "QueueMoveMapsEntity.findAll", query = "select b from QueueMoveMapsEntity b"),
	@NamedQuery(name = "QueueMoveMapsEntity.findTotal", query = "select count(b.id) from QueueMoveMapsEntity b"),
	@NamedQuery(name = "QueueMoveMapsEntity.findById", query = "select b from QueueMoveMapsEntity b where b.id=:id") })

//@Cacheable(false)
public class QueueMoveMapsEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="QueueMoveMapsEntityIdGenerator")
	@TableGenerator(name="QueueMoveMapsEntityIdGenerator", table="FINCFG.IDGENLIST",
	pkColumnName="TABCOLNAME", valueColumnName="IDVALUE",
	pkColumnValue="QUEUEMOVEMAPS_ID") 
	@Column(unique=true, nullable=false, precision=38, updatable=false)
	@URLId
	@MapsId("id")
	private Integer id;

	@Column(nullable=false)
	@JoinColumn(name = "ID", referencedColumnName="SOURCEQUEUEID")
	@MergeEntity
	private Integer sourcequeueid;
	
	@Column(nullable=false)
	private Integer destinationqueueid;
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSourcequeueid() {
		return sourcequeueid;
	}

	public void setSourcequeueid(Integer sourcequeueid) {
		this.sourcequeueid = sourcequeueid;
	}

	public Integer getDestinationqueueid() {
		return destinationqueueid;
	}

	public void setDestinationqueueid(Integer destinationqueueid) {
		this.destinationqueueid = destinationqueueid;
	}

	public String getClassEvent() {
		return Invariants.configClassEvents.MANAGE.toString();
	}	
	
	
	@Override
	public String toString() {
		return "QueueMoveMapsEntity [id=" + id + ", sourcequeueid=" + sourcequeueid + ", destinationqueueid="
				+ destinationqueueid + "]";
	}

	@Override
	public String getMessage() {
		return "entry in list banks";
	}
}

