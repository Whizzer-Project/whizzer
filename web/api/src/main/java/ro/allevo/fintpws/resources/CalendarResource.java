package ro.allevo.fintpws.resources;


import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import ro.allevo.fintpws.model.CalendarEntity;
import ro.allevo.fintpws.util.Roles;



public class CalendarResource  extends BaseResource<CalendarEntity> {
	
	
	public CalendarResource(UriInfo uriInfo, EntityManager entityManagerList, long calendarResourceId) {
		super(CalendarEntity.class, uriInfo, entityManagerList, calendarResourceId);	
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.CONFIGURATION_LISTS_VIEW})
	public CalendarEntity getCalendar() {
		return get();
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.CONFIGURATION_LISTS_MODIFY})
	public Response updateCalendar(CalendarEntity entity) {
		return put(entity);
	}

	@DELETE
	@RolesAllowed({Roles.CONFIGURATION_LISTS_MODIFY})
	public Response deleteBCalendar() {
		return delete();
	}

}
