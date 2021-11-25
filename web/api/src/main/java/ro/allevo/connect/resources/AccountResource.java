package ro.allevo.connect.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.springframework.http.HttpHeaders;

import ro.allevo.connect.model.AccountEntity;
import ro.allevo.connect.model.AccountWrapperEntity;

public class AccountResource extends BaseApiResource<AccountWrapperEntity>{
	
	private HttpHeaders httpHeaders;
	private String url;
	
	public AccountResource(String url, HttpHeaders httpHeaders){
		super(url, httpHeaders, AccountWrapperEntity.class);
		this.httpHeaders = httpHeaders;
		this.url = url;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public AccountEntity getAccount(@Context UriInfo uriInfo) {
		return get(uriInfo).getAccount();
	}

	@Path("transactions")
	public TransactionsResource getTransactions(){
		return new TransactionsResource(url, httpHeaders);
	}
	
	@Path("balances")
	public AccountBalancesResponseResource getBalanceAmount(){	
		return new AccountBalancesResponseResource(this.url, this.httpHeaders);
	}
	
	@Override
	public void close() throws Exception {
		throw new UnsupportedOperationException();
	}
	
}
