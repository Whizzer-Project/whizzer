package ro.allevo.whizzer.dao;

import org.springframework.stereotype.Service;

import ro.allevo.fintpui.utils.PagedCollection;
import ro.allevo.whizzer.model.CashflowComparison;
import ro.allevo.whizzer.model.CashflowComparisonReport;


@Service
public interface CashflowComparisonDao {
	
	public CashflowComparison[] getAllCashflowComparison();
	public CashflowComparison getCashflowComparison(String id);
	public void insertCashflowComparison(CashflowComparison cashflowForecast);
	public void updateCashflowComparison(CashflowComparison cashflowForecast, String id);
	public void deleteCashflowComparison(String id);
	CashflowComparisonReport[] getPageReport(String entity, String date);
}
