package ro.allevo.whizzer.dao;


import org.springframework.stereotype.Service;

import ro.allevo.whizzer.model.BalanceSheetReport;
import ro.allevo.whizzer.model.PKIEntity;
@Service
public interface BalanceSheetReportDao {
	
	public BalanceSheetReport[] getAllBalanceSheetReports();

}
