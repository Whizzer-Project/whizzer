package ro.allevo.fintpuiws.resources;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import ro.allevo.fintpws.model.QueueEntity;
import ro.allevo.fintpuiws.model.UserActionEntity;

public class UserActionMapsResource {
	private EntityManager entityManager;
	
	private QueueEntity queueEntity;
	
	public UserActionMapsResource(EntityManager entityManager, QueueEntity queue) {
		this.entityManager = entityManager;
		this.queueEntity = queue;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<UserActionEntity> getUserActions(@QueryParam("area") String area, 
			@QueryParam("messageType") String messageType) {
		
		String userActionQuery = null;
		
		switch (area) {
		case "selection" :
			userActionQuery = "UserActionMapEntity.findActionsForSelection";
			break;
		case "group" :
			userActionQuery = "UserActionMapEntity.findActionsForGroup";
			break;
		default:
			return new ArrayList<>();
		}
		
		TypedQuery<UserActionEntity> query = entityManager.createNamedQuery(userActionQuery, UserActionEntity.class);
		query = query.setParameter("messageType", messageType)
				.setParameter("queueTypeId", queueEntity.getQueueTypeId());
		
		return query.getResultList();
	}
}
