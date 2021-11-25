package ro.allevo.connect.resources;

import java.net.URI;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.springframework.http.HttpHeaders;

import ro.allevo.connect.model.AccountBalancesResponseEntity;
import ro.allevo.connect.model.BalanceEntity;

public class AccountBalancesResponseResource extends BaseApiResource<AccountBalancesResponseEntity> {

	
	public AccountBalancesResponseResource(String url, HttpHeaders httpHeaders){
		super(url, httpHeaders, AccountBalancesResponseEntity.class);
	}
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getBalanceAmount(@Context UriInfo uriInfo, @PathParam("account-id") String accountId){
		String balanceAmount = null;

		UriBuilder uriBuilder = UriBuilder.fromPath(uriInfo.getAbsolutePath().getScheme() + "://" + uriInfo.getAbsolutePath().getHost() + ":8080");			
		URI uriInfoBalance = uriBuilder.path("mock/accounts/" + accountId + "/balances").build();

		AccountBalancesResponseEntity accountBalancesResponse = (AccountBalancesResponseEntity) getObjectFromUri(uriInfoBalance);
		if(accountBalancesResponse != null) {
			List<BalanceEntity> balances = accountBalancesResponse.getBalances();
			for(int i=0; i<balances.size(); i++ ) {
				BalanceEntity balance = balances.get(i);
				if(balance.getBalanceType().equalsIgnoreCase("expected")) {
					balanceAmount = balance.getBalanceAmount().getAmount();
					break;
				}
			}	
		}
		return balanceAmount;
	}

	@Override
	public void close() throws Exception {
		throw new UnsupportedOperationException();
	}

}
