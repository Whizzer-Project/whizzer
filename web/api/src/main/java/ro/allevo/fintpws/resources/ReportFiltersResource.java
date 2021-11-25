package ro.allevo.fintpws.resources;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import ro.allevo.fintpws.model.ReportMessageCriteriaTemplatesEntity;
import ro.allevo.fintpws.util.JsonResponseWrapper;

public class ReportFiltersResource  extends PagedCollection<ReportMessageCriteriaTemplatesEntity>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String businessArea;
	private String templateName;
	private int userId;

	public ReportFiltersResource(UriInfo uriInfo, EntityManager entityManagerConfig, String businessArea, String templateName, Integer userId) {
		super(uriInfo,
				entityManagerConfig
				.createNamedQuery("ReportMessageCriteriaTemplatesEntity.FindAllByTemplateAndBusinessArea", ReportMessageCriteriaTemplatesEntity.class)
				.setParameter("businessArea", businessArea)
				.setParameter("template", templateName)
				.setParameter("userId", userId),
			null,
			entityManagerConfig,
			ReportMessageCriteriaTemplatesEntity.class
			);
		this.businessArea = businessArea;
		this.templateName = templateName;
		this.userId = userId;
	}
	
	public ReportFiltersResource(UriInfo uriInfo, EntityManager entityManager, String businessArea, Integer userId) {
		super(uriInfo,
				entityManager
				.createNamedQuery("ReportMessageCriteriaTemplatesEntity.FindAllByBusinessArea", ReportMessageCriteriaTemplatesEntity.class)
				.setParameter("businessArea", businessArea)
				.setParameter("userId", userId),
				null,
				entityManager,
			ReportMessageCriteriaTemplatesEntity.class
			);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@JsonIgnore
	public PagedCollection<ReportMessageCriteriaTemplatesEntity> getAllRecords(){
		return this;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response saveFilters(List<ReportMessageCriteriaTemplatesEntity> filters) {
		Response resp = post(filters);
		return JsonResponseWrapper.getResponse(Response.Status.CREATED, resp.toString());
	}
	
	@DELETE
	public Response deleteFilters() {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("businessArea", this.businessArea);
		parameters.put("template", this.templateName);
		parameters.put("userId", this.userId);
		Response resp = delete(parameters);
		return JsonResponseWrapper.getResponse(resp.getStatus(), resp.toString());
	}

}
