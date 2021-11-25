///*
//* FinTP - Financial Transactions Processing Application
//* Copyright (C) 2013 Business Information Systems (Allevo) S.R.L.
//*
//* This program is free software: you can redistribute it and/or modify
//* it under the terms of the GNU General Public License as published by
//* the Free Software Foundation, either version 3 of the License, or
//* (at your option) any later version.
//*
//* This program is distributed in the hope that it will be useful,
//* but WITHOUT ANY WARRANTY; without even the implied warranty of
//* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
//* GNU General Public License for more details.
//*
//* You should have received a copy of the GNU General Public License
//* along with this program. If not, see <http://www.gnu.org/licenses/>
//* or contact Allevo at : 031281 Bucuresti, 23C Calea Vitan, Romania,
//* phone +40212554577, office@allevo.ro <mailto:office@allevo.ro>, www.allevo.ro.
//*/
//
//package ro.allevo.fintpws.resources;
//
//import java.net.URI;
//
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceException;
//import javax.persistence.StoredProcedureQuery;
//import javax.ws.rs.Consumes;
//import javax.ws.rs.GET;
//import javax.ws.rs.POST;
//import javax.ws.rs.Path;
//import javax.ws.rs.PathParam;
//import javax.ws.rs.Produces;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//import javax.ws.rs.core.Response.Status;
//import javax.ws.rs.core.UriInfo;
//
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.codehaus.jettison.json.JSONException;
//import org.codehaus.jettison.json.JSONObject;
//import org.jsoup.Jsoup;
//import org.jsoup.safety.Whitelist;
//
//import ro.allevo.fintpws.exceptions.ApplicationJsonException;
//import ro.allevo.fintpws.model.BatchRequestEntity;
//import ro.allevo.fintpws.util.JsonResponseWrapper;
///**
// * Resource class implementing /batchrequests path methods
// * @author remus
// *
// */
//public class BatchRequestsResource extends PagedCollection<BatchRequestEntity> {
//	
//	
//	
//	/**
//	 * Field logger.
//	 */
//	private static Logger logger = LogManager.getLogger(BatchRequestsResource.class
//			.getName());
//
//	/**
//	 * Field ERROR_MESSAGE_GET_BATCH_REQUESTS. (value is ""Error returning batch requests : "")
//	 */
//	static final String ERROR_MESSAGE_GET_BATCH_REQUESTS = "Error returning batch requests : ";
//	/**
//	 * Field ERROR_MESSAGE_POST_BATCH_REQUESTS. (value is ""Error creating batch request : "")
//	 */
//	static final String ERROR_MESSAGE_POST_BATCH_REQUESTS = "Error creating batch request : ";
//
//	/**
//	 * Field ERROR_REASON_JSON. (value is ""json"")
//	 */
//	static final String ERROR_REASON_JSON = "json";
//	/**
//	 * Field ERROR_REASON_NUMBER_FORMAT. (value is ""number format"")
//	 */
//	static final String ERROR_REASON_NUMBER_FORMAT = "number format";
//	/**
//	 * Field ERROR_REASON_ROLLBACK_EXCEPTION. (value is ""conflict"")
//	 */
//	static final String ERROR_REASON_CONFLICT = "conflict";
//
//	/**
//	 * Field ERROR_REASON_ROLLBACK. (value is ""rollback"")
//	 */
//	static final String ERROR_REASON_ROLLBACK = "rollback";
//	/**
//	 * Field MESSAGE_POST_BATCH_REUQESTS. (value is ""Accepted batch request"")
//	 */
//	static final String MESSAGE_POST_BATCH_REUQESTS= "Accepted batch request";
//	
//	/**
//	 * Creates a new instance of BatchRequestsResource
//	 * 
//	 * @param uriInfo
//	 *            UriInfo
//	 * @param entityManager
//	 *            EntityManager
//	 */
//	public BatchRequestsResource(UriInfo uriInfo, EntityManager entityManagerData) {
//		super(uriInfo,
//				entityManagerData.createNamedQuery("BatchRequestEntity.findAllGroupKeys", String.class),
//				null,
//				entityManagerData,
//				BatchRequestEntity.class
//				);
//	}
//	
//	public BatchRequestsResource(UriInfo uriInfo, EntityManager entityManagerData, String user) {
//		super(uriInfo,
//				entityManagerData.createNamedQuery("BatchRequestEntity.findGroupKeysByUser", String.class)
//					.setParameter("user", user),
//				null,
//				entityManagerData,
//				BatchRequestEntity.class
//				);
//	}
//	
//	public static BatchRequestsResource create(UriInfo uriInfo, EntityManager entityManagerData) {
//		if(!uriInfo.getQueryParameters().containsKey("user"))
//			return new BatchRequestsResource(uriInfo, entityManagerData, uriInfo.getQueryParameters().getFirst("user"));
//		
//		return new BatchRequestsResource(uriInfo, entityManagerData);
//	}
//
//	/**
//	 * Sub-resource locator for /batchrequests/{groupkey} resource
//	 * @param groupKey The groupkey path parameter
//	 * @return BatchRequestResource
//	 */
//	@Path("{groupkey}")
//	public BatchRequestResource getBatchRequest(@PathParam("groupkey") String groupKey){
//		groupKey = Jsoup.clean(groupKey, Whitelist.none());
//		return new BatchRequestResource(getUriInfo(), getEntityManager(), groupKey);
//	}
//	
//	/**
//	 * GET method : returns an application/json formatted list of batches
//	 * 
//	 * @return JSONObject The list of group keys
//	 */
//	@GET
//	@Produces(MediaType.APPLICATION_JSON)
//	public PagedCollection<BatchRequestEntity> getBatchesAsJson() {
//		return this;
//	}
//	
//	
//	/**
//	 * POST method : creates a batch request
//	 * @param jsonEntity
//	 * 		JSONObject Entity containing arguments necessary for creating a new batch request
//	 * @return Response The URI of the batch request that started to be processed
//	 */
//	@POST
//	@Consumes(MediaType.APPLICATION_JSON)
//	public Response createBatchRequest(JSONObject jsonEntity){
//		EntityManager entityManagerData = getEntityManager();
//		
//		StoredProcedureQuery query = entityManagerData
//				.createNamedStoredProcedureQuery("createBatchRequest");
//		String groupkey = "";
//		try {
//			groupkey = jsonEntity.getString("groupkey");
//			query.setParameter("inqueuename", jsonEntity.getString("queuename"));
//			query.setParameter("inmsgtype", jsonEntity.getString("msgtype"));
//			query.setParameter("ingroupkey", jsonEntity.getString("groupkey"));
//			query.setParameter("intimekey", jsonEntity.getString("timekey"));
//			query.setParameter("infield1val", jsonEntity.optString("field1val", null));
//			query.setParameter("infield2val", jsonEntity.optString("field2val", null));
//			query.setParameter("infield3val", jsonEntity.optString("field3val", null));
//			query.setParameter("infield4val", jsonEntity.optString("field4val", null));
//			query.setParameter("infield5val", jsonEntity.optString("field5val", null));
//			query.setParameter("inusername", jsonEntity.getString("username"));
//			entityManagerData.getTransaction().begin();
//			query.execute();
//			entityManagerData.getTransaction().commit();
//			
//		} catch (JSONException e) {
//			logger.error(ERROR_MESSAGE_POST_BATCH_REQUESTS + ERROR_REASON_JSON,
//					e);
//			throw new ApplicationJsonException(e,
//					ERROR_MESSAGE_POST_BATCH_REQUESTS + ERROR_REASON_JSON,
//					Response.Status.BAD_REQUEST);
//		} 
//		catch (PersistenceException e) {
//			e.printStackTrace();
//			ApplicationJsonException.handleSQLException(e,
//					ERROR_MESSAGE_POST_BATCH_REQUESTS, logger);
//			throw e;
//		}
//		
//		finally {
//			if(null != entityManagerData){
//				entityManagerData.close();
//			}
//		}
//		URI requestUri = getUriInfo().getAbsolutePathBuilder()
//				.path(groupkey).build();
//		return JsonResponseWrapper.getResponse(Status.ACCEPTED, MESSAGE_POST_BATCH_REUQESTS,
//				requestUri, groupkey);
//	}
//}
