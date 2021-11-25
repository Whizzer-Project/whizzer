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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ro.allevo.fintpws.util.Invariants;
import ro.allevo.fintpws.util.ModelUtils;
import ro.allevo.fintpws.util.annotations.URLId;



@Entity
@Table(schema = "FINLIST", name="EXTERNALACCOUNTS")
@NamedQueries({
	@NamedQuery(name = "ExternalAccountEntity.findAll", query = "select ea from ExternalAccountEntity ea"),
	@NamedQuery(name = "ExternalAccountEntity.findTotal", query = "select count(ea.id) from ExternalAccountEntity ea"),
	@NamedQuery(name = "ExternalAccountEntity.findAllEntity", query = "select ea from ExternalAccountEntity ea where ea.entityName = :entityname"),
	@NamedQuery(name = "ExternalAccountEntity.findTotalEntity", query = "select count(ea.id) from ExternalAccountEntity ea where ea.entityName = :entityname"),
	@NamedQuery(name = "ExternalAccountEntity.findById", query = "select ea from ExternalAccountEntity ea where ea.id=:id") })
@Cacheable(false)
public class ExternalAccountEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="ExternalAccountIdGenerator")
    @TableGenerator(name="ExternalAccountIdGenerator", table="FINCFG.IDGENLIST",
         pkColumnName="TABCOLNAME", valueColumnName="IDVALUE",
         pkColumnValue="EXTERNALACCOUNTS_ID")
	@Column(unique=true, nullable=false, length=38, updatable=false)
	@URLId
	private long id;

	@ManyToOne(targetEntity = ExternalEntity.class, cascade = { CascadeType.REFRESH }, fetch = FetchType.LAZY)
	@JoinColumn(name = "EXTERNALENTITYNAME", referencedColumnName = "NAME", insertable = false, updatable = false)
	private ExternalEntity externalEntity;
	
	@OneToOne(targetEntity = BankEntity.class, cascade = { CascadeType.REFRESH }, fetch = FetchType.LAZY)
	@JoinColumn(name = "bankbic", referencedColumnName = "bic", insertable = false, updatable = false)
	private BankEntity bankEntity;

	@Column(nullable=false, length=3)
	private String currency;

	@Column(nullable=false, length=35, name="accountnumber")
	private String accountNumber;
	
	@Column(nullable=false, length=8, name="bankbic")
	private String bic;
	
	@Column(length=70)
	private String description;
	
	@Column(length=1)
	private String locked;
	
	@Column(length=1, name="defaultaccount")
	private String defaultAccount;
	
	@Column(length=70, name="otherdetails")
	private String otherDetails;
	
	@Column(nullable=false, length=35, name="externalentityname")
	private String entityName;
	
	@Column(precision=17, scale=2, name="maxamount")
	private BigDecimal maxAmount;

	public long getId() {
		return id;
	}

	public void setId(long accid) {
		this.id = accid;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accnumber) {
		this.accountNumber = accnumber;
	}

	public String getBic() {
		return bic;
	}

	public void setBic(String bic) {
		this.bic = bic;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLocked() {
		return locked;
	}

	public void setLocked(String locked) {
		this.locked = ModelUtils.toBoolYN(locked);
	}

	public String getDefaultAccount() {
		return defaultAccount;
	}

	public void setDefaultAccount(String defaultacc) {
		this.defaultAccount = ModelUtils.toBoolYN(defaultacc);
	}

	public String getOtherDetails() {
		return otherDetails;
	}

	public void setOtherDetails(String otherdet) {
		this.otherDetails = otherdet;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityname) {
		this.entityName = entityname;
	}
	
	public BigDecimal getMaxAmount() {
		return maxAmount;
	}

	public void setMaxAmount(BigDecimal maxamount) {
		this.maxAmount = maxamount;
	}
	
	//gets 404 at serialize
	@JsonIgnore
	public ExternalEntity getExternalEntity() {
		return externalEntity;
	}

	@JsonIgnore
	public BankEntity getBankEntity() {
		return bankEntity;
	}

	@Override
	public String toString() {
		return "ExternalAccountEntity [id=" + id + ", externalEntity=" + externalEntity + ", bankEntity=" + bankEntity
				+ ", currency=" + currency + ", accountNumber=" + accountNumber + ", bic=" + bic + ", description="
				+ description + ", locked=" + locked + ", defaultAccount=" + defaultAccount + ", otherDetails="
				+ otherDetails + ", entityName=" + entityName + ", maxAmount=" + maxAmount + "]";
	}

	@Override
	public String getClassEvent() {
		return Invariants.configClassEvents.MANAGE.toString();
	}
	
	@Override
	public String getMessage() {
		return "entry in list external accounts";
	}
}

