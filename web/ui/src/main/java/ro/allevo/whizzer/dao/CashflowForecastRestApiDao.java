package ro.allevo.whizzer.dao;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.dao.RestApiDao;
import ro.allevo.whizzer.model.CashflowForecast;

@Service
public class CashflowForecastRestApiDao extends RestApiDao<CashflowForecast> implements CashflowForecastDao{

	@Autowired
	Config config;
	
	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getWhizzerURL()).path("cash-reporting/cash-flow/forecast").build();
	}
	
	public CashflowForecastRestApiDao() {
		super(CashflowForecast.class);
	}
	
	@Override
	public CashflowForecast[] getAllCashflowForecast() {
		return getAll();
	}

	@Override
	public CashflowForecast getCashflowForecast(String id) {
		return get(id);
	}

	@Override
	public void insertCashflowForecast(CashflowForecast cashflowForecast) {
		post(cashflowForecast);
	}

	@Override
	public void updateCashflowForecast(CashflowForecast cashflowForecast, String id) {
		put(id, cashflowForecast);
	}

	@Override
	public void deleteCashflowForecast(String id) {
		delete(id);
	}
}
