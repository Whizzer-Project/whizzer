package ro.allevo.connect.resources;

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

import ro.allevo.connect.model.AuthorizationServersEntity;
import ro.allevo.fintpws.resources.PagedCollection;
import ro.allevo.fintpws.util.Roles;

public class AuthorizationsServersResource extends PagedCollection<AuthorizationServersEntity>{
	
	private static final long serialVersionUID = 1L;
	
	public AuthorizationsServersResource(UriInfo uriInfo, EntityManager entityManagerList) {
		super(uriInfo,
				entityManagerList.createNamedQuery("AuthorizationServersEntity.findAll", AuthorizationServersEntity.class),
				entityManagerList.createNamedQuery("AuthorizationServersEntity.findTotal", Long.class),
				entityManagerList, 
				AuthorizationServersEntity.class);
	}
	
	@Path("{id}")
	public AuthorizationServersResource getIdResource(@PathParam("id") Long id) {
		return new AuthorizationServersResource(getUriInfo(), getEntityManager(), id);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.API_INTERFACE_MODIFY})
	@JsonIgnore
	public PagedCollection<AuthorizationServersEntity> getBanksAsJson() {
		return this;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.API_INTERFACE_MODIFY})
	public Response postForm(AuthorizationServersEntity authorizationServersEntity) {
		//post(authorizationServersEntity.getConsentEntity(), true);
		return post(authorizationServersEntity);
	}

}
