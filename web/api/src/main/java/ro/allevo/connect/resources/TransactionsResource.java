package ro.allevo.connect.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import ro.allevo.connect.model.TransactionEntity;
import ro.allevo.connect.model.TransactionsEntity;

public class TransactionsResource extends PagedAPICollection<TransactionEntity> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HttpHeaders httpHeaders;
	private String url;
	
	public TransactionsResource(String url, HttpHeaders httpHeaders) {
		super(url, httpHeaders, TransactionsEntity.class);
		this.url = url;
		this.httpHeaders = httpHeaders;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public PagedAPICollection<TransactionEntity> getTransactions(@Context UriInfo uriInfo){
		TransactionsEntity transactions = (TransactionsEntity) getObject(uriInfo, HttpMethod.GET);
		setItems(transactions.getBooked());
		return this;
	}
	
	@Path("{transactionId}")
	public TransactionResource getTransaction(){
		return new TransactionResource(url, this.httpHeaders);
	}
	
}
