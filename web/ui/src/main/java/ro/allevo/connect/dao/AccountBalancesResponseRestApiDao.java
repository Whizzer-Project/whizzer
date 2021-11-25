package ro.allevo.connect.dao;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.dao.RestApiDao;

@Service
public class AccountBalancesResponseRestApiDao extends RestApiDao<String> implements AccountBalancesResponseDao{

	@Autowired
	Config config;
	
	public AccountBalancesResponseRestApiDao() {
		super(String.class);
	}

	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getConnectUrl()).build();
	}
	
	@Override
	public String getBalanceAmount(String bic, String accountId) {
		URI uri = UriBuilder.fromUri(getBaseUrl()).path(bic).path("accounts").path(accountId).path("balances").build();
		return get(uri);
	}
}
