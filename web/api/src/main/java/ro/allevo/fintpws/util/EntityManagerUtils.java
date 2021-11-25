package ro.allevo.fintpws.util;

import java.text.ParseException;
import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.RollbackException;
import javax.persistence.TransactionRequiredException;
import javax.ws.rs.core.UriInfo;

import ro.allevo.fintpws.model.BaseEntity;
import ro.allevo.fintpws.model.StatusEntity;
import ro.allevo.fintpws.util.enums.EventsLogger;

public class EntityManagerUtils {
	
	private static String errorMsg = "Error while";
	
	private EntityManagerUtils() {
	    throw new IllegalStateException("Utility class");
	}
	
	public static void persistEntitiesToDB(BaseEntity entity, UriInfo uriInfo, EntityManager entityManager, String restOperation) throws ParseException {
		
		EntityManager eManager = null;
		
		try {
			eManager = entityManager.getEntityManagerFactory().createEntityManager();
			eManager.getTransaction().begin();
			eManager.persist(entity);			
			eManager.getTransaction().commit();
			
			persistStatusEntityToDB(entity, uriInfo, entityManager, restOperation, false);						
			
		} catch (TransactionRequiredException|IllegalArgumentException|EntityExistsException e) {
			
			persistStatusEntityToDB(entity, uriInfo, entityManager, restOperation, true);

		} catch (RollbackException re) {
			throw re;
		} finally {
			if( null != eManager && eManager.isOpen()) {
				eManager.close();
			}
		}
	}
	
	// restOperation = insert, delete, update
	public static void persistStatusEntityToDB(BaseEntity entity, UriInfo uriInfo, EntityManager entityManager, String restOperation, boolean fromError) throws RollbackException, ParseException {
		
		EntityManager eManager = null;		
		StatusEntity statusEntity = null;
		try {
			statusEntity = ModelUtils.getStatusEntity(entity, uriInfo, restOperation);
			
			if (null == statusEntity)
				return;
			
			if (null == statusEntity.getMessage() || statusEntity.getMessage().equals(""))
				return;
			
			if(fromError) {
				statusEntity.setType(EventsLogger.configUIClassEvents.ERROR.toString());
				statusEntity.setMessage(errorMsg + " " + statusEntity.getMessage());
			}
			
			eManager = entityManager.getEntityManagerFactory().createEntityManager();
			eManager.getTransaction().begin();
			eManager.persist(statusEntity);
			eManager.getTransaction().commit();
			
		} catch (TransactionRequiredException|IllegalArgumentException|EntityExistsException e) {
						
			if(null != eManager && eManager.getTransaction().isActive()) {
				statusEntity.setType(EventsLogger.configUIClassEvents.ERROR.toString());
				statusEntity.setMessage(errorMsg + " " + statusEntity.getMessage());
				eManager.persist(statusEntity);
				eManager.getTransaction().commit();
			}
			
		} catch (RollbackException re) {
			throw re;
		} finally {
			if(eManager != null && eManager.isOpen()) {
				eManager.close();
			}
		}	
	}
	
	public static <T> void persistStatusEntityToDBFromList(List<T> entities, UriInfo uriInfo, EntityManager entityManager, String restOperation, boolean fromError) throws RollbackException, ParseException {
		
		EntityManager eManager = null;		
		StatusEntity statusEntity = null;
		try {
			statusEntity = ModelUtils.getStatusEntityFromList(entities, uriInfo, restOperation);
			
			if (null == statusEntity)
				return;
			
			if(fromError) {
				statusEntity.setType(EventsLogger.configUIClassEvents.ERROR.toString());
				statusEntity.setMessage(errorMsg + " " + statusEntity.getMessage());
			}
			
			eManager = entityManager.getEntityManagerFactory().createEntityManager();
			eManager.getTransaction().begin();
			eManager.persist(statusEntity);
			eManager.getTransaction().commit();
			
		} catch (TransactionRequiredException|IllegalArgumentException|EntityExistsException e) {
						
			if(null != eManager && eManager.getTransaction().isActive()) {
				statusEntity.setType(EventsLogger.configUIClassEvents.ERROR.toString());
				statusEntity.setMessage(errorMsg + " " + statusEntity.getMessage());
				eManager.persist(statusEntity);
				eManager.getTransaction().commit();
			}
			
		} catch (RollbackException re) {
			throw re;
		} finally {
			if(eManager != null && eManager.isOpen()) {
				eManager.close();
			}
		}	
	}
}
