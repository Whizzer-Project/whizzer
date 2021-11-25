package ro.allevo.whizzer.dao;

import org.springframework.stereotype.Service;

import ro.allevo.whizzer.model.BalanceSheetComparison;

@Service
public interface BalanceSheetComparisonDao {
	
	public BalanceSheetComparison[] getAllBalanceSheetComparison();
}
