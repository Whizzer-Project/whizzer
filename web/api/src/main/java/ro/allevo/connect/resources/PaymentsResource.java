package ro.allevo.connect.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import ro.allevo.connect.model.PaymentEntity;

public class PaymentsResource {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private HttpHeaders httpHeaders;
	private String body = "";

	private String url;
	
	public PaymentsResource(String url, HttpHeaders httpHeaders) {
		this.url = url;
		this.httpHeaders = httpHeaders;
	}

//	@JsonIgnore
//	public PaymentResource getPayment(@PathParam(value="id") String id){
//		return new PaymentResource(this.url, this.httpHeaders);	
//	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response postPayment(@Context UriInfo uriInfo, @PathParam(value = "payment-id") String id, @RequestBody String xmlMessage){
		this.body = xmlMessage;
		ResponseEntity<PaymentEntity> payment = (ResponseEntity<PaymentEntity>) this.getObject(uriInfo, HttpMethod.POST, PaymentEntity.class);
		return Response.status(HttpStatus.CREATED.value()).entity(payment.getBody()).build();
	}

	@POST
	@Path("{id}/authorisations")
	@Produces(MediaType.APPLICATION_JSON)
	@ResponseStatus(HttpStatus.CREATED)
	public Response startAuthorisation(@Context UriInfo uriInfo) {
		this.body = this.httpHeaders.getFirst("name") + "&" + this.httpHeaders.getFirst("passwd");
		this.httpHeaders.remove("name", this.httpHeaders.getFirst("name"));
		this.httpHeaders.remove("passwd", this.httpHeaders.getFirst("passwd"));
		ResponseEntity<String> abc = (ResponseEntity<String>) this.getObject(uriInfo, HttpMethod.POST, String.class);
		return Response.status(HttpStatus.CREATED.value()).entity(abc.getBody()).build();
	}

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity<String> getPaymnet(@Context UriInfo uriInfo) {
		ResponseEntity<String> payment = (ResponseEntity<String>) this.getObject(uriInfo, HttpMethod.GET, String.class);
		return payment;
	}
	
	@GET
	@Path("{id}/status")
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity<String> getStatus(@Context UriInfo uriInfo) {
		ResponseEntity<String> payment = (ResponseEntity<String>) this.getObject(uriInfo, HttpMethod.GET, String.class);
		return payment;
	}

	protected Object getObject(UriInfo uriInfo, HttpMethod method, Class<?> clas) {
		RestTemplate restTemplate = new RestTemplate();
		@SuppressWarnings({ "unchecked", "rawtypes" })
		HttpEntity entity = new HttpEntity(this.body, this.httpHeaders);
		try {
			List<PathSegment> pathSegments = uriInfo.getPathSegments();

			String urlSegments = "";
			for(PathSegment path:pathSegments.subList(2, pathSegments.size()))
				urlSegments += ("/"+path.getPath());

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url + urlSegments);
			for(String paramName : uriInfo.getQueryParameters().keySet()){
				 builder.queryParam(paramName, uriInfo.getQueryParameters().getFirst(paramName));
			}
			ResponseEntity<?>  response = restTemplate.exchange(builder.toUriString(), method, entity,  clas);
			return response;
		}catch(HttpClientErrorException e) {
			System.out.println(e.getResponseBodyAsString());
		}
		return null;
	}
}
