package ro.allevo.tracker.resources;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceException;
import javax.persistence.StoredProcedureQuery;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ro.allevo.fintpws.exceptions.ApplicationJsonException;

public abstract class TraceResource {

	static final String ERROR_MESSAGE_TRACE = "Error starting trace operation. Another trace operation is already in progress.";
	
	private final EntityManager entityManager;
	
	private final Logger logger = LogManager.getLogger(getClass().getName());
	
	public TraceResource(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	protected EntityManager getEntityManager() {
		return entityManager;
	}
	
	public EntityManager startTrace(Integer userId) {
		try {
			EntityManager em = entityManager.getEntityManagerFactory().createEntityManager();
			em.getTransaction().begin();
			//start trace
			StoredProcedureQuery procedure = em.createStoredProcedureQuery("fintrack.starttrace")
					.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN)
					.setParameter(1, userId);
			procedure.executeUpdate();
			em.getTransaction().commit();
			
			return em;
		}
		catch (PersistenceException pe) {
			if (pe.getMessage().contains("Tracking already running"))
				throw new ApplicationJsonException(
						ERROR_MESSAGE_TRACE,
						Response.Status.CONFLICT);
			else
				ApplicationJsonException.handleSQLException(pe, ERROR_MESSAGE_TRACE, logger);
		}
		
		return null;
	}
}
