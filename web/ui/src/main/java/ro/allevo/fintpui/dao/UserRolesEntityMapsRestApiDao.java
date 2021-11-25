package ro.allevo.fintpui.dao;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.model.UserRolesEntityMaps;

@Service
public class UserRolesEntityMapsRestApiDao extends RestApiDao<UserRolesEntityMaps> implements UserRolesEntityMapsDao {
	
	@Autowired
	Config config;
	

	public UserRolesEntityMapsRestApiDao() {
		super(UserRolesEntityMaps.class);
	}

	@Override
	public UserRolesEntityMaps[] getUserRolesEntityMaps(String userName, LinkedHashMap<String, List<String>> params) {
		URI uri = UriBuilder.fromUri(config.getAPIUrl()).path("users")
				.path(userName).path("user-roles-entity").build();
		if (null == params) {
			return getAll(uri);
		}
		return getAll(uri, params);
	}

	@Override
	public URI getBaseUrl() {
		return null;
	}

	@Override
	public void insertUserRolesEntityMaps(String userName, List<UserRolesEntityMaps> userRolesEntityMaps) {
		URI uri = UriBuilder.fromUri(config.getAPIUrl()).path("users")
				.path(userName).path("user-roles-entity").build();
		post(uri, userRolesEntityMaps.toArray(new UserRolesEntityMaps[0]));
	}

	@Override
	public void deleteUserRolesEntityMaps(String userName) {
		URI uri = UriBuilder.fromUri(config.getAPIUrl()).path("users")
				.path(userName).path("user-roles-entity").build();
		delete(uri);
	}
}
