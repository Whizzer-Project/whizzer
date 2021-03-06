package ro.allevo.fintpui.dao;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.model.UserRole;

@Service
public class UserRoleRestApiDao extends RestApiDao<UserRole> implements UserRoleDao {
	
	@Autowired
	Config config;
	
	public UserRoleRestApiDao() {
		super(UserRole.class);
	}
	
	@Override
	public URI getBaseUrl() {
		return null;
	}
	
	@Override
	public void addRoles(String username, UserRole[] roles) {
		URI uri = UriBuilder.fromUri(config.getAPIUrl()).path("users")
				.path(username).path("user-roles").build();
		post(uri, roles);
		
	}

	@Override
	public UserRole[] getRoles(String username) {
		URI uri = UriBuilder.fromUri(config.getAPIUrl()).path("users")
				.path(username).path("user-roles").build();

		return getAll(uri);
	}
	
	@Override
	public UserRole[] getRolesByUserDefined(Integer userId, LinkedHashMap<String, List<String>> params) {
		URI uri = UriBuilder.fromUri(config.getAPIUrl()).path("users").path("by-id")
				.path(String.valueOf(userId)).path("user-roles").build();

		return getAll(uri, params);
	}

	@Override
	public void deleteRoles(String username) {
		URI uri = UriBuilder.fromUri(config.getAPIUrl()).path("users")
				.path(username).path("user-roles").build();

		delete(uri);
	}

}
