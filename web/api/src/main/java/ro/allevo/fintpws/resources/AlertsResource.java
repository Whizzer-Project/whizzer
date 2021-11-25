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

import org.codehaus.jettison.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ro.allevo.fintpws.model.AlertEntity;

/**
 * Resource class implementing /alerts path methods and acting as /alerts/{name}
 * sub-resource locator to {@link AlertResource}.
 * 
 * @author costi
 * @version $Revision: 1.0 $
 */
public class AlertsResource extends PagedCollection<AlertEntity> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5913788711114291124L;

	/**
	 * Creates a new instance of AlertsResource
	 * 
	 * @param uriInfo
	 *            UriInfo
	 * 
	 * @param entityManagerConfig
	 *            EntityManager
	 * @param alertsEntity
	 *            AlertEntity
	 * @param queueEntity QueueEntity
	 */
	public AlertsResource(UriInfo uriInfo, EntityManager entityManagerConfig) {
		super(uriInfo,
				entityManagerConfig.createNamedQuery("AlertEntity.findAll", AlertEntity.class),
				entityManagerConfig.createNamedQuery("AlertEntity.findTotal", Long.class),
				entityManagerConfig,
				AlertEntity.class
				);
	}
	
	/*public AlertsResource(UriInfo uriInfo, EntityManager entityManagerConfig, QueueEntity queueEntity) {
		super(uriInfo,
				entityManagerConfig.createNamedQuery("AlertEntity.findAllQueue", EntryQueueEntity.class)
					.setParameter("queueid", queueEntity.getId()),
				entityManagerConfig.createNamedQuery("AlertEntity.findTotalQueue", Long.class)
					.setParameter("queueid", queueEntity.getId()),
				entityManagerConfig,
				AlertEntity.class
				);
	}*/

	/**
	 * Returns a alert sub-resource named alertName
	 * 
	 * @param alertName
	 *            String name of the alert as last element in the path
	 * @return AlertResource The alert sub-resource
	 */
	@Path("{name}")
	public AlertResource getAlert(@PathParam("name") String alertName) {
		alertName = Jsoup.clean(alertName, Whitelist.none());
		return new AlertResource(getUriInfo(), getEntityManager(), alertName);
	}

	/**
	 * GET method : returns an application/json formatted list of alerts
	 * 
	 * @return JSONObject The list of alerts
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@JsonIgnore
	public PagedCollection<AlertEntity> getAlertsAsJson() {
		return this;
	}

	/**
	 * POST method : creates a alert
	 * @param jsonEntity
	 *            JSONObject The alert to be created
	 * @return Response The URI of the newly created alert
	 * @throws JSONException
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postForm(AlertEntity entity) {
		return post(entity);
	}
}
