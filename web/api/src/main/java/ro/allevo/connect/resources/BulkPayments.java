package ro.allevo.connect.resources;

import javax.ws.rs.POST;
import javax.ws.rs.core.Response;

import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import reactor.core.publisher.Flux;
import ro.allevo.connect.model.AccountEntity;
import ro.allevo.connect.model.PaymentsEntity;
import ro.allevo.connect.model.TransactionEntity;
import ro.allevo.fintpws.util.JsonResponseWrapper;
public class BulkPayments extends PagedAPICollection<AccountEntity> {
	
	private static final long serialVersionUID = 1L;
	private HttpHeaders httpHeaders;
	private String url;
	
	public BulkPayments(String url, HttpHeaders httpHeaders) {
		super(url, httpHeaders, PaymentsEntity.class);
		this.url = url;
		this.httpHeaders = httpHeaders;
	}
	
	@POST
	public Response createBulkPayment(String messages){
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
	   
		
		Flux<Object> tweetFlux = WebClient.create().post()
				  .uri(builder.toUriString())
			      .retrieve()
			      .bodyToFlux(Object.class);
		tweetFlux.blockFirst();
		return JsonResponseWrapper.getResponse(Response.Status.CREATED, "");
	}
}
