package ro.allevo.fintpui.dao;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.core.UriBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;

import com.fasterxml.jackson.databind.ObjectMapper;

import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.model.Bank;

@Service
public class BankRestApiDao extends RestApiDao<Bank> implements BankDao{

	@Autowired
	Config config;
	
	@Autowired
	private RestOperations client;
	
	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getAPIUrl()).path("banks").build();
	}
	
	public BankRestApiDao() {
		super(Bank.class);
	}
	
	@Override
	public Bank[] getAllBanks() {
		return getAll();
	}

	@Override
	public Bank getBank(String bic) {
		return get(bic);
	}

	@Override
	public void insertBank(Bank bank) {
		post(bank);
	}

	@Override
	public void updateBank(Bank bank, String bic) {
		put(bic, bank);
	}

	@Override
	public void deleteBank(String bic) {
		delete(bic);
	}
	
	@Override
	public List<HashMap<String, Object>> getAllBankBics(){
		URI uri = UriBuilder.fromUri(getBaseUrl()).path("access").build();
		ResponseEntity<String> response = client.getForEntity(uri, String.class);
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(response.getBody(),
					mapper.getTypeFactory().constructParametricType(List.class, HashMap.class));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}
}
