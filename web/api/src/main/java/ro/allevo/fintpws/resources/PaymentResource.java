package ro.allevo.fintpws.resources;

import java.text.ParseException;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.persistence.RollbackException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import ro.allevo.fintpws.model.PaymentEntity;
import ro.allevo.fintpws.util.EntityManagerUtils;
import ro.allevo.fintpws.util.Roles;

public class PaymentResource{
	
	private EntityManager emb;
	private UriInfo uriInfo;

	public PaymentResource(UriInfo uriInfo, EntityManager emb) {
		this.emb = emb;
		this.uriInfo = uriInfo;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.BANKS_LIST_MODIFY})
	public Response postForm(PaymentEntity paymentEntity) {
		try {		
			EntityManagerUtils.persistEntitiesToDB(paymentEntity, uriInfo, emb, "POST");
			return Response.ok().build();
		}catch (RollbackException re) {
			throw re;
		}catch (ParseException pe) {
		
		} 
		return Response.serverError().build();
	}

}
