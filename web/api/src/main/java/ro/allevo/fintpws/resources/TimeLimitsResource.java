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
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ro.allevo.fintpws.model.RoutingSchemaEntity;
import ro.allevo.fintpws.model.TimeLimitEntity;
import ro.allevo.fintpws.util.Roles;

/**
 * @author remus
 * @version $Revision: 1.0 $
 */
public class TimeLimitsResource extends PagedCollection<TimeLimitEntity> {

	public TimeLimitsResource(UriInfo uriInfo, EntityManager entityManagerConfig) {
		
		super(uriInfo,
				entityManagerConfig.createNamedQuery("TimeLimitEntity.findAll", TimeLimitEntity.class),
				entityManagerConfig.createNamedQuery("TimeLimitEntity.findTotal", Long.class),
				entityManagerConfig,
				TimeLimitEntity.class
				);
	}
	
	public TimeLimitsResource(UriInfo uriInfo,
			EntityManager entityManagerConfig, RoutingSchemaEntity routingSchemaEntity) {
		
		super(uriInfo,
				entityManagerConfig.createNamedQuery("TimeLimitEntity.findAllRoutingSchema", TimeLimitEntity.class)
					.setParameter("startlimit", routingSchemaEntity.getStartLimitEntity().getId())
					.setParameter("stoplimit", routingSchemaEntity.getStopLimitEntity().getId()),
				entityManagerConfig.createNamedQuery("TimeLimitEntity.findTotalRoutingSchema", Long.class)
					.setParameter("startlimit", routingSchemaEntity.getStartLimitEntity().getId())
					.setParameter("stoplimit", routingSchemaEntity.getStopLimitEntity().getId()),
				entityManagerConfig,
				TimeLimitEntity.class
				);
	}

	/**
	 * Returns a time limit sub-resource with guid
	 * 
	 * @param guid
	 *            String guid of the time limit as last element in the path
	 * 
	 * @return TimeLimitResource The time limit sub-resource
	 */
	@Path("{timeLimitId}")
	public TimeLimitResource getTimeLimitsResource(@PathParam("timeLimitId") long id) {
		//name = Jsoup.clean(name, Whitelist.none());
		return new TimeLimitResource(getUriInfo(), getEntityManager(), id);
	}

	/**
	 * GET method : returns an application/json formatted list of time limits
	 * 
	 * @return JSONObject The list of time limits
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.ROUTINGRULES_VIEW})
	@JsonIgnore
	public PagedCollection<TimeLimitEntity> getTimeLimitsAsJson() {
		return this;
	}

	/**
	 * POST method : creates a time limit
	 * 
	 * @param jsonEntity
	 *            JSONObject
	 * @return Response
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.ROUTINGRULES_MODIFY})
	public Response postTimelimit(TimeLimitEntity timeLimitEntity) {
		return post(timeLimitEntity);
	}

}
