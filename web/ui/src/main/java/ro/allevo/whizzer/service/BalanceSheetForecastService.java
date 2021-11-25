package ro.allevo.whizzer.service;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.whizzer.dao.BalanceSheetForecastRestApiDao;
import ro.allevo.whizzer.model.BalanceSheetForecast;

@Service
public class BalanceSheetForecastService {
	
	@Autowired
	private BalanceSheetForecastRestApiDao balanceSheetRestApiDao;
	
	public BalanceSheetForecast[] getAllBalanceSheets() {
		return balanceSheetRestApiDao.getAllBalanceSheets();
	}

	public BalanceSheetForecast[] getBalanceSheets(LinkedHashMap<String, List<String>> params) {
		return balanceSheetRestApiDao.getBalanceSheets(params);
	}

	public BalanceSheetForecast getBalanceSheet(String id) {
		return balanceSheetRestApiDao.getBalanceSheet(id);
	}

	public void insertBalanceSheet(BalanceSheetForecast balanceSheet) {
		balanceSheetRestApiDao.insertBalanceSheet(balanceSheet);
	}

	public void updateBalanceSheet(BalanceSheetForecast balanceSheet, String id) {
		balanceSheetRestApiDao.updateBalanceSheet(balanceSheet, id);
	}
	
	public void deleteBalanceSheet(String id) {
		balanceSheetRestApiDao.deleteBalanceSheet(id);
	}

}
