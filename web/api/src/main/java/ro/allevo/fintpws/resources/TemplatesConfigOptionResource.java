package ro.allevo.fintpws.resources;

import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import ro.allevo.fintpws.model.TxTemplatesConfigOptionEntity;

public class TemplatesConfigOptionResource extends BaseResource<TxTemplatesConfigOptionEntity>{

	public TemplatesConfigOptionResource(UriInfo uriInfo, EntityManager entityManager, String id) {
		super(TxTemplatesConfigOptionEntity.class, uriInfo, entityManager, Long.parseLong(id));
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public TxTemplatesConfigOptionEntity getTemplateConfigOption() {
		return get();
	}

}
