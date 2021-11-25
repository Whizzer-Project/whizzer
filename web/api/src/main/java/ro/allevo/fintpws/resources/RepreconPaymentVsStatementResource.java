package ro.allevo.fintpws.resources;


import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ro.allevo.fintpws.model.RepreconPaymentVsStatementEntity;


public class RepreconPaymentVsStatementResource extends PagedCollection<RepreconPaymentVsStatementEntity> {

	private static final long serialVersionUID = 1L;

	public RepreconPaymentVsStatementResource(UriInfo uriInfo, EntityManager entityManagerList) {
		super(uriInfo, 
				entityManagerList.createNamedQuery("RepreconPaymentVsStatementEntity.findAll", RepreconPaymentVsStatementEntity.class),
				entityManagerList.createNamedQuery("RepreconPaymentVsStatementEntity.findTotal", Long.class),
				entityManagerList,
				RepreconPaymentVsStatementEntity.class
				);
	}
	

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	//@RolesAllowed(Roles.RECONCILIATION_VIEW)
	@JsonIgnore
	public PagedCollection<RepreconPaymentVsStatementEntity> getRepreconPaymentVsStatementAsJson() {
		return this;
	}


}
