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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import ro.allevo.fintpws.model.UserEntity;
import ro.allevo.fintpws.model.UserRoleEntity;
import ro.allevo.fintpws.util.Roles;

public class UserResource extends BaseResource<UserEntity> {

	/**
	 * Constructor for UsersResource.
	 * 
	 * @param uriInfo             UriInfo
	 * @param entityManagerConfig EntityManager
	 * @param guid                String
	 */
	public UserResource(UriInfo uriInfo, EntityManager entityManagerConfig, String username) {

		super(UserEntity.class, uriInfo, entityManagerConfig, username, "findByUsername");
	}

	public UserResource(UriInfo uriInfo, EntityManager entityManagerConfig, long userid) {

		super(UserEntity.class, uriInfo, entityManagerConfig, userid);
	}

	/**
	 * Sub-resource locator for /roles resource
	 * 
	 * @return roles
	 */
	@Path("user-roles")
	public UserRolesResource getUserRoles() {
		return new UserRolesResource(getUriInfo(), getEntityManager(), get());
	}

	@Path("user-roles-entity")
	public UserRolesEntities getUserRolesEntities() {
		return new UserRolesEntities(getUriInfo(), getEntityManager(), get().getId());
	}

	/**
	 * GET Method : returns an application/json formatted time limit
	 * 
	 * @return JSONObject the time limit
	 */

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public UserEntity getUser() {
		return get();
	}

	/**
	 * PUT method: updates the user
	 * 
	 * @param jsonEntity JSONObject the time limit holding new values
	 * @return Response
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({ Roles.USERS_MODIFY })
	public Response updateUser(UserEntity entity) {
		return put(entity);
	}

	/**
	 * DLETE method: delete the user
	 * 
	 * @param null
	 * @return Response
	 */
	@DELETE
	@RolesAllowed({ Roles.USERS_MODIFY })
	public Response deleteUser() {
		return delete();
	}

	private List<UserRoleEntity> getRoles() {
		return getUserRoles().getUserRoles().asList();
	}

	/*
	 * public List<UserDefinedRoleEntity> getAuthorizedRoles(String messageType,
	 * String entity, UserRoleEntity.Action action) { List<UserDefinedRoleEntity>
	 * authorized = new ArrayList<UserDefinedRoleEntity>();
	 * 
	 * for (UserRoleEntity role : getRoles()) if
	 * (role.getAction().equals(action.toString())) for (UserDefinedRoleEntity
	 * udRole : role.getRoleEntity().getUserDefinedRoles()) if ( (messageType ==
	 * null || udRole.getMessageType().equals(messageType)) //null skips check &&
	 * (entity == null || udRole.getInternalEntityName().equals(entity)) )
	 * authorized.add(udRole);
	 * 
	 * return authorized; }
	 */
	/*
	 * public Set<String> getAuthorizedEntities(String messageType,
	 * UserRoleEntity.Action action) { Set<String> entities = new HashSet<String>();
	 * 
	 * entities.add(""); // handle not authorized for select ... where x in ('',
	 * ...)
	 * 
	 * List<UserDefinedRoleEntity> roles = getAuthorizedRoles(messageType, null,
	 * action);
	 * 
	 * for (UserDefinedRoleEntity role : roles)
	 * entities.add(role.getInternalEntityName());
	 * 
	 * System.out.println(entities); return entities; }
	 */
	public String getAuthorizedWhere(Integer userid, boolean fromReport, boolean withEntity) {
		Map<String, Object> queryParams = new HashMap<>();
		queryParams.put("userid", userid);

		StringBuilder where = new StringBuilder();
		Long count = (Long) getByNamedQuery("UserRoleEntity.isSuperviser", queryParams);

		if (count == 0L) {
			@SuppressWarnings("unchecked")
			List<Object[]> roles = (List<Object[]>) getByNamedQuery("UserDefinedRoleEntity.findRoles", Object[].class,
					queryParams);
			if (!roles.isEmpty()) {
				where.append(" where( ");
			}

			for (Object[] role : roles) {
				where.append(where.length() == 8 ? "" : " or");
				if (withEntity)
					where.append(" (messagetype='" + role[fromReport ? 1 : 3] + "' and entity='" + role[2] + "')");
				else
					where.append(" (messagetype='" + role[fromReport ? 1 : 3] + "')");
			}
			if (!roles.isEmpty()) {
				where.append(" ) ");
			} else {
				where.append(" 1 = 2 ");
			}
		}
		if (0 == where.length())
			where.append(" where 1=1 ");
		return where.toString();
	}

}
