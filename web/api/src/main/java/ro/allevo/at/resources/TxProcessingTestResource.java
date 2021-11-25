package ro.allevo.at.resources;

import java.util.HashMap;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ro.allevo.at.model.TxProcessingTestEntity;
import ro.allevo.fintpws.resources.BaseResource;
import ro.allevo.fintpws.util.Roles;

public class TxProcessingTestResource extends BaseResource<TxProcessingTestEntity>{
	
	public TxProcessingTestResource(UriInfo uriInfo, EntityManager entityManagerAutoTesting, Integer entityUniqueId) {
		super(TxProcessingTestEntity.class, uriInfo, entityManagerAutoTesting, entityUniqueId);	
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.AUTOMATED_TESTING_VIEW})
	@JsonIgnore
	public TxProcessingTestEntity getInputDataSet() {
		return get();
	}
	
	@GET
	@Path("call-procedure")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.AUTOMATED_TESTING_VIEW})
	@JsonIgnore
	public TxProcessingTestEntity callStoredProcedure() {

		HashMap<String, Object> queryParams = new HashMap<>();
		queryParams.put("intestid", get().getId());
		queryParams.put("intxtype", get().getTxtype());
		List<Object> procedureResult = callNamedStoreProcedure("TxProcessingTestEntity.gettestcontrollerdata", queryParams);
		
		get().setCallProcedureResult(procedureResult);
		return get();
	}
}
