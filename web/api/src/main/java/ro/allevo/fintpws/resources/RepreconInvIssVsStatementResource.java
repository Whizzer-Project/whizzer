package ro.allevo.fintpws.resources;

import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ro.allevo.fintpws.model.RepreconInvIssVsStatementEntity;


public class RepreconInvIssVsStatementResource extends PagedCollection<RepreconInvIssVsStatementEntity> {

	private static final long serialVersionUID = 1L;

	public RepreconInvIssVsStatementResource(UriInfo uriInfo, EntityManager entityManagerList) {
		super(uriInfo, 
				entityManagerList.createNamedQuery("RepreconInvIssVsStatementEntity.findAll", RepreconInvIssVsStatementEntity.class),
				entityManagerList.createNamedQuery("RepreconInvIssVsStatementEntity.findTotal", Long.class),
				entityManagerList,
				RepreconInvIssVsStatementEntity.class
				);
	}
	

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	//@RolesAllowed(Roles.RECONCILIATION_VIEW)
	@JsonIgnore
	public PagedCollection<RepreconInvIssVsStatementEntity> getRepreconInvIssVsStatementAsJson() {
		return this;
	}


}

