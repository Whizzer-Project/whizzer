package ro.allevo.fintpui.dao;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.model.LocationCode;

@Service
public class LocationCodeRestApiDao extends RestApiDao<LocationCode> implements LocationCodeDao{

	@Autowired
	Config config;
	
	public LocationCodeRestApiDao() {
		super(LocationCode.class);
	}
	
	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getAPIUrl()).path("location-codes").build();
	}
	
	@Override
	public LocationCode[] getAllLocationCodes() {
		return getAll();
	}

	@Override
	public LocationCode getLocationCode(String id) {
		return get(id);
	}

	@Override
	public void insertLocationCode(LocationCode locationCode) {
		post(locationCode);
	}

	@Override
	public void updateLocationCode(LocationCode locationCode, String id) {
		put(id, locationCode);
	}

	@Override
	public void deleteLocationCode(String id) {
		delete(id);
	}

}
