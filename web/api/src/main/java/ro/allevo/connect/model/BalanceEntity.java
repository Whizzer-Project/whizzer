package ro.allevo.connect.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class BalanceEntity {
	
	private String balanceType;
	private LocalDateTime lastChangeDateTime;
	private LocalDate referenceDate;
//	private String lastCommittedTransaction;
	private AmountEntity balanceAmount;
	
	public String getBalanceType() {
		return balanceType;
	}
	public void setBalanceType(String balanceType) {
		this.balanceType = balanceType;
	}
	public LocalDateTime getLastChangeDateTime() {
		return lastChangeDateTime;
	}
	public void setLastChangeDateTime(LocalDateTime lastChangeDateTime) {
		this.lastChangeDateTime = lastChangeDateTime;
	}
	public LocalDate getReferenceDate() {
		return referenceDate;
	}
	public void setReferenceDate(LocalDate referenceDate) {
		this.referenceDate = referenceDate;
	}
//	public String getLastCommittedTransaction() {
//		return lastCommittedTransaction;
//	}
//	public void setLastCommittedTransaction(String lastCommittedTransaction) {
//		this.lastCommittedTransaction = lastCommittedTransaction;
//	}
	public AmountEntity getBalanceAmount() {
		return balanceAmount;
	}
	public void setBalanceAmount(AmountEntity balanceAmount) {
		this.balanceAmount = balanceAmount;
	}
			
}
