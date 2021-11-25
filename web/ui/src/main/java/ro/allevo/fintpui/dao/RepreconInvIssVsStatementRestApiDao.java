package ro.allevo.fintpui.dao;
import java.net.URI;

import javax.ws.rs.core.UriBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.model.RepreconInvVsStatement;

@Service
public class RepreconInvIssVsStatementRestApiDao extends RestApiDao<RepreconInvVsStatement> implements RepreconInvIssVsStatementDao{
	
	@Autowired
	Config config;
	
	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getAPIUrl()).path("repreconInvIssVsStatement").build();
	}
	
	public RepreconInvIssVsStatementRestApiDao() {
		super(RepreconInvVsStatement.class);
	}
	
	@Override
	public RepreconInvVsStatement[] getAllRepreconInvIssVsStatement() {
		return getAll();
	}

	@Override
	public RepreconInvVsStatement getRepreconInvIssVsStatement(String id) {
		return get(id);
	}

	
}


