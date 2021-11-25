package ro.allevo.fintpui.dao;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.UriBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;

import com.fasterxml.jackson.databind.ObjectMapper;

import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.model.InternalEntity;

@Service
public class InternalEntityRestApiDao extends RestApiDao<InternalEntity> implements InternalEntityDao{

	@Autowired
	Config config;
	
	@Autowired
	private RestOperations client;
	
	public InternalEntityRestApiDao() {
		super(InternalEntity.class);
	}
	
	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getAPIUrl()).path("internal-entities").build();
	}
	
	@Override
	public InternalEntity[] getAllInternalEntities(Boolean isAdmin) {
		if (Boolean.TRUE.equals(isAdmin)) {
			URI uri = UriBuilder.fromUri(getBaseUrl()).queryParam("isAdmin", true).build();
			return getAll(uri);
		}
		return getAll();
	}
	
	@Override
	public List<HashMap<String, Object>> getAllInternalEntities(){
		URI uri = UriBuilder.fromUri(getBaseUrl()).path("access").build();
		InternalEntity[] internalEntities = getAll(uri);
		Arrays.sort(internalEntities, (a,b) -> a.getName().compareTo(b.getName()));
		List<HashMap<String, Object>> entity = new ArrayList<>();
		for (InternalEntity internalEntity : internalEntities) {
			HashMap<String, Object> mapEntity = new HashMap<>();
			mapEntity.put("id", internalEntity.getId());
			mapEntity.put("name", internalEntity.getName());
			entity.add(mapEntity);
		}
		return entity;
	}
	
	@Override
	public InternalEntity[] getAllInternalEntities(LinkedHashMap<String, List<String>> params) {
		return getAll(params);
	}

	@Override
	public InternalEntity getInternalEntity(String id) {
		return get(id);
	}

	@Override
	public void insertInternalEntity(InternalEntity internalEntity) {
		post(internalEntity);
	}

	@Override
	public void updateInternalEntity(InternalEntity internalEntity, String id) {
		put(id, internalEntity);
	}

	@Override
	public void deleteInternalEntity(String id) {
		delete(id);
	}

	@Override
	public String[] getInternalsByRights(String messagetype) {
		URI uri = UriBuilder.fromUri(getBaseUrl()).path("byRights").queryParam("messageType", messagetype).build();
		return getList(uri, String.class);
	}



}
