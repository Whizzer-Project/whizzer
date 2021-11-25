package ro.allevo.fintpws.resources;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.OneToOne;
import javax.persistence.Query;
import javax.persistence.RollbackException;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ro.allevo.fintpws.exceptions.ApplicationJsonException;
import ro.allevo.fintpws.model.BaseEntity;
import ro.allevo.fintpws.util.EntityManagerUtils;
import ro.allevo.fintpws.util.JsonResponseWrapper;
import ro.allevo.fintpws.util.ResourcesUtils;
import ro.allevo.fintpws.util.enums.QueryProvider;

public abstract class BaseResource<T extends BaseEntity> implements AutoCloseable {

	static final String ERROR_MESSAGE_NOT_FOUND = "%s with id [%s] not found";
	
	static final String ERROR_MESSAGE_PUT = "Error updating %s";
	
	static final String ERROR_MESSAGE_DELETE = "Error deleting %s";
	
	static final String ERROR_REASON_ROLLBACK = "rollback";
	
	static final String ERROR_REASON_JSON = "json";
	
	static final String ERROR_UNSUPPORTED_OPERATION_EXCEPTION = "Query cannot be null";
	
	static final String ERROR_MISSING_REQUEST = "Missing request object. Please provide a valid request object before running the query";
	
	static final String ERROR_REASON_PARSE = "Could not parse when try to create Entity";
	
	private final Logger logger;
	
	private final Class<T> entityClass;
	
	private final EntityManager entityManager;
	
	private final UriInfo uriInfo;
	
	private Object entityObject;
	
	private Query query;
	
	private QueryProvider queryProvider;
	
	private Object id = null;
	
	private HttpServletRequest request;
	
	protected void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	
	private BaseResource(Class<T> entityClass, UriInfo uriInfo, EntityManager entityManager) {
		this.uriInfo = uriInfo;
		this.entityManager = entityManager;
		this.entityClass = entityClass;
		
		logger = LogManager.getLogger(getClass().getName());
	}
	
	public BaseResource(Class<T> entityClass, UriInfo uriInfo, EntityManager entityManager, Object id) {
		this(entityClass, uriInfo, entityManager, id, "findById");
		
		this.id = id;
	}
	
	public BaseResource(Class<T> entityClass, UriInfo uriInfo, EntityManager entityManager, Object id, String methodName) {
		this(entityClass, uriInfo, entityManager);
		
		this.id = id;
		
		findById(entityManager, entityClass, id, methodName);
		
		//handleSelected(id);
	}
	
	public BaseResource(Class<T> entityClass, UriInfo uriInfo, EntityManager entityManager, TypedQuery<T> query) {
		this(entityClass, uriInfo, entityManager);
		
		this.query = query;
		//Object id = query.getParameterValue("id");
		
		//Object result = findByQuery(query);
		//entity = entityClass.cast(result);
		
		//handleSelected(id);
	}
	
	public BaseResource(UriInfo uriInfo, EntityManager entityManager, QueryProvider queryProvider, Object id) {
		this(null, uriInfo, entityManager);
		
		this.queryProvider = queryProvider;
		this.id = id;
	}
	
//	public static <T extends BaseEntity> T findById(EntityManager entityManager, Class<T> entityClass, Object id) {
//		return findById(entityManager, entityClass, id, "findById");
//	}
	
	private void findById(EntityManager entityManager, Class<T> entityClass, Object id, String methodName) {
		query = entityManager
				.createNamedQuery(entityClass.getSimpleName() + "." + methodName, entityClass)
				.setParameter("id", id);
		
		
	}
	
	private Object findByQuery(Query query) {
		if (null == query)
			throw new UnsupportedOperationException(ERROR_UNSUPPORTED_OPERATION_EXCEPTION);
		
		return query.getSingleResult();
		
	}
	
	protected T get() {
		Object result = getObject();
		T entity = entityClass.cast(result);
		
		if (null == entity) {
			Object id = query.getParameter("id");
			String message = String.format(ERROR_MESSAGE_NOT_FOUND, entityClass.getSimpleName(), id); 
			
			logger.error(message);
			throw new EntityNotFoundException(message);
		}
		
		return entity;
	}
	
	protected List<?> getByNamedQuery(String namedQuery, Class<?> class1, Map<String,Object> queryParams) {
		 TypedQuery<?> query = entityManager.createNamedQuery(namedQuery, class1);
		 for (Map.Entry<String,Object> entry : queryParams.entrySet())  
			 query.setParameter(entry.getKey(), entry.getValue());
		 return query.getResultList();
	}
	protected Object getByNamedQuery(String namedQuery, Map<String,Object> queryParams) {
		 Query query = entityManager.createNamedQuery(namedQuery);
		 for (Map.Entry<String,Object> entry : queryParams.entrySet())  
			 query.setParameter(entry.getKey(), entry.getValue());
		 return query.getSingleResult();
	}
	
	protected Object getObject() {
		if (null == query) // try to get the query from the query provider
			if (null != queryProvider) {
				if (null != request)
					query = queryProvider.getByIdQuery(request, id.toString());
				else
					throw new UnsupportedOperationException(ERROR_MISSING_REQUEST);
			}
		
		if (null == entityObject)
			entityObject = findByQuery(query);
		
		return entityObject;
	}
	
	protected Response put(T newEntity) {
		String message = null;
		EntityManager em = null;
		T entity = get();
		
		try {
			em = entityManager.getEntityManagerFactory().createEntityManager();
			em.getTransaction().begin();

			entity.mergeWith(newEntity);
			entity = em.merge(entity);
			
			em.getTransaction().commit();
			
			EntityManagerUtils.persistStatusEntityToDB(entity, uriInfo, em, "PUT", false);

		}catch (Exception e) {
			
			// rethrow RollbackException or if IllegalAccessException -- throw ApplicationJsonException
			
			if(e instanceof RollbackException) {				
				handleRollbackError((RollbackException)e, ERROR_MESSAGE_PUT);				
			}
			
			if(e instanceof ParseException) {
				parseErrorLog();				
			}
			
			if(e instanceof IllegalAccessException) {
				message = String.format(ERROR_MESSAGE_PUT, entityClass.getSimpleName());				
				logger.error(message + ERROR_REASON_JSON, e);
				throw new ApplicationJsonException(e, message + ERROR_REASON_JSON,
						Response.Status.BAD_REQUEST);
			}
			
			try {
				if(entity != null && em != null) {
					EntityManagerUtils.persistStatusEntityToDB(entity, uriInfo, em, "PUT", true);
				}
			} catch (RollbackException e1) {
				handleRollbackError(e1, ERROR_MESSAGE_PUT);
				e1.printStackTrace();
				throw e1;
			} catch (ParseException e1) {
				parseErrorLog();
				e1.printStackTrace();
			}			
			e.printStackTrace();
			
			if(e instanceof RollbackException) {
				throw (RollbackException)e;				
			}
							
		}finally {
			if(em != null && em.isOpen()) {
				em.close();
			}
		}

		return JsonResponseWrapper.getResponse(Response.Status.OK, entityClass.getSimpleName() + " updated");
	}
	
	protected Response getUnsupportedOperationResponse() {
		return JsonResponseWrapper.getResponse(Response.Status.BAD_REQUEST, "Unsupported operation");
	}
	
	//cascade default false
	protected Response delete(boolean cascade) {	

		EntityManager em = null;
		T entity = null;
		
		try {
			em = entityManager.getEntityManagerFactory().createEntityManager();
			entity = get();
			
			if (cascade) {
				Object child = ResourcesUtils.getValueForAnnotation(entity, OneToOne.class);
				
				if (null != child ) {
					em.getTransaction().begin();
					
					if (!em.contains(child))
						child = em.merge(child);
					
					em.remove(child);
					
					em.getTransaction().commit();
				}
			}
			
			em.getTransaction().begin();
			
			if (!em.contains(entity))
				entity = em.merge(entity);
			
			em.remove(entity);
			
			em.getTransaction().commit();
			EntityManagerUtils.persistStatusEntityToDB(entity, uriInfo, em, "DELETE", false);
			
		}catch (Exception e) {	
			// rethrow RollbackException
			
			if(e instanceof RollbackException) {				
				handleRollbackError((RollbackException)e, ERROR_MESSAGE_DELETE);
				
			}else if(e instanceof ParseException) {
				parseErrorLog();
			}
			
			try {
				if(entity != null && em != null) {
					EntityManagerUtils.persistStatusEntityToDB(entity, uriInfo, em, "DELETE", true);
				}
			} catch (RollbackException e1) {
				handleRollbackError(e1, ERROR_MESSAGE_DELETE);
				e1.printStackTrace();
				throw e1;
			} catch (ParseException e1) {
				parseErrorLog();
				e1.printStackTrace();
			}			
			e.printStackTrace();
			
			if(e instanceof RollbackException) {
				throw (RollbackException)e;				
			}
		}finally {
			if(em != null && em.isOpen()) {
				em.close();
			}
		}				
		return JsonResponseWrapper.getResponse(Response.Status.OK, entityClass.getSimpleName() + " deleted");
	}
	
	protected Response delete() {
		return delete(false);
	}
	
	@Override
	public void close() throws Exception {
		if (null != entityManager)
			entityManager.close();
	}

	public Logger getLogger() {
		return logger;
	}

	protected UriInfo getUriInfo() {
		return uriInfo;
	}
	
	protected EntityManager getEntityManager() {
		return entityManager;
	}
	
	protected Object getId() {
		return id;
	}
	
	private void parseErrorLog() {
		logger.error(ERROR_REASON_PARSE);
	}
	
	private void handleRollbackError(RollbackException re, String errorMessage) {
		String message = String.format(errorMessage, entityClass.getSimpleName());		
		ApplicationJsonException.handleSQLException(re, message, logger, entityClass);
		logger.error(message + ERROR_REASON_ROLLBACK, re);
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> callNamedStoreProcedure(String nameOfProcedure, HashMap<String, Object> queryParams) {
		
		EntityManager em = null;
		List<Object> result = null;
		try {
			em = entityManager.getEntityManagerFactory().createEntityManager();


			StoredProcedureQuery query = em.createNamedStoredProcedureQuery(nameOfProcedure);	
			
			for(Map.Entry<String, Object> entry : queryParams.entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				
				if(value instanceof Integer) {
					query.setParameter(key , value);
				}
				
				if(value instanceof String) {
					query.setParameter(key , value);
				}
			}
			
			em.getTransaction().begin();
			query.execute();
			result = query.getResultList();
			em.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(em != null && em.isOpen()) {
				em.close();
			}		
		}

		return result;
	}
}
