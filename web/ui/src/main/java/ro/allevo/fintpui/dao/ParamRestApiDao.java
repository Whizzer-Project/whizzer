package ro.allevo.fintpui.dao;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.model.Params;

@Service
public class ParamRestApiDao extends RestApiDao<Params> implements ParamDao{
	
	@Autowired
	Config config;
	
	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getAPIUrl()).path("params").build();
	}
	
	public ParamRestApiDao() {
		super(Params.class);
	}

	@Override
	public Params[] getAllParams() {
		return getAll();
	}

	@Override
	public Params getParam(String code) {
		return get(code);
	}

	@Override
	public void updateParam(Params params, String code) {
		put(code , params);	
	}

}
