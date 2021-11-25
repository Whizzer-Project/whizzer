package ro.allevo.fintpws.resources;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import ro.allevo.fintpws.model.UserRoleEntity;
import ro.allevo.fintpws.util.Roles;

public class UserRoleResource extends BaseResource<UserRoleEntity> {
	
	public UserRoleResource(UriInfo uriInfo, EntityManager entityManagerList, long mapid) {
		super(UserRoleEntity.class, uriInfo, entityManagerList, mapid);	
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public UserRoleEntity getUserRole() {
		return get();
	}

	@DELETE
	@RolesAllowed({Roles.USERS_MODIFY})
	public Response deleteUserRole() {
		return delete();
	}
}
