/*
* FinTP - Financial Transactions Processing Application
* Copyright (C) 2013 Business Information Systems (Allevo) S.R.L.
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>
* or contact Allevo at : 031281 Bucuresti, 23C Calea Vitan, Romania,
* phone +40212554577, office@allevo.ro <mailto:office@allevo.ro>, www.allevo.ro.
*/

package ro.allevo.fintpws.resources;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Repository;


/**
 * Resource class implementing /api entry point and acting as sub-resource
 * locator for all main resources
 * 
 * @author horia
 * @version $Revision: 1.0 $
 */

@Path("/api")
@Singleton
@Repository
@ComponentScan
//@EnableTransactionManagement
public class ApiResource {
	
	/**
	 * Field logger.
	 */
	/*
	 * private static Logger logger = LogManager.getLogger(ApiResource.class
	 * .getName());
	 */

	/**
	 * Field uriInfo.
	 */
	@Context
	private UriInfo uriInfo;
	
	@Value("${project.type}")
	private String projectType;
	
	@PersistenceContext(unitName="fintpCFG")
	public EntityManager emc;
	
	@PersistenceContext(unitName="fintpDATA")
	public EntityManager emd;
	
	@PersistenceContext(unitName="fintpLIST")
	public EntityManager eml;
	
	@PersistenceContext(unitName="fintpBO")
	public EntityManager emb;
	
	/** Creates a new instance of ApiResource 
	 * @return */
	public ApiResource(){
//		logger.debug("Entering ApiResource.()");
		
		//configEntityManagerFactory = new CustomUserDetailsService().emc.getEntityManagerFactory();
//		listEntityManagerFactory = Persistence
//				.createEntityManagerFactory(PERSISTENCE_UNITNAME_LIST);
	//	dataEntityManagerFactory = new CustomUserDetailsService().emd.getEntityManagerFactory();
		
		
//		logger.debug("Exiting ApiResource.()");
	}
	
	/**
	 * Sub-resource locator for /queues resource
	 * 
	 * @return QueuesResource
	 */
	@Path("queues")
	public QueuesResource getQueues() {
		
		// 1.Tomcat: No @PersistenceContext injection of a container managed
		// persistence unit is available - use
		// Persistence.createEntityManagerFactory(JTA_PU_NAME)
		// 2.All EntityManager instances injected using the @PersistenceContext
		// annotation are container managed. This means that the container takes
		// care of the mundane task of looking up, opening, and closing the
		// EntityManager behind the scenes

		/*EntityManager emc = configEntityManagerFactory.createEntityManager();
		EntityManager emd = dataEntityManagerFactory.createEntityManager();*/
		
		return new QueuesResource(uriInfo, emc);
	}

	/**
	 * Sub-resource locator for /queues/{name} resource
	 * 
	 * @return QueueResource
	 */
	/*@Path("queues/{name}")
	public QueueResource getQueue(@PathParam("name") String name) {
		// 1.Tomcat: No @PersistenceContext injection of a container managed
		// persistence unit is available - use
		// Persistence.createEntityManagerFactory(JTA_PU_NAME)
		// 2.All EntityManager instances injected using the @PersistenceContext
		// annotation are container managed. This means that the container takes
		// care of the mundane task of looking up, opening, and closing the
		// EntityManager behind the scenes

		EntityManager emc = configEntityManagerFactory.createEntityManager();
		EntityManager emd = dataEntityManagerFactory.createEntityManager();
		
		
		
		return new QueueResource(uriInfo, emc, emd, name);
	}*/

	/**
	 * Sub-resource locator for /routingrules resource
	 * 
	 * @return RoutingRulesResource
	 */
	@Path("routing-rules")
	public RoutingRulesResource getRoutingRules() {
		// logger.info("Requested " + uriInfo.getRequestUri());
		return new RoutingRulesResource(uriInfo, emc);
	}

	/**
	 * 
	 * Sub-resource locator for /timelimits resource
	 * 
	 * @return TimeLimitsResource
	 */
	@Path("time-limits")
	public TimeLimitsResource getTimeLimits() {
		// logger.info("Requested " + uriInfo.getRequestUri());
		return new TimeLimitsResource(uriInfo, emc);
	}
	
	/**
	 * 
	 * Sub-resource locator for /users resource
	 * 
	 * @return TimeLimitsResource
	 */
	@Path("users")
	public UsersResource getUsers() {
		// logger.info("Requested " + uriInfo.getRequestUri());
		return new UsersResource(uriInfo, emc);
	}

	/**
	 * Sub-resource locator for /alerts resource
	 * 
	 * @return alerts
	 */
	@Path("alerts")
	public AlertsResource getAlerts() {
		return new AlertsResource(uriInfo, emc);
	}

	/**
	 * Sub-resource locator for /messages resource
	 * 
	 * @return MessagesResource
	 */
	@Path("messages")
	public MessagesResource getMessages() {
		// logger.info("Requested " + uriInfo.getRequestUri());

		return new MessagesResource(uriInfo, emd);
	}

	/**
	 * Sub-resource locator for /events resource
	 * 
	 * @return EventsResource
	 */
	@Path("events")
	public EventsResource getEvents() {
		return new EventsResource(uriInfo, emd);
	}
	
	/**
	 * Sub-resource locator for /queuetypes resource
	 * 
	 * @return QueueTypesResource
	 */
	@Path("queue-types")
	public QueueTypesResource getQueueTypes(){
		return new QueueTypesResource(uriInfo, emc);
	}
	
	
	
	/**
	 * Sub-resource locator for /routingschemas resource
	 * 
	 * @return RoutingSchemasResource
	 */
	@Path("routing-schemas")
	public RoutingSchemasResource getRoutingSchemas(){
		return new RoutingSchemasResource(uriInfo, emc);
	}
	
	/**
	 * Sub-resource locator for /servicemaps resource
	 * 
	 * @return ServiceMapsResource
	 */
	@Path("service-maps")
	public ServiceMapsResource getServiceMaps(){
		return new ServiceMapsResource(uriInfo, emc);
	}
	
	/**
	 * Sub-resource locator for /serviceperformances resource
	 * 
	 * @return ServicePerformancesResource
	 */
	@Path("serviceperformances")
	public ServicePerformancesResource getServicePerformances(){
		return new ServicePerformancesResource(uriInfo, emd);
	}
	
	/**
	 * Sub-resource locator for /histories resource
	 * 
	 * @return HistoriesResource
	 */
	@Path("histories")
	public HistoriesResource getHistories(){
		return new HistoriesResource(uriInfo, emd);
	}
	
	/**
	 * Sub-resource locator for /routingjobs resource
	 * 
	 * @return RoutingJobsResource
	 */
	@Path("routingjobs")
	public RoutingJobsResource getRoutingJobs(){
		return new RoutingJobsResource(uriInfo, emd);
	}
	
	/**
	 * Sub-resource locator for /roles resource
	 * 
	 * @return RoutingJobsResource
	 */
	@Path("roles")
	public RolesResource getRoles(){
		return new RolesResource(uriInfo, emc);
	}
	
	
	/**
	 * Sub-resource locator for /batches resource
	 * 
	 * @return BatchesResource
	 */
	@Path("batches")
	public BatchesResource getBatches(){
		return new BatchesResource(uriInfo, emd);
	}
	
	/**
	 * Sub-resource locator for /batchrequests resource
	 * 
	 * @return BatchRequestResource
	 */
	/*
	@Path("batchrequests")
	public BatchRequestsResource getBatchRequests(){
		return BatchRequestsResource.create(uriInfo, emd);
	}*/
	
	@Path("banks")
	public BanksResource getBanks() {
		return new BanksResource(uriInfo, eml);
	}
	
	@Path("internal-entities")
	public InternalEntitiesResource getInternalEntities(@QueryParam("isAdmin") Boolean isAdmin) {
		if (Boolean.TRUE.equals(isAdmin)) {
			return new InternalEntitiesResource(uriInfo, eml); 
		}
		return new InternalEntitiesResource(uriInfo, eml, projectType);
	}
	
	@Path("internal-accounts")
	public InternalAccountsResource getInternalAccounts() {
		return new InternalAccountsResource(uriInfo, eml);
	}
	
	@Path("external-entities")
	public ExternalEntitiesResource getExternalEntities() {
		return new ExternalEntitiesResource(uriInfo, eml);
	}
	
	@Path("external-accounts")
	public ExternalAccountsResource getExternalAccounts() {
		return new ExternalAccountsResource(uriInfo, eml);
	}
	
	@Path("location-codes")
	public LocationCodesResource getLocationCodes() {
		return new LocationCodesResource(uriInfo, eml);
	}
	
	@Path("accounting-codes")
	public AccountingCodesResource getAccountingCodes() {
		return new AccountingCodesResource(uriInfo, eml);
	}
	
	@Path("budget-codes")
	public BudgetCodesResource getBudgetCodes() {
		return new BudgetCodesResource(uriInfo, eml);
	}

	@Path("message-types")
	public MessageTypesResource getMessageTypes() {
		return new MessageTypesResource(uriInfo, emc);
	}
	
	@Path("business-areas")
	public BusinessAreasResource getBusinessAreas() {
		return new BusinessAreasResource(uriInfo, emc);
	}
	
	@Path("repreconInvSuppVsStatement")
	public RepreconInvSuppVsStatementResource getRepreconInvSuppVsStatementResource() {
		return new RepreconInvSuppVsStatementResource(uriInfo, emd);
	}
	
	@Path("repreconInvIssVsStatement")
	public RepreconInvIssVsStatementResource getRepreconInvIssVsStatementResource() {
		return new RepreconInvIssVsStatementResource(uriInfo, emd);
	}
	
	@Path("repreconPaymentVsStatement")
	public RepreconPaymentVsStatementResource getRepreconPaymentVsStatementResource() {
		return new RepreconPaymentVsStatementResource(uriInfo, emd);
	}

	@Path("templates-config")
	public TemplatesConfigsResource getTemplateConfigs() {
		return new TemplatesConfigsResource(uriInfo, emc);
	}
	
	@Path("templates-options")
	public TemplatesConfigOptionsResource getTemplatesOptions() {
		return new TemplatesConfigOptionsResource(uriInfo, emc);
	}

	@Path("templates")
	public TemplatesResource getTemplates() {
		return new TemplatesResource(uriInfo, emc);
	}
	
	@Path("templates-payment")
	public PaymentResource getPayments() {
		return new PaymentResource(uriInfo, emb);
	}
	
	@Path("validations")
	public ValidationsResource getValidation() {
		return new ValidationsResource(uriInfo, emc);
	}	
	
//	@Path("report-filters")
//	public ReportFiltersResource getReportFilter(@QueryParam("businessArea") String businessArea, 
//			@QueryParam("templateName") String templateName, 
//			@QueryParam("userId") Integer userId) {
//		if (null == templateName) {
//			return new ReportFiltersResource(uriInfo, emc, businessArea, userId);
//		}else {
//		return new ReportFiltersResource(uriInfo, emc, businessArea, templateName, userId);
//		}
//	}
	
	@Path("report-filters")
	public ReportFiltersResource getReportFilter(@QueryParam("businessArea") String businessArea, 
			@QueryParam("templateName") String templateName, @QueryParam("userId") Integer userId) {
		if (null == templateName) {
			return new ReportFiltersResource(uriInfo, emc, businessArea, userId);
		}else {
			return new ReportFiltersResource(uriInfo, emc, businessArea, templateName, userId);
		}
	}
	
	@Path("enrich")
	public EnrichRulesResource getEnrichs() {
		return new EnrichRulesResource(uriInfo, emc); 
	}
	
	@Path("params")
	public ParamsResource getParams() {
		return new ParamsResource(uriInfo, emc);
	}
	
	@Path("message-edit")
	public MessagesEditResource getEditRules() {
		return new MessagesEditResource(uriInfo, emc);
	}
	
	@Path("enrichment-with-validation")
	public EnrichmentWithValidationResource getEnrichment() {
		return new EnrichmentWithValidationResource(uriInfo, emc, projectType);
	}
	
	@Path("calendar")
	public CalendarsResource getCalendarResource() {
		return new CalendarsResource(uriInfo, emc);
	}
	
	
	/**
	 * Returns a JSONObject prefilled with the required metadata.
	 * 
	 * @param resourceClass
	 *            Class<?>
	 * @param path
	 *            String
	 * @return JSONObject * @throws JSONException
	 */
	@Deprecated
	public static JSONObject getMetaResource(String path, Class<?> resourceClass)
			throws JSONException {
		// fill in required metadata
		return new JSONObject().put("href", path).put("_type",
				resourceClass.getName());
	}
}
