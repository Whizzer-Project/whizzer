package ro.allevo.fintpui.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.ObjectMapper;

import ro.allevo.fintpui.model.AccountingCode;
import ro.allevo.fintpui.model.Bank;
import ro.allevo.fintpui.model.BudgetCode;
import ro.allevo.fintpui.model.ExternalAccount;
import ro.allevo.fintpui.model.ExternalEntity;
import ro.allevo.fintpui.model.FinTPAccessLists;
import ro.allevo.fintpui.model.FinTPLists;
import ro.allevo.fintpui.model.InternalAccount;
import ro.allevo.fintpui.model.InternalEntity;
import ro.allevo.fintpui.model.LocationCode;

public class Utils {

	private Utils() {
		throw new IllegalStateException("Utils class");
	}

	public static final List<FinTPLists> finTPLists = new LinkedList<>();
	
	static {
		finTPLists.add(new FinTPLists("bank", "banks", Bank.class));
		finTPLists.add(new FinTPLists("internalaccount", "internal-accounts", InternalAccount.class));
		finTPLists.add(new FinTPLists("externalaccount", "external-accounts", ExternalAccount.class));
		finTPLists.add(new FinTPLists("internalentity", "internal-entities", InternalEntity.class));
		finTPLists.add(new FinTPLists("externalentity", "external-entities", ExternalEntity.class));
		finTPLists.add(new FinTPLists("locationcode", "location-codes", LocationCode.class));
		finTPLists.add(new FinTPLists("budgetcode", "budget-codes", BudgetCode.class));
		finTPLists.add(new FinTPLists("accountingcode", "accounting-codes", AccountingCode.class));
	}
	
	public static final List<FinTPAccessLists> finTPAccessLists = new LinkedList<>();
	
	static {
		finTPAccessLists.add(new FinTPAccessLists("bank", "banks", new String[]{"name", "bic", "country", "nbrcode"}));
		finTPAccessLists.add(new FinTPAccessLists("internalaccount","internalaccounts", new String[]{"id", "internalentityname", "currency", "accountnumber", "bankbic", "locked", "defaultaccount", "accountnumberid", "resourceid", "balance"}));
		finTPAccessLists.add(new FinTPAccessLists("externalaccount", "externalaccounts", new String[]{"id", "externalentityname", "currency", "accountnumber", "bankbic", "locked", "defaultaccount"}));
		finTPAccessLists.add(new FinTPAccessLists("internalentity",  "internalentities", new String[]{"id","name", "address", "city", "country", "fiscalcode"}));
		finTPAccessLists.add(new FinTPAccessLists("externalentity", "externalentities", new String[]{"id","name", "address", "city", "country", "fiscalcode"}));
		finTPAccessLists.add(new FinTPAccessLists("locationcode", "locationcodes", new String[]{"id","internalentityname", "code", "\"default\""}));
		finTPAccessLists.add(new FinTPAccessLists("budgetcode", "budgetcodes", new String[]{"id", "name", "code"}));
		finTPAccessLists.add(new FinTPAccessLists("accountingcode",  "accountingcodes", new String[] {"id", "txtype", "code", "name"}));
	}
	
	public static final List<FinTPLists> adpharmaLists = new LinkedList<>();
	
	static {
		adpharmaLists.add(new FinTPLists("bank", "banks", Bank.class));
		adpharmaLists.add(new FinTPLists("internalaccount", "internal-accounts", InternalAccount.class));
		adpharmaLists.add(new FinTPLists("externalaccount", "external-accounts", ExternalAccount.class));
		adpharmaLists.add(new FinTPLists("internalentity", "internal-entities", InternalEntity.class));
		adpharmaLists.add(new FinTPLists("externalentity", "external-entities", ExternalEntity.class));
		adpharmaLists.add(new FinTPLists("locationcode", "location-codes", LocationCode.class));
//		adpharmaLists.add(new FinTPLists("budgetcode", "budget-codes", BudgetCode.class));
		adpharmaLists.add(new FinTPLists("accountingcode", "accounting-codes", AccountingCode.class));
	}


	public static List<HashMap<String, Object>> getResourceByListHashMap(ResponseEntity<String> response) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(response.getBody(),
					mapper.getTypeFactory().constructParametricType(List.class, HashMap.class));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}
	
	public static Map<String, String> getKeyValueFromEntity(JSONArray entities, String key, String value){
		Map<String, String> map = new HashMap<>();
		for (int ind = 0; ind < entities.length(); ind++) {
			JSONObject obj = entities.getJSONObject(ind);
			map.put(obj.getString(key), obj.getString(value));
		}
		return map;
	}
	
	public static Set<String> getValueFromEntity(JSONArray entities, String key){
		Set<String> map = new HashSet<>();
		for (int ind = 0; ind < entities.length(); ind++) {
			JSONObject obj = entities.getJSONObject(ind);
			map.add(obj.getString(key));
		}
		return map;
	}
	/**
	 * 
	 * @param entitiesData
	 * @param selected entity from entitiesData
	 * @param columns
	 * @return
	 */
	public static JSONArray getInternalEntities(JSONObject entitiesData, String entity, String... columns) {
		JSONArray returnList = new JSONArray();
		
		JSONArray internalEntities = entitiesData.getJSONArray(entity);
		for (int ind = 0; ind < internalEntities.length(); ind++) {
			JSONObject object = internalEntities.getJSONObject(ind);
			JSONObject objEntity = new JSONObject();
			for (String column : columns) {
				objEntity.put(column, object.getString(column));
			}
			returnList.put(objEntity);
		}
		return returnList;
	}
	
	public static JSONArray getInternalEntities(JSONObject entitiesData, String entity) {
		return entitiesData.getJSONArray(entity);
	}
	
	public static List<Map<String, String>> getMapInternalEntities(JSONArray  entitiesData, String... columns) {
		List<Map<String, String>> returnList = new ArrayList<>();
		
		for (int ind = 0; ind < entitiesData.length(); ind++) {
			JSONObject object = entitiesData.getJSONObject(ind);
			Map<String, String> objEntity = new HashMap<>();
			for (String column : columns) {
				objEntity.put(column, object.getString(column));
			}
			returnList.add(objEntity);
		}
		return returnList;
	}
	
	public static JSONArray filteredInternalEntities(JSONArray internalEntities, Set<String> entities){
		JSONArray filteredInternalEntities = new JSONArray();
		for (String entity : entities) {
			for (int ind= 0; ind < internalEntities.length(); ind++) {
				JSONObject object = internalEntities.getJSONObject(ind);
				if (object.getString("id").equalsIgnoreCase(entity)) {
					filteredInternalEntities.put(object);
				}
			}
		}
		return filteredInternalEntities;
	}

	public static JSONArray filteredInternalEntities(JSONArray internalEntity, String column, String bic) {
		JSONArray filteredInternalEntities = new JSONArray();
		for (int ind= 0; ind < internalEntity.length(); ind++) {
			JSONObject object = internalEntity.getJSONObject(ind);
			if (object.getString(column).equalsIgnoreCase(bic)) {
				filteredInternalEntities.put(object);
			}
		}
		return filteredInternalEntities;
	}
	
}
