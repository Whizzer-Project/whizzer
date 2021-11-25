package ro.allevo.fintpws.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import ro.allevo.fintpws.util.Invariants;
import ro.allevo.fintpws.util.annotations.URLId;

@Entity
@Table(schema = "FINDATA", name = "MTBKTOCSTMRDBTCDTTAB")

@NamedQueries({

		@NamedQuery(name = "MsgStatementEntity.findAll", query = "SELECT ms FROM MsgStatementEntity ms"),

		@NamedQuery(name = "MsgStatementEntity.findTotalDistinctMessageStatement", query = "SELECT COUNT(DISTINCT ms.statementNumber) FROM MsgStatementEntity ms") })

public class MsgStatementEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique = true, nullable = false, length = 30)
	@URLId
	private String oid;

	@Column(nullable = false, length = 30, name = "correlationid")
	private String correlationId;

	@Column(length = 18, name = "statementnumber")
	private String statementNumber;

	@Column(nullable = false, length = 50, name = "messagetype")
	private String messageType;

	@Column(length = 6, name = "valuedate")
	private String valueDate;

	@Column(length = 50, name = "amount")
	private String amount;

	@Column(length = 3, name = "currency")
	private String currency;

	@Column(length = 35, name = "accountnumber")
	private String accountNumber;

	@Column(length = 35, name = "statementreference")
	private String statementReference;

	@Column(length = 6, name = "openbalancedate")
	private String openBalanceDate;

	@Column(length = 6, name = "closebalancedate")
	private String closeBalanceDate;

	@Column(length = 5, name = "trxmark")
	private String trxmark;

	@Column(length = 140, name = "name")
	private String name;

	@Column(length = 500, name = "remittanceinfo")
	private String remittanceInfo;

	public MsgStatementEntity() {
		super();
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getCorrelationId() {
		return correlationId;
	}

	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}

	public String getStatementNumber() {
		return statementNumber;
	}

	public void setStatementNumber(String statementNumber) {
		this.statementNumber = statementNumber;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
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

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getStatementReference() {
		return statementReference;
	}

	public void setStatementReference(String statementReference) {
		this.statementReference = statementReference;
	}

	public String getOpenBalanceDate() {
		return openBalanceDate;
	}

	public void setOpenBalanceDate(String openBalanceDate) {
		this.openBalanceDate = openBalanceDate;
	}

	public String getCloseBalanceDate() {
		return closeBalanceDate;
	}

	public void setCloseBalanceDate(String closeBalanceDate) {
		this.closeBalanceDate = closeBalanceDate;
	}

	public String getTrxmark() {
		return trxmark;
	}

	public void setTrxmark(String trxmark) {
		this.trxmark = trxmark;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemittanceInfo() {
		return remittanceInfo;
	}

	public void setRemittanceInfo(String remittanceInfo) {
		this.remittanceInfo = remittanceInfo;
	}

	public String getValueDate() {
		return valueDate;
	}

	public void setValueDate(String valueDate) {
		this.valueDate = valueDate;
	}

	@Override
	public String toString() {
		return "MsgStatementEntity [oid=" + oid + ", correlationId=" + correlationId + ", statementNumber="
				+ statementNumber + ", messageType=" + messageType + ", valueDate=" + valueDate + ", amount=" + amount
				+ ", currency=" + currency + ", accountNumber=" + accountNumber + ", statementReference="
				+ statementReference + ", openBalanceDate=" + openBalanceDate + ", closeBalanceDate=" + closeBalanceDate
				+ ", trxmark=" + trxmark + ", name=" + name + ", remittanceInfo=" + remittanceInfo + "]";
	}

	@Override
	public String getClassEvent() {
		return Invariants.messageClassEvents.BATCH.toString();
	}
	
	@Override
	public String getMessage() {
		return "message statement";
	}

}
