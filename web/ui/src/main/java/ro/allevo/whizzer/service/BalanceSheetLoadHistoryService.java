package ro.allevo.whizzer.service;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.whizzer.dao.BalanceSheetLoadHistoryRestApiDao;
import ro.allevo.whizzer.model.BalanceSheetLoadHistory;

@Service
public class BalanceSheetLoadHistoryService {
	
	@Autowired
	private BalanceSheetLoadHistoryRestApiDao balanceSheetRestApiDao;
	
	public BalanceSheetLoadHistory[] getAllBalanceSheets() {
		return balanceSheetRestApiDao.getAllBalanceSheets();
	}

	public BalanceSheetLoadHistory[] getBalanceSheets(LinkedHashMap<String, List<String>> params) {
		return balanceSheetRestApiDao.getBalanceSheets(params);
	}

	public BalanceSheetLoadHistory getBalanceSheet(String id) {
		return balanceSheetRestApiDao.getBalanceSheet(id);
	}

	public void insertBalanceSheet(BalanceSheetLoadHistory balanceSheet) {
		balanceSheetRestApiDao.insertBalanceSheet(balanceSheet);
	}

	public void updateBalanceSheet(BalanceSheetLoadHistory balanceSheet, String id) {
		balanceSheetRestApiDao.updateBalanceSheet(balanceSheet, id);
	}
	
	public void deleteBalanceSheet(String id) {
		balanceSheetRestApiDao.deleteBalanceSheet(id);
	}

}
