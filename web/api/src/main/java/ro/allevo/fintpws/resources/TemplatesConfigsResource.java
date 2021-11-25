package ro.allevo.fintpws.resources;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ro.allevo.fintpws.model.TxTemplatesConfigEntity;
import ro.allevo.fintpws.util.Roles;

public class TemplatesConfigsResource extends PagedCollection<TxTemplatesConfigEntity>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	public TemplatesConfigsResource(UriInfo uriInfo, EntityManager entityManager) {
		super(uriInfo,
				entityManager.createNamedQuery("TxTemplatesConfigEntity.findAll", TxTemplatesConfigEntity.class),
				entityManager.createNamedQuery("TxTemplatesConfigEntity.findTotal", Long.class),
				entityManager,
				TxTemplatesConfigEntity.class
				);
	}
	
	@Path("validationxsd")
	public ValidationXsdsResource validationsxsd() {
		return new ValidationXsdsResource(getUriInfo(), getEntityManager());
	}
	
	@Path("{templateConfigId}")
	public TemplateConfigResource getTemplatesConfigResource(@PathParam("templateConfigId") String templateId) {
		return new TemplateConfigResource(getUriInfo(), getEntityManager(), Integer.parseInt(templateId));
	}
	
	@Path("fields")
	public TemplatesConfigDetailedsResource getTemplateConfigDetailed() {
		return new TemplatesConfigDetailedsResource(getUriInfo(), getEntityManager());
	}
//		
//	@GET
//	@Produces(MediaType.APPLICATION_JSON)
//	public List<TxTemplatesConfigEntity> getTemplateConfig(){
//		TypedQuery<TxTemplatesConfigEntity> query = entityManager.createNamedQuery("TxTemplatesConfigEntity.findAll", TxTemplatesConfigEntity.class);
//		return query.getResultList();
//	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.PAYMENT_TEMPLATE_CONFIGURATION_VIEW})
	@JsonIgnore
	public PagedCollection<TxTemplatesConfigEntity> getTemplatesAsJson() {
		return this;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void postTemplateConfig(TxTemplatesConfigEntity templateConfigEntity){
		post(templateConfigEntity);
	}
		
}
