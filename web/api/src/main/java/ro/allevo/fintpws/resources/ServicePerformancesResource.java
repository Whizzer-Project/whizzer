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

import ro.allevo.fintpws.model.ServicePerformanceEntity;

public class ServicePerformancesResource extends PagedCollection<ServicePerformanceEntity> {

	public ServicePerformancesResource(UriInfo uriInfo, EntityManager entityManagerData) {
		super(uriInfo,
				entityManagerData.createNamedQuery("ServicePerformanceEntity.findAll", ServicePerformanceEntity.class),
				entityManagerData.createNamedQuery("ServicePerformanceEntity.findTotal", Long.class),
				entityManagerData,
				ServicePerformanceEntity.class
				);
	}

	@Path("{id}")
	public ServicePerformanceResource getServicePerformancesResource(
			@PathParam("id") String serviceId) {
		serviceId = Jsoup.clean(serviceId, Whitelist.none());
		return new ServicePerformanceResource(getUriInfo(), getEntityManager(), serviceId);
	}

	/**
	 * GET method : returns an application/json formatted list of service
	 * performances
	 * 
	 * @return JSONObject The list of service performances
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@JsonIgnore
	public PagedCollection<ServicePerformanceEntity> getServiceMapsAsJson() {
		return this;
	}

	/**
	 * POST method : creates a service performance
	 * 
	 * @param jsonEntity
	 *            JSONObject
	 * @return Response
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postForm(ServicePerformanceEntity entity) {
		return post(entity);
	}
}
