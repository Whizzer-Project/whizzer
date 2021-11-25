package ro.allevo.fintpui.dao;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import ro.allevo.fintpui.model.UserRolesEntityMaps;

@Service
public interface UserRolesEntityMapsDao {
	
	public UserRolesEntityMaps[] getUserRolesEntityMaps(String userName, LinkedHashMap<String, List<String>> params);
	public void insertUserRolesEntityMaps(String userName, List<UserRolesEntityMaps> userRolesEntityMaps);
	public void deleteUserRolesEntityMaps(String userName);
}
