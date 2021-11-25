package ro.allevo.connect.model;

import java.util.Date;

//import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="root")
public class Transaction {
	
	private String transactionId;
	private String creditorName;
	private String debtorName;
	private AmountEntity transactionAmount;
	private Date bookingDate;
	private Date valueDate;
	private String details;
	private IbanEntity ibanAccount;
	private String systemDateTime;
	private String fromDate;
	private String toDate;
	private String accountId;
	private String remittanceInformationUnstructured;
	private String internalEntityId; 
	private String internalEntityName;
	private String internalEntityCity;
	private String internalEntityCountry;
	private String internalEntityAddress;

	public String getRemittanceInformationUnstructured() {
		return remittanceInformationUnstructured;
	}
	public void setRemittanceInformationUnstructured(String remittanceInformationUnstructured) {
		this.remittanceInformationUnstructured = remittanceInformationUnstructured;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public String getSystemDateTime() {
		return systemDateTime;
	}
	public void setSystemDateTime(String systemDateTime) {
		this.systemDateTime = systemDateTime;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}	
	public IbanEntity getIbanAccount() {
		return ibanAccount;
	}
	public void setIbanAccount(IbanEntity debtorAccount) {
		this.ibanAccount = debtorAccount;
	}
//	@XmlElement
	public String getCreditorName() {
		return creditorName;
	}
	public void setCreditorName(String creditorName) {
		this.creditorName = creditorName;
	}
//	@XmlElement
	public String getDebtorName() {
		return debtorName;
	}
	public void setDebtorName(String debtorName) {
		this.debtorName = debtorName;
	}
//	@XmlElement
	public AmountEntity getTransactionAmount() {
		return transactionAmount;
	}
	public void setTransactionAmount(AmountEntity transactionAmount) {
		this.transactionAmount = transactionAmount;
	}
//	@XmlElement
	public Date getBookingDate() {
		return bookingDate;
	}
	public void setBookingDate(Date bookingDate) {
		this.bookingDate = bookingDate;
	}
//	@XmlElement
	public Date getValueDate() {
		return valueDate;
	}
	public void setValueDate(Date valueDate) {
		this.valueDate = valueDate;
	}
//	@XmlElement
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public String getInternalEntityId() {
		return internalEntityId;
	}
	public void setInternalEntityId(String internalEntityId) {
		this.internalEntityId = internalEntityId;
	}
	public String getInternalEntityName() {
		return internalEntityName;
	}
	public void setInternalEntityName(String internalEntityName) {
		this.internalEntityName = internalEntityName;
	}
	public String getInternalEntityCity() {
		return internalEntityCity;
	}
	public void setInternalEntityCity(String internalEntityCity) {
		this.internalEntityCity = internalEntityCity;
	}
	public String getInternalEntityCountry() {
		return internalEntityCountry;
	}
	public void setInternalEntityCountry(String internalEntityCountry) {
		this.internalEntityCountry = internalEntityCountry;
	}
	public String getInternalEntityAddress() {
		return internalEntityAddress;
	}
	public void setInternalEntityAddress(String internalEntityAddress) {
		this.internalEntityAddress = internalEntityAddress;
	}
	@Override
	public String toString() {
		return "Transaction [transactionId=" + transactionId + ", creditorName=" + creditorName + ", debtorName="
				+ debtorName + ", transactionAmount=" + transactionAmount + ", bookingDate=" + bookingDate
				+ ", valueDate=" + valueDate + ", details=" + details + ", ibanAccount=" + ibanAccount.getIban()
				+ ", systemDateTime=" + systemDateTime + ", fromDate=" + fromDate + ", toDate=" + toDate
				+ ", accountId=" + accountId + ", remittanceInformationUnstructured="
				+ remittanceInformationUnstructured + ", internalEntityId=" + internalEntityId + ", internalEntityName="
				+ internalEntityName + ", internalEntityCity=" + internalEntityCity + ", internalEntityCountry="
				+ internalEntityCountry + ", internalEntityAddress=" + internalEntityAddress + "]";
	}	
	
}
