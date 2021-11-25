package ro.allevo.tracker.resources;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import ro.allevo.fintpws.util.Roles;
import ro.allevo.tracker.model.ComplexityEntity;

public class ComplexityResource {
	
	private final EntityManager entityManager;
	
	public ComplexityResource(UriInfo uriInfo, EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.TRACKER_VIEW})
	public List<ComplexityEntity> getAll() {
		TypedQuery<ComplexityEntity> query = entityManager.createNamedQuery("ComplexityEntity.findAll", ComplexityEntity.class);
		
		List<ComplexityEntity> result = query.getResultList();
		
		return result;
	}
}
