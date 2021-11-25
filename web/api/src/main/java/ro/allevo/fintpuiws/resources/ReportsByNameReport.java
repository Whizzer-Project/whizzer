package ro.allevo.fintpuiws.resources;

import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import ro.allevo.fintpuiws.model.MessageCriterionEntity;
import ro.allevo.fintpws.resources.BaseResource;
import ro.allevo.fintpws.resources.MessagesByBusinessAreaResource;
import ro.allevo.fintpws.util.enums.BusinessAreaToViews;

public class ReportsByNameReport extends BaseResource<MessageCriterionEntity> {
	
	String reportName;
	EntityManager entityManager;

	public ReportsByNameReport(UriInfo uriInfo, EntityManager entityManager, String reportName) {
		super(MessageCriterionEntity.class, uriInfo, entityManager, 
				entityManager.createNamedQuery("MessageCriterionEntity.findByBusinessArea", MessageCriterionEntity.class)
				.setParameter("businessarea", reportName)
				.setFirstResult(0)
				.setMaxResults(1)
				);
		this.reportName = reportName;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public MessageCriterionEntity getReport() {
		return get();
	}

	@Path("messages")
	public MessagesByBusinessAreaResource getMessages(@QueryParam("pagination") String pagination) {
		BusinessAreaToViews businessArea = BusinessAreaToViews.getView(getEntityManager(), reportName);
		if (null != pagination)
			return new MessagesByBusinessAreaResource(getUriInfo(), businessArea, false);
		return new MessagesByBusinessAreaResource(getUriInfo(), businessArea, true);
	}

}
