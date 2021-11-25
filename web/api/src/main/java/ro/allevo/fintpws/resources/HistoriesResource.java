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
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ro.allevo.fintpws.model.HistoryEntity;

/**
 * @author anda
 * @version $Revision: 1.0 $
 */

public class HistoriesResource extends PagedCollection<HistoryEntity> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7591943990692851106L;

	public HistoriesResource(UriInfo uriInfo, EntityManager entityManagerData) {
		super(uriInfo, 
				entityManagerData.createNamedQuery("HistoryEntity.findAll", HistoryEntity.class), 
				entityManagerData.createNamedQuery("HistoryEntity.findTotal", Long.class),
				entityManagerData,
				HistoryEntity.class);
	}

	
	/**
	 * Returns a history sub-resource with guid
	 * 
	 * @param guid
	 *            String guid of the history as last element in the path
	 * 
	 * @return HistoryResource The history sub-resource
	 */
	@Path("{id}")
	public HistoryResource getHystoriesResource(@PathParam("id") String guid) {
		guid = Jsoup.clean(guid, Whitelist.none());
		return new HistoryResource(getUriInfo(), getEntityManager(), guid);
	}
	
	/**
	 * GET method : returns an application/json formatted list of histories
	 * 
	 * @return JSONObject The list of histories
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@JsonIgnore
	public PagedCollection<HistoryEntity> getHistoriesAsJson() {
		return this;
	}
	
	/**
	 * POST method : creates a history
	 * 
	 * @param jsonEntity
	 *            JSONObject
	 * @return Response
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postForm(HistoryEntity entity) {
		return post(entity);
	}
	
}
