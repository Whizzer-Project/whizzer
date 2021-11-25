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

import ro.allevo.fintpws.model.RoutingSchemaEntity;
import ro.allevo.fintpws.util.Roles;


public class RoutingSchemasResource extends PagedCollection<RoutingSchemaEntity>{

	/**
	 * 
	 * @param uriInfo UriInfo
	 * @param entityManagerConfig EntityManager
	 */
	public RoutingSchemasResource(UriInfo uriInfo, EntityManager entityManagerConfig) {
		super(uriInfo,
				entityManagerConfig.createNamedQuery("RoutingSchemaEntity.findAll", RoutingSchemaEntity.class),
				entityManagerConfig.createNamedQuery("RoutingSchemaEntity.findTotal", Long.class),
				entityManagerConfig,
				RoutingSchemaEntity.class
				);
	}
	
	@Path("{routingSchemaId}")
	public RoutingSchemaResource getRoutingSchemaResource(@PathParam("routingSchemaId") long id){
		//name = Jsoup.clean(name, Whitelist.none());
		return new RoutingSchemaResource(getUriInfo(), getEntityManager(), id);
	}
	
	/**
	 * GET method : returns an application/json formatted list of routing schemas
	 * 
	 * @return JSONObject The list of routing schemas
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.ROUTINGRULES_VIEW})
	@JsonIgnore
	public PagedCollection<RoutingSchemaEntity> getRoutingSchemasAsJson(){
		return this;
	}
	
	/**
	 * POST method : creates a routing schema
	 * 
	 * @param jsonEntity JSONObject
	 * @return Response
	 */
	
	@POST 
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.ROUTINGRULES_MODIFY})
	public Response postForm(RoutingSchemaEntity entity){
		return post(entity);
	}
	
	/**
	 * Returns the resource formatted as json
	 * 
	 * @throws JSONException
	 * @return JSONObject
	 * @throws JSONException
	 * 
	 */
	/*
	@SuppressWarnings("unchecked")
	public JSONObject asJson() throws JSONException {
		final JSONObject routingSchemasAsJson = pagedCollection.asJson();

		// fill data
		final JSONArray routingSchemasArray = new JSONArray();
		final List<?> items = pagedCollection.getItems();

		if (items.size() > 0) {
			for (RoutingSchemaEntity routingSchemaEntity : (List<RoutingSchemaEntity>) items) {
				routingSchemasArray.put(RoutingSchemaResource.asJson(
						routingSchemaEntity,
						UriBuilder.fromPath(uriInfo.getPath())
								.path(String.valueOf(routingSchemaEntity.getName())).build()
								.getPath()));
			}
		}
		routingSchemasAsJson.put("routingschemas", routingSchemasArray);
		return routingSchemasAsJson;
	}*/
}
