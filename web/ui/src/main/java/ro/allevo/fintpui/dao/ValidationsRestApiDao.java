package ro.allevo.fintpui.dao;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.config.Config;

@Service
public class ValidationsRestApiDao extends RestApiDao<Boolean> implements ValidationsDao{

	public ValidationsRestApiDao() {
		super(Boolean.class);
	}

	@Autowired
	Config config;
	
	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getAPIUrl()).path("validations").build();
	}

	@Override
	public boolean validateIban(String iban) {
		return get(UriBuilder.fromUri(getBaseUrl()).path("iban").queryParam("value", iban).build());
	}

	@Override
	public boolean validateCif(String cif) {
		return get(UriBuilder.fromUri(getBaseUrl()).path("cif").queryParam("value", cif).build());
	}

	@Override
	public boolean validateCNP(String cnp) {
		return get(UriBuilder.fromUri(getBaseUrl()).path("cnp").queryParam("value", cnp).build());
	}
	
	@Override
	public boolean validateIbanRo(String iban, String debtor) {
		return get(UriBuilder.fromUri(getBaseUrl()).path("ibanRo").queryParam("iban", iban).queryParam("debtor", debtor).build());
	}
	
}
