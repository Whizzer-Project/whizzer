package ro.allevo.whizzer.service;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.utils.PagedCollection;
import ro.allevo.whizzer.dao.CashflowComparisonRestApiDao;
import ro.allevo.whizzer.model.CashflowComparison;
import ro.allevo.whizzer.model.CashflowComparisonReport;

@Service
public class CashflowComparisonService {

	@Autowired
	private CashflowComparisonRestApiDao cashflowForecastDao;

	public CashflowComparison[] getAllCashflowForecasts() {
		return cashflowForecastDao.getAllCashflowComparison();
	}
	
	public CashflowComparison[] getAllCashflowComparisons(LinkedHashMap<String, List<String>> params) {
		return cashflowForecastDao.getAll(params);
	}
	
	public PagedCollection<CashflowComparison> getPage() {
		return cashflowForecastDao.getPage();
	}
	
	public PagedCollection<CashflowComparison> getPage(LinkedHashMap<String, List<String>> params){
		return cashflowForecastDao.getPage(params);
	}

	public CashflowComparison getCashflowComparison(String bic) {
		return cashflowForecastDao.getCashflowComparison(bic);
	}

	public void insertCashflowComparison(CashflowComparison cashflowForecast) {
		cashflowForecastDao.insertCashflowComparison(cashflowForecast);
	}

	public void updateCashflowComparison(CashflowComparison cashflowForecast, String bic) {
		cashflowForecastDao.updateCashflowComparison(cashflowForecast, bic);
	}

	public void deleteCashflowComparison(String bic) {
		cashflowForecastDao.deleteCashflowComparison(bic);
	}
	
	public CashflowComparisonReport[] getPageReport(String entity, String date){
		return cashflowForecastDao.getPageReport(entity, date);
	}

}
