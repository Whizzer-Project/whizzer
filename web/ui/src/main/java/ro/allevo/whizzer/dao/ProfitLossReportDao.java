package ro.allevo.whizzer.dao;


import org.springframework.stereotype.Service;

import ro.allevo.whizzer.model.ProfitLossReport;
@Service
public interface ProfitLossReportDao {
	
	public ProfitLossReport[] getAllProfitLossReports();
	
}
