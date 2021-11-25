package ro.allevo.at.resources;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ro.allevo.at.model.InputDatasetEntity;
import ro.allevo.fintpws.resources.PagedCollection;
import ro.allevo.fintpws.util.Roles;

public class InputDatasetsResource extends PagedCollection<InputDatasetEntity> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InputDatasetsResource(UriInfo uriInfo, EntityManager entityManagerAutoTesting) {
		super(uriInfo,
				entityManagerAutoTesting.createNamedQuery("InputDatasetEntity.findAll", InputDatasetEntity.class),
				entityManagerAutoTesting.createNamedQuery("InputDatasetEntity.findTotal", Long.class),
				entityManagerAutoTesting,
				InputDatasetEntity.class);
	}
	
	public InputDatasetsResource(UriInfo uriInfo, EntityManager entityManagerAutoTesting, Integer commonid) {
		super(uriInfo,
				entityManagerAutoTesting.createNamedQuery("InputDatasetEntity.findAllById", InputDatasetEntity.class)
											.setParameter("commonid", commonid),
				entityManagerAutoTesting.createNamedQuery("InputDatasetEntity.findTotal", Long.class),
				entityManagerAutoTesting,
				InputDatasetEntity.class);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.AUTOMATED_TESTING_VIEW})
	@JsonIgnore
	public PagedCollection<InputDatasetEntity> getInputDatasetEntitiesAsJson() {
		return this;
	}

	@Path("{entityUniqueId}")
	public InputDatasetResource getInputDataset(@PathParam("entityUniqueId") Integer entityUniqueId) {
		return new InputDatasetResource(getUriInfo(), getEntityManager(), entityUniqueId);
	}
}
