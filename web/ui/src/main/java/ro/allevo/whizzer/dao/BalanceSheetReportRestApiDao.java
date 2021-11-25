package ro.allevo.whizzer.dao;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.dao.RestApiDao;
import ro.allevo.whizzer.model.BalanceSheetReport;

@Service
public class BalanceSheetReportRestApiDao extends RestApiDao<BalanceSheetReport> implements BalanceSheetReportDao{

	@Autowired
	Config config;
	
	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getWhizzerURL()).path("balance-sheet-report").build();
	}
	
	public BalanceSheetReportRestApiDao() {
		super(BalanceSheetReport.class);
	}
	
	@Override
	public BalanceSheetReport[] getAllBalanceSheetReports() {
		return getAll();
	}

}
