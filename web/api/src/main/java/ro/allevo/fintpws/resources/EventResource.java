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

/**
 * 
 */
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

import ro.allevo.fintpws.model.EventEntity;
import ro.allevo.fintpws.util.Roles;


public class EventResource extends BaseResource<EventEntity> {

	public EventResource(UriInfo uriInfo, EntityManager entityManagerList, String bic) {
		super(EventEntity.class, uriInfo, entityManagerList, bic);	
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.EVENTS_VIEW})
	public EventEntity getEvent() {
		return get();
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.EVENTS_MODIFY})
	public Response updateEvent(EventEntity entity) {
		return put(entity);
	}

	@DELETE
	@RolesAllowed({Roles.EVENTS_MODIFY})
	public Response deleteEvent() {
		return delete();
	}
	
}
