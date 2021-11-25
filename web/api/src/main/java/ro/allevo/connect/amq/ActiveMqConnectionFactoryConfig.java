package ro.allevo.connect.amq;

import javax.jms.Destination;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

@Configuration
@EnableJms
@ConditionalOnProperty(prefix = "activate", name = "activemq", havingValue = "true")
public class ActiveMqConnectionFactoryConfig {

	@Value("${brokerURL}")
	private String brokerURL;
	
	@Value("${amqPaymentIn}")
	private String amqPaymentIn;
	
	@Bean
	public ActiveMQConnectionFactory connectionFatory() {
		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
		activeMQConnectionFactory.setBrokerURL(brokerURL);
		RedeliveryPolicy policy = activeMQConnectionFactory.getRedeliveryPolicy();
       
        policy.setInitialRedeliveryDelay(2000);
        policy.setBackOffMultiplier(2);
        policy.setUseExponentialBackOff(true);
        policy.setMaximumRedeliveries(2);
        policy.setMaximumRedeliveryDelay(90000);
        
		return activeMQConnectionFactory;
	}

	@Bean
	public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		factory.setConnectionFactory(connectionFatory());
		return factory;
	}

	@Bean
	public Destination amqPaymentIn() {
		return new ActiveMQQueue(amqPaymentIn);
	}
	
	@Bean
	public JmsTemplate jmsTemplate(){
	    return new JmsTemplate(connectionFatory());
	}

}

/*package ro.allevo.connect.amq;

import javax.jms.Destination;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.destination.JndiDestinationResolver;

@Configuration
@EnableJms
public class ActiveMqConnectionFactoryConfig {

//	@Value("${brokerURL}")
//	private String brokerURL;
	
	@Value("${amqPaymentIn}")
	private String amqPaymentIn;
	
	//@Value("${amqPaymentOut}")
	//private String amqPaymentOut;
	
	@Bean
	public ActiveMQConnectionFactory connectionFatory() {
		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
		//activeMQConnectionFactory.setBrokerURL(brokerURL);
		RedeliveryPolicy policy = activeMQConnectionFactory.getRedeliveryPolicy();
       
        policy.setInitialRedeliveryDelay(2000);
        policy.setBackOffMultiplier(2);
        policy.setUseExponentialBackOff(true);
        policy.setMaximumRedeliveries(2);
       policy.setMaximumRedeliveryDelay(90000);
        
		return activeMQConnectionFactory;
	}

    @Bean
	public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		factory.setConnectionFactory(connectionFatory());
		return factory;
	}

	@Bean
	public Destination amqPaymentIn() {
		return new ActiveMQQueue(amqPaymentIn);
	}
	
	@Bean
	public JndiDestinationResolver jndiDestinationResolver() {
	  return new JndiDestinationResolver();
	}

	@Bean
	public JmsTemplate jmsTemplate() {
	  JmsTemplate jmsTemplate = new JmsTemplate();
	  jmsTemplate.setDestinationResolver(jndiDestinationResolver());
	  jmsTemplate.setConnectionFactory(connectionFatory());
	  return jmsTemplate;
	}

}*/
