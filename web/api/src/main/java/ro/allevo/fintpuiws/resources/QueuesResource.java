package ro.allevo.fintpuiws.resources;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TypedQuery;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import ro.allevo.fintpuiws.model.GroupHeaderEntity;
import ro.allevo.fintpuiws.model.QueueMessageGroupEntity;
import ro.allevo.fintpuiws.model.QueueMessageGroupsFilterEntity;
import ro.allevo.fintpuiws.model.QueuesCountEntity;
import ro.allevo.fintpuiws.model.QueuesDuplicateEntity;
import ro.allevo.fintpuiws.model.TransactionHeaderEntity;
import ro.allevo.fintpuiws.model.TransactionStateEntity;
import ro.allevo.fintpws.resources.QueueResource;

public class QueuesResource {
	
	private final UriInfo uriInfo;
	
	private final EntityManager entityManager;
	
	public QueuesResource(UriInfo uriInfo, EntityManager entityManager) {
		this.uriInfo = uriInfo;
		this.entityManager = entityManager;
	}
	
	@Path("{name}/user-actions")
	public UserActionMapsResource getUserActionMaps(@PathParam("name") String name) {
		QueueResource queue = new QueueResource(uriInfo, entityManager, name);
		return new UserActionMapsResource(entityManager, queue.getQueue());
	}
	
	@SuppressWarnings("unchecked")
	@GET
	@Path("queues-count")
	@Produces(MediaType.APPLICATION_JSON)
	public List<QueuesCountEntity> getQueuesCount() {
		EntityManager em = entityManager.getEntityManagerFactory().createEntityManager();
		em.getTransaction().begin();
		StoredProcedureQuery procedure = em.createStoredProcedureQuery("findata.getqueuetxno", QueuesCountEntity.class).
				registerStoredProcedureParameter(1, void.class, ParameterMode.REF_CURSOR);
		List<QueuesCountEntity> queuesCount = procedure.getResultList();
		em.getTransaction().commit();
		em.close();
		return queuesCount;
	}
	
	@SuppressWarnings("unchecked")
	@GET
	@Path("queue-duplicates")
	@Produces(MediaType.APPLICATION_JSON)
	public List<QueuesDuplicateEntity> getDuplicateMSGDetails(@QueryParam("id") String id, @QueryParam("queueName") String queueName){
		EntityManager em = entityManager.getEntityManagerFactory().createEntityManager();
		em.getTransaction().begin();
		
		StoredProcedureQuery procedure = em.createStoredProcedureQuery("findata.getduplicatemsgdetails", QueuesDuplicateEntity.class)
				.registerStoredProcedureParameter(1, void.class, ParameterMode.REF_CURSOR)
				.registerStoredProcedureParameter(2, String.class, ParameterMode.IN)
				.registerStoredProcedureParameter(3, Integer.class, ParameterMode.IN)
				.registerStoredProcedureParameter(4, String.class, ParameterMode.IN)
				.setParameter(2, id.trim())
				.setParameter(3, 1)
				.setParameter(4, queueName.trim());
		List<QueuesDuplicateEntity> duplicates = procedure.getResultList();
		em.getTransaction().commit();
		em.close();
		if (null == duplicates) {
			duplicates = new ArrayList<>();
		}
		return duplicates;
	}
	
	@SuppressWarnings("unchecked")
	@GET
	@Path("table-headers")
	@Produces(MediaType.APPLICATION_JSON)
	public List<TransactionHeaderEntity> getTableHeaders(@QueryParam("messageType") String messageType) {
		
		EntityManager em = entityManager.getEntityManagerFactory().createEntityManager();
		em.getTransaction().begin();
		
		StoredProcedureQuery procedure = em.createStoredProcedureQuery("findata.getgroupsheaderformtqueue", TransactionHeaderEntity.class)
				.registerStoredProcedureParameter(1, void.class, ParameterMode.REF_CURSOR)
				.registerStoredProcedureParameter(2, String.class, ParameterMode.IN)
				.registerStoredProcedureParameter(3, String.class, ParameterMode.IN)
				.setParameter(2, messageType)
				.setParameter(3, "T");
		
		List<TransactionHeaderEntity> headers = procedure.getResultList(); 
		em.getTransaction().commit();
		em.close();
		
		return headers;
	}
	
	@SuppressWarnings("unchecked")
	@GET
	@Path("group-headers")
	@Produces(MediaType.APPLICATION_JSON)
	public List<GroupHeaderEntity> getGroupHeaders(@QueryParam("messageType") String messageType) {
		
		EntityManager em = entityManager.getEntityManagerFactory().createEntityManager();
		em.getTransaction().begin();
		
		StoredProcedureQuery procedure = em.createStoredProcedureQuery("findata.getgroupsheaderformtqueue", GroupHeaderEntity.class)
				.registerStoredProcedureParameter(1, void.class, ParameterMode.REF_CURSOR)
				.registerStoredProcedureParameter(2, String.class, ParameterMode.IN)
				.registerStoredProcedureParameter(3, String.class, ParameterMode.IN)
				.setParameter(2, messageType)
				.setParameter(3, "G");
		
		List<GroupHeaderEntity> headers = procedure.getResultList(); 
		em.getTransaction().commit();
		em.close();
		
		return headers;
	}
	
	@SuppressWarnings("rawtypes")
	@GET
	@Path("transactions-in-group")
	@Produces(MediaType.APPLICATION_JSON)
	public List getTransactionsInGroup(@QueryParam("queueName") String queueName,
			@QueryParam("messageType") String messageType,
			@QueryParam("amount") Integer amount,
			@QueryParam("searchValue") String searchValue
			) {
		
		EntityManager em = entityManager.getEntityManagerFactory().createEntityManager();
		em.getTransaction().begin();
		
		StoredProcedureQuery procedure = em.createStoredProcedureQuery("findata.getgroupsformtqueue")
				.registerStoredProcedureParameter(1, void.class, ParameterMode.REF_CURSOR)
				.registerStoredProcedureParameter(2, String.class, ParameterMode.IN)
				.registerStoredProcedureParameter(3, String.class, ParameterMode.IN)
				.registerStoredProcedureParameter(4, Integer.class, ParameterMode.IN)
				.registerStoredProcedureParameter(5, String.class, ParameterMode.IN)
				.setParameter(2, queueName)
				.setParameter(3, messageType)
				.setParameter(4, amount)
				.setParameter(5, searchValue);
		
		List groups = procedure.getResultList();
		em.getTransaction().commit();
		em.close();
		
		return groups;
	}
	
	@GET
	@Path("queue-group-filters")
	@Produces(MediaType.APPLICATION_JSON)
	public List<QueueMessageGroupsFilterEntity> getQueueMessageGroupsFilter() {
		TypedQuery<QueueMessageGroupsFilterEntity> query = entityManager.createNamedQuery("QueueMessageGroupsFilterEntity.findAll",
				QueueMessageGroupsFilterEntity.class);

		return query.getResultList();
	}
	
	@GET
	@Path("queue-group")
	@Produces(MediaType.APPLICATION_JSON)
	public List<QueueMessageGroupEntity> getQueueMessageGroups() {
		TypedQuery<QueueMessageGroupEntity> query = entityManager.createNamedQuery("QueueMessageGroupEntity.findAll",
				QueueMessageGroupEntity.class);

		return query.getResultList();
	}
	
}
