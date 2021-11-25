package ro.allevo.fintpui.dao;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;

import com.fasterxml.jackson.databind.ObjectMapper;

import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.model.ExternalEntity;

@Service
public class ExternalEntityRestApiDao extends RestApiDao<ExternalEntity> implements ExternalEntityDao{

	@Autowired
	Config config;
	
	@Autowired
	private RestOperations client;
	
	public ExternalEntityRestApiDao() {
		super(ExternalEntity.class);
	}
	
	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getAPIUrl()).path("external-entities").build();
	}
	
	@Override
	public ExternalEntity[] getAllExternalEntities() {
		return getAll();
	}

	@Override
	public ExternalEntity getExternalEntity(String id) {
		return get(id);
	}

	@Override
	public void insertExternalEntity(ExternalEntity externalEntity) {
		post(externalEntity);
	}

	@Override
	public void updateExternalEntity(ExternalEntity externalEntity, String id) {
		put(id, externalEntity);
	}

	@Override
	public void deleteExternalEntity(String id) {
		delete(id);
	}

	public List<HashMap<String, Object>> getExternalEntitiesWithOutRules() {
		URI uri = UriBuilder.fromUri(getBaseUrl()).path("access").build();
		ResponseEntity<String> response = client.getForEntity(uri, String.class);
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(response.getBody(),
					mapper.getTypeFactory().constructParametricType(List.class, HashMap.class));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}



}
