package ro.allevo.fintpuiws.resources;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.springframework.http.HttpStatus;

import ro.allevo.fintpuiws.model.FiltersEntity;
import ro.allevo.fintpuiws.model.MessageCriterionEntity;
import ro.allevo.fintpuiws.model.MessageResultEntity;
import ro.allevo.fintpuiws.model.TransactionStateEntity;
import ro.allevo.fintpws.model.RoleEntity;
import ro.allevo.fintpws.model.UserRoleEntity;
import ro.allevo.fintpws.resources.UserRolesResource;
import ro.allevo.fintpws.util.Roles;

public class ReportsResource {

	private final EntityManager entityManager;
	private HttpServletRequest request;
	private UriInfo uriInfo; 

	public ReportsResource(EntityManager entityManager, @Context HttpServletRequest request, UriInfo uriInfo) {
		this.entityManager = entityManager;
		this.request = request;
		this.uriInfo = uriInfo;
	}

	@GET
	@Path("transaction-states")
	@Produces(MediaType.APPLICATION_JSON)
	public List<TransactionStateEntity> getTransactionStates() {
		TypedQuery<TransactionStateEntity> query = entityManager.createNamedQuery("TransactionStateEntity.findAll",
				TransactionStateEntity.class);

		return query.getResultList();
	}

	@GET
	@Path("message-criteria")
	@Produces(MediaType.APPLICATION_JSON)
	public List<MessageCriterionEntity> getFilters(@QueryParam("businessArea") String businessArea) 
			throws IOException{
		TypedQuery<MessageCriterionEntity> query = entityManager
				.createNamedQuery("MessageCriterionEntity.findByBusinessArea", MessageCriterionEntity.class)
				.setParameter("businessarea", businessArea);

		List<MessageCriterionEntity> criterias = query.getResultList();

		for (MessageCriterionEntity criterion : criterias)
			criterion.fetchDatasource(entityManager);

		return criterias;
	}

	private Long getEventId(List<RoleEntity> roles, String roleName) {
		for (RoleEntity role : roles) {
			if (role.getName().equalsIgnoreCase(roleName.split("_")[1])) {
				return role.getId();
			}
		}
		return -1L;
	}
	
	private boolean hasEventRule(List<UserRoleEntity> userRoleEntities, Long eventId) {
		for (UserRoleEntity userRoleEntity : userRoleEntities) {
			if (userRoleEntity.getRoleId() == eventId) {
				return true;
			}
		}
		return false;
	}
	
	@GET
	@Path("message-results")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getResultHeaders(@QueryParam("businessArea") String businessArea) {
		Integer userId = (Integer) request.getSession().getAttribute("user_id");

		TypedQuery<MessageResultEntity> query = null;
		if (null == businessArea) {
			query = entityManager
					.createNamedQuery("MessageResultEntity.findAll", MessageResultEntity.class);
			return Response.status(HttpStatus.OK.value()).entity(query.getResultList()).build();
		}
		switch (businessArea) {
		case "Events":
			return getEventsResponse(businessArea, userId, Roles.EVENTS_VIEW);
		case "Payments":
		case "Statements":
		case "Invoices":
			return getPayStateInvResponse(businessArea, userId);
		case "Outstanding":
			return getEventsResponse(businessArea, userId, Roles.SUPERVISER);
		default:
			return getForbidden();
		}
	}
	
	private Response getPayStateInvResponse(String businessArea, Integer userId) {
		UserRolesResource userRolesResource = new UserRolesResource(null, entityManager, userId);
		if (null != userRolesResource.getUserRoles()) {
			List<UserRoleEntity> userRoleEntities = userRolesResource.getUserRoles().getItems();
			if (userRoleEntities.stream().parallel().filter(role -> role.getRoleId() > 100).findAny().isPresent()) {
				TypedQuery<MessageResultEntity> query = entityManager
						.createNamedQuery("MessageResultEntity.findByBusinessArea", MessageResultEntity.class)
						.setParameter("businessarea", businessArea);

				return Response.status(HttpStatus.OK.value()).entity(query.getResultList()).build();
			}
		}
		return getForbidden();
		
	}
	
	private Response getForbidden() {
		return Response.status(HttpStatus.FORBIDDEN.value()).entity("<body><p>You do not have sufficient permissions to access this page!</p></body>").build();
	}
	
	private List<RoleEntity> getRoleEntities(){
		TypedQuery<RoleEntity> queryRole= entityManager.createNamedQuery("RoleEntity.findAll", RoleEntity.class);
		return queryRole.getResultList();
	}
	
	private Response getEventsResponse(String businessArea, Integer userId, String roleName) {
		Long eventId = getEventId(getRoleEntities(), roleName);
		UserRolesResource userRolesResource = new UserRolesResource(null, entityManager, userId);
		if (null != userRolesResource.getUserRoles()) {
			List<UserRoleEntity> userRoleEntities = userRolesResource.getUserRoles().getItems();
			if (!hasEventRule(userRoleEntities, eventId)) {
				return getForbidden();
			}
		}
		TypedQuery<MessageResultEntity> query = entityManager
				.createNamedQuery("MessageResultEntity.findByBusinessArea", MessageResultEntity.class)
				.setParameter("businessarea", businessArea);

		return Response.status(HttpStatus.OK.value()).entity(query.getResultList()).build();
	}

	@Path("report/{report}")
	public ReportsByNameReport getMessagesByNameReport(@PathParam("report") String report) {
			return new ReportsByNameReport(uriInfo, entityManager, report);
	}
	
	@GET
	@Path("message-statement")
	@Produces(MediaType.APPLICATION_JSON)
	public Long getCountStatement() {
		TypedQuery<Long> query = entityManager.createNamedQuery("MsgStatementEntity.findTotalDistinctMessageStatement",
				Long.class);
		return query.getResultList().get(0);
	}
	
	@GET
	@Path("filters")
	@Produces(MediaType.APPLICATION_JSON)
	public List<FiltersEntity> getFilterFileds(@QueryParam("businessArea") String businessArea, @QueryParam("user") String user){
		TypedQuery<FiltersEntity> query = entityManager.createNamedQuery("FiltersEntity.findByBusinessAreaAndUserName", FiltersEntity.class)
				.setParameter("businessarea", businessArea)
				.setParameter("user", user);
		return query.getResultList();
	}
}
