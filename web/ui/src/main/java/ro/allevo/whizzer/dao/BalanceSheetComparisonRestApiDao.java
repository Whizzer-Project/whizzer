package ro.allevo.whizzer.dao;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.dao.RestApiDao;
import ro.allevo.whizzer.model.BalanceSheetComparison;

@Service
public class BalanceSheetComparisonRestApiDao extends RestApiDao<BalanceSheetComparison> implements BalanceSheetComparisonDao{

	@Autowired
	Config config;
	
	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getWhizzerURL()).path("cash-reporting").path("balance-sheet").path("comparison").build();
	}
	
	public BalanceSheetComparisonRestApiDao() {
		super(BalanceSheetComparison.class);
	}
	
	@Override
	public BalanceSheetComparison[] getAllBalanceSheetComparison() {
		return getAll();
	}

}
