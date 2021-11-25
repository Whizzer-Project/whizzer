package ro.allevo.connect.model;

import java.util.List;

public class TransactionsEntity {
	
	private Account account;
	private List<TransactionEntity> booked;
	private List<TransactionEntity> pending;
	
	public List<TransactionEntity> getBooked() {
		return booked;
	}
	public void setBooked(List<TransactionEntity> booked) {
		this.booked = booked;
	}
	public List<TransactionEntity> getPending() {
		return pending;
	}
	public void setPending(List<TransactionEntity> pending) {
		this.pending = pending;
	}
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	
}


