package ro.allevo.fintpws.resources;

import java.util.Arrays;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ro.allevo.fintpws.model.UserRolesEntityMaps;
import ro.allevo.fintpws.util.Roles;

public class UserRolesEntities extends PagedCollection<UserRolesEntityMaps> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4561933315179138032L;
	private Long userId;

	public UserRolesEntities(UriInfo uriInfo, EntityManager entityManager, Long userId) {
		super(uriInfo, entityManager.createNamedQuery("UserRolesEntityMaps.findByUserId", UserRolesEntityMaps.class)
				.setParameter("userId", userId), null, entityManager, UserRolesEntityMaps.class);
		this.userId = userId;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@JsonIgnore
	public PagedCollection<UserRolesEntityMaps> getUserRolesEntityMaps() {
		return this;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({ Roles.USERS_MODIFY })
	public Response postUserRolesEntityMaps(UserRolesEntityMaps[] userRolesEntityMaps) {
		return post(Arrays.asList(userRolesEntityMaps));
	}

	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({ Roles.USERS_MODIFY })
	public Response deleteUserRolesEntityMaps() {
		return delete(userId);
	}

}
