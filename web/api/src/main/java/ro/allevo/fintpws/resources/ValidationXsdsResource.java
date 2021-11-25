package ro.allevo.fintpws.resources;

import javax.persistence.EntityManager;
import javax.ws.rs.POST;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import ro.allevo.fintpws.model.TxTemplatesSmallConfigEntity;

public class ValidationXsdsResource extends PagedCollection<TxTemplatesSmallConfigEntity> {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public ValidationXsdsResource(UriInfo uriInfo, EntityManager entityManager) {
		super(uriInfo, null, null, entityManager, TxTemplatesSmallConfigEntity.class);
	}

	@POST
	public Response postTemplateConfig(TxTemplatesSmallConfigEntity templatesSmallConfig) {
		return post(templatesSmallConfig);
	}

}
