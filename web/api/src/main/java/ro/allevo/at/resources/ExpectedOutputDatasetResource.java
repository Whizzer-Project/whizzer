package ro.allevo.at.resources;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ro.allevo.at.model.ExpectedOutputDatasetEntity;
import ro.allevo.fintpws.resources.PagedCollection;
import ro.allevo.fintpws.util.Roles;

public class ExpectedOutputDatasetResource extends PagedCollection<ExpectedOutputDatasetEntity> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ExpectedOutputDatasetResource(UriInfo uriInfo, EntityManager entityManagerData) {
		super(uriInfo,
				entityManagerData.createNamedQuery("ExpectedOutputDatasetEntity.findAll", ExpectedOutputDatasetEntity.class),
				entityManagerData.createNamedQuery("ExpectedOutputDatasetEntity.findTotal", Long.class),
				entityManagerData,
				ExpectedOutputDatasetEntity.class
			);
	}
		
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.AUTOMATED_TESTING_VIEW})
	@JsonIgnore
	public PagedCollection<ExpectedOutputDatasetEntity> getExpectedOutputDatasetsAsJson() {
		return this;
	}
}
