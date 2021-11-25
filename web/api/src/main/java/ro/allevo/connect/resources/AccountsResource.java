package ro.allevo.connect.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ro.allevo.connect.model.AccountEntity;
import ro.allevo.connect.model.AccountsEntity;


public class AccountsResource extends PagedAPICollection<AccountEntity> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HttpHeaders httpHeaders;
	private String url;
	
	
	public AccountsResource(String url, HttpHeaders httpHeaders) {
		super(url, httpHeaders, AccountsEntity.class);
		this.url = url;
		this.httpHeaders = httpHeaders;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@JsonIgnore
	public PagedAPICollection<AccountEntity> getAccounts(@Context UriInfo uriInfo){
		AccountsEntity accounts = (AccountsEntity)this.getObject(uriInfo, HttpMethod.GET);
		this.setItems(accounts.getAccounts());
		return this;
	}
		
	@Path("{account-id}")
	@JsonIgnore
	public AccountResource getAccount(){
		return new AccountResource(this.url, this.httpHeaders);
	}
	
}
