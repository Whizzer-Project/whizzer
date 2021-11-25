package ro.allevo.whizzer.dao;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.dao.RestApiDao;
import ro.allevo.whizzer.model.CashFlowForecastGenData;

@Service
public class CashflowForecastReportRestApiDao extends RestApiDao<CashFlowForecastGenData> implements CashflowForecastReportDao{

	@Autowired
	Config config;
	
	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getWhizzerURL()).path("cash-reporting/cash-flow/forecast/report").build();
	}
	
	public CashflowForecastReportRestApiDao() {
		super(CashFlowForecastGenData.class);
	}
	
	@Override
	public CashFlowForecastGenData[] getAllCashflowForecast() {
		return getAll();
	}

}
