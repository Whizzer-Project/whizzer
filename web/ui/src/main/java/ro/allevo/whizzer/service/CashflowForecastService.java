package ro.allevo.whizzer.service;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.utils.PagedCollection;
import ro.allevo.whizzer.dao.CashflowForecastRestApiDao;
import ro.allevo.whizzer.model.CashflowForecast;

@Service
public class CashflowForecastService {

	@Autowired
	private CashflowForecastRestApiDao cashflowForecastDao;

	public CashflowForecast[] getAllCashflowForecasts() {
		return cashflowForecastDao.getAllCashflowForecast();
	}
	
	public CashflowForecast[] getAllCashflowForecasts(LinkedHashMap<String, List<String>> params) {
		return cashflowForecastDao.getAll(params);
	}
	
	public PagedCollection<CashflowForecast> getPage() {
		return cashflowForecastDao.getPage();
	}
	
	public PagedCollection<CashflowForecast> getPage(LinkedHashMap<String, List<String>> params){
		return cashflowForecastDao.getPage(params);
	}

	public CashflowForecast getCashflowForecast(String bic) {
		return cashflowForecastDao.getCashflowForecast(bic);
	}

	public void insertCashflowForecast(CashflowForecast cashflowForecast) {
		cashflowForecastDao.insertCashflowForecast(cashflowForecast);
	}

	public void updateCashflowForecast(CashflowForecast cashflowForecast, String bic) {
		cashflowForecastDao.updateCashflowForecast(cashflowForecast, bic);
	}

	public void deleteCashflowForecast(String bic) {
		cashflowForecastDao.deleteCashflowForecast(bic);
	}

	
	
}
