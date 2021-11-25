package ro.allevo.fintpui.dao;

import org.springframework.stereotype.Service;

import ro.allevo.fintpui.model.RepreconPaymentVsStatement;

@Service
public interface RepreconPaymentVsStatementDao {
	
	public RepreconPaymentVsStatement[] getAllRepreconPaymentVsStatement();
	public RepreconPaymentVsStatement getRepreconPaymentVsStatement(String id);

}
