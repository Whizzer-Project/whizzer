package ro.allevo.fintpui.dao;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.model.TemplateConfigDetailed;
import ro.allevo.fintpui.utils.PagedCollection;

@Service
public class TemplateConfigDetailedRestApiDao extends RestApiDao<TemplateConfigDetailed> implements TemplateConfigDetailedDao{

	@Autowired
	Config config;

	public TemplateConfigDetailedRestApiDao() {
		super(TemplateConfigDetailed.class);
	}

	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getAPIUrl()).path("templates-config").build();
	}

	@Override
	public TemplateConfigDetailed[] getAllTemplateConfigDetailed(String configId) {
		URI uri = UriBuilder.fromUri(getBaseUrl()).path(configId).path("fields").build();
		return getAll(uri);
	}

	@Override
	public TemplateConfigDetailed getTemplateConfigDetailedById(String configId, Integer fieldId) {
		URI uri = UriBuilder.fromUri(getBaseUrl()).path("" + configId).path("fields").path("" + fieldId).build();
		return get(uri);
	}
	
	@Override
	public ResponseEntity<String> insertTemplatesConfigDetailed(Integer templateConfigId, TemplateConfigDetailed[] templateConfigDetailed) {
		URI uri = UriBuilder.fromUri(getBaseUrl()).path("" + templateConfigId).path("fields").build();
		return post(uri, templateConfigDetailed);
	}

//	@Override
//	public Response deleteTemplatesConfigDetailedsByConfigId(Integer configId) {
//		URI uri = UriBuilder.fromUri(getBaseUrl()).path("" + configId).path("fields").build();
//		ResponseEntity<String> del = delete(uri); 
//		return Response.ok(del).build();
//	}

	public ResponseEntity<String> updateTempatesConfigDetailed(TemplateConfigDetailed templateConfigDetailed) {
		URI uri = UriBuilder.fromUri(getBaseUrl()).path("" + templateConfigDetailed.getTxtemplatesconfig().getId()).path("fields").path(""+templateConfigDetailed.getId()).build();
		return put(uri, templateConfigDetailed);
	}

	@Override
	public Response deleteTemplatesConfigDetailedById(String id) {
		URI uri = UriBuilder.fromUri(getBaseUrl()).path("fields").path(id).build();
		delete(uri);
		String del = delete(uri).getBody(); 
		return Response.ok(del).build();
	}

	@Override
	public PagedCollection<TemplateConfigDetailed> getTemplateConfigDetailedPage(String configId, LinkedHashMap<String, List<String>> param) {
		URI uri = UriBuilder.fromUri(getBaseUrl()).path(configId).path("fields").build();
		return getPage(uri, param);
	}

}
