package ro.allevo.fintpws.resources;

import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import ro.allevo.fintpws.model.TxTemplatesConfigDetailedEntity;

public class TemplatesConfigDetailedResource extends BaseResource<TxTemplatesConfigDetailedEntity>{

	public TemplatesConfigDetailedResource(UriInfo uriInfo, EntityManager entityManager, Integer templateConfigId, Integer id) {
		super(
				TxTemplatesConfigDetailedEntity.class,
				uriInfo,
				entityManager,
				id);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public TxTemplatesConfigDetailedEntity getTemplateConfigDetailed() {
		return get();
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateTemplatesConfigDetailed(TxTemplatesConfigDetailedEntity templatesConfigDetailedEntity) {
//		templatesConfigDetailedEntity.getTxtemplatesconfig().setValidationxsd(null);
		return put(templatesConfigDetailedEntity);
	}
	
	@DELETE
	public Response deleteTemplateConfigDetailedById() {
		return delete();
	}

}
