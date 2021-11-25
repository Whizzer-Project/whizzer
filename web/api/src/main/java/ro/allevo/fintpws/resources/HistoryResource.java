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

import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.codehaus.jettison.json.JSONException;

import ro.allevo.fintpws.model.HistoryEntity;

/**
 * @author anda
 * @version $Revision: 1.0 $
 */

public class HistoryResource extends BaseResource<HistoryEntity> {

	/**
	 * Field ERROR_MESSAGE_GET_HISTORY. (value is ""Error returning history : "")
	 */
	static final String ERROR_MESSAGE_GET_HISTORY = "Error returning history : ";
	/**
	 * Field ERROR_MESSAGE_PUT_HISTORY. (value is ""Error updating history : "")
	 */
	static final String ERROR_MESSAGE_PUT_HISTORY = "Error updating history : ";
	/**
	 * Field ERROR_MESSAGE_HISTORY_NOT_FOUND. (value is ""History with
	 * guid [%s] not found"")
	 */
	static final String ERROR_MESSAGE_HISTORY_NOT_FOUND = "Historyn with guid [%s] not found";
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
	 * Constructor for HistoryResourse.
	 * 
	 * @param uriInfo
	 *            UriInfo
	 * @param entityManagerData
	 *            EntityManager
	 * @param guid
	 *            String
	 */
	
	public HistoryResource(UriInfo uriInfo, EntityManager entityManagerData, String guid) {
		super(HistoryEntity.class, uriInfo, entityManagerData, guid);
	}
	
	/**
	 * GET Method : returns an application/json formatted history
	 * 
	 * @return JSONObject the history
	 */

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public HistoryEntity getHistory() {
		return get();
	}
	
	/**
	 * PUT method: updates the history
	 * 
	 * @param jsonEntity
	 *            JSONObject the history holding new values
	 * @return Response
	 * @throws JSONException 
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateHistory(HistoryEntity entity) {
		return put(entity);
	}
	
	/**
	 * DELETE method : deletes the history
	 * 
	 * @return Response
	 */

	@DELETE
	public Response deleteHistory() {
		return delete();
	}	
}
