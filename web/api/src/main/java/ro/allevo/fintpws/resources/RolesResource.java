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

import java.util.List;

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

import ro.allevo.fintpws.model.RoleEntity;
import ro.allevo.fintpws.model.UserDefinedRoleEntity;
import ro.allevo.fintpws.model.UserEntity;
import ro.allevo.fintpws.util.Roles;

public class RolesResource extends PagedCollection<RoleEntity> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RolesResource(UriInfo uriInfo, EntityManager entityManagerConfig) {
		super(uriInfo, 
				entityManagerConfig.createNamedQuery("RoleEntity.findAll", RoleEntity.class),
				entityManagerConfig.createNamedQuery("RoleEntity.findTotal", Long.class),
				entityManagerConfig,
				RoleEntity.class
				);
	}
	

	/**
	 * 
	 * @param uriInfo
	 *            UriInfo
	 * @param entityManagerConfig
	 *            EntityManager
	 */

	public RolesResource(UriInfo uriInfo, EntityManager entityManagerConfig, UserEntity usersEntity) {

		super(uriInfo, 
				entityManagerConfig.createNamedQuery("RoleEntity.findUserAuthorities", RoleEntity.class)
					.setParameter("userid", usersEntity.getId()),
				entityManagerConfig.createNamedQuery("RoleEntity.findTotalUserAuthorities", Long.class)
					.setParameter("userid", usersEntity.getId()),
				entityManagerConfig,
				RoleEntity.class);
	}


	/**
	 * Returns a role sub-resource with roleid
	 * 
	 * @param roleid
	 *            String roleid of the role as last element in the path
	 * 
	 * @return RoleResource The role sub-resource
	 */
	@Path("{roleid}")
	public RoleResource getRoleResource(@PathParam("roleid") long roleid) {
		//roleid = Jsoup.clean(roleid, Whitelist.none());
		return new RoleResource(getUriInfo(), getEntityManager(), roleid);
	}

	/**
	 * GET method : returns an application/json formatted list of roles
	 * 
	 * @return JSONObject The list of roles
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@JsonIgnore
	public PagedCollection<RoleEntity> getRolesAsJson() {
		return this;		
	}

	/**
	 * POST method: creates a role
	 * 
	 * @param jsonEntity
	 *            JSONObject
	 * @return Response
	 */

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.USERS_MODIFY})
	public Response postRole(RoleEntity entity) {
		List<UserDefinedRoleEntity> userDefinedRoles = entity.getUserDefinedRoles();
		entity.setUserDefinedRoles(null);
		
		//save role without userDefinedRoles
		Response response = post(entity);
		
		long roleId = entity.getId();
		
		for (UserDefinedRoleEntity r : userDefinedRoles)
			r.setRoleid(roleId);
		
		entity.setUserDefinedRoles(userDefinedRoles);
		
		RoleResource role = getRoleResource(roleId);
		//update role
		//inserts UserDefinedRoleEntity
		role.updateRole(entity);
		
		return response;
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
		final JSONObject rolesAsJson = pagedCollection.asJson();

		// fill data
		final JSONArray rolesArray = new JSONArray();
		final List<?> items = pagedCollection.getItems();

		if (items.size() > 0) {
			for (RoleEntity roleEntity : (List<RoleEntity>) items) {
				rolesArray.put(RoleResource.asJson(
						roleEntity,
						UriBuilder.fromPath(uriInfo.getPath())
								.path(String.valueOf(roleEntity.getRoleid()))
								.build().getPath()));
			}
		}
		rolesAsJson.put("roles", rolesArray);
		return rolesAsJson;
	}*/
}
