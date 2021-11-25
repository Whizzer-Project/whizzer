package ro.allevo.whizzer.dao;

import org.springframework.stereotype.Service;

import ro.allevo.whizzer.model.CashFlowForecastGenData;

@Service
public interface CashflowForecastReportDao {
	
	public CashFlowForecastGenData[] getAllCashflowForecast();
}
