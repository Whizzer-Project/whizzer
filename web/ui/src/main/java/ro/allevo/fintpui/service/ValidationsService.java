package ro.allevo.fintpui.service;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.dao.ValidationsRestApiDao;
import ro.allevo.fintpui.dao.ValidationsRulesRestApiDao;
import ro.allevo.fintpui.model.ValidationsRules;
import ro.allevo.fintpui.utils.PagedCollection;

@Service
public class ValidationsService {

	@Autowired
	private ValidationsRulesRestApiDao validationsRulesRestApiDao;
	
	@Autowired
	private ValidationsRestApiDao validationsRestApiDao;
	
	public ValidationsRules[] getAllvalidationsRules() {
		return validationsRulesRestApiDao.getAllValidationsRules();
	}

	public PagedCollection<ValidationsRules> getPage() {
		return validationsRulesRestApiDao.getPage();
	}

	public PagedCollection<ValidationsRules> getPage(LinkedHashMap<String, List<String>> param){
		return validationsRulesRestApiDao.getPage(param);
	}

	public ValidationsRules getValidationRules(String id) {
		return validationsRulesRestApiDao.getValidationRules(id);
	}

	public void insertValidationRules(ValidationsRules validationsRules) {
		validationsRulesRestApiDao.insertValidationRules(validationsRules);
	}
	
	public void updateValidationRules(ValidationsRules validationsRules, String id) {
		validationsRulesRestApiDao.updateValidationRules(validationsRules, id);
	}
	
	public void deleteValidationRules(String id) {
		validationsRulesRestApiDao.deleteValidationRules(id);
	}
	
	public boolean validateIban(String iban) {
		return validationsRestApiDao.validateIban(iban);
	}
	
	public boolean validateIbanRo(String iban, String debtor) {
		return validationsRestApiDao.validateIbanRo(iban, debtor);
	}
	
	public boolean validateCif(String cif) {
		return validationsRestApiDao.validateCif(cif);
	}
	
	public boolean validateCnp(String cnp) {
		return validationsRestApiDao.validateCNP(cnp);
	}
	
}
