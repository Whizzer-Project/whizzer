package ro.allevo.whizzer.service;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.whizzer.dao.BalanceSheetReportRestApiDao;
import ro.allevo.whizzer.model.BalanceSheetReport;
import ro.allevo.whizzer.model.PKIEntity;
import ro.allevo.fintpui.utils.PagedCollection;

@Service
public class BalanceSheetReportService {

	@Autowired
	private BalanceSheetReportRestApiDao balanceSheetReportDao;

	public BalanceSheetReport[] getAllBalanceSheetReports() {
		return balanceSheetReportDao.getAllBalanceSheetReports();
	}
		
	public PagedCollection<BalanceSheetReport> getPage(LinkedHashMap<String, List<String>> params){
		return balanceSheetReportDao.getPage(params);
	}

	
}
