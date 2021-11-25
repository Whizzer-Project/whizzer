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
 * @author remus
 * @version $Revision: 1.0 $
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

import ro.allevo.fintpws.model.QueueEntity;
import ro.allevo.fintpws.model.RoutingRuleEntity;
import ro.allevo.fintpws.model.RoutingSchemaEntity;
import ro.allevo.fintpws.util.Roles;

/**
 * @author remus
 * @version $Revision: 1.0 $
 */
public class RoutingRulesResource extends PagedCollection<RoutingRuleEntity> {

	public RoutingRulesResource(UriInfo uriInfo, EntityManager entityManagerConfig) {		
		super(uriInfo,
				entityManagerConfig.createNamedQuery("RoutingRuleEntity.findAll", RoutingRuleEntity.class),
				entityManagerConfig.createNamedQuery("RoutingRuleEntity.findTotal", Long.class),
				entityManagerConfig,
				RoutingRuleEntity.class
				);
	}
	
	public RoutingRulesResource(UriInfo uriInfo, EntityManager entityManagerConfig, RoutingSchemaEntity routingSchemaEntity) {		
		super(uriInfo,
				entityManagerConfig.createNamedQuery("RoutingRuleEntity.findAllRoutingSchema", RoutingRuleEntity.class)
					.setParameter("schemaid", routingSchemaEntity.getId()),
				entityManagerConfig.createNamedQuery("RoutingRuleEntity.findTotalRoutingSchema", Long.class)
					.setParameter("schemaid", routingSchemaEntity.getId()),
				entityManagerConfig,
				RoutingRuleEntity.class
				);
	}
	
	public RoutingRulesResource(UriInfo uriInfo, EntityManager entityManagerConfig, QueueEntity queueEntity) {
		super(uriInfo,
				entityManagerConfig.createNamedQuery("RoutingRuleEntity.findAllQueue", RoutingRuleEntity.class)
					.setParameter("queueid", queueEntity.getId()),
				entityManagerConfig.createNamedQuery("RoutingRuleEntity.findTotalQueue", Long.class)
					.setParameter("queueid", queueEntity.getId()),
				entityManagerConfig,
				RoutingRuleEntity.class
				);
	}

	/**
	 * Returns a routing rule sub-resource with guid
	 * 
	 * @param guid
	 *            String guid of the routing rule as last element in the path
	 * 
	 * @return RoutingRuleResource The routing rule sub-resource
	 */
	@Path("{id}")
	public RoutingRuleResource getRoutingRuleResource(@PathParam("id") long id) {
		//guid = Jsoup.clean(guid, Whitelist.none());
		return new RoutingRuleResource(getUriInfo(), getEntityManager(), id);
	}

	/**
	 * GET method : returns an application/json formatted list of routing rules
	 * 
	 * @return JSONObject The list of routing rules
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.ROUTINGRULES_VIEW})
	@JsonIgnore
	public PagedCollection<RoutingRuleEntity> getRoutingRulesAsJson() {
		return this;
	}

	/**
	 * POST method : creates a routing rule
	 * 
	 * @param jsonEntity
	 *            JSONObject
	 * @return Response
	 */

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.ROUTINGRULES_MODIFY})
	public Response postForm(RoutingRuleEntity entity) {
		return post(entity);
	}
}
