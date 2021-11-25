package ro.allevo.whizzer.dao;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.dao.RestApiDao;
import ro.allevo.whizzer.model.BalanceSheetLoadHistory;

@Service
public class BalanceSheetLoadHistoryRestApiDao extends RestApiDao<BalanceSheetLoadHistory> implements BalanceSheetLoadHistoryDao{
	
	@Autowired
	Config config;
	
	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getWhizzerURL()).path("balance-sheet").build();		
	}
	
	public BalanceSheetLoadHistoryRestApiDao() {
		super(BalanceSheetLoadHistory.class);
	}

	@Override
	public BalanceSheetLoadHistory getBalanceSheet(String id) {
		return get(id);
	}

	@Override
	public BalanceSheetLoadHistory[] getAllBalanceSheets() {
		return getAll();
	}

	@Override
	public BalanceSheetLoadHistory[] getBalanceSheets(LinkedHashMap<String, List<String>> params) {
		return getAll(params);
	}

	@Override
	public void insertBalanceSheet(BalanceSheetLoadHistory balanceSheet) {
		post(balanceSheet);
		
	}

	@Override
	public void updateBalanceSheet(BalanceSheetLoadHistory balanceSheet, String id) {
		put(id, balanceSheet);
	}

	@Override
	public void deleteBalanceSheet(String id) {
		delete(id);
	
	}

}
