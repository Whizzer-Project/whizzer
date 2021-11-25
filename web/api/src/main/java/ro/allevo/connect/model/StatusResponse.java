package ro.allevo.connect.model;

public class StatusResponse {
	
	String transactionStatus;
	boolean fundsAvailable;
	
	public String getTransactionStatus() {
		return transactionStatus;
	}
	public void setTransactionStatus(String transactionStatus) {
		this.transactionStatus = transactionStatus;
	}
	public boolean isFundsAvailable() {
		return fundsAvailable;
	}
	public void setFundsAvailable(boolean fundsAvailable) {
		this.fundsAvailable = fundsAvailable;
	}
	
	
}
