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

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ro.allevo.fintpws.model.UserEntity;
import ro.allevo.fintpws.util.Roles;



public class UsersResource extends PagedCollection<UserEntity> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1151390780734849836L;

	/**
	 * 
	 * @param uriInfo
	 * @param itemsQuery
	 * @param totalQuery
	 */
	public UsersResource(UriInfo uriInfo, EntityManager entityManagerConfig) {
		super(uriInfo,
				entityManagerConfig.createNamedQuery("UserEntity.findAll", UserEntity.class), 
				entityManagerConfig.createNamedQuery("UserEntity.findTotal", Long.class),
				entityManagerConfig,
				UserEntity.class
				);
	}
	
	@Path("{username}")
	public UserResource getUserResource(@PathParam("username") String username) {
		username = Jsoup.clean(username, Whitelist.none());
		return new UserResource(getUriInfo(), getEntityManager(), username);
	}
	
	@Path("by-id/{id}")
	public UserResource getUserResource(@PathParam("id") long userid) {
		//username = Jsoup.clean(username, Whitelist.none());
		return new UserResource(getUriInfo(), getEntityManager(), userid);
	}
	
	
	/**
	 * GET method : returns an application/json formatted list of time limits
	 * 
	 * @return JSONObject The list of time limits
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@JsonIgnore
	public PagedCollection<UserEntity> getUsersAsJson() {
		return this;
	}
	

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.USERS_MODIFY})
	public Response postForm(List<UserEntity> userEntity) {
		return post(userEntity);
	}
	
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.USERS_MODIFY})
	public Response deleteAll(List<Long> users) {
		return delete(users);
	}
}
