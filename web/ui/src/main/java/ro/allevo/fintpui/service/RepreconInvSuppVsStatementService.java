package ro.allevo.fintpui.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.dao.RepreconInvSuppVsStatementRestApiDao;
import ro.allevo.fintpui.model.Bank;
import ro.allevo.fintpui.model.InternalAccount;
import ro.allevo.fintpui.model.RepreconInvVsStatement;
import ro.allevo.fintpui.utils.PagedCollection;

@Service
public class RepreconInvSuppVsStatementService {
	
	@Autowired
	private  RepreconInvSuppVsStatementRestApiDao repreconInvSuppVsStatementDao;

	public  RepreconInvVsStatement[] getAllRepreconInvSuppVsStatement() {
		return repreconInvSuppVsStatementDao.getAllRepreconInvSuppVsStatement();
	}
	
	public PagedCollection<RepreconInvVsStatement> getPage() {
		return repreconInvSuppVsStatementDao.getPage();
	}
	
	
	public PagedCollection<RepreconInvVsStatement> getPage(LinkedHashMap<String, List<String>> params) {
		return repreconInvSuppVsStatementDao.getPage(params);
	}


	public RepreconInvVsStatement getRepreconInvSuppVsStatement(String id) {
		return repreconInvSuppVsStatementDao.getRepreconInvSuppVsStatement(id);
	}
	
	
	public Set<String> getAllReconciliationStmtNumber() {
		RepreconInvVsStatement[] reconciliations = getAllRepreconInvSuppVsStatement();
		Set<String> result = new HashSet<String>();
 		for(RepreconInvVsStatement reconciliation : reconciliations){
			result.add(reconciliation.getStmtstatementNumber());
		}
		return result;
	}
	


}

