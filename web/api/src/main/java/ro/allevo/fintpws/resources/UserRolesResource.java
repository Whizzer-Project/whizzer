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
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ro.allevo.fintpws.model.UserEntity;
import ro.allevo.fintpws.model.UserRoleEntity;
import ro.allevo.fintpws.util.Roles;

public class UserRolesResource extends PagedCollection<UserRoleEntity>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final UserEntity userEntity;
	
	public UserRolesResource(UriInfo uriInfo, EntityManager entityManagerConfig, 
			UserEntity userEntity) {
		super(uriInfo,
				entityManagerConfig
					.createNamedQuery("UserRoleEntity.findAllForUser", UserRoleEntity.class)
					.setParameter("userid", userEntity.getId()), 
				entityManagerConfig
					.createNamedQuery("UserRoleEntity.findTotalForUser", Long.class)
					.setParameter("userid", userEntity.getId()),
				entityManagerConfig,
				UserRoleEntity.class
				);
		
		this.userEntity = userEntity;
	}
	
	public UserRolesResource(UriInfo uriInfo, EntityManager entityManagerConfig, Integer userId) {
		super(uriInfo,
				entityManagerConfig
					.createNamedQuery("UserRoleEntity.findAllForUser", UserRoleEntity.class)
					.setParameter("userid", userId), 
				entityManagerConfig
					.createNamedQuery("UserRoleEntity.findTotalForUser", Long.class)
					.setParameter("userid", userId),
				entityManagerConfig,
				UserRoleEntity.class
				);
		this.userEntity = null;
	}
	
	@Path("{mapid}")
	public UserRoleResource getUserRole(@PathParam("mapid") long mapId) {
		return new UserRoleResource(getUriInfo(), getEntityManager(), mapId);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@JsonIgnore
	public PagedCollection<UserRoleEntity> getUserRoles() {
		//TODO change PagedCollection to List
		//in order to get all items
		disablePaging();
		return this;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.USERS_MODIFY})
	public Response postUserRoles(List<UserRoleEntity> roles) {
		
		for (UserRoleEntity role : roles)
			role.setUserId(userEntity.getId());
		
		return post(roles);
	}
	
	@DELETE
	@RolesAllowed({Roles.USERS_MODIFY})
	public Response deleteRoles() {
		return delete(userEntity.getId());
	}
}
