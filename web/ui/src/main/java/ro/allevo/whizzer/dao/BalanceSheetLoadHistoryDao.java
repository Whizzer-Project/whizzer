package ro.allevo.whizzer.dao;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import ro.allevo.whizzer.model.BalanceSheetLoadHistory;

@Service
public interface BalanceSheetLoadHistoryDao {
	
	public BalanceSheetLoadHistory getBalanceSheet(String id);
	public BalanceSheetLoadHistory[] getAllBalanceSheets();
	public BalanceSheetLoadHistory[] getBalanceSheets(LinkedHashMap<String, List<String>> params);
	public void insertBalanceSheet(BalanceSheetLoadHistory balanceSheet);
	public void updateBalanceSheet(BalanceSheetLoadHistory balanceSheet, String id);
	public void deleteBalanceSheet(String id);

}
