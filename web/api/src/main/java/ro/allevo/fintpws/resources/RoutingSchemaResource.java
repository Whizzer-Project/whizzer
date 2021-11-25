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

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import ro.allevo.fintpws.model.RoutingSchemaEntity;
import ro.allevo.fintpws.util.Roles;

/**
 * @author remus
 * @version $Revision: 1.0 $
 */
public class RoutingSchemaResource extends BaseResource<RoutingSchemaEntity> {

	public RoutingSchemaResource(UriInfo uriInfo, EntityManager entityManagerConfig, long id) {
		super(RoutingSchemaEntity.class, uriInfo, entityManagerConfig, id);
	}
	
	/**
	 * Sub-resource locator for /timelimits resource
	 * @return timelimits
	 */
	@Path("time-limits")
	public TimeLimitsResource getTimeLimits() {
		return new TimeLimitsResource(getUriInfo(), getEntityManager(), get());
	}
	
	/**
	 * Sub-resource locator for /routingrules resource
	 * @return routingrules
	 */
	@Path("routing-rules")
	public RoutingRulesResource getRoutingRules() {
		return new RoutingRulesResource(getUriInfo(), getEntityManager(), get());
	}
	
	
	/**
	 * GET Method : returns an application/json formatted routing schema
	 * 
	 * @return JSONObject the routing schema
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.ROUTINGRULES_VIEW})
	public RoutingSchemaEntity getRoutingSchema(){
		return get();
	}
	
	/**
	 * PUT method : updates the routing schema
	 * 
	 * @param jsonEntity
	 *            JSONObject the routing schema holding new values
	 * @return Response
	 */
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.ROUTINGRULES_MODIFY})
	public Response updateRoutingSchema(RoutingSchemaEntity entity){
		return put(entity);
	}
	
	/**
	 * DELETE method : deletes the routing schema
	 * 
	 * @return Response
	 */
	@DELETE
	@RolesAllowed({Roles.ROUTINGRULES_MODIFY})
	public Response deleteRoutingSchema(){
		return delete();
	}
	
	/**
	 * Returns the resource formatted as json
	 * 
	 * @param routingSchemaEntity
	 *            RoutingSchemaEntity
	 * @param path
	 *            String
	 * @throws JSONException
	 * @return JSONObject * @throws JSONException
	 */
/*
	public static JSONObject asJson(RoutingSchemaEntity routingSchemaEntity,
			String path) throws JSONException {
		JSONObject routingSchemaAsJson = ApiResource.getMetaResource(path,
				RoutingSchemaResource.class);

		routingSchemaAsJson.put("guid", routingSchemaEntity.getGuid())
				.put("name", routingSchemaEntity.getName())
				.put("description", routingSchemaEntity.getDescription())
				.put("active", routingSchemaEntity.getActive())
				.put("startlimit", routingSchemaEntity.getStartLimitEntity().getLimitname())
				.put("stoplimit", routingSchemaEntity.getStopLimitEntity().getLimitname())
				.put("sessioncode", routingSchemaEntity.getSessioncode())
				.put("isvisible", routingSchemaEntity.getIsvisible());
		
		routingSchemaAsJson = ResourcesUtils.createLink(routingSchemaAsJson, path + "/timelimits", "timelimits");
		
		return routingSchemaAsJson;
	}*/

}
