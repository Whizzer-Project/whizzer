package ro.allevo.connect.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.springframework.http.HttpHeaders;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ro.allevo.connect.model.AccountEntity;

@JsonIgnoreProperties(ignoreUnknown=true)
public class TransactionResource extends BaseApiResource<AccountEntity> {

	public TransactionResource(String url, HttpHeaders httpHeaders) {
		super(url, httpHeaders, AccountEntity.class);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public AccountEntity getTransaction(@Context UriInfo uriInfo) {
		return get(uriInfo);
	}

	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub
	}

}
