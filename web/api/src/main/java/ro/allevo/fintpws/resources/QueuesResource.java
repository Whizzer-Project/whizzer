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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
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

import com.fasterxml.jackson.annotation.JsonIgnore;

//import ro.allevo.fintpws.model.QMovePrivMapEntity;
import ro.allevo.fintpws.model.QueueEntity;
import ro.allevo.fintpws.util.Roles;

/**
 * Resource class implementing /queues path methods and acting as /queues/{name}
 * sub-resource locator to {@link QueueResource}.
 * 
 * @author horia
 * @version $Revision: 1.0 $
 */

public class QueuesResource extends PagedCollection<QueueEntity> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6003485696594342073L;

	/**
	 * Creates a new instance of QueuesResource
	 * 
	 * @param uriInfo
	 *            UriInfo
	 * 
	 * @param entityManagerConfig
	 *            EntityManager
	 * @param entityManagerData
	 *            EntityManager
	 */
	public QueuesResource(UriInfo uriInfo, EntityManager entityManagerConfig) {
		super(uriInfo, 
				entityManagerConfig.createNamedQuery("QueueEntity.findAll", QueueEntity.class), 
				entityManagerConfig.createNamedQuery("QueueEntity.findTotal", Long.class),
				entityManagerConfig,
				QueueEntity.class);
	}

	/**
	 * Returns a queue sub-resource named queueName
	 * 
	 * @param queueName
	 *            String Name of the queue as last element in the path
	 * @return QueueResource The queue sub-resource
	 */
	@Path("{queueId}")
	public QueueResource getQueue(@PathParam("queueId") long queueId) {
		//queueName = Jsoup.clean(queueName, Whitelist.none());
		return new QueueResource(getUriInfo(), getEntityManager(), queueId);
	}
	
	@Path("by-name/{name}")
	public QueueResource getQueue(@PathParam("name") String name) {
		//queueName = Jsoup.clean(queueName, Whitelist.none());
		return new QueueResource(getUriInfo(), getEntityManager(), name);
	}

	/**
	 * GET method : returns an application/json formatted list of queues
	 * 
	 * @return JSONObject The list of queues
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.QUEUES_VIEW})
	@JsonIgnore
	public PagedCollection<QueueEntity> getQueuesAsJson() {
		return this;
	}
	
	@GET
	@Path("access")
	@Produces(MediaType.APPLICATION_JSON)
	@JsonIgnore
	public List<HashMap<String, Object>> getAccessQueuess() {
		List<HashMap<String, Object>> list = new ArrayList<>();
		for (QueueEntity qe : this.getItems()) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", qe.getId());
			map.put("name", qe.getName());
			map.put("label", qe.getLabel());
			map.put("description", qe.getDescription());
			list.add((HashMap<String, Object>) map);
		}
		return list;
	}

	/**
	 * POST method : creates a queue
	 * 
	 * @param jsonEntity
	 *            JSONObject The queue to be created
	 * @return Response The URI of the newly created queue * @throws
	 *         JSONException * @throws JSONException
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.QUEUES_MODIFY})
	public Response postForm(QueueEntity entity) {
		return post(entity);
	}
}
