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

import ro.allevo.fintpws.util.Invariants;
import ro.allevo.fintpws.util.annotations.URLId;


/**
 * The persistent class for the ROUTINGSCHEMAS database table.
 * 
 */
@Entity
@Table(schema = "FINCFG", name="ROUTINGSCHEMAS")
@NamedQueries({
	@NamedQuery(name = "RoutingSchemaEntity.findAll", query = "select rs from RoutingSchemaEntity rs"),
	@NamedQuery(name = "RoutingSchemaEntity.findTotal", query = "select count(rs.id) from RoutingSchemaEntity rs"),
	@NamedQuery(name = "RoutingSchemaEntity.findById", query = "select rs from RoutingSchemaEntity rs where rs.id=:id") })
@Cacheable(false)
public class RoutingSchemaEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "RsGuidGenerator")
	@TableGenerator(name = "RsGuidGenerator", table = "FINCFG.IDGENLIST",
			pkColumnName="TABCOLNAME", valueColumnName="IDVALUE",
			pkColumnValue="ROUTINGSCHEMAS_ID")
	@Column(unique=true, nullable=false, precision=10)
	@URLId
	private long id;

	@Column(nullable=false, precision=5)
	private BigDecimal active;

	@Column(length=250)
	private String description;

	@Column(length=1)
	private String visible;

	@Column(nullable=false, length=10)
	private String name;

	@Column(length=10, name="sessioncode")
	private String sessionCode;

	@Column(nullable=false, precision=10, name="timelimitid1")
	private long startLimit;

	@Column(nullable=false, precision=10, name="timelimitid2")
	private long stopLimit;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "timelimitid1")
	private TimeLimitEntity startLimitEntity;

	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "timelimitid2")
	private TimeLimitEntity stopLimitEntity;

	public long getId() {
		return this.id;
	}

	public void setGuid(long id) {
		this.id = id;
	}

	public BigDecimal getActive() {
		return this.active;
	}

	public void setActive(BigDecimal active) {
		this.active = active;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getVisible() {
		return this.visible;
	}

	public void setVisible(String isvisible) {
		this.visible = isvisible;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSessionCode() {
		return this.sessionCode;
	}

	public void setSessionCode(String sessioncode) {
		this.sessionCode = sessioncode;
	}

	public TimeLimitEntity getStartLimitEntity() {
		return startLimitEntity;
	}

	public TimeLimitEntity getStopLimitEntity() {
		return stopLimitEntity;
	}
	
	public long getStartLimit() {
		return startLimit;
	}
	
	public long getStopLimit() {
		return stopLimit;
	}

	public void setStartLimit(long startLimit) {
		this.startLimit = startLimit;
	}

	public void setStopLimit(long stopLimit) {
		this.stopLimit = stopLimit;
	}

	@Override
	public String toString() {
		return "RoutingSchemaEntity [id=" + id + ", active=" + active + ", description=" + description + ", visible="
				+ visible + ", name=" + name + ", sessionCode=" + sessionCode + ", startLimit=" + startLimit
				+ ", stopLimit=" + stopLimit + ", startLimitEntity=" + startLimitEntity + ", stopLimitEntity="
				+ stopLimitEntity + "]";
	}

	@Override
	public String getClassEvent() {
		return Invariants.configClassEvents.MANAGE.toString();
	}
	
	@Override
	public String getMessage() {
		return "routing schema";
	}

}
