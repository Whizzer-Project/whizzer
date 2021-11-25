package ro.allevo.fintpui.service;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.dao.RepreconInvIssVsStatementRestApiDao;
import ro.allevo.fintpui.model.RepreconInvVsStatement;
import ro.allevo.fintpui.utils.PagedCollection;

@Service
public class RepreconInvIssVsStatementService {
	
	@Autowired
	private  RepreconInvIssVsStatementRestApiDao repreconInvIssVsStatementDao;

	public  RepreconInvVsStatement[] getAllRepreconInvIssVsStatement() {
		return repreconInvIssVsStatementDao.getAllRepreconInvIssVsStatement();
	}
	
	public PagedCollection<RepreconInvVsStatement> getPage() {
		return repreconInvIssVsStatementDao.getPage();
	}
	
	
	public PagedCollection<RepreconInvVsStatement> getPage(LinkedHashMap<String, List<String>> params) {
		return repreconInvIssVsStatementDao.getPage(params);
	}


	public RepreconInvVsStatement getRepreconInvIssVsStatement(String id) {
		return repreconInvIssVsStatementDao.getRepreconInvIssVsStatement(id);
	}
	
	
	public Set<String> getAllReconciliationStmtNumber() {
		RepreconInvVsStatement[] reconciliations = getAllRepreconInvIssVsStatement();
		Set<String> result = new HashSet<String>();
 		for(RepreconInvVsStatement reconciliation : reconciliations){
			result.add(reconciliation.getStmtstatementNumber());
		}
		return result;
	}
	


}


