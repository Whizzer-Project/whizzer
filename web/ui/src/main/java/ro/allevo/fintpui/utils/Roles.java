package ro.allevo.fintpui.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import ro.allevo.fintpui.model.UserRole;

public final class Roles {
	
	private Roles() {
		throw new IllegalStateException("Roles class");
	}
		
	public static final String PREFIX_ROLE = "ROLE_";
	
	public static final String BANKS_LIST_VIEW = PREFIX_ROLE + "Banks List_view"; 
	public static final String BANKS_LIST_MODIFY = PREFIX_ROLE + "Banks List_modify"; 
	public static final String CONFIGURATION_LISTS_VIEW = PREFIX_ROLE + "Configuration Lists_view"; 
	public static final String CONFIGURATION_LISTS_MODIFY = PREFIX_ROLE + "Configuration Lists_modify"; 
	public static final String EVENTS_VIEW = PREFIX_ROLE + "Events_view"; 
	public static final String EVENTS_MODIFY = PREFIX_ROLE + "Events_modify"; 
	public static final String EXTERNAL_ENTITIES_LIST_VIEW = PREFIX_ROLE + "External Entities List_view"; 
	public static final String EXTERNAL_ENTITIES_LIST_MODIFY = PREFIX_ROLE + "External Entities List_modify"; 
	public static final String INTERNAL_ENTITIES_LIST_VIEW = PREFIX_ROLE + "Internal Entities List_view";
	public static final String INTERNAL_ENTITIES_LIST_MODIFY = PREFIX_ROLE + "Internal Entities List_modify"; 
	public static final String QUEUES_VIEW = PREFIX_ROLE + "Queues_view"; 
	public static final String QUEUES_MODIFY = PREFIX_ROLE + "Queues_modify"; 
	public static final String RECONCILIATION_VIEW = PREFIX_ROLE + "Reconciliation_view"; 
	public static final String RECONCILIATION_MODIFY = PREFIX_ROLE + "Reconciliation_modify"; 
	public static final String ROUTINGRULES_VIEW = PREFIX_ROLE + "Routing Rules_view";
	public static final String ROUTINGRULES_MODIFY = PREFIX_ROLE + "Routing Rules_modify";
	public static final String USERS_VIEW = PREFIX_ROLE + "Users_view";
	public static final String USERS_MODIFY = PREFIX_ROLE + "Users_modify";
	public static final String TRACKER_VIEW = PREFIX_ROLE + "Tracking Reports_view";
	public static final String TRIGGERS_LIST_VIEW = PREFIX_ROLE + "TRIGGERS List_view";
	public static final String TRIGGERS_LIST_MODIFY = PREFIX_ROLE + "TRIGGERS List_modify";
	public static final String AUTOMATED_TESTING_VIEW = PREFIX_ROLE + "Automated testing_view";
	public static final String SUPERVISER = PREFIX_ROLE + "Supervise_modify";
	public static final String API_INTERFACE_MODIFY = PREFIX_ROLE + "API Interface_modify";
//	public static final String API_INTERFACE_MODIFY = PREFIX_ROLE + "API Interface_modify";
	public static final String BALANCE_SHEET_VIEW = PREFIX_ROLE + "Static Balance Sheet Reports_view"; 
	public static final String BALANCE_SHEET_MODIFY = PREFIX_ROLE + "Static Balance Sheet Reports_modify"; 
	public static final String KPI_DYNAMIC_REPORTS_VIEW = PREFIX_ROLE + "KPI &  dynamic Reports_view"; 
	public static final String PAYMENT_TEMPLATE_CONFIGURATION_VIEW = PREFIX_ROLE + "Transactions templates configuration_view";
	public static final String PAYMENT_TEMPLATE_CONFIGURATION_MODIFY = PREFIX_ROLE + "Transactions templates configuration_modify";
	
	
	public static boolean hasRoles(OAuth2Authentication auth, String... roles) {
//		if (auth.getAuthorities().contains(new SimpleGrantedAuthority(SUPERVISER)))
//			return true;
		for(String role : roles) {
			if (auth.getAuthorities().contains(new SimpleGrantedAuthority(role))) {
				return true;
			}
		}
		return false;
	}
	
	public static Set<String> getMessageTypeByRoles(UserRole[] userRoles) {
		return Arrays.stream(userRoles).map(userRole -> userRole.getRoleEntity().getMessageTypes())
				.flatMap(Collection::stream).collect(Collectors.toSet());
	}

	public static Set<String> getEntityByRoles(UserRole[] userRoles) {
		return Arrays.stream(userRoles)
				.filter(userRole -> 1 == userRole.getRoleEntity().getUserDefined())
				.map(userRole -> userRole.getRoleEntity().getInternalEntities())
				.flatMap(Collection::stream).collect(Collectors.toSet());
	}
	
	public static JSONArray getInternalEntityFromRules(JSONArray internalAccounts, List<String> fromListEntities) throws JSONException {
		JSONArray returnJSON = new JSONArray();
		for(int ind = 0; ind < internalAccounts.length(); ind++) {
			JSONObject internalAccount = internalAccounts.getJSONObject(ind);
			for (String entity : fromListEntities) {
				if (internalAccount.getString("name").equalsIgnoreCase(entity)) {
					returnJSON.put(internalAccount);
					break;
				}
			}
		}
		return returnJSON;
	}
}
