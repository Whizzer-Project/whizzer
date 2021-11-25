package ro.allevo.at.resources;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.UriInfo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.ws.rs.core.MediaType;

import ro.allevo.at.model.TxProcessingTestEntity;
import ro.allevo.fintpws.resources.PagedCollection;
import ro.allevo.fintpws.util.Roles;

public class TxProcessingTestsResource extends PagedCollection<TxProcessingTestEntity> {

	private static final long serialVersionUID = 1L;

	public TxProcessingTestsResource(UriInfo uriInfo, EntityManager entityManagerAutoTesting) {
		super(uriInfo,
				entityManagerAutoTesting.createNamedQuery("TxProcessingTestEntity.findAll", TxProcessingTestEntity.class),
				entityManagerAutoTesting.createNamedQuery("TxProcessingTestEntity.findTotal", Long.class),
				entityManagerAutoTesting,
				TxProcessingTestEntity.class);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.AUTOMATED_TESTING_VIEW})
	@JsonIgnore	
	public PagedCollection<TxProcessingTestEntity> getTxProcessingTestEntitiesAsJson() {
		return this;
	}
	
	@Path("{id}")
	public TxProcessingTestResource getTxProcessingTestEntity(@PathParam("id") Integer id) {
		return new TxProcessingTestResource(getUriInfo(), getEntityManager(), id);
	}	
	
	@Path("{id}/datasetinput")
	public InputDatasetsResource getInputDatasetResource(@PathParam("id") Integer id){
		return new InputDatasetsResource(getUriInfo(), getEntityManager());
	}
	
	@Path("{id}/datasetinput/getbycommonid/{commonid}")
	public InputDatasetsResource getInputDatasesByCommonId(@PathParam("id") Integer id, @PathParam("commonid") Integer commonid){
		return new InputDatasetsResource(getUriInfo(), getEntityManager(), commonid);
	}	
	
	@Path("processing-log")
	public TxProcessingTestLogResource getTxProcessingTestLogResource(){
		return new TxProcessingTestLogResource(getUriInfo(), getEntityManager());
	}
	
	@Path("{id}/datasetoutput")
	public ExpectedOutputDatasetResource getExpectedOutputDatasetResource(){
		return new ExpectedOutputDatasetResource(getUriInfo(), getEntityManager());
	}

//	@Path("{id}/interface-configs")
//	public InterfaceConfigResource getInterfaceConfigResource(){
//		return new InterfaceConfigResource();
//	}

}
