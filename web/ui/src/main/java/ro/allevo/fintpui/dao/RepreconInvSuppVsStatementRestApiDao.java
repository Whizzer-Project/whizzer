package ro.allevo.fintpui.dao;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.model.RepreconInvVsStatement;

@Service
public class RepreconInvSuppVsStatementRestApiDao extends RestApiDao<RepreconInvVsStatement> implements RepreconInvSuppVsStatementDao{
	
	@Autowired
	Config config;
	
	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getAPIUrl()).path("repreconInvSuppVsStatement").build();
	}
	
	public RepreconInvSuppVsStatementRestApiDao() {
		super(RepreconInvVsStatement.class);
	}
	
	@Override
	public RepreconInvVsStatement[] getAllRepreconInvSuppVsStatement() {
		return getAll();
	}

	@Override
	public RepreconInvVsStatement getRepreconInvSuppVsStatement(String id) {
		return get(id);
	}

	
}

