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

import java.text.ParseException;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.codehaus.jettison.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ro.allevo.fintpws.model.RicbicEntity;
import ro.allevo.fintpws.model.StatusEntity;
import ro.allevo.fintpws.util.Invariants;

/**
 * @author alexandru vinogradov
 * @version $Revision: 1.0 $
 */

public class BICsResource extends PagedCollection<RicbicEntity> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8925573255419847815L;
	/**
	 * Field ERROR_MESSAGE_GET_BICS. (value is ""Error returning BICS : "")
	 */
	static final String ERROR_MESSAGE_GET_BICS = "Error returning BICS : ";
	/**
	 * Field ERROR_MESSAGE_POST_BICS. (value is ""Error creating BICS : "")
	 */
	static final String ERROR_MESSAGE_POST_BICS = "Error creating BICS : ";

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
	
	/*private final EntityManager entityManagerData =
			Persistence
			.createEntityManagerFactory(ApiResource.PERSISTENCE_UNITNAME_DATA)
			.createEntityManager();*/


	/**
	 * 
	 * @param uriInfo
	 *            UriInfo
	 * @param entityManagerData
	 *            EntityManagerData
	 */

	public BICsResource(UriInfo uriInfo, EntityManager entityManagerConfig) {

		super(uriInfo, 
				entityManagerConfig.createNamedQuery("RicbicEntity.findAll", RicbicEntity.class), 
				entityManagerConfig.createNamedQuery("RicbicEntity.findTotal", Long.class),
				entityManagerConfig,
				RicbicEntity.class
				);
	}

	/**
	 * Returns a BIC sub-resource with BIC
	 * 
	 * @param BIC
	 *            String BIC of the BIC as last element in the path
	 * 
	 * @return BICResource The BIC sub-resource
	 */
	@Path("{bic}")
	public BICResource getBICsResource(@PathParam("bic") String bic) {
		bic = Jsoup.clean(bic, Whitelist.none());
		return new BICResource(getUriInfo(), getEntityManager(), bic);
	}

	/**
	 * GET method : returns an application/json formatted list of BICS
	 * 
	 * @return JSONObject The list of BICS
	 */
	@GET
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@JsonIgnore
	public PagedCollection<RicbicEntity> getBICSAsJson() {
		return this;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postForm(@DefaultValue("0") @QueryParam("uid") int userid, @Context HttpServletRequest requestContext, RicbicEntity entity) throws JSONException, ParseException {
		StatusEntity audit = new StatusEntity();
//		EventsResource insertEvent = new EventsResource(getUriInfo(), getEntityManager());
	    
		audit.setClasS(Invariants.configClassEvents.MANAGE.toString());
		audit.setUserid(userid);
					
		Response response = post(entity);
		
		audit.setMessage("New Bic ["+entity.getBic()+"] inserted successfuly!");
		audit.setType("info");
	
//		insertEvent.postForm(requestContext,audit);
		
		return response;
	}

	/**
	 * Returns the resource formatted as json
	 * 
	 * @throws JSONException
	 * @return JSONObject
	 * 
	 */
	/*@SuppressWarnings("unchecked")
	public JSONObject asJson() throws JSONException {
		final JSONObject BICSAsJson = pagedCollection.asJson();

		// fill data
		final JSONArray BICSArray = new JSONArray();
		final List<?> items = pagedCollection.getItems();

		if (items.size() > 0) {
			for (RicbicEntity RicbicEntity : (List<RicbicEntity>) items) {
				BICSArray.put(BICResource.asJson(
						RicbicEntity,
						UriBuilder.fromPath(getUriInfo().getPath())
								.path(RicbicEntity.toString()).build()
								.getPath()));
			}
		}
		BICSAsJson.put("bics", BICSArray);
		return BICSAsJson;
	}*/

}
