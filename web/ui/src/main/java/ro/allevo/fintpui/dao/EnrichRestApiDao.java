package ro.allevo.fintpui.dao;

import java.lang.reflect.Field;
import java.net.URI;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.UriBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.model.AccountingCode;
import ro.allevo.fintpui.model.Bank;
import ro.allevo.fintpui.model.BudgetCode;
import ro.allevo.fintpui.model.EnrichRules;
import ro.allevo.fintpui.model.ExternalAccount;
import ro.allevo.fintpui.model.ExternalEntity;
import ro.allevo.fintpui.model.FinTPLists;
import ro.allevo.fintpui.model.InternalAccount;
import ro.allevo.fintpui.model.InternalEntity;
import ro.allevo.fintpui.model.LocationCode;
import ro.allevo.fintpui.utils.Utils;

@Service
public class EnrichRestApiDao extends RestApiDao<EnrichRules> implements EnrichDao{
	
	public EnrichRestApiDao() {
		super(EnrichRules.class);
	}

	@Override
	public Map<String, List<String>> getListsMetadata() {
		
		Map<String, List<String>> listsMetadata = new HashMap<>();
		List<FinTPLists> finTPLists = config.getProjectType().equalsIgnoreCase("adpharma")?Utils.adpharmaLists:Utils.finTPLists;
		for(FinTPLists finLists:finTPLists) {
			Class<?> c = finLists.getClassName();
			listsMetadata.put(c.getSimpleName(), getFields(c));
		}
		return listsMetadata;
	}
	
	private List<String> getFields(Class<?> c){
		Field[] fields = c.getDeclaredFields();
		List<String> fieldNames = new LinkedList<String>();
		for(Field field:fields) {
			fieldNames.add(field.getName());
		}
		return fieldNames;
	}
	
	@Autowired
	Config config;
	
	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getAPIUrl()).path("enrich").build();
	}

	@Override
	public EnrichRules[] getAllEnrichRules() {
		return getAll();
	}

	@Override
	public EnrichRules getEnrichRules(String id) {
		return get(id);
	}

	@Override
	public void insertEnrichRules(EnrichRules enrichRules) {
		post(enrichRules);
	}

	@Override
	public void updateEnrichRules(EnrichRules enrichRules, String id) {
		put(id, enrichRules);
	}

	@Override
	public void deleteEnrichRules(String id) {
		delete("" + id);
	}
	
}
