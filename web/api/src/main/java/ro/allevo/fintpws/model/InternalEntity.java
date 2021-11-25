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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
@Table(schema = "FINLIST", name="INTERNALENTITIES")

@NamedQueries({
	@NamedQuery(name = "InternalEntity.findAll", query = "select e from InternalEntity e"),
	@NamedQuery(name = "InternalEntity.findTotal", query = "select count(e.id) from InternalEntity e"),
	@NamedQuery(name = "InternalEntity.findById", query = "select e from InternalEntity e where e.id=:id"), 
	@NamedQuery(name = "InternalEntity.findByName", query = "select e from InternalEntity e where e.name=:id"),
	@NamedQuery(name = "InternalEntity.findByRights", query = "select distinct udr.internalEntityName from UserDefinedRoleEntity udr, RoleEntity r, UserRoleEntity urm, UserEntity u "
			+ " where u.id=:userId and udr.messageType=:messageType "),
	})

public class InternalEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="InternalEntityIdGenerator")
	@TableGenerator(name="InternalEntityIdGenerator", table="FINCFG.IDGENLIST",
	pkColumnName="TABCOLNAME", valueColumnName="IDVALUE",
	pkColumnValue="INTERNALENTITIES_ID") 
	@Column(unique=true, nullable=false, precision=38, updatable=false)
	@URLId
	private long id;

	@Column(nullable=false, length=35)
	private String name;
	
	@Column(length=70)
	private String address;
	
	@Column(length=35)
	private String city;
	
	@Column(length=2)
	private String country;
	
	@Column(length=10, name="fiscalcode")
	private String fiscalCode;
	
	@OneToMany(mappedBy="internalEntity")
	private List<InternalAccountEntity> internalAccounts;

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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getFiscalCode() {
		return fiscalCode;
	}

	public void setFiscalCode(String fiscalcode) {
		this.fiscalCode = fiscalcode;
	}

	@JsonIgnore
	public List<InternalAccountEntity> getInternalAccounts() {
		return internalAccounts;
	}

	@Override
	public String toString() {
		return "InternalEntity [id=" + id + ", name=" + name + ", address=" + address + ", city=" + city + ", country="
				+ country + ", fiscalCode=" + fiscalCode + ", internalAccounts=" + internalAccounts + "]";
	}

	public String getClassEvent() {
		return Invariants.configClassEvents.MANAGE.toString();
	}	
	
	@Override
	public String getMessage() {
		return "entry in list internal entities";
	}
}

