package ro.allevo.fintpui.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.allevo.fintpui.dao.RepreconPaymentVsStatementRestApiDao;
import ro.allevo.fintpui.model.Bank;
import ro.allevo.fintpui.model.InternalAccount;
import ro.allevo.fintpui.model.RepreconPaymentVsStatement;
import ro.allevo.fintpui.utils.PagedCollection;

@Service
public class RepreconPaymentVsStatementService {
	
	@Autowired
	private RepreconPaymentVsStatementRestApiDao repreconPaymentVsStatementDao;

	public RepreconPaymentVsStatement[] getAllRepreconPaymentVsStatement() {
		return repreconPaymentVsStatementDao.getAllRepreconPaymentVsStatement();
	}
	
	public PagedCollection<RepreconPaymentVsStatement> getPage() {
		return repreconPaymentVsStatementDao.getPage();
	}
	
	
	public PagedCollection<RepreconPaymentVsStatement> getPage(LinkedHashMap<String, List<String>> params) {
		return repreconPaymentVsStatementDao.getPage(params);
	}


	public RepreconPaymentVsStatement getRepreconPaymentVsStatement(String id) {
		return repreconPaymentVsStatementDao.getRepreconPaymentVsStatement(id);
	}
	
	
	public Set<String> getAllReconciliationStmtNumber() {
		RepreconPaymentVsStatement[] reconciliations = getAllRepreconPaymentVsStatement();
		Set<String> result = new HashSet<String>();
 		for(RepreconPaymentVsStatement reconciliation : reconciliations){
			result.add(reconciliation.getStmtstatementNumber());
		}
		return result;
	}
	


}
