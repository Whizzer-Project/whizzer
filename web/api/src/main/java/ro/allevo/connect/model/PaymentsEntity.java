package ro.allevo.connect.model;

import java.util.List;

public class PaymentsEntity {

	private List<PaymentEntity> payments;
	
	public List<PaymentEntity> getPayments() {
		return payments;
	}
	
	public void setPayments(List<PaymentEntity> payments) {
		this.payments = payments;
	}
}
