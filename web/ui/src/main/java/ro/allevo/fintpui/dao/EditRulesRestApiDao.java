package ro.allevo.fintpui.dao;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.model.EditRules;

@Service
public class EditRulesRestApiDao extends RestApiDao<EditRules> implements EditRulesDao{
	
	@Autowired
	Config config;
	
	public EditRulesRestApiDao() {
		super(EditRules.class);
	}

	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getAPIUrl()).path("message-edit/rules").build();
	}

	@Override
	public EditRules[] getAllEditRules() {
		return getAll();
	}

	@Override
	public EditRules getEditRules(String id) {
		return get(id);
	}
	
	@Override
	public void insertEditRules(EditRules editRules) {
		post(editRules);
	}
	
	@Override
	public void updateEditRules(EditRules editRules, String id) {
		put(id, editRules);
	}
	
	@Override
	public void deleteEditRules(String id) {
		delete(id);
	}

	@Override
	public EditRules[] getEditRulesByMessageType(String messageType) {
		URI uri = UriBuilder.fromUri(getBaseUrl()).queryParam("message_type", messageType).build();
		return getAll(uri);
	}

}
