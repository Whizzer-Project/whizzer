package ro.allevo.whizzer.dao;

import java.net.URI;
import javax.ws.rs.core.UriBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.dao.RestApiDao;
import ro.allevo.fintpui.model.Bank;
import ro.allevo.whizzer.model.ProfitLossReport;

@Service
public class ProfitLossReportRestApiDao extends RestApiDao<ProfitLossReport> implements ProfitLossReportDao{

	@Autowired
	Config config;
	
	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getWhizzerURL()).path("profit-loss-report").build();
	}
	
	public ProfitLossReportRestApiDao() {
		super(ProfitLossReport.class);
	}
	
	@Override
	public ProfitLossReport[] getAllProfitLossReports() {
		return getAll();
	}

	
}
