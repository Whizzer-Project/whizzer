package ro.allevo.whizzer.service;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.utils.PagedCollection;
import ro.allevo.whizzer.dao.BalanceSheetComparisonRestApiDao;
import ro.allevo.whizzer.model.BalanceSheetComparison;

@Service
public class BalanceSheetComparisonService {

	@Autowired
	private BalanceSheetComparisonRestApiDao balanceSheetComparisonDao;

	public BalanceSheetComparison[] getAllBalanceSheetComparisons() {
		return balanceSheetComparisonDao.getAllBalanceSheetComparison();
	}
	
	public BalanceSheetComparison[] getAllBalanceSheetComparisons(LinkedHashMap<String, List<String>> params) {
		return balanceSheetComparisonDao.getAll(params);
	}
	
	public PagedCollection<BalanceSheetComparison> getPage() {
		return balanceSheetComparisonDao.getPage();
	}
	
	public PagedCollection<BalanceSheetComparison> getPage(LinkedHashMap<String, List<String>> params){
		return balanceSheetComparisonDao.getPage(params);
	}

}
