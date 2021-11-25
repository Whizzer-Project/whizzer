package ro.allevo.fintpws.resources;

import java.util.Arrays;

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

import ro.allevo.fintpws.model.BusinessAreaEntity;
import ro.allevo.fintpws.model.MsgTypeListEntity;
import ro.allevo.fintpws.model.TxTemplatesConfigDetailedEntity;
import ro.allevo.fintpws.model.TxTemplatesConfigEntity;
import ro.allevo.fintpws.util.Roles;

public class TemplatesConfigDetailedsResource extends PagedCollection<TxTemplatesConfigDetailedEntity>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer templateConfigId;
	
	public TemplatesConfigDetailedsResource(UriInfo uriInfo, EntityManager entityManager,
			TxTemplatesConfigEntity templateConfigEntity) {
		super(
				uriInfo,
				entityManager.createNamedQuery("TxTemplatesConfigDetailedEntity.findAllByConfigId", TxTemplatesConfigDetailedEntity.class)
						.setParameter("txtemplatesconfig", templateConfigEntity.getId()),
				entityManager.createNamedQuery("TxTemplatesConfigDetailedEntity.findTotalByConfigId", Long.class)
						.setParameter("txtemplatesconfig", templateConfigEntity.getId()),
				entityManager,
				TxTemplatesConfigDetailedEntity.class);
		templateConfigId = templateConfigEntity.getId();
		//disablePaging();
	}

	@Path("{fieldId}")
	public TemplatesConfigDetailedResource getDetailedField(@PathParam("fieldId") String id) {
		return new TemplatesConfigDetailedResource(getUriInfo(), getEntityManager(),  this.templateConfigId, Integer.parseInt(id));
	}
	

	public TemplatesConfigDetailedsResource(UriInfo uriInfo, EntityManager entityManager) {
		super(uriInfo,
				entityManager.createNamedQuery("TxTemplatesConfigDetailedEntity.findAll", TxTemplatesConfigDetailedEntity.class),
				null,
				entityManager,
				TxTemplatesConfigDetailedEntity.class
				);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.PAYMENT_TEMPLATE_CONFIGURATION_VIEW})
	@JsonIgnore
	public PagedCollection<TxTemplatesConfigDetailedEntity> getTemplatesAsJson() {
		return this;
	}
	
//	@POST
//	@Consumes(MediaType.APPLICATION_JSON)
//	public Response postTemplateConfigDetailed(TxTemplatesConfigDetailedEntity templateConfigDetailedEntity) {
//		return post(templateConfigDetailedEntity);
//	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postListTemplateConfigDetailed(TxTemplatesConfigDetailedEntity[] listTemplateConfigDetailedEntity) {
		return post(Arrays.asList(listTemplateConfigDetailedEntity));
	}
	
	@DELETE
	public void deleteTemplateConfigDetailedByConfigId() {
		delete(templateConfigId);
	}
		
}
