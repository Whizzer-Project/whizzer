package ro.allevo.whizzer.service;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.whizzer.dao.ProfitLossReportRestApiDao;
import ro.allevo.whizzer.model.ProfitLossReport;
import ro.allevo.fintpui.utils.PagedCollection;

@Service
public class ProfitLossReportService {

	@Autowired
	private ProfitLossReportRestApiDao profitLossReportDao;

	public ProfitLossReport[] getAllProfitLossReports() {
		return profitLossReportDao.getAllProfitLossReports();
	}
		
	public PagedCollection<ProfitLossReport> getPage(LinkedHashMap<String, List<String>> params){
		return profitLossReportDao.getPage(params);
	}

	
}
