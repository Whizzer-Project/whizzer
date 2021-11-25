package ro.allevo.fintpui.dao;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.model.ValidationsRules;

@Service
public class ValidationsRulesRestApiDao extends RestApiDao<ValidationsRules> implements ValidationsRulesDao {

	public ValidationsRulesRestApiDao() {
		super(ValidationsRules.class);
	}
	
	@Autowired
	Config config;
	
	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getAPIUrl()).path("validations/rules").build();
	}

	@Override
	public ValidationsRules[] getAllValidationsRules() {
		return getAll();
	}

	@Override
	public ValidationsRules getValidationRules(String id) {
		return get(id);
	}

	@Override
	public void insertValidationRules(ValidationsRules validationsRules) {
		post(validationsRules);		
	}

	@Override
	public void updateValidationRules(ValidationsRules validationsRules, String id) {
		put(id, validationsRules);		
	}

	@Override
	public void deleteValidationRules(String id) {
		delete(id);		
	}	
	
}
