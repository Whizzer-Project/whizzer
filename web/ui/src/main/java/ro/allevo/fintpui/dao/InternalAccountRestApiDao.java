package ro.allevo.fintpui.dao;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.ws.rs.core.UriBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.model.InternalAccount;

@Service
public class InternalAccountRestApiDao extends RestApiDao<InternalAccount> implements InternalAccountDao {

	@Autowired
	Config config;
	
	@Autowired
	private RestOperations client;

	public InternalAccountRestApiDao() {
		super(InternalAccount.class);
	}

	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getAPIUrl()).path("internal-accounts").build();
	}

	@Override
	public InternalAccount[] getAllInternalAccounts() {
		return getAll();

	}

	@Override
	public InternalAccount getInternalAccount(String id) {
		return get(id);
	}
	
	@Override
	public List<HashMap<String, Object>> getInternalAccountCurrency(){
		URI uri = UriBuilder.fromUri(getBaseUrl()).path("access").build();
		ResponseEntity<String> response =  client.getForEntity(uri, String.class);
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(response.getBody(),
					mapper.getTypeFactory().constructParametricType(List.class, HashMap.class));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}

	@Override
	public void insertInternalAccount(InternalAccount internalAccount) {
		post(internalAccount);
	}

	@Override
	public void updateInternalAccount(InternalAccount internalAccount, String id) {
		put(id, internalAccount);
	}

	@Override
	public void deleteInternalAccount(String id) {
		delete(id);
	}

	public InternalAccount[] getAllInternalAccounts(LinkedHashMap<String, List<String>> params) {
		return getAll(params);
	}

}
