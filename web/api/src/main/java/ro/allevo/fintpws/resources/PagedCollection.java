/*
* FinTP - Financial Transactions Processing Application
* Copyright (C) 2013 Business Information Systems (Allevo) S.R.L.
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>
* or contact Allevo at : 031281 Bucuresti, 23C Calea Vitan, Romania,
* phone +40212554577, office@allevo.ro <mailto:office@allevo.ro>, www.allevo.ro.
*/

package ro.allevo.fintpws.resources;

import java.io.Serializable;
import java.net.URI;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.RollbackException;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ro.allevo.fintpws.exceptions.ApplicationJsonException;
import ro.allevo.fintpws.model.BaseEntity;
import ro.allevo.fintpws.util.EntityManagerUtils;
import ro.allevo.fintpws.util.JpaFilter;
import ro.allevo.fintpws.util.JsonResponseWrapper;
import ro.allevo.fintpws.util.ResourcesUtils;
import ro.allevo.fintpws.util.URIFilter;
import ro.allevo.fintpws.util.annotations.URLId;
import ro.allevo.fintpws.util.enums.QueryProvider;

/**
 * @author horia
 * @version $Revision: 1.0 $
 */
public class PagedCollection<T extends BaseEntity> implements AutoCloseable, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Field logger.
	 */
	private final Logger logger;

	static String ERROR_MESSAGE_POST = "Error creating %s";

	static String ERROR_MESSAGE_DELETE = "Error deleting %s for id %s : ";

	static final String ERROR_REASON_ROLLBACK = "rollback";

	static final String ERROR_REASON_JSON = "json";

	static String ERROR_BAD_REQUEST_EMPTY_LIST = "Please provide a list of [%s]";

	static final String ERROR_UNSUPPORTED_OPERATION_QUERY_EXCEPTION = "ItemsQuery and TotalQuery cannot be null";

	static final String ERROR_UNSUPPORTED_OPERATION_ENTITY_EXCEPTION = "EntityManager and EntityClass cannot be null";
	
	static final String ERROR_REASON_PARSE = "Could not parse when try to create Entity";

	/**
	 * Field PARAM_PAGE. (value is ""page"")
	 */
	static final String PARAM_PAGE = "page";
	/**
	 * Field PARAM_PAGE_SIZE. (value is ""filter"")
	 */
	static final String PARAM_PAGE_SIZE = "page_size";
	/**
	 * Field FILTER_TOTAL. (value is ""t"")
	 */
	static final String PARAM_TOTAL = "total";

	/**
	 * Field DEFAULT_PAGE. (value is 1)
	 */
	private static final int DEFAULT_PAGE = 1;
	/**
	 * Field DEFAULT_PAGE_SIZE. (value is 100)
	 */
	private static final int DEFAULT_PAGE_SIZE = 100;
	/**
	 * Field MAX_PAGE_SIZE. (value is 100)
	 */
	private static final int MAX_PAGE_SIZE = 100;

	private EntityManager entityManager = null;
	protected final Class<T> entityClass;

	private QueryProvider queryProvider = null;

	@JsonIgnore
	private HttpServletRequest request = null;

	private int page;
	private int pageSize;
	private List<T> items = null;
	private List<Object> itemsObject = null;
	private Integer total;

	// actual uri info provided by parent resource
	private UriInfo uriInfo;
	private TypedQuery<T> itemsQuery;
	private TypedQuery<Long> totalQuery;

	private boolean needsTotal;
	private boolean hasMore;
	private boolean hasItems;

	private boolean pagingDisabled = false;

	private URIFilter[] extraFilters;
	private URIFilter[] apiFilters;

	protected void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	public PagedCollection(UriInfo uriInfo, EntityManager entityManager, Class<T> entityClass) {
		this.entityManager = entityManager;
		this.entityClass = entityClass;
		this.uriInfo = uriInfo;
		logger = LogManager.getLogger(getClass().getName());
	}
	
	public PagedCollection(UriInfo uriInfo, QueryProvider queryProvider, URIFilter... extraFilters) {
		this(uriInfo, null, null, null, null);
		this.queryProvider = queryProvider;

		this.extraFilters = extraFilters;

		if (null == this.extraFilters)
			this.extraFilters = new URIFilter[] {};
	}

	public PagedCollection(UriInfo uriInfo, TypedQuery<T> itemsQuery, TypedQuery<Long> totalQuery,
			EntityManager entityManager, Class<T> entityClass, URIFilter... apiFilters) {
		this.apiFilters = apiFilters;
		this.entityManager = entityManager;
		this.entityClass = entityClass;
		this.itemsQuery = itemsQuery;
		this.totalQuery = totalQuery;

		this.uriInfo = uriInfo;
		this.total = 0;
		this.page = DEFAULT_PAGE;
		this.pageSize = DEFAULT_PAGE_SIZE;
		this.hasMore = false;
		this.needsTotal = false;
		this.hasItems = false;

		logger = LogManager.getLogger(getClass().getName());

		String entityName = "NULL";

		if (null != entityClass)
			entityName = entityClass.getSimpleName();

		// format custom messages
		ERROR_MESSAGE_POST = String.format(ERROR_MESSAGE_POST, entityName);
		ERROR_BAD_REQUEST_EMPTY_LIST = String.format(ERROR_BAD_REQUEST_EMPTY_LIST, entityName);
		ERROR_MESSAGE_DELETE = String.format(ERROR_MESSAGE_DELETE, entityName, "%s");
	}
	public PagedCollection(UriInfo uriInfo, TypedQuery<T> itemsQuery, TypedQuery<Long> totalQuery,
			EntityManager entityManager, Class<T> entityClass) {
		this.entityManager = entityManager;
		this.entityClass = entityClass;
		this.itemsQuery = itemsQuery;
		this.totalQuery = totalQuery;

		this.uriInfo = uriInfo;
		this.total = 0;
		this.page = DEFAULT_PAGE;
		this.pageSize = DEFAULT_PAGE_SIZE;
		this.hasMore = false;
		this.needsTotal = false;
		this.hasItems = false;

		logger = LogManager.getLogger(getClass().getName());

		String entityName = "NULL";

		if (null != entityClass)
			entityName = entityClass.getSimpleName();

		// format custom messages
		ERROR_MESSAGE_POST = String.format(ERROR_MESSAGE_POST, entityName);
		ERROR_BAD_REQUEST_EMPTY_LIST = String.format(ERROR_BAD_REQUEST_EMPTY_LIST, entityName);
		ERROR_MESSAGE_DELETE = String.format(ERROR_MESSAGE_DELETE, entityName, "%s");
	}

	/**
	 * Method getPage. Sanitizes the input query string parameters [page] and
	 * [page_size] and retrieves the requested page of items from the database
	 * page_size is limited to 100 If page is invalid ( not a number, <1 ), the
	 * first page is returned If page_size is invalid ( not a number, <0, >100 ),
	 * the page size is set to 100
	 */
	@JsonIgnore
	private void getPage() {
		MultivaluedMap<String, String> params = null;

		hasItems = true;

		try {
			params = uriInfo.getQueryParameters();
		} catch (Exception e) {
			params = new MultivaluedHashMap<>();
			e.printStackTrace();
		}

		// get page
		page = DEFAULT_PAGE;
		if (params.containsKey(PARAM_PAGE)) {
			try {
				page = Integer.parseInt(params.getFirst(PARAM_PAGE));
			} catch (NumberFormatException nfe) {
				// just ignore garbage, return default page
			}
		}
		// check boundaries
		if (page < 1) {
			page = DEFAULT_PAGE;
		}

		// get page size
		pageSize = DEFAULT_PAGE_SIZE;
		if (params.containsKey(PARAM_PAGE_SIZE)) {
			try {
				pageSize = Integer.parseInt(params.getFirst(PARAM_PAGE_SIZE));
			} catch (NumberFormatException nfe) {
				// just ignore garbage, return default page
			}
		}

		// check boundaries
		if ((pageSize < 0) || (pageSize > MAX_PAGE_SIZE)) {
			pageSize = DEFAULT_PAGE_SIZE;
		}

		// try to replace itemsQuery with filter
		if (entityManager != null && entityClass != null) {
			JpaFilter<T> jpaFilter = new JpaFilter<T>(uriInfo, entityManager, entityClass, apiFilters);
			TypedQuery<T> newQuery = jpaFilter.createQuery();
			
			if (null != newQuery) {
				itemsQuery = newQuery;
				totalQuery = jpaFilter.getTotalQuery();
			}
		}

		Query providedQuery = null;
		Query providedTotalQuery = null;

		if (null == itemsQuery && null != queryProvider) {
			// set entitymanager in child
			providedQuery = queryProvider.getItemsQuery(request, uriInfo, extraFilters);
			providedTotalQuery = queryProvider.getTotalQuery(request, uriInfo, extraFilters);
		}

		if (null == itemsQuery && null == providedQuery)
			throw new UnsupportedOperationException(ERROR_UNSUPPORTED_OPERATION_QUERY_EXCEPTION);

		// request +1 item and set has_more metadata if it is returned
		if (!pagingDisabled) {

			if (null != itemsQuery) {
				itemsQuery.setFirstResult((page - 1) * pageSize);
				itemsQuery.setMaxResults(pageSize + 1);
			} else if (null != providedQuery) {
				providedQuery.setFirstResult((page - 1) * pageSize);
				providedQuery.setMaxResults(pageSize + 1);
			}
		}

		hasMore = false;

		int itemsSize = 0;

		// execute the query
		if (null != itemsQuery) {
			items = itemsQuery.getResultList();
			itemsSize = items.size();
		} else if (null != providedQuery) {
			itemsObject = providedQuery.getResultList();
			itemsSize = itemsObject.size();
		}

		if (itemsSize == pageSize + 1) {
			// remove the extra item
			if (null != items)
				items.remove(pageSize);
			else if (null != itemsObject)
				itemsObject.remove(pageSize);

			hasMore = true;
		}

		// look for a fiter to request total number of items
		needsTotal = false;
		if (params.containsKey(PARAM_TOTAL)) {
			needsTotal = true;

			// optimization ( if we're on the first page and has_more is false,
			// return the count from the selection )
			if ((page == DEFAULT_PAGE) && (!hasMore))
				total = itemsSize;
			else if (null != totalQuery)
				total = totalQuery.getSingleResult().intValue();
			else if (null != providedTotalQuery)
				total = ((Long) providedTotalQuery.getSingleResult()).intValue();
		}

	}

	/**
	 * Method getItems.
	 * 
	 * @return List<?>
	 */
	public List<T> getItems() {
		if (!hasItems)
			getPage();

		return items;
	}

	protected List<Object> getItemsObjects() {
		getItems();

		return itemsObject;
	}

	/**
	 * Method has_more
	 * 
	 * @return boolean
	 */
	public boolean getHasMore() {
		if (!hasItems)
			getPage();

		return hasMore;
	}

	public Integer getTotal() {
		if (!hasItems)
			getPage();

		if (needsTotal)
			return total;

		return null;
	}

	/**
	 * Method getUriInfo.
	 * 
	 * @return UriInfo
	 */
	@JsonIgnore
	public UriInfo getUriInfo() {
		return uriInfo;
	}

	@JsonIgnore
	protected Logger getLogger() {
		return logger;
	}

	@Override
	public void close() throws Exception {
		if (null != entityManager)
			entityManager.close();
	}

	@JsonIgnore
	protected EntityManager getEntityManager() {
		return entityManager;
	}

	protected void disablePaging() {
		pagingDisabled = true;
	}

	public List<T> asList() {
		if (null == entityClass)
			throw new UnsupportedOperationException(ERROR_UNSUPPORTED_OPERATION_ENTITY_EXCEPTION);

		List<T> list = getItems();

		return list;
	}	

	protected Response post(BaseEntity entity, boolean fetchNewID) {
		if (null == entityManager)
			throw new UnsupportedOperationException(ERROR_UNSUPPORTED_OPERATION_ENTITY_EXCEPTION);

		try {
			EntityManagerUtils.persistEntitiesToDB(entity, uriInfo, entityManager, "POST");
			
		}catch (RollbackException re) {
			ApplicationJsonException.handleSQLException(re, ERROR_MESSAGE_POST, logger, entityClass);

			// log and rethrow the original error
			logger.error(ERROR_MESSAGE_POST + ERROR_REASON_ROLLBACK, re);
			throw re;
		}catch (ParseException pe) {
			parseErrorLog();
		} 		
		
		URI uri;

		if (fetchNewID) {
			// get id
			String id = ResourcesUtils.getValueForAnnotation(entity, URLId.class) + "";
			// path to the new entity
			uri = uriInfo.getAbsolutePathBuilder().path(id).build();
		} else {
			uri = uriInfo.getAbsolutePathBuilder().build();
		}

		return JsonResponseWrapper.getResponse(Response.Status.CREATED, uri);
	}		

	protected Response post(T entity) {
		return post(entity, true);
	}

	protected Response post(List<T> entities) {
		if (null == entityManager)
			throw new UnsupportedOperationException(ERROR_UNSUPPORTED_OPERATION_ENTITY_EXCEPTION);

		if (entities.size() == 0)
			return JsonResponseWrapper.getResponse(Response.Status.BAD_REQUEST, ERROR_BAD_REQUEST_EMPTY_LIST);

		EntityManager eml = null;
		
		try {
			// perform update
			eml = entityManager.getEntityManagerFactory().createEntityManager();
			eml.getTransaction().begin();

			for (T entity : entities)
				eml.persist(entity);

			eml.getTransaction().commit();
			
			EntityManagerUtils.persistStatusEntityToDBFromList(entities, uriInfo, eml, "POST", false);

		} catch (RollbackException re) {
			ApplicationJsonException.handleSQLException(re, ERROR_MESSAGE_POST, logger, entityClass);

			// log and rethrow the original error
			logger.error(ERROR_MESSAGE_POST + ERROR_REASON_ROLLBACK, re);
			throw re;
		} catch (ParseException e) {
			parseErrorLog();
		}finally {
			if(eml != null && eml.isOpen()) {
				eml.close();
			}
		}

		URI uri = uriInfo.getAbsolutePathBuilder().build();
		return JsonResponseWrapper.getResponse(Response.Status.CREATED, uri);
	}

	protected Response delete(Object id) {
		if (null == entityManager || null == entityClass)
			throw new UnsupportedOperationException(ERROR_UNSUPPORTED_OPERATION_ENTITY_EXCEPTION);

		EntityManager em = null;
		
		try {
			em = entityManager.getEntityManagerFactory().createEntityManager();
			em.getTransaction().begin();

			Query q = em.createNamedQuery(entityClass.getSimpleName() + ".deleteAllForId").setParameter("id", id);
			q.executeUpdate();

			em.getTransaction().commit();
			
			EntityManagerUtils.persistStatusEntityToDB(em.find(entityClass, id), uriInfo, em, "DELETE", false);

		} catch (RollbackException re) {
			String message = String.format(ERROR_MESSAGE_DELETE, id);

			ApplicationJsonException.handleSQLException(re, message, logger, entityClass);

			// log and rethrow the original error
			logger.error(message + ERROR_REASON_ROLLBACK, re);
			throw re;
		} catch (ParseException e) {
			parseErrorLog();
		}finally {
			if(em != null && em.isOpen()) {
				em.close();
			}
		}

		return JsonResponseWrapper.getResponse(Response.Status.OK, entityClass.getName() + " deleted");
	}
	
	private void parseErrorLog() {
		logger.error(ERROR_REASON_PARSE);
	}
	
	protected Response delete(Map<String, Object> parameters) {
		if (null == entityManager || null == entityClass)
			throw new UnsupportedOperationException(ERROR_UNSUPPORTED_OPERATION_ENTITY_EXCEPTION);
		EntityManager em = null;
		Integer afectedRow = 0;
		try {
			em = entityManager.getEntityManagerFactory().createEntityManager();
			em.getTransaction().begin();

			Query q = em.createNamedQuery(entityClass.getSimpleName() + ".deleteAllForIds");
			for (String name : parameters.keySet())  { 
	            q.setParameter(name, parameters.get(name));
	        }
			List<T> entities = this.asList();
			afectedRow = q.executeUpdate();

			em.getTransaction().commit();
			EntityManagerUtils.persistStatusEntityToDBFromList(entities, uriInfo, em, "DELETE", false);
		} catch (RollbackException re) {
			String message = String.format(ERROR_MESSAGE_DELETE, parameters.toString());

			ApplicationJsonException.handleSQLException(re, message, logger, entityClass);

			// log and rethrow the original error
			logger.error(message + ERROR_REASON_ROLLBACK, re);
			throw re;
		}catch (ParseException e) {
			parseErrorLog();
		}finally {
			if(em != null && em.isOpen()) {
				em.close();
			}
		}
		if (afectedRow > 0) {
			return JsonResponseWrapper.getResponse(Response.Status.NO_CONTENT, entityClass.getName() + " deleted");
		}
		return JsonResponseWrapper.getResponse(Response.Status.OK, entityClass.getName() + " deleted");
	}
}
