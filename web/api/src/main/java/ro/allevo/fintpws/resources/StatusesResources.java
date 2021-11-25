package ro.allevo.fintpws.resources;

import javax.persistence.EntityManager;
import javax.persistence.RollbackException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import ro.allevo.fintpws.model.StatusEntity;
import ro.allevo.fintpws.util.JsonResponseWrapper;
import ro.allevo.fintpws.util.ResourcesUtils;
import ro.allevo.fintpws.util.annotations.URLId;

public class StatusesResources extends PagedCollection<StatusEntity> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1021112978878782640L;

	public StatusesResources(UriInfo uriInfo, EntityManager entityManager) {
		super(uriInfo, entityManager.createNamedQuery("StatusEntity.findAll", StatusEntity.class),
				entityManager.createNamedQuery("StatusEntity.findTotal", Long.class), entityManager,
				StatusEntity.class);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	// @RolesAllowed({Roles.BANKS_LIST_MODIFY})
	public Response postForm(StatusEntity statusEntity) {

		EntityManager eManager = null;
		String id = "-1";
		try {
			eManager = getEntityManager().getEntityManagerFactory().createEntityManager();
			eManager.getTransaction().begin();
			eManager.persist(statusEntity);
			eManager.getTransaction().commit();
			id = ResourcesUtils.getValueForAnnotation(statusEntity, URLId.class) + "";
		} catch (RollbackException re) {
			throw re;
		} finally {
			if (null != eManager && eManager.isOpen()) {
				eManager.close();
			}
		}
		/*
		 * String host = getUriInfo().getBaseUri().getHost();
		 * statusEntity.setMachine(host);
		 */
		// return post(statusEntity);
		return JsonResponseWrapper.getResponse(Response.Status.CREATED, id);
	}

}
