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
import static javax.persistence.ParameterMode.REF_CURSOR;

import java.io.Serializable;
import java.sql.Clob;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: RMessagesEntity
 * 
 */
@Entity

@NamedStoredProcedureQueries({
		@NamedStoredProcedureQuery(name = "RMessagesEntity.GetRmsgWaitingApprv", procedureName = "ManGenerateRmsg.GetRmsgWaitingApprv", resultClasses = RMessagesEntity.class, parameters = {

				@StoredProcedureParameter(name = "inMsgType", type = String.class, mode = IN),
				@StoredProcedureParameter(name = "outCount", type = Integer.class, mode = OUT),
				@StoredProcedureParameter(name = "retCursor", type = void.class, mode = REF_CURSOR) }),
		@NamedStoredProcedureQuery(name = "RMessagesEntity.updateRmessages", procedureName = "ManGenerateRmsg.updateRmessages", resultClasses = void.class, parameters = {
				@StoredProcedureParameter(name = "inINSTRREF", type = String.class, mode = IN),
				@StoredProcedureParameter(name = "inOprType", type = String.class, mode = IN),
				@StoredProcedureParameter(name = "inUserid", type = String.class, mode = IN),
				@StoredProcedureParameter(name = "inMsgType", type = String.class, mode = IN)}),
		@NamedStoredProcedureQuery(name = "RMessagesEntity.getOrgPayload", procedureName = "ManGenerateRmsg.GETORGPAYLOAD", parameters = {
				@StoredProcedureParameter( name = "outpayload", type = Clob.class, mode = OUT),
				@StoredProcedureParameter(name = "inMsgREF", type = String.class, mode = IN)
				})

})
@NamedQueries({
		@NamedQuery(name = "RMessagesEntity.findAll", query = "SELECT r FROM RMessagesEntity r"),
		@NamedQuery(name = "RMessagesEntity.findTotal", query = "SELECT count(r.reference) FROM RMessagesEntity r") })
public class RMessagesEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String reference;

	private String orgpayload;

	private String messagetype;

	private String counterparty;

	private String origamount;

	private String currency;

	private String valuedate;

	private String debtoraccount;

	private String debtorcustomername;

	private String creditoraccount;

	private String creditorcustomername;

	private String refusalcod;

	private String originatorname_bic;

	private String iduser;
	
	private String refPl; 
	
	private String tagErr; 
	
	private String details; 
	
	private String orgguid; 
	
	public RMessagesEntity() {
		super();
	}

	/**
	 * @return the reference
	 */
	public String getReference() {
		return reference;
	}

	/**
	 * @param reference
	 *            the reference to set
	 */
	public void setReference(String reference) {
		this.reference = reference;
	}

	/**
	 * @return the orgpayload
	 */
	public String getOrgpayload() {
		return orgpayload;
	}

	/**
	 * @param orgpayload
	 *            the orgpayload to set
	 */
	public void setOrgpayload(String orgpayload) {
		this.orgpayload = orgpayload;
	}

	/**
	 * @return the messagetype
	 */
	public String getMessagetype() {
		return messagetype;
	}

	/**
	 * @param messagetype
	 *            the messagetype to set
	 */
	public void setMessagetype(String messagetype) {
		this.messagetype = messagetype;
	}

	/**
	 * @return the counterparty
	 */
	public String getCounterparty() {
		return counterparty;
	}

	/**
	 * @param counterparty
	 *            the counterparty to set
	 */
	public void setCounterparty(String counterparty) {
		this.counterparty = counterparty;
	}

	/**
	 * @return the origamount
	 */
	public String getOrigamount() {
		return origamount;
	}

	/**
	 * @param origamount
	 *            the origamount to set
	 */
	public void setOrigamount(String origamount) {
		this.origamount = origamount;
	}

	/**
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}

	/**
	 * @param currency
	 *            the currency to set
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	/**
	 * @return the valuedate
	 */
	public String getValuedate() {
		return valuedate;
	}

	/**
	 * @param valuedate
	 *            the valuedate to set
	 */
	public void setValuedate(String valuedate) {
		this.valuedate = valuedate;
	}

	/**
	 * @return the debtoraccount
	 */
	public String getDebtoraccount() {
		return debtoraccount;
	}

	/**
	 * @param debtoraccount
	 *            the debtoraccount to set
	 */
	public void setDebtoraccount(String debtoraccount) {
		this.debtoraccount = debtoraccount;
	}

	/**
	 * @return the debtorcustomername
	 */
	public String getDebtorcustomername() {
		return debtorcustomername;
	}

	/**
	 * @param debtorcustomername
	 *            the debtorcustomername to set
	 */
	public void setDebtorcustomername(String debtorcustomername) {
		this.debtorcustomername = debtorcustomername;
	}

	/**
	 * @return the creditoraccount
	 */
	public String getCreditoraccount() {
		return creditoraccount;
	}

	/**
	 * @param creditoraccount
	 *            the creditoraccount to set
	 */
	public void setCreditoraccount(String creditoraccount) {
		this.creditoraccount = creditoraccount;
	}

	/**
	 * @return the creditorcustomername
	 */
	public String getCreditorcustomername() {
		return creditorcustomername;
	}

	/**
	 * @param creditorcustomername
	 *            the creditorcustomername to set
	 */
	public void setCreditorcustomername(String creditorcustomername) {
		this.creditorcustomername = creditorcustomername;
	}

	/**
	 * @return the refusalcod
	 */
	public String getRefusalcod() {
		return refusalcod;
	}

	/**
	 * @param refusalcod
	 *            the refusalcod to set
	 */
	public void setRefusalcod(String refusalcod) {
		this.refusalcod = refusalcod;
	}

	/**
	 * @return the originatorname_bic
	 */
	public String getOriginatorname_bic() {
		return originatorname_bic;
	}

	/**
	 * @param originatorname_bic
	 *            the originatorname_bic to set
	 */
	public void setOriginatorname_bic(String originatorname_bic) {
		this.originatorname_bic = originatorname_bic;
	}

	/**
	 * @return the iduser
	 */
	public String getIduser() {
		return iduser;
	}

	/**
	 * @param iduser
	 *            the iduser to set
	 */
	public void setIduser(String iduser) {
		this.iduser = iduser;
	}

	public String getRefPl() {
		return refPl;
	}

	public void setRefPl(String refPl) {
		this.refPl = refPl;
	}

	public String getTagErr() {
		return tagErr;
	}

	public void setTagErr(String tagErr) {
		this.tagErr = tagErr;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}
	
	public String getOrgguid() {
		return orgguid;
	}

	public void setOrgguid(String orgguid) {
		this.orgguid = orgguid;
	}



	@Override
	public String toString() {
		String res = "";
		res += getReference();
		return res;
	}
}
