package ro.allevo.fintpui.dao;

import java.net.URI;
import javax.ws.rs.core.UriBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.model.AccountingCode;


@Service
public class AccountingCodeRestApiDao extends RestApiDao<AccountingCode> implements AccountingCodeDao{

	@Autowired
	Config config;
	
	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getAPIUrl()).path("accounting-codes").build();
	}
	
	public AccountingCodeRestApiDao() {
		super(AccountingCode.class);
	}
	
	@Override
	public AccountingCode[] getAllAccountingCodes() {
		return getAll();
	}

	@Override
	public AccountingCode getAccountingCode(String id) {
		return get(id);
	}

	@Override
	public void insertAccountingCode(AccountingCode accountingCode) {
		post(accountingCode);
	}

	@Override
	public void updateAccountingCode(AccountingCode accountingCode, String id) {
		put(id, accountingCode);
	}

	@Override
	public void deleteAccountingCode(String id) {
		delete(id);
	}
}
