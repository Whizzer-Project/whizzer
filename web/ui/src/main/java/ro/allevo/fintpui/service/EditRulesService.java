package ro.allevo.fintpui.service;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.dao.EditRulesRestApiDao;
import ro.allevo.fintpui.model.EditRules;
import ro.allevo.fintpui.utils.PagedCollection;

@Service
public class EditRulesService {
	
	@Autowired
	private EditRulesRestApiDao editRulesRestApiDao;
	
	public EditRules[] getAllEditRules() {
		return editRulesRestApiDao.getAllEditRules();
	}
	
	public PagedCollection<EditRules> getPage() {
		return editRulesRestApiDao.getPage();
	}
	
	public PagedCollection<EditRules> getPage(LinkedHashMap<String, List<String>> param){
		return editRulesRestApiDao.getPage(param);
	}

	public EditRules getEditRules(String id) {
		return editRulesRestApiDao.getEditRules(id);
	}

	public void insertEditRules(EditRules editRules) {
		editRulesRestApiDao.insertEditRules(editRules);
	}

	public void updateEditRules(EditRules editRules, String id) {
		editRulesRestApiDao.updateEditRules(editRules, id);
	}

	public void deleteEditRules(String id) {
		editRulesRestApiDao.deleteEditRules(id);
	}
	
	public EditRules[] getAllEditRulesByMessage(String messageType) {
		return editRulesRestApiDao.getEditRulesByMessageType(messageType);
	}

}
