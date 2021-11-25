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
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import ro.allevo.fintpws.model.RoleEntity;
import ro.allevo.fintpws.model.UserDefinedRoleEntity;
import ro.allevo.fintpws.model.UserRoleEntity;
import ro.allevo.fintpws.util.Roles;

public class RoleResource extends BaseResource<RoleEntity> {

	/**
	 * Field ERROR_MESSAGE_GET_ROLE. (value is ""Error returning role: "")
	 */
	static final String ERROR_MESSAGE_GET_ROLE = "Error returning role : ";
	/**
	 * Field ERROR_MESSAGE_PUT_ROLE. (value is ""Error updating role
	 * : "")
	 */
	static final String ERROR_MESSAGE_PUT_ROLE = "Error updating role: ";
	/**
	 * Field ERROR_MESSAGE_ROLE_NOT_FOUND. (value is ""Role with roleid [%s] not
	 * found"")
	 */
	
	static final String ERROR_MESSAGE_ROLE_NOT_FOUND = "Role with roleid [%s] not found";
	/**
	 * Field ERROR_REASON_JSON. (value is ""json"")
	 */
	static final String ERROR_REASON_JSON = "json";
	/**
	 * Field ERROR_REASON_NUMBER_FORMAT. (value is ""number format"")
	 */
	static final String ERROR_REASON_NUMBER_FORMAT = "number format";
	/**
	 * Field ERROR_REASON_CONFLICT. (value is ""conflict"")
	 */
	static final String ERROR_REASON_CONFLICT = "conflict";
	/**
	 * Field ERROR_REASON_ROLLBACK. (value is ""rollback"")
	 */
	static final String ERROR_REASON_ROLLBACK = "rollback";
	/**
	 * Field ERROR_REASON_PARSE. (value is ""parse"")
	 */
	static final String ERROR_REASON_PARSE = "parse";
	
	/**
	 * Constructor for RoleResource
	 * 
	 * @param uriInfo
	 * @param entityManagerConfig
	 * @param roleid
	 */
	public RoleResource(UriInfo uriInfo, EntityManager entityManagerConfig, long roleId) {
		super(RoleEntity.class, uriInfo, entityManagerConfig, roleId);
	}

	/**
	 * 
	 * @param entityManager
	 *            EntityManager
	 * @param roleid
	 *            String
	 * @param queueid
	 *            String
	 * @return UserRoleEntity
	 */

	@Deprecated
	public static UserRoleEntity findByRoleidAndUsername(
			EntityManager entityManager, long roleid, long userid) {

		final TypedQuery<UserRoleEntity> query = entityManager
				.createNamedQuery("UserRoleEntity.findUserAuthorities",
						UserRoleEntity.class);

		final java.util.List<UserRoleEntity> results = query
				.setParameter("roleid", roleid).setParameter("userid", userid)
				.getResultList();
		if (!results.isEmpty()) {
			return results.get(0);
		}
		return null;
	}

		
	
	/**
	 * GET Method : returns an application/json formatted role
	 * 
	 * @return JSONObject the role
	 */

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public RoleEntity getRole() {
		return get();
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.USERS_MODIFY})
	public Response updateRole(RoleEntity entity) {
		List<UserDefinedRoleEntity> roles = entity.getUserDefinedRoles();
		long roleId = get().getId();

		//prevent roleid update
		for (UserDefinedRoleEntity role : roles)
			role.setRoleid(roleId);
		
		return put(entity);
	}

	/**
	 * DELETE method : deletes the selected role
	 * 
	 * @return
	 */
	@DELETE
	@RolesAllowed({Roles.USERS_MODIFY})
	public Response deleteRole() {
		RoleEntity role = get();
		if (role.getUserDefined() == 0)
			throw new UnsupportedOperationException();
		
		return delete();
		
	}

	/**
	 * Returns the resource formatted as json
	 * 
	 * @param routingRuleEntity
	 *            RoutingRuleEntity
	 * @param path
	 *            String
	 * @throws JSONException
	 * @return JSONObject * @throws JSONException
	 */
/*
	public static JSONObject asJson(RoleEntity roleEntity, String path)
			throws JSONException {
		final JSONObject roleAsJson = ApiResource.getMetaResource(path,
				RoleResource.class);

		roleAsJson.put("roleid", roleEntity.getRoleid())
				.put("name", roleEntity.getName())
				.put("description", roleEntity.getDescription())
				.put("isusercreated", roleEntity.isUserCreated());
		List<QueuesRoleMapEntity> queueRolesList = roleEntity.getQueueRoles();
		String queueRoles = "";
		for (int i = 0; i < queueRolesList.size(); i++) {
			queueRoles += queueRolesList.get(i).getQueueId();
		}
		roleAsJson.put("queueroles", queueRoles);

		return roleAsJson;
	}*/
}
