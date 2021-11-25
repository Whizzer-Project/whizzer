package ro.allevo.connect.resources;

import java.util.UUID;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.springframework.http.HttpHeaders;

import ro.allevo.connect.model.AuthorizationServersEntity;
import ro.allevo.fintpws.util.Roles;

public class BicResource {

	private EntityManager entityManagerConnect;
	AuthorizationServersEntity authorizationServer;
	HttpHeaders heders;

	public BicResource(String bic, EntityManager entityManagerConnect) {
		this.entityManagerConnect = entityManagerConnect;
		this.authorizationServer = getAuthorizationServer(bic);
		this.heders = getHeaders(authorizationServer);
	}

	@Path("accounts")
	public AccountsResource accounts() {
		/* authorizationServer.getAispUrl() + "/accounts" */
		return new AccountsResource(authorizationServer.getAispUrl(), heders);
	}

	@Path("consents")
	@RolesAllowed({Roles.API_INTERFACE_MODIFY})
	public ConsentsResource getConsents( @PathParam("bic") String bic, @Context	UriInfo uriInfo) {
		return new ConsentsResource(uriInfo, entityManagerConnect, bic);
	}
	
	@Path("{payments}/{payment-id}")
	@RolesAllowed({Roles.API_INTERFACE_MODIFY})
	public PaymentsResource authorisations(@PathParam(value = "payments") String payments,
			@PathParam(value = "payment-id") String paymentId) {
		heders.add("name", this.authorizationServer.getUserId());
		heders.add("passwd", this.authorizationServer.getUserSecret());
		switch (payments) {
			case "payments":
			case "periodic-payments":
				return new PaymentsResource(this.authorizationServer.getAispUrl(), heders);
			default:
				return null;
		}		
	}

	@Path("bulk-payments/{payment-product}")
	@RolesAllowed({Roles.API_INTERFACE_MODIFY})
	public BulkPayments bulkPayments() {
		return new BulkPayments(authorizationServer.getResourceServerUri(), heders);
	}

	private AuthorizationServersEntity getAuthorizationServer(String bic) {
		TypedQuery<AuthorizationServersEntity> queryAuth = this.entityManagerConnect
				.createNamedQuery("AuthorizationServersEntity.findByBIC", AuthorizationServersEntity.class)
				.setParameter("bic", bic);
		AuthorizationServersEntity auth = queryAuth.getSingleResult();
		// only for testing
		// auth.setResourceServerUri("https://cfebd61f-72c4-4098-a9ad-84f0f25ca1be.mock.pstmn.io/v1");
		// auth.setResourceServerUri("http://localhost:8089");
		return auth;
	}

	private HttpHeaders getHeaders(AuthorizationServersEntity authorizationServers) {
		HttpHeaders headers = new HttpHeaders();
		// headers.add("Content-Type", MediaType.APPLICATION_JSON);
		headers.add("Content-Type", MediaType.APPLICATION_XML);
		// headers.add("Content-Type", MediaType.MULTIPART_FORM_DATA);
		headers.add("consent-id", authorizationServers.getConsentEntity().getConsentId());
		headers.add("Authorization", "Bearer " + authorizationServers.getToken());
		headers.add("x-request-id", UUID.randomUUID().toString());
		return headers;
	}
}
