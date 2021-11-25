package ro.allevo.fintpui.dao;

import org.springframework.stereotype.Service;

import ro.allevo.fintpui.model.ValidationsRules;

@Service
public interface ValidationsRulesDao {
	
//	Map<String, List<String>> getListsMetadata();
	
	public ValidationsRules[] getAllValidationsRules();
	public ValidationsRules getValidationRules(String id);
	public void insertValidationRules(ValidationsRules enrichRules);
	public void updateValidationRules(ValidationsRules enrichRules, String id);
	public void deleteValidationRules(String id);

}
