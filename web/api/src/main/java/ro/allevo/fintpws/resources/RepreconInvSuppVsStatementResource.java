package ro.allevo.fintpws.resources;

import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ro.allevo.fintpws.model.RepreconInvSuppVsStatementEntity;


public class RepreconInvSuppVsStatementResource extends PagedCollection<RepreconInvSuppVsStatementEntity> {

	private static final long serialVersionUID = 1L;

	public RepreconInvSuppVsStatementResource(UriInfo uriInfo, EntityManager entityManagerList) {
		super(uriInfo, 
				entityManagerList.createNamedQuery("RepreconInvSuppVsStatementEntity.findAll", RepreconInvSuppVsStatementEntity.class),
				entityManagerList.createNamedQuery("RepreconInvSuppVsStatementEntity.findTotal", Long.class),
				entityManagerList,
				RepreconInvSuppVsStatementEntity.class
				);
	}
	

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	//@RolesAllowed(Roles.RECONCILIATION_VIEW)
	@JsonIgnore
	public PagedCollection<RepreconInvSuppVsStatementEntity> getRepreconInvSuppVsStatementAsJson() {
		return this;
	}


}
