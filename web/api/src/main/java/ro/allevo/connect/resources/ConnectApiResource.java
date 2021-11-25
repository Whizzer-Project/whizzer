package ro.allevo.connect.resources;

import javax.inject.Singleton;
import javax.jms.ConnectionFactory;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Repository;

@Path("/connect-api")
@Singleton
@Repository
@ComponentScan
public class ConnectApiResource {
	
	@Context
	private UriInfo uriInfo;
	
	@Autowired
	ApplicationContext context;
	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	@Value("${amqStatement}")
	private String amqStatement;
	
	@Value("${FinTP_Api.url}")
	private String apiUrl;
	
	@Autowired
	private ConnectionFactory connectionFactory;
	
	@PersistenceContext(unitName="fintpCONNECT")
	public EntityManager entityManagerConnect;
	
	@PersistenceContext(unitName="fintpLIST")
	public EntityManager entityManagerList;
	
	@Path("connects")
	public AuthorizationsServersResource getAuthorizationsServers() {
		return new AuthorizationsServersResource(uriInfo, entityManagerConnect);
	}
	
	@Path("scheduler")
	public SchedulerResource getScheduler() {
		return new SchedulerResource();
	}
	
	@Path("jobs")
	public JobsResource getJobs() {
		return new JobsResource(entityManagerConnect, entityManagerList, connectionFactory, amqStatement, apiUrl);
	}
	
	@Path("triggers")
	public TriggersResource getTriggers() {
		return new TriggersResource(entityManagerConnect);
	}
	
	@Path("{bic}")
	public BicResource getBic(@PathParam("bic") String bic) {
		return new BicResource(bic, entityManagerConnect);
	}
	
	@Path("jms")
	public JmsResources getJms() {
		return new JmsResources(context, jmsTemplate, amqStatement);
	}
	
	
}
