package ro.allevo.fintpui.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.dao.EnrichRestApiDao;
import ro.allevo.fintpui.model.Bank;
import ro.allevo.fintpui.model.EnrichRules;
import ro.allevo.fintpui.utils.PagedCollection;

import java.util.*;

@Service
public class EnrichService {
	
	@Autowired
	private EnrichRestApiDao enrichDao;
	
	public Map<String, List<String>> getListsMetadata() {
		return enrichDao.getListsMetadata();
	}
	
	public EnrichRules[] getAllEnrichRules() {
		return enrichDao.getAllEnrichRules();
	}
	
	public PagedCollection<EnrichRules> getPage() {
		return enrichDao.getPage();
	}
	
	public PagedCollection<EnrichRules> getPage(LinkedHashMap<String, List<String>> param){
		return enrichDao.getPage(param);
	}

	public EnrichRules getEnrich(String id) {
		return enrichDao.getEnrichRules(id);
	}

	public void insertRules(EnrichRules enrichRules) {
		enrichDao.insertEnrichRules(enrichRules);
	}

	public void updateEnrichRules(EnrichRules enrichRules, String id) {
		enrichDao.updateEnrichRules(enrichRules, id);
	}

	public void deleteEnrichRules(String id) {
		enrichDao.deleteEnrichRules(id);
	}
}
