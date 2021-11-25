package ro.allevo.fintpws.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class Config {

	@Value("${auth.url}")
	private String authURL;
	
	@Value("${amqPaymentIn}")
	private String amqPaymentIn;
	
	@Value("${amqStatement}")
	private String amqStatement;
	

	@Bean
	public RestTemplate restTemplate() {
	    return new RestTemplate();
	}
	
	public String getAmqPaymentIn() {
		return amqPaymentIn;
	}

	public void setAmqPaymentIn(String amqPaymentIn) {
		this.amqPaymentIn = amqPaymentIn;
	}

	public String getAmqStatement() {
		return amqStatement;
	}

	public String getAuthUrl() {
    	return authURL;
    }

}