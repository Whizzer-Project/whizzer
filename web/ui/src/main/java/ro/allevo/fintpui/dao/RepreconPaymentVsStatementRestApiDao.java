package ro.allevo.fintpui.dao;


import java.net.URI;
import javax.ws.rs.core.UriBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.model.RepreconPaymentVsStatement;

@Service
public class RepreconPaymentVsStatementRestApiDao extends RestApiDao<RepreconPaymentVsStatement> implements RepreconPaymentVsStatementDao{
	
	@Autowired
	Config config;
	
	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getAPIUrl()).path("repreconPaymentVsStatement").build();
	}
	
	public RepreconPaymentVsStatementRestApiDao() {
		super(RepreconPaymentVsStatement.class);
	}
	
	@Override
	public RepreconPaymentVsStatement[] getAllRepreconPaymentVsStatement() {
		return getAll();
	}

	@Override
	public RepreconPaymentVsStatement getRepreconPaymentVsStatement(String id) {
		return get(id);
	}

	
}
