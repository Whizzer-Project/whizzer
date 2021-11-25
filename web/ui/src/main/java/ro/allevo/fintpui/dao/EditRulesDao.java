package ro.allevo.fintpui.dao;

import org.springframework.stereotype.Service;

import ro.allevo.fintpui.model.EditRules;

@Service
public interface EditRulesDao {
	
	public EditRules[] getAllEditRules();
	public EditRules getEditRules(String id);
	public void insertEditRules(EditRules editRules);
	public void updateEditRules(EditRules editRules, String id);
	public void deleteEditRules(String id);
	public EditRules[] getEditRulesByMessageType(String messageType);

}
