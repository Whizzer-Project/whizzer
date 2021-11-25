package ro.allevo.fintpws.resources;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.UriInfo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ro.allevo.fintpws.model.BudgetCodesEntity;
import ro.allevo.fintpws.model.CalendarEntity;
import ro.allevo.fintpws.util.Roles;

public class CalendarsResource extends PagedCollection<CalendarEntity> {


	private static final long serialVersionUID = 1L;
	
	public CalendarsResource(UriInfo uriInfo, EntityManager entityManagerList) {
		super(uriInfo, 
				entityManagerList.createNamedQuery("CalendarEntity.findAll", CalendarEntity.class),
				entityManagerList.createNamedQuery("CalendarEntity.findTotal", Long.class),
				entityManagerList,
				CalendarEntity.class
				);
		}
	
	@Path("{calendarid}")
	public CalendarResource getCalendarResource(@PathParam("codid") long calendarResourceId) {
		return new CalendarResource(getUriInfo(), getEntityManager(), calendarResourceId);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.CONFIGURATION_LISTS_VIEW})
	@JsonIgnore
	public PagedCollection<CalendarEntity> getCalendarEntityAsJson() {
		return this;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.CONFIGURATION_LISTS_MODIFY})
	public Response postForm(CalendarEntity[] calendarEntity) {
		return post(Arrays.asList(calendarEntity));
	}
	
	@DELETE
	@RolesAllowed({Roles.CONFIGURATION_LISTS_MODIFY})
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteForm(List<Date> calendarEntities) {
		return delete(calendarEntities);
	}
	
}
