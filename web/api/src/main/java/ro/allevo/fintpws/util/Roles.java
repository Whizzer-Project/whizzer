package ro.allevo.fintpws.util;

public final class Roles {
	
	private Roles(){
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
	public static final String TRIGGERS_LIST_VIEW = PREFIX_ROLE + "Triggers List_view"; 
	public static final String TRIGGERS_LIST_MODIFY = PREFIX_ROLE + "Triggers List_modify"; 
//	public static final String API_INTERFACE_VIEW = PREFIX_ROLE + "API Interface_view";
	public static final String API_INTERFACE_MODIFY = PREFIX_ROLE + "API Interface_modify";
	public static final String AUTOMATED_TESTING_VIEW = PREFIX_ROLE + "Automated testing_view";
	public static final String SUPERVISER = PREFIX_ROLE + "Supervise_modify";
	public static final String BALANCE_SHEET_VIEW = PREFIX_ROLE + "Static Balance Sheet Reports_view"; 
	public static final String BALANCE_SHEET_MODIFY = PREFIX_ROLE + "Static Balance Sheet Reports_modify"; 
	public static final String KPI_DYNAMIC_REPORTS_VIEW = PREFIX_ROLE + "KPI &  dynamic Reports_view"; 
	public static final String PAYMENT_TEMPLATE_CONFIGURATION_VIEW = PREFIX_ROLE + "Transactions templates configuration_view";
	public static final String PAYMENT_TEMPLATE_CONFIGURATION_MODIFY = PREFIX_ROLE + "Transactions templates configuration_modify";
	
	
}
