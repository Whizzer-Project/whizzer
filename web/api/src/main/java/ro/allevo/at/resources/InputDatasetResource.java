package ro.allevo.at.resources;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ro.allevo.at.model.InputDatasetEntity;
import ro.allevo.fintpws.resources.BaseResource;
import ro.allevo.fintpws.util.Roles;

public class InputDatasetResource extends BaseResource<InputDatasetEntity>{


	public InputDatasetResource(UriInfo uriInfo, EntityManager entityManagerAutoTesting, Integer entityUniqueId) {
		super(InputDatasetEntity.class, uriInfo, entityManagerAutoTesting, entityUniqueId);	

	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.AUTOMATED_TESTING_VIEW})
	@JsonIgnore
	public InputDatasetEntity getInputDataSet() {
		return get();
	}

}
