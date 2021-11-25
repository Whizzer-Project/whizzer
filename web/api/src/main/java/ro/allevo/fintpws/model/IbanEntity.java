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

import java.io.Serializable;

import static javax.persistence.ParameterMode.IN;
import static javax.persistence.ParameterMode.OUT;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The persistent class for the CLIENTACCOUNTS database table.
 * 
 */
@XmlRootElement
@Entity
@Table(schema = "FINCFG",name = "CLIENTACCOUNTS")
@NamedStoredProcedureQueries({

		@NamedStoredProcedureQuery(name = "GetlongIBAN", procedureName = "GetlongIBAN", resultClasses = String.class, parameters = {
				@StoredProcedureParameter(name = "inShortAcc", type = String.class, mode = IN),
				@StoredProcedureParameter(name = "outlongIBAN", type = String.class, mode = OUT) }),
		@NamedStoredProcedureQuery(name = "Getbiccode", procedureName = "Getbiccode", resultClasses = String.class, parameters = {
				@StoredProcedureParameter(name = "inRIC", type = String.class, mode = IN),
				@StoredProcedureParameter(name = "outBicCode", type = String.class, mode = ParameterMode.OUT) }),
		@NamedStoredProcedureQuery(name = "readLive", procedureName = "manageclientaccounts.readliveclientaccounts", resultClasses = IbanEntity.class, parameters = {
					@StoredProcedureParameter(name = "inIBAN", type = String.class, mode = IN),
					@StoredProcedureParameter(name = "inShAccount", type = String.class, mode = IN),
					@StoredProcedureParameter(name = "inClientName", type = String.class, mode = IN),
					@StoredProcedureParameter(name = "inFiscalCode", type = String.class, mode = IN),
					@StoredProcedureParameter(name = "inBOapp", type = String.class, mode = IN),
					@StoredProcedureParameter(name = "inClientID", type = String.class, mode = IN),
					@StoredProcedureParameter(name = "inPos", type = Integer.class, mode = IN),
					@StoredProcedureParameter(name = "inNo", type = Integer.class, mode = IN),
					@StoredProcedureParameter(name = "outCount", type = Integer.class, mode = OUT),
					@StoredProcedureParameter(name = "outRetCursor", type = void.class, mode = ParameterMode.REF_CURSOR) })
				

})
@NamedQueries({
		@NamedQuery(name = "IbanEntity.findAll", query = "SELECT i FROM IbanEntity i"),
		@NamedQuery(name = "IbanEntity.findByIban", query = "SELECT i FROM IbanEntity i WHERE i.iban = :iban"),
		@NamedQuery(name = "IbanEntity.findTotal", query = "SELECT count(i.iban) FROM IbanEntity i") })
@Cacheable(false)
public class IbanEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String iban;

	private String clientname;

	private String fiscalcode;

	private String shortaccount;

	private String sourceapp;
	
	private String clientid;

	@Transient
	private Boolean isvalid;
	
	@Transient
	private Boolean isDeleted;

	public IbanEntity() {
	}

	public String getIban() {
		return this.iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public String getClientname() {
		return this.clientname;
	}

	public void setClientname(String clientname) {
		this.clientname = clientname;
	}

	public String getFiscalcode() {
		return this.fiscalcode;
	}

	public void setFiscalcode(String fiscalcode) {
		this.fiscalcode = fiscalcode;
	}

	public String getShortaccount() {
		return this.shortaccount;
	}

	public void setShortaccount(String shortaccount) {
		this.shortaccount = shortaccount;
	}

	public String getSourceapp() {
		return this.sourceapp;
	}

	public void setSourceapp(String sourceapp) {
		this.sourceapp = sourceapp;
	}

	public Boolean getIsvalid() {
		return isvalid;
	}

	public void setIsvalid(Boolean isvalid) {
		this.isvalid = isvalid;
	}
	
	public Boolean getIsdeleted() {
		return isDeleted;
	}

	public void setIsdeleted(Boolean isdeleted) {
		this.isDeleted = isdeleted;
	}
	
	@Override
	public String toString() {
		String res = "";
		res += getIban();
		return res;
	}

	/**
	 * @return the clientid
	 */
	public String getClientid() {
		return clientid;
	}

	/**
	 * @param clientid the clientid to set
	 */
	public void setClientid(String clientid) {
		this.clientid = clientid;
	}

}