package ro.allevo.fintpws.config;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.client.RestTemplate;

public class EndpointFilter implements ContainerRequestFilter{
 
	@Autowired
	private Config config;
	
    @Override
	public void filter(ContainerRequestContext containerRequestContext) throws IOException {
    	if(!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
    		try {
			 	OAuth2Authentication authentication = (OAuth2Authentication) SecurityContextHolder.getContext()
	        																	.getAuthentication();
	     	    OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
	     	    try {
	             	HttpHeaders headers = new HttpHeaders();
	             	headers.set("Authorization", details.getTokenValue());
	             	HttpEntity<String> entityReq = new HttpEntity<String>("parameters", headers);
	             	RestTemplate template = new RestTemplate();
				    ResponseEntity<String> respEntity = template
						.exchange(config.getAuthUrl() + "/endpoints/checkEndpoint?" + 
								"endpoint=" + containerRequestContext.getUriInfo().getPath() + "&" + 
								"type=" + containerRequestContext.getMethod(), HttpMethod.GET, entityReq, String.class);
	             	
	             	if(!respEntity.getBody().equals("true")) {
	             		Response response = Response.status(Response.Status.FORBIDDEN).build();
	             		containerRequestContext.abortWith(response);
	             	}
	         	}catch(Exception e) {
	         		e.printStackTrace();
	         		Response response = Response.status(Response.Status.FORBIDDEN).build();
             		containerRequestContext.abortWith(response);
	         	}
	        } catch (Exception e) {
	            e.printStackTrace();
	            Response response = Response.status(Response.Status.FORBIDDEN).build();
         		containerRequestContext.abortWith(response);
	        }
    	}
	}
}