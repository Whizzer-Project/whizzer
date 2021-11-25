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

/**
 * 
 */
package ro.allevo.fintpws.resources;

import java.net.HttpURLConnection;
import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import ro.allevo.fintpws.model.RicbicEntity;
import ro.allevo.fintpws.model.StatusEntity;
import ro.allevo.fintpws.util.Invariants;

/**
 * @author alexandru vinogradov
 * @version $Revision: 1.0 $
 */

public class BICResource extends BaseResource<RicbicEntity> {

	/**
	 * Field ERROR_MESSAGE_GET_BIC. (value is ""Error returning bic : "")
	 */
	static final String ERROR_MESSAGE_GET_BIC = "Error returning bic : ";
	/**
	 * Field ERROR_MESSAGE_PUT_BIC. (value is ""Error updating bic : "")
	 */
	static final String ERROR_MESSAGE_PUT_BIC = "Error updating bic : ";
	/**
	 * Field ERROR_MESSAGE_DELETE_BIC. (value is ""Error deleting bic : "")
	 */
	static final String ERROR_MESSAGE_DELETE_BIC = "Error deleting bic : ";
	/**
	 * Field ERROR_MESSAGE_BIC_NOT_FOUND. (value is ""Time limit with name [%s]
	 * not found"")
	 */
	static final String ERROR_MESSAGE_BIC_NOT_FOUND = "Bic registration with ric [%s] not found";
	/**
	 * Field ERROR_REASON_JSON. (value is ""json"")
	 */
	static final String ERROR_REASON_JSON = "json";
	/**
	 * Field ERROR_REASON_NUMBER_FORMAT. (value is ""number format"")
	 */
	static final String ERROR_REASON_NUMBER_FORMAT = "number format";
	/**
	 * Field ERROR_REASON_CONFLICT. (value is ""conflict"")
	 */
	static final String ERROR_REASON_CONFLICT = "conflict";
	/**
	 * Field ERROR_REASON_ROLLBACK. (value is ""rollback"")
	 */
	static final String ERROR_REASON_ROLLBACK = "rollback";
	/**
	 * Field ERROR_REASON_PARSE. (value is ""parse"")
	 */
	static final String ERROR_REASON_PARSE = "parse";

	/**
	 * Field entityManagerData
	 */

	/*private final EntityManager entityManagerData = Persistence
			.createEntityManagerFactory(ApiResource.PERSISTENCE_UNITNAME_DATA)
			.createEntityManager();*/

	//private RicbicEntity ricbicEntity;

	public BICResource(UriInfo uriInfo, EntityManager entityManagerConfig, String ric) {
		super(RicbicEntity.class, uriInfo, entityManagerConfig, ric);
		
		//ricbicEntity = findByRic(entityManagerList, ric);
	}

	/**
	 * 
	 * @param entityManager
	 *            EntityManager
	 * @param ric
	 *            String
	 */

	/*public static RicbicEntity findByRic(EntityManager entityManager, String ric) {

		final TypedQuery<RicbicEntity> query = entityManager.createNamedQuery(
				"RicbicEntity.findByRic", RicbicEntity.class);

		final java.util.List<RicbicEntity> results = query.setParameter("ric",
				ric).getResultList();
		if (!results.isEmpty()) {
			return results.get(0);
		}
		return null;
	}*/

	/**
	 * PUT method: updates the bic
	 * 
	 * @param jsonEntity
	 *            JSONObject the bic holding new values
	 * @return Response
	 * @throws JSONException
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateBic(@DefaultValue("0") @QueryParam("uid") int userid, @Context HttpServletRequest requestContext,
			RicbicEntity entity) throws JSONException {

		/*if (!RolesUtils.hasReadParamsRole()) {
			throw new ApplicationJsonException(new AccessDeniedException(
					"forbidden"), "forbidden", Status.FORBIDDEN.getStatusCode());
		}

		if (!RolesUtils.hasReadParamsRole()) {
			throw new ApplicationJsonException(new AccessDeniedException(
					"forbidden"), "forbidden", Status.FORBIDDEN.getStatusCode());
		}
		*/
		/*if (null == ricbicEntity) {
			logger.error(String.format(ERROR_MESSAGE_BIC_NOT_FOUND, param));
			throw new EntityNotFoundException(String.format(
					ERROR_MESSAGE_BIC_NOT_FOUND, param));
		}*/

		JSONObject auditJson = new JSONObject();
		
		auditJson.put("class", Invariants.configClassEvents.MANAGE).put("userid",
				userid);
		
		Response response = put(entity);
		
		auditJson.put(
				"message",
				"Updated BIC [" + entity.getBic()
						+ "] successfuly!").put("type", "info");
		
		return response;
	}

	/**
	 * DELETE method : deletes the bic
	 * 
	 * @return Response
	 * @throws JSONException
	 */

	@DELETE
	public Response deleteBic(@DefaultValue("0") @QueryParam("uid") int userid,
			@Context HttpServletRequest requestContext) throws JSONException {

		/*if (!RolesUtils.hasReadParamsRole()) {
			throw new ApplicationJsonException(new AccessDeniedException(
					"forbidden"), "forbidden", Status.FORBIDDEN.getStatusCode());
		}

		if (!RolesUtils.hasReadParamsRole()) {
			throw new ApplicationJsonException(new AccessDeniedException(
					"forbidden"), "forbidden", Status.FORBIDDEN.getStatusCode());
		}*/
		
		/*if (null == ricbicEntity) {
			logger.error(String.format(ERROR_MESSAGE_BIC_NOT_FOUND, param));
			throw new EntityNotFoundException(String.format(
					ERROR_MESSAGE_BIC_NOT_FOUND, param));
		}*/
		
		if (userid == 0) {
			throw new WebApplicationException(Response
					.status(HttpURLConnection.HTTP_BAD_REQUEST)
					.entity("Username parameter is mandatory").build());
		}

		StatusEntity audit = new StatusEntity();
//		EventsResource insertEvent = new EventsResource(getUriInfo(), getEntityManager());

		audit.setClasS(Invariants.configClassEvents.MANAGE.toString());
		audit.setUserid(userid);

		Response response = delete();
		
//		insertEvent.postForm(requestContext, audit);
		
		return response;
	}

	/**
	 * GET Method : returns an application/xml or application/json formatted BIC
	 * 
	 * @return XML result of BIC Validation
	 * @throws SQLException
	 * @throws Exception
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public RicbicEntity getBic() {
		return get();
	}
	/*public RicbicEntity getBic() {

		RicbicEntity ie = new RicbicEntity();
		String parameter = getParam();
		List<RicbicEntity> bic;
try{
		StoredProcedureQuery query = entityManagerList
				.createNamedStoredProcedureQuery("getbicinfo");
		query.setParameter("inRIC", parameter);
		query.execute();
		bic = (List<RicbicEntity>) query
				.getOutputParameterValue("outRetCursor");

		if (bic.size() > 0) {
			ie.setBic(bic.get(0).getBic());
			ie.setId(bic.get(0).getId());
			ie.setName(bic.get(0).getName());
			ie.setDetails(bic.get(0).getDetails());
			ie.setRic(bic.get(0).getRic());
		} else {

			StoredProcedureQuery queryric = entityManagerList
					.createNamedStoredProcedureQuery("getriccode");
			queryric.setParameter("inBIC", parameter);
			queryric.execute();
			String ric = (String) queryric.getOutputParameterValue(2);
			StoredProcedureQuery querybic = entityManagerList
					.createNamedStoredProcedureQuery("getbicinfo");
			querybic.setParameter("inRIC", ric);
			querybic.execute();
			bic = (List<RicbicEntity>) querybic.getOutputParameterValue(2);

			if (bic.size() > 0) {
				ie.setBic(bic.get(0).getBic());
				ie.setId(bic.get(0).getId());
				ie.setName(bic.get(0).getName());
				ie.setDetails(bic.get(0).getDetails());
				ie.setRic("-");
			} else {
				ie.setBic("");
				ie.setId(0);
				ie.setName("");
				ie.setDetails("");
				ie.setRic(parameter);
			}
		}
}
finally{
	if (null != entityManagerList) {
		entityManagerList.close();
	}
}
return ie;

	}*/


	/**
	 * Returns the resource formatted as json
	 * 
	 * @param RicbicEntity
	 *            ricbicEntity
	 * @param path
	 *            String
	 * @return JSONObject
	 * @throws JSONException
	 */
	/*public static JSONObject asJson(RicbicEntity ricbicEntity, String path)
			throws JSONException {
		final JSONObject bicAsJson = ApiResource.getMetaResource(path,
				BICResource.class);
		bicAsJson.put("ric", ricbicEntity.getRic())
				.put("bic", ricbicEntity.getBic())
				.put("name", ricbicEntity.getName())
				.put("details", ricbicEntity.getDetails())
				.put("id", ricbicEntity.getId());

		return bicAsJson;

	}*/

}
