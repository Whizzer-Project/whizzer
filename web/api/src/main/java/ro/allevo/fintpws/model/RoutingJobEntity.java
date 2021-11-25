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
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;

import ro.allevo.fintpws.util.Invariants;
import ro.allevo.fintpws.util.annotations.URLId;


/**
 * The persistent class for the ROUTINGJOBS database table.
 * 
 */
@Entity
@Table(schema = "FINDATA", name="ROUTINGJOBS")
@NamedStoredProcedureQueries({
	@NamedStoredProcedureQuery(name = "RoutingJobEntity.createRoutingJob", procedureName = "findata.createroutingjob", parameters = {
			@StoredProcedureParameter(type = String.class, name = "inqueuename", mode = ParameterMode.IN),
			@StoredProcedureParameter(type = String.class, name = "inaction", mode = ParameterMode.IN),
			@StoredProcedureParameter(type = String.class, name = "inreason", mode = ParameterMode.IN),
			@StoredProcedureParameter(type = String.class, name = "inactiondetails", mode = ParameterMode.IN),
			@StoredProcedureParameter(type = String.class, name = "inmsgtype", mode = ParameterMode.IN),
			@StoredProcedureParameter(type = String.class, name = "inmsgid", mode = ParameterMode.IN),
			@StoredProcedureParameter(type = String.class, name = "ingroupkey", mode = ParameterMode.IN),
			@StoredProcedureParameter(type = String.class, name = "intimekey", mode = ParameterMode.IN),
			@StoredProcedureParameter(type = String.class, name = "infield1val", mode = ParameterMode.IN),
			@StoredProcedureParameter(type = String.class, name = "infield2val", mode = ParameterMode.IN),
			@StoredProcedureParameter(type = String.class, name = "infield3val", mode = ParameterMode.IN),
			@StoredProcedureParameter(type = String.class, name = "infield4val", mode = ParameterMode.IN),
			@StoredProcedureParameter(type = String.class, name = "infield5val", mode = ParameterMode.IN),
			@StoredProcedureParameter(type = Integer.class, name = "inuserid", mode = ParameterMode.IN),
			@StoredProcedureParameter(type = java.sql.Array.class, name = "intxids", mode = ParameterMode.IN)
	})
})
@NamedQueries({
	@NamedQuery(name = "RoutingJobEntity.findAll", query = "select q from RoutingJobEntity q order by q.id asc"),
	@NamedQuery(name = "RoutingJobEntity.findTotal", query = "select count(q.id) from RoutingJobEntity q"),
	@NamedQuery(name = "RoutingJobEntity.findById", query = "select q from RoutingJobEntity q where trim(q.id)=:id") })
@Cacheable(false)
public class RoutingJobEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false, length=30)
	@URLId
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(nullable=false, precision=22)
	private long backout;

	@Column(nullable=false, length=200)
	private String function;

	@Column(nullable=false, precision=22)
	private long priority;

	@Column(length=50)
	private String routingpoint;

	@Column(nullable=false, precision=22)
	private long status;

	@Column(length=5)
	private long userid;

	public long getBackout() {
		return this.backout;
	}

	public void setBackout(long backout) {
		this.backout = backout;
	}

	public String getFunction() {
		return this.function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	public long getPriority() {
		return this.priority;
	}

	public void setPriority(long priority) {
		this.priority = priority;
	}

	public String getRoutingpoint() {
		return this.routingpoint;
	}

	public void setRoutingpoint(String routingpoint) {
		this.routingpoint = routingpoint;
	}

	public long getStatus() {
		return this.status;
	}

	public void setStatus(long status) {
		this.status = status;
	}

	public long getUserid() {
		return this.userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	@Override
	public String toString(){
		String res = "";
		res+=getId();
		return res;
	}

	@Override
	public String getClassEvent() {
		return Invariants.configClassEvents.MANAGE.toString();
	}
	
	@Override
	public String getMessage() {
		return "routing job";
	}
}
