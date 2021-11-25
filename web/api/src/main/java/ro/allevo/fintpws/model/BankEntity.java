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
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import ro.allevo.fintpws.util.Invariants;
import ro.allevo.fintpws.util.annotations.URLId;


/**
 * The persistent class for the BANKS database table.
 * 
 */
@Entity
@Table(schema = "FINLIST", name="BANKS")

@NamedQueries({
	@NamedQuery(name = "BankEntity.findAll", query = "select b from BankEntity b"),
	@NamedQuery(name = "BankEntity.findTotal", query = "select count(b.bic) from BankEntity b"),
	@NamedQuery(name = "BankEntity.findById", query = "select b from BankEntity b where trim(b.bic)=:id") })

//@Cacheable(false)
public class BankEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false, length=8, updatable=false)
	@URLId
	private String bic;

	@Column(nullable=false, length=35)
	private String name;
	
	@Column(length=35)
	private String address;
	
	@Column(nullable=false, length=2)
	private String country;
	
	@Column(length=9)
	private String nbrcode;

	public String getBic() {
		return bic;
	}

	public void setBic(String bic) {
		this.bic = bic;
	}

	public String getName() {
		return name;
	}

	public void setName(String bankname) {
		this.name = bankname;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getClassEvent() {
		return Invariants.configClassEvents.MANAGE.toString();
	}
	
	public String getNbrcode() {
		return nbrcode;
	}

	public void setNbrcode(String nbrcode) {
		this.nbrcode = nbrcode;
	}

	@Override
	public String toString() {
		return "Bank [bic=" + bic + ", name=" + name + ", address=" + address + ", country=" + country  + "]";//+ ", nrbcode=" + nbrcode
	}
	
	@Override
	public String getMessage() {
		return "entry in list banks";
	}
}

