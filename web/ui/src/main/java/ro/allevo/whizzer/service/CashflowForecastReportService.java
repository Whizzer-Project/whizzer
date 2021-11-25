package ro.allevo.whizzer.service;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.utils.PagedCollection;
import ro.allevo.whizzer.dao.CashflowForecastReportRestApiDao;
import ro.allevo.whizzer.model.CashFlowForecastGenData;

@Service
public class CashflowForecastReportService {

	@Autowired
	private CashflowForecastReportRestApiDao cashflowForecastReportDao;

	public PagedCollection<CashFlowForecastGenData> getPage() {
		return cashflowForecastReportDao.getPage();
	}
	
	public PagedCollection<CashFlowForecastGenData> getPage(LinkedHashMap<String, List<String>> params){
		return cashflowForecastReportDao.getPage(params);
	}	
	
}
