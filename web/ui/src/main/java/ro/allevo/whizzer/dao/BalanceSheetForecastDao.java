package ro.allevo.whizzer.dao;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import ro.allevo.whizzer.model.BalanceSheetForecast;

@Service
public interface BalanceSheetForecastDao {
	
	public BalanceSheetForecast getBalanceSheet(String id);
	public BalanceSheetForecast[] getAllBalanceSheets();
	public BalanceSheetForecast[] getBalanceSheets(LinkedHashMap<String, List<String>> params);
	public void insertBalanceSheet(BalanceSheetForecast balanceSheet);
	public void updateBalanceSheet(BalanceSheetForecast balanceSheet, String id);
	public void deleteBalanceSheet(String id);

}
