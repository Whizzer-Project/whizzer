package ro.allevo.fintpws.resources;

import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import ro.allevo.fintpws.model.ParamsEntity;

public class ParamResource extends BaseResource<ParamsEntity>{
	
	public ParamResource(UriInfo uriInfo, EntityManager entityManagerList, String code) {
		super(ParamsEntity.class, uriInfo, entityManagerList, code);	
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ParamsEntity getParam() {
		return get();
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateParam(ParamsEntity entity) {
		return put(entity);
	}
}
