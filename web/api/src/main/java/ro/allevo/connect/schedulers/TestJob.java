package ro.allevo.connect.schedulers;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import ch.qos.logback.core.net.SyslogOutputStream;
import ro.allevo.fintpws.config.Config;


@ComponentScan
@Component
public class TestJob implements Job {
	
	@Autowired
	HttpServletRequest request;
	
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDetail job = context.getJobDetail();
	    JobDataMap parameters = job.getJobDataMap();
	    JmsTemplate jms = new JmsTemplate((ActiveMQConnectionFactory)parameters.get("emf"));
	   System.out.println(jms);
//	    EntityManager entityManagerConnect = (EntityManager)parameters.get("emf");
	}

}
