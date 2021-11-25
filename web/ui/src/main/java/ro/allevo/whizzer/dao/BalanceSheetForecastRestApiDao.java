package ro.allevo.whizzer.dao;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.dao.RestApiDao;
import ro.allevo.whizzer.model.BalanceSheetForecast;

@Service
public class BalanceSheetForecastRestApiDao extends RestApiDao<BalanceSheetForecast> implements BalanceSheetForecastDao{
	
	@Autowired
	Config config;
	
	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getWhizzerURL()).path("balance-sheet-forecast").build();		
	}
	
	public BalanceSheetForecastRestApiDao() {
		super(BalanceSheetForecast.class);
	}

	@Override
	public BalanceSheetForecast getBalanceSheet(String id) {
		return get(id);
	}

	@Override
	public BalanceSheetForecast[] getAllBalanceSheets() {
		return getAll();
	}

	@Override
	public BalanceSheetForecast[] getBalanceSheets(LinkedHashMap<String, List<String>> params) {
		return getAll(params);
	}

	@Override
	public void insertBalanceSheet(BalanceSheetForecast balanceSheet) {
		post(balanceSheet);
		
	}

	@Override
	public void updateBalanceSheet(BalanceSheetForecast balanceSheet, String id) {
		put(id, balanceSheet);
	}

	@Override
	public void deleteBalanceSheet(String id) {
		delete(id);
	
	}

}
