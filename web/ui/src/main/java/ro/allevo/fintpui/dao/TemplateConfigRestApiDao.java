package ro.allevo.fintpui.dao;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.model.TemplateConfig;

@Service
public class TemplateConfigRestApiDao extends RestApiDao<TemplateConfig> implements TemplateConfigDao{

	@Autowired
	Config config;

	public TemplateConfigRestApiDao() {
		super(TemplateConfig.class);
	}

	@Override
	public TemplateConfig[] getAllTemplateConfings() {
		return getAll();
	}
	
	@Override
	public TemplateConfig[] getAllTemplateConfings(LinkedHashMap<String, List<String>> params) {
		return getAll(params);
	}

	@Override
	public TemplateConfig getTemplateConfigWithXsd(String id) {
		URI uri = UriBuilder.fromUri(getBaseUrl()).path(id).queryParam("validationxsd", "").build();
		return get(uri);
	}
	
	@Override
	public TemplateConfig getTemplateConfig(String id) {
		URI uri = UriBuilder.fromUri(getBaseUrl()).path(id).build();
		return get(uri);
	}

	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getAPIUrl()).path("templates-config").build();
	}

	public void updateTemplateConfig(TemplateConfig templateConfig) {
		put("" + templateConfig.getId(), templateConfig);
	}

}
