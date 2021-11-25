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

import static javax.persistence.ParameterMode.IN;
import static javax.persistence.ParameterMode.OUT;

import java.io.Serializable;

import javax.persistence.*;

/**
 * The persistent class for the TMPCLIENTACCOUNTS database table.
 * 
 */

@Entity
@Table(name = "TMPCLIENTACCOUNTS")
@NamedStoredProcedureQueries({
	
@NamedStoredProcedureQuery(name = "insertClientAccount", procedureName = "manageclientaccounts.insertclientaccount", parameters = {
		@StoredProcedureParameter(name = "inIBAN", type = String.class, mode = IN),
		@StoredProcedureParameter(name = "inShAccount", type = String.class, mode = IN),
		@StoredProcedureParameter(name = "inClientName", type = String.class, mode = IN),
		@StoredProcedureParameter(name = "inFiscalCode", type = String.class, mode = IN),
		@StoredProcedureParameter(name = "inBOapp", type = String.class, mode = IN),
		@StoredProcedureParameter(name = "inUserName", type = String.class, mode = IN),
		@StoredProcedureParameter(name = "inClientID", type = String.class, mode = IN),
}),
@NamedStoredProcedureQuery(name = "updateClientAccount", procedureName = "manageclientaccounts.updateclientaccount", parameters = {
		@StoredProcedureParameter(name = "inIBAN", type = String.class, mode = IN),
		@StoredProcedureParameter(name = "inShAccount", type = String.class, mode = IN),
		@StoredProcedureParameter(name = "inClientName", type = String.class, mode = IN),
		@StoredProcedureParameter(name = "inFiscalCode", type = String.class, mode = IN),
		@StoredProcedureParameter(name = "inBOapp", type = String.class, mode = IN),
		@StoredProcedureParameter(name = "inUserName", type = String.class, mode = IN),
		@StoredProcedureParameter(name = "inClientID", type = String.class, mode = IN),
}),
@NamedStoredProcedureQuery(name = "deleteClientAccount", procedureName = "manageclientaccounts.deleteclientaccount", parameters = {
		@StoredProcedureParameter(name = "inIBAN", type = String.class, mode = IN),
		@StoredProcedureParameter(name = "inShAccount", type = String.class, mode = IN),
		@StoredProcedureParameter(name = "inClientName", type = String.class, mode = IN),
		@StoredProcedureParameter(name = "inFiscalCode", type = String.class, mode = IN),
		@StoredProcedureParameter(name = "inBOapp", type = String.class, mode = IN),
		@StoredProcedureParameter(name = "inUserName", type = String.class, mode = IN),
		@StoredProcedureParameter(name = "inClientID", type = String.class, mode = IN),
}),
@NamedStoredProcedureQuery(name = "readTemp", procedureName = "manageclientaccounts.readtmpclientaccounts", resultClasses = TmpIbanEntity.class, parameters = {
				@StoredProcedureParameter(name = "inIBAN", type = String.class, mode = IN),
				@StoredProcedureParameter(name = "inUID", type = String.class, mode = IN),
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
@NamedQuery(name = "TmpIbanEntity.findAll", query = "SELECT t FROM TmpIbanEntity t"),
@NamedQuery(name = "TmpIbanEntity.findByPk", query = "SELECT t FROM TmpIbanEntity t WHERE t.uid_col = :uid_col"),
@NamedQuery(name = "TmpIbanEntity.findByIbanStatus", query = "SELECT count(t.iban) FROM TmpIbanEntity t WHERE t.iban = :iban and t.optype=:optype"),
@NamedQuery(name = "TmpIbanEntity.findTotal", query = "SELECT count(t.iban) FROM TmpIbanEntity t")
})
@Cacheable(false)
public class TmpIbanEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private String clientname;

	private String fiscalcode;

	private String clientid;
	
	private String iban;

	private String optype;

	private Integer persistent;

	private String shortaccount;

	private String sourceapp;

	private String user1apprv;

	private String user2apprv;
	
	@Id
	private String uid_col;

	public TmpIbanEntity() {
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

	public String getIban() {
		return this.iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public String getOptype() {
		return this.optype;
	}

	public void setOptype(String optype) {
		this.optype = optype;
	}

	public Integer getPersistent() {
		return this.persistent;
	}

	public void setPersistent(Integer persistent) {
		this.persistent = persistent;
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

	public String getUser1apprv() {
		return this.user1apprv;
	}

	public void setUser1apprv(String user1apprv) {
		this.user1apprv = user1apprv;
	}

	public String getUser2apprv() {
		return this.user2apprv;
	}

	public void setUser2apprv(String user2apprv) {
		this.user2apprv = user2apprv;
	}

	@Override
	public String toString() {
		String res = "";
		res += getIban();
		return res;
	}

	/**
	 * @return the uid_col
	 */
	public String getUid_col() {
		return uid_col;
	}

	/**
	 * @param uid_col the uid_col to set
	 */
	public void setUid_col(String uid_col) {
		this.uid_col = uid_col;
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