package ro.allevo.connect.model;

import ro.allevo.fintpws.model.BaseEntity;
import ro.allevo.fintpws.util.Invariants;

public class PaymentEntity extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String paymentId;
	private String transactionStatus;
	private String psuMessage;
	private String tppMessage;
	
	public String getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}
	public String getTransactionStatus() {
		return transactionStatus;
	}
	public void setTransactionStatus(String transactionStatus) {
		this.transactionStatus = transactionStatus;
	}
	public String getPsuMessage() {
		return psuMessage;
	}
	public void setPsuMessage(String psuMessage) {
		this.psuMessage = psuMessage;
	}
	public String getTppMessage() {
		return tppMessage;
	}
	public void setTppMessage(String tppMessage) {
		this.tppMessage = tppMessage;
	}
	@Override
	public String toString() {
		return "PaymentEntity [paymentId=" + paymentId + ", transactionStatus=" + transactionStatus + ", psuMessage="
				+ psuMessage + ", tppMessage=" + tppMessage + "]";
	}
	@Override
	public String getClassEvent() {
		return Invariants.connectClassEvents.MANAGE.toString();
	}
	
	@Override
	public String getMessage() {
		return "payments template";
	}
}
