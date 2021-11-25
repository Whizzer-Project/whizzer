package ro.allevo.fintpws.resources;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ro.allevo.fintpws.model.TxTemplatesConfigOptionEntity;
import ro.allevo.fintpws.util.Roles;

public class TemplatesConfigOptionsResource extends PagedCollection<TxTemplatesConfigOptionEntity>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TemplatesConfigOptionsResource(UriInfo uriInfo, EntityManager entityManager) {
		super(uriInfo,
				entityManager.createNamedQuery("TxTemplatesConfigOptionEntity.findAll", TxTemplatesConfigOptionEntity.class),
				null,
				entityManager,
				TxTemplatesConfigOptionEntity.class
				);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.PAYMENT_TEMPLATE_CONFIGURATION_VIEW})
	@JsonIgnore
	public PagedCollection<TxTemplatesConfigOptionEntity> getTemplatesAsJson() {
		return this;
	}

	@Path("{id}")
	public TemplatesConfigOptionResource getTemplatesConfigOptionById(@PathParam(value="id") String id) {
		return new TemplatesConfigOptionResource(getUriInfo(), getEntityManager(), id);
	}
}
