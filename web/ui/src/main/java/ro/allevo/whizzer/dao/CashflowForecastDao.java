package ro.allevo.whizzer.dao;

import org.springframework.stereotype.Service;

import ro.allevo.whizzer.model.CashflowForecast;


@Service
public interface CashflowForecastDao {
	
	public CashflowForecast[] getAllCashflowForecast();
	public CashflowForecast getCashflowForecast(String id);
	public void insertCashflowForecast(CashflowForecast cashflowForecast);
	public void updateCashflowForecast(CashflowForecast cashflowForecast, String id);
	public void deleteCashflowForecast(String id);
}
