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
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

//import ro.allevo.fintpws.model.QMovePrivMapEntity;
import ro.allevo.fintpws.model.QueueEntity;
import ro.allevo.fintpws.util.Roles;

/**
 * Resource class implementing /queues/{name} path methods.
 * 
 * @author horia
 * @version $Revision: 1.0 $
 */
public class QueueResource extends BaseResource<QueueEntity> {

	public QueueResource(UriInfo uriInfo, EntityManager entityManagerConfig, long id) {
		super(QueueEntity.class, uriInfo, entityManagerConfig, id);
	}
	
	public QueueResource(UriInfo uriInfo, EntityManager entityManagerConfig, String name) {
		super(QueueEntity.class, uriInfo, entityManagerConfig,
				entityManagerConfig.createNamedQuery("QueueEntity.findByName", QueueEntity.class)
					.setParameter("id", name)
				);
	}
	
	/**
	 * GET method : returns an application/json formatted queue
	 * 
	 * @return JSONObject the queue
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.QUEUES_VIEW})
	public QueueEntity getQueue() {
		return get();
	}
	
	@GET
	@Path(value="transaction")
	@Produces(MediaType.APPLICATION_JSON)
//	@RolesAllowed({Roles.QUEUES_VIEW})
	public QueueEntity getQueuebyTransaction() {
		return get();
	}

	/**
	 * Sub-resource locator for /alerts resource
	 * 
	 * @return alerts
	 */
	/*
	 * @Path("alerts") public AlertsResource getAlerts() { if (null ==
	 * this.queueEntity) { logger.error(String.format(ERROR_MESSAGE_Q_NOT_FOUND,
	 * this.queueName)); throw new EntityNotFoundException(String.format(
	 * ERROR_MESSAGE_Q_NOT_FOUND, this.queueName)); } return new
	 * AlertsResource(uriInfo, entityManagerConfig, null, queueEntity); }
	 */

	/**
	 * Method getMessages.
	 * 
	 * @return MessagesResource
	 */
	@Path("messages")
	public MessagesResource getMessages() {
		//return new MessagesResource(getUriInfo(), entityManagerData, getEntity());
		return new MessagesResource(getUriInfo(), getEntityManager(), get());
	}
	
	/**
	 * Method getMessageTypes.
	 * 
	 * @return MessageTypesResource
	 */
	@Path("message-types")
	public MessageTypesResource getMessageTypes() {
		//return new MessageTypesResource(getUriInfo(), entityManagerData, getEntity());
		return new MessageTypesResource(getUriInfo(), getEntityManager(), get());
	}
	
	@Path("queue-type")
	public QueueTypeResource getQueueType() {
		return new QueueTypeResource(getUriInfo(), getEntityManager(), get().getQueueTypeId());
	}
	
	@Path("service-map")
	public ServiceMapResource getServiceMap() {
		return new ServiceMapResource(getUriInfo(), getEntityManager(), get().getExitPoint());
	}
	
	@Path("routing-rules")
	public RoutingRulesResource getRoutingRules() {
		return new RoutingRulesResource(getUriInfo(), getEntityManager(), get());
	}
	
	@Path("routing-jobs")
	public RoutingJobsResource getRoutingJobs() {
		return new RoutingJobsResource(getUriInfo(), getEntityManager(), get());
	}
	
	/**
	 * PUT method : updates the queue
	 * 
	 * @param jsonEntity
	 *            JSONObject the queue holding new values
	 * @return Response
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.QUEUES_MODIFY})
	public Response updateQueue(QueueEntity entity) {
		return put(entity);
	}

	/**
	 * DELETE method : deletes the queue
	 * 
	 * @return Response
	 */
	@DELETE
	@RolesAllowed({Roles.QUEUES_MODIFY})
	public Response deleteQueue() {
		return delete();
	}

	
	
	/**
	 * Method toString.
	 * 
	 * @return String
	 */
	public String toString() {
		return get().getName();
	}
}
