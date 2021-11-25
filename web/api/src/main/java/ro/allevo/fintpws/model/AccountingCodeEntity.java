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


import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ro.allevo.fintpws.util.Invariants;
import ro.allevo.fintpws.util.annotations.URLId;


/**
 * The persistent class for the ALERTS database table.
 * 
 */
@Entity
@Table(schema = "FINLIST", name="ACCOUNTINGCODES")

@NamedQueries({
	@NamedQuery(name = "AccountingCodeEntity.findAll", query = "select e from AccountingCodeEntity e"),
	@NamedQuery(name = "AccountingCodeEntity.findTotal", query = "select count(e.id) from AccountingCodeEntity e"),
	@NamedQuery(name = "AccountingCodeEntity.findById", query = "select e from AccountingCodeEntity e where e.id=:id"), 
	@NamedQuery(name = "AccountingCodeEntity.findByName", query = "select e from AccountingCodeEntity e where e.name=:id")
	})

public class AccountingCodeEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="AccountingCodeEntityIdGenerator")
	@TableGenerator(name="AccountingCodeEntityIdGenerator", table="FINCFG.IDGENLIST",
	pkColumnName="TABCOLNAME", valueColumnName="IDVALUE",
	pkColumnValue="ACCOUNTINGCODES_ID") 
	@Column(unique=true, nullable=false, precision=38, updatable=false)
	@URLId
	private long id;
	
	@ManyToOne(targetEntity = MsgTypeListEntity.class, cascade = { CascadeType.REFRESH }, fetch = FetchType.LAZY)
	@JoinColumn(name = "TXTYPE", referencedColumnName = "MESSAGETYPE", insertable = false, updatable = false, nullable=false)
	private MsgTypeListEntity msgTypeListEntity; 
	
	@Column(nullable=false, length=50)
	private String txtype; 
	
	@Column(nullable=false, length=35)
	private String code;
	
	@Column(nullable=false, length=35)
	private String name;
	
	public long getId() {
		return id;
	}

	public void setId(long entityid) {
		this.id = entityid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}


	@JsonIgnore
	public MsgTypeListEntity getMsgTypeListEntity() {
		return msgTypeListEntity;
	}
	

	public String getClassEvent() {
		return Invariants.configClassEvents.MANAGE.toString();
	}

	public String getTxtype() {
		return txtype;
	}

	public void setTxtype(String txtype) {
		this.txtype = txtype;
	}

	@Override
	public String toString() {
		return "AccountingCodeEntity [id=" + id + ", msgTypeListEntity=" + msgTypeListEntity + ", txtype=" + txtype
				+ ", code=" + code + ", name=" + name + "]";
	}
	
	@Override
	public String getMessage() {
		return "entry in list accounting codes";
	}

}

