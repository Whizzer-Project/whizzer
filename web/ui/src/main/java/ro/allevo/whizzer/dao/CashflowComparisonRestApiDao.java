package ro.allevo.whizzer.dao;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.dao.RestApiDao;
import ro.allevo.fintpui.model.EditRules;
import ro.allevo.fintpui.utils.PagedCollection;
import ro.allevo.whizzer.model.CashflowComparison;
import ro.allevo.whizzer.model.CashflowComparisonReport;

@Service
public class CashflowComparisonRestApiDao extends RestApiDao<CashflowComparison> implements CashflowComparisonDao{

	@Autowired
	Config config;
	
	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getWhizzerURL()).path("cash-reporting").path("cash-flow").path("comparison").build();
	}
	
	public CashflowComparisonRestApiDao() {
		super(CashflowComparison.class);
	}
	
	@Override
	public CashflowComparison[] getAllCashflowComparison() {
		return getAll();
	}

	@Override
	public CashflowComparison getCashflowComparison(String id) {
		return get(id);
	}

	@Override
	public void insertCashflowComparison(CashflowComparison cashflowForecast) {
		post(cashflowForecast);
	}

	@Override
	public void updateCashflowComparison(CashflowComparison cashflowForecast, String id) {
		put(id, cashflowForecast);
	}

	@Override
	public void deleteCashflowComparison(String id) {
		delete(id);
	}
	
	@Override
	public CashflowComparisonReport[] getPageReport(String entity, String date) {
		URI uri = UriBuilder.fromUri(config.getWhizzerURL()).path("cash-reporting").path("cash-flow").path("comparison-report")
				.queryParam("entityName", entity).queryParam("date", date).build();
		return getList(uri, CashflowComparisonReport.class);
	}

}
