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

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ro.allevo.fintpws.model.QueueEntity;
import ro.allevo.fintpws.model.ServiceMapEntity;
import ro.allevo.fintpws.util.Roles;

public class ServiceMapsResource extends PagedCollection<ServiceMapEntity> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3926696383455515289L;

	public ServiceMapsResource(UriInfo uriInfo, EntityManager entityManagerConfig) {
		super(uriInfo,
				entityManagerConfig.createNamedQuery("ServiceMapEntity.findAll", ServiceMapEntity.class),
				entityManagerConfig.createNamedQuery("ServiceMapEntity.findTotal", Long.class),
				entityManagerConfig,
				ServiceMapEntity.class
				);
	}
	
	public ServiceMapsResource(UriInfo uriInfo, EntityManager entityManagerConfig, QueueEntity queueEntity) {
		super(uriInfo,
				entityManagerConfig.createNamedQuery("ServiceMapEntity.findAllQueue", ServiceMapEntity.class)
					.setParameter("serviceid", queueEntity.getExitPoint()),
				entityManagerConfig.createNamedQuery("ServiceMapEntity.findTotalQueue", Long.class)
					.setParameter("serviceid", queueEntity.getExitPoint()),
				entityManagerConfig,
				ServiceMapEntity.class
				);
	}

	@Path("{id}")
	public ServiceMapResource getServiceMapsResource(@PathParam("id") long id) {
		//name = Jsoup.clean(name, Whitelist.none());
		return new ServiceMapResource(getUriInfo(), getEntityManager(), id);
	}

	/**
	 * GET method : returns an application/json formatted list of service maps
	 * 
	 * @return JSONObject The list of service maps
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.TRACKER_VIEW, Roles.QUEUES_VIEW})
	@JsonIgnore
	public PagedCollection<ServiceMapEntity> getServiceMapsAsJson() {
		return this;
	}
}
