package ro.allevo.fintpws.resources;

import java.util.Arrays;

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
import ro.allevo.fintpws.model.TxTemplatesGroupsEntity;

public class TemplateGroupsResource extends PagedCollection<TxTemplatesGroupsEntity>{

	private static final long serialVersionUID = 1L;
	
	public TemplateGroupsResource(UriInfo uriInfo, EntityManager entityManager, Integer templateId) {
		super(uriInfo,
				entityManager.createNamedQuery("TxTemplatesGroupsEntity.findAll", TxTemplatesGroupsEntity.class)
				.setParameter("templateid", templateId),
				entityManager.createNamedQuery("TxTemplatesGroupsEntity.findTotal", Long.class)
				.setParameter("templateid", templateId),
				entityManager,
				TxTemplatesGroupsEntity.class
				);
	}
	
	@Path("{groupId}")
	public TemplateGroupResource getTemplateResource(@PathParam("groupId") Integer groupId) {
		return new TemplateGroupResource(getUriInfo(), getEntityManager(), groupId);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@JsonIgnore
	public PagedCollection<TxTemplatesGroupsEntity> getTemplatesAsJson() {
		return this;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response saveGroup(TxTemplatesGroupsEntity[] templateGroupEntity) {
		return post(Arrays.asList(templateGroupEntity));
	}
}
