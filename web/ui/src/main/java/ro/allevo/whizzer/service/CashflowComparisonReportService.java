package ro.allevo.whizzer.service;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.utils.PagedCollection;
import ro.allevo.whizzer.dao.CashflowComparisonRestApiDao;
import ro.allevo.whizzer.model.CashflowComparison;

@Service
public class CashflowComparisonReportService {

	@Autowired
	private CashflowComparisonRestApiDao cashflowComparisonDao;

	public CashflowComparison[] getAllCashflowComparisons() {
		return cashflowComparisonDao.getAllCashflowComparison();
	}
	
	public CashflowComparison[] getAllCashflowComparisons(LinkedHashMap<String, List<String>> params) {
		return cashflowComparisonDao.getAll(params);
	}
	
	public PagedCollection<CashflowComparison> getPage() {
		return cashflowComparisonDao.getPage();
	}
	
	public PagedCollection<CashflowComparison> getPage(LinkedHashMap<String, List<String>> params){
		return cashflowComparisonDao.getPage(params);
	}

}
