package ro.allevo.whizzer.dao;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.dao.RestApiDao;
import ro.allevo.whizzer.model.ConfigBsandPl;
import ro.allevo.whizzer.model.PKIEntity;

@Service
public class ConfigBsandPlRestApiDao extends RestApiDao<ConfigBsandPl> implements ConfigBsandPlDao{

	@Autowired
	Config config;
	
	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getWhizzerURL()).path("config").path("labels").build();
	}
	
	public ConfigBsandPlRestApiDao() {
		super(ConfigBsandPl.class);
	}
	
	@Override
	public ConfigBsandPl[] getAllConfigBsandPl() {
		return getAll();
	}

	@Override
	public PKIEntity getPkis(String entity, Integer year) {
		return getObject( UriBuilder.fromUri(config.getWhizzerURL()).path("config").path("PKIs").queryParam("entity", entity).queryParam("year", year).build(), PKIEntity.class );
	}
	
}
