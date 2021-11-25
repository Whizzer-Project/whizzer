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
import java.util.UUID;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.springframework.http.HttpHeaders;
import org.springframework.web.client.HttpClientErrorException;
import ro.allevo.connect.model.ConsentEntity;
import ro.allevo.fintpws.resources.BaseResource;
import ro.allevo.fintpws.util.JsonResponseWrapper;
import ro.allevo.fintpws.util.Roles;

/**
 * @author costi
 * @version $Revision: 1.0 $
 */

public class ConsentResource extends BaseResource<ConsentEntity> {

	public ConsentResource(UriInfo uriInfo, EntityManager entityManagerConnect, String bic) {
		super(ConsentEntity.class, uriInfo, entityManagerConnect, bic);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.API_INTERFACE_MODIFY})
	public ConsentEntity getConsent() {
		return get();
		
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.API_INTERFACE_MODIFY})
	public Response updateConsent(ConsentEntity entity) {
		return put(entity);
	}

	@DELETE
	@RolesAllowed({Roles.API_INTERFACE_MODIFY})
	public Response deleteConsent() {
		ConsentEntity consent = get();
		String consentId = consent.getConsentId();
		//String consentId = consent.getConsentId();
		//TODO - de pornit cand trecem pe productie
		//RestTemplate restTemplate = new RestTemplate();
		//HttpEntity<ConsentRequestEntity> entity = new HttpEntity<ConsentRequestEntity>(getHeaders());
		try {
			
			//restTemplate.exchange(consent.getUrl()+"/"+consentId, HttpMethod.DELETE, entity, ConsentsResponseEntity.class);
			consent.setConsentId(null);
			consent.setValidUntil(null);
			put(consent);
		}catch(HttpClientErrorException e) {
			return JsonResponseWrapper.getResponse(e.getStatusCode().value(), e.getLocalizedMessage());
		}
		return JsonResponseWrapper.getResponse(Response.Status.OK, consentId + " deleted");
	}
	
	private static HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", MediaType.APPLICATION_JSON);
		headers.add("X-Request-ID", UUID.randomUUID().toString());
		headers.add("PSU-IP-Address", getLocalIpAddress());
//		headers.add("PSU-IP-Address", "127.0.0.1");
		headers.add("X-IBM-Client-Secret", "test");
		headers.add("X-IBM-Client-ID", "test");
		headers.add("Authorization", "Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoxNjAyLCJ1c2VyX25hbWUiOiJ3YXN0ZXN0Iiwic2NvcGUiOlsiZm9vIiwicmVhZCIsIndyaXRlIl0sImV4cCI6MTU3MjY5Mjc5NiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9UcmFja2luZyBSZXBvcnRzX3ZpZXciLCJST0xFX0NvbmZpZ3VyYXRpb24gTGlzdHNfdmlldyIsIlJPTEVfRXh0ZXJuYWwgRW50aXRpZXMgTGlzdF9tb2RpZnkiLCJST0xFX0V4dGVybmFsIEVudGl0aWVzIExpc3RfdmlldyIsIlJPTEVfUXVldWVzX3ZpZXciLCJST0xFX0V2ZW50c192aWV3IiwiUk9MRV9CYW5rcyBMaXN0X3ZpZXciLCJST0xFX1JlY29uY2lsaWF0aW9uX3ZpZXciLCJST0xFX1JlY29uY2lsaWF0aW9uX21vZGlmeSIsIlJPTEVfQmFua3MgTGlzdF9tb2RpZnkiLCJST0xFX0ludGVybmFsIEVudGl0aWVzIExpc3RfbW9kaWZ5IiwiUk9MRV9Sb3V0aW5nIFJ1bGVzX3ZpZXciLCJST0xFX1F1ZXVlc19tb2RpZnkiLCJST0xFX0ludGVybmFsIEVudGl0aWVzIExpc3RfdmlldyIsIlJPTEVfQ29uZmlndXJhdGlvbiBMaXN0c19tb2RpZnkiLCJST0xFX1VzZXJzX21vZGlmeSIsIlJPTEVfUm91dGluZyBSdWxlc19tb2RpZnkiXSwianRpIjoiNDQzNDMzZWYtZjdjZi00MjhhLWE2MGEtYWY3MTU0YmFiZTI4IiwiY2xpZW50X2lkIjoiU2FtcGxlQ2xpZW50SWQifQ.e2ngRoXKCH82aanKS20AJ9R15EFKomu3FwXzt-3kp50DAr-xxPyCym0fxXLXpI10JbbJyyblWM6pQ-pt6nfPKngmP_HatJRKrSYYjtWnLgsrnLfIl5exnPjwLVmGcxnAhku87jYluLrFIu_SHpicBPI3AAa-vY5yqT95W9IGTFgmYHiycUAWk_8fe7zfNRLtJb7c_9B_8xPku_hlgqviVehIMLXCU3f3_jKdAsMbdseC53Y4Zv3T9OttljzWqcKlJoUvx6pERL8Ptos4RfCfg8MVX10mF0-VD9e0kiBxJQZ4sCw6PvjAu6rHlXWKgDI3faCohCScPJgk80GkzZc8BA");
		return headers;
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
