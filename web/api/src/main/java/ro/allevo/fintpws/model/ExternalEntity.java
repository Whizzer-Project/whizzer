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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import ro.allevo.fintpws.util.Invariants;
import ro.allevo.fintpws.util.annotations.URLId;


/**
 * The persistent class for the ALERTS database table.
 * 
 */
@Entity
@Table(schema = "FINLIST", name="EXTERNALENTITIES")

@NamedQueries({
	@NamedQuery(name = "ExternalEntity.findAll", query = "select e from ExternalEntity e"),
	@NamedQuery(name = "ExternalEntity.findTotal", query = "select count(e.id) from ExternalEntity e"),
	@NamedQuery(name = "ExternalEntity.findById", query = "select e from ExternalEntity e where e.id=:id"),
	@NamedQuery(name = "ExternalEntity.findByName", query = "select e from ExternalEntity e where e.name=:id")
	})

public class ExternalEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="ExternalEntityIdGenerator")
	@TableGenerator(name="ExternalEntityIdGenerator", table="FINCFG.IDGENLIST",
	pkColumnName="TABCOLNAME", valueColumnName="IDVALUE",
	pkColumnValue="EXTERNALENTITIES_ID") 
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
	
	@Column(length=35)
	private String email;
	

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getClassEvent() {
		return Invariants.configClassEvents.MANAGE.toString();
	}

	@Override
	public String toString() {
		return "ExternalEntity [id=" + id + ", name=" + name + ", address=" + address + ", city=" + city + ", country="
				+ country + ", fiscalCode=" + fiscalCode + ", email=" + email + "]";
	}
	
	@Override
	public String getMessage() {
		return "entry in list external entities";
	}
}

