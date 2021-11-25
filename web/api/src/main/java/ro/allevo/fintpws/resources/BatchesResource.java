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

import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ro.allevo.fintpws.model.BatchJobEntity;

/**
 * Resource class implementing /batchres path methods
 * 
 * @author remus
 *
 */
public class BatchesResource extends PagedCollection<BatchJobEntity> {

	public BatchesResource(UriInfo uriInfo, EntityManager entityManagerData) {
		super(uriInfo, 
				entityManagerData.createNamedQuery("BatchJobEntity.findAll", BatchJobEntity.class),
				entityManagerData.createNamedQuery("BatchJobEntity.findTotal", Long.class),
				entityManagerData,
				BatchJobEntity.class
				);
	}
	
	/**
	 * Returns a batch sub-resource with id
	 * 
	 * @param id
	 *            String id of the batch as last element in the path
	 * 
	 * @return BatchResource The batch sub-resource
	 */
	@Path("{id}")
	public BatchResource getRoutingRuleResource(@PathParam("id") String guid) {
		guid = Jsoup.clean(guid, Whitelist.none());
		return new BatchResource(getUriInfo(), getEntityManager(), guid);
	}
	
	/**
	 * GET method : returns an application/json formatted list of batches
	 * 
	 * @return JSONObject The list of batches
	 */
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@JsonIgnore
	public PagedCollection<BatchJobEntity> getBatchesAsJson() {
		return this;
	}
}
