package ro.allevo.fintpws.resources;

import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ro.allevo.fintpws.model.ParamsEntity;

public class ParamsResource extends PagedCollection<ParamsEntity>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ParamsResource(UriInfo uriInfo, EntityManager entityManagerCfg) {
		super(uriInfo, 
			entityManagerCfg.createNamedQuery("ParamsEntity.findAll", ParamsEntity.class),
			entityManagerCfg.createNamedQuery("ParamsEntity.findTotal", Long.class),
			entityManagerCfg,
			ParamsEntity.class
		);
	}
	
	@Path("{code}")
	public ParamResource getParamResource(@PathParam("code") String code) {
		return new ParamResource(getUriInfo(), getEntityManager(), code);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@JsonIgnore
	public PagedCollection<ParamsEntity> getParamsAsJson() {
		return this;
	}
	
}
