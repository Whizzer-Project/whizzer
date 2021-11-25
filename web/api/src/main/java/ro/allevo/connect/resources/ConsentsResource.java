/*
 * FinTP - Financial Transactions Processing Application
 * Copyright (C) 2013 Business Information Systems (Allevo) S.R.L.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>
 * or contact Allevo at : 031281 Bucuresti, 23C Calea Vitan, Romania,
 * phone +40212554577, office@allevo.ro <mailto:office@allevo.ro>, www.allevo.ro.
 */

/**
 * 
 */
package ro.allevo.connect.resources;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.UUID;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.persistence.RollbackException;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.logging.log4j.LogManager;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import ro.allevo.connect.model.AccessEntity;
import ro.allevo.connect.model.AuthorizationInfoEntity;
import ro.allevo.connect.model.AuthorizationServersEntity;
import ro.allevo.connect.model.ConsentEntity;
import ro.allevo.connect.model.ConsentRequestEntity;
import ro.allevo.connect.model.ConsentsResponseEntity;
import ro.allevo.fintpws.util.EntityManagerUtils;
import ro.allevo.fintpws.util.JsonResponseWrapper;
import ro.allevo.fintpws.util.Roles;

/**
 * @author costi
 * @version $Revision: 1.0 $
 */

public class ConsentsResource {

	private ConsentEntity consent = null;
	private EntityManager entityManagerConnect;
	private UriInfo uriInfo;
	private String bic;

	public ConsentsResource(UriInfo uriInfo, EntityManager entityManagerConnect, String bic) {
		this.uriInfo = uriInfo;
		this.entityManagerConnect = entityManagerConnect;
		this.bic = bic;
	}

	@Path("{bic}")
	@RolesAllowed({Roles.API_INTERFACE_MODIFY})
	public ConsentResource getBICsResource(@PathParam("bic") String bic) {
		return new ConsentResource(uriInfo, this.entityManagerConnect, bic);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.API_INTERFACE_MODIFY})
	public Response createConsent() {
		/*
		 * String bic = uriInfo.getQueryParameters().get("bic").get(0);
		 * TypedQuery<ConsentEntity> query =
		 * entityManagerConnect.createNamedQuery("ConsentEntity.findById",
		 * ConsentEntity.class).setParameter("id", bic); this.consent =
		 * query.getSingleResult();
		 */

		// this.consent = bic;
		TypedQuery<AuthorizationServersEntity> queryAuth = entityManagerConnect
				.createNamedQuery("AuthorizationServersEntity.findByBIC", AuthorizationServersEntity.class)
				.setParameter("bic", this.bic);
		AuthorizationServersEntity authorizationServers = queryAuth.getSingleResult();

		ConsentRequestEntity consentRequest = getConsentRequest();
		
		LocalDate localDate = LocalDate.now();//For reference
		localDate = localDate.plusDays(85);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String formattedString = localDate.format(formatter);
		
		consentRequest.setValidUntil(formattedString);
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<ConsentRequestEntity> entity = new HttpEntity<>(consentRequest, getHeaders());
		try {
			ResponseEntity<ConsentsResponseEntity> consentsResponse = restTemplate.exchange(
					authorizationServers.getConsentEntity().getUrl(), HttpMethod.POST, entity,
					ConsentsResponseEntity.class);
			ConsentsResponseEntity consent = consentsResponse.getBody();
			// String authInfoUrl = consent.get_links().getScaOAuth().getHref();
			ResponseEntity<AuthorizationInfoEntity> authorizationInfo = restTemplate
					.getForEntity(consent.get_links().getScaOAuth().getHref(), AuthorizationInfoEntity.class);
			AuthorizationInfoEntity authInfo = authorizationInfo.getBody();
			updateConsent(consent, consentRequest.getValidUntil(), authorizationServers, authInfo);
		} catch (HttpClientErrorException e) {
			System.out.println(e.getResponseBodyAsString());
			return JsonResponseWrapper.getResponse(e.getStatusCode().value(), e.getResponseBodyAsString());
		}
		return JsonResponseWrapper.getResponse(Response.Status.OK, consent.getConsentId() + ":" + consentRequest.getValidUntil() );
	}

	private boolean updateConsent(ConsentsResponseEntity consent, String validUntil,
			AuthorizationServersEntity authorizationServers, AuthorizationInfoEntity authInfo) {
		try {
			// patch update
			EntityManager eml = entityManagerConnect.getEntityManagerFactory().createEntityManager();
			eml.getTransaction().begin();
			/*
			 * //update consent this.consent.setConsentId(consent.getConsentId());
			 * this.consent.setValidUntil(validUntil); eml.merge(this.consent);
			 */
			// update authorizationServers

			authorizationServers.getConsentEntity().setConsentId(consent.getConsentId());
			authorizationServers.getConsentEntity().setValidUntil(validUntil);
			authorizationServers.setUserAuthorizationUri(authInfo.getAuthorization_endpoint());
			authorizationServers.setAccessTokenUri(authInfo.getToken_endpoint());
			// To do- add urlAuthInfo column
			//authorizationServers.setAccessTokenUri(consent.get_links().getScaOAuth().getHref());
			eml.merge(authorizationServers);

			eml.getTransaction().commit();
			EntityManagerUtils.persistStatusEntityToDB(consent, uriInfo, eml, "PUT", false);
			eml.close();
		} catch (RollbackException re) {
			return false;
		}
		catch (ParseException e) {
		
			LogManager.getLogger(getClass().getName()).error("Could not parse when creating statusEntity in ModelUtils");
		}
		return true;
	}

	private static HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", MediaType.APPLICATION_JSON);
		headers.add("X-Request-ID", UUID.randomUUID().toString());
		headers.add("PSU-IP-Address", "127.0.0.1");
//		headers.add("PSU-IP-Address", getLocalIpAddress());
		headers.add("Accept", MediaType.APPLICATION_JSON);
		return headers;
	}

	private ConsentRequestEntity getConsentRequest() {
		AccessEntity acc = new AccessEntity();
		acc.setAvailableAccounts("allAccounts");

		ConsentRequestEntity consentRequestEntity = new ConsentRequestEntity();
		consentRequestEntity.setAccess(acc);
		consentRequestEntity.setValidUntil(getValidUntil());
		consentRequestEntity.setRecurringIndicator(true);
		consentRequestEntity.setFrequencyPerDay(4);
		consentRequestEntity.setCombinedServiceIndicator(false);
		return consentRequestEntity;
	}

	private static String getValidUntil() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, 90);
		return formatter.format(calendar.getTime());
	}

	private static String getLocalIpAddress() {
		InetAddress inetAddress = null;
		try {
			inetAddress = InetAddress.getLocalHost();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (null != inetAddress)
			return inetAddress.getHostAddress();
		return null;
	}

}
