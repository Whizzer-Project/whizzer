package ro.allevo.fintpui.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import ro.allevo.fintpui.model.EnrichRules;
import ro.allevo.fintpui.model.EnrichRules;

@Service
public interface EnrichDao {

	Map<String, List<String>> getListsMetadata();
	
	public EnrichRules[] getAllEnrichRules();
	public EnrichRules getEnrichRules(String id);
	public void insertEnrichRules(EnrichRules enrichRules);
	public void updateEnrichRules(EnrichRules enrichRules, String id);
	public void deleteEnrichRules(String id);

}
