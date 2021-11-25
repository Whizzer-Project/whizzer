package ro.allevo.whizzer.resources;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ro.allevo.fintpws.resources.PagedCollection;
import ro.allevo.fintpws.util.Roles;
import ro.allevo.whizzer.model.ConfigBsandPlEntity;
import ro.allevo.whizzer.model.PKIEntity;

public class ConfigResource extends PagedCollection<ConfigBsandPlEntity> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EntityManager entityManagerData;
	
	public ConfigResource(UriInfo uriInfo, EntityManager entityManagerCfg, EntityManager entityManagerData) {
		super(uriInfo, 
				entityManagerCfg.createNamedQuery("ConfigBsandPlEntity.findAll", ConfigBsandPlEntity.class),
				entityManagerCfg.createNamedQuery("ConfigBsandPlEntity.findTotal", Long.class),
				entityManagerCfg,
				ConfigBsandPlEntity.class
				);
		this.entityManagerData = entityManagerData;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("labels")
	@RolesAllowed({Roles.BALANCE_SHEET_VIEW})
	@JsonIgnore
	public PagedCollection<ConfigBsandPlEntity> getProfitLossAsJson() {
		return this;
	}
	
	
	@GET
	@Path("PKIs")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.KPI_DYNAMIC_REPORTS_VIEW})
	public PKIEntity getPKI(@QueryParam("year") Integer year, @QueryParam("entity") String entity) {
		
		EntityManager em = entityManagerData.getEntityManagerFactory().createEntityManager();
		em.getTransaction().begin();

		StoredProcedureQuery procedure = em.createStoredProcedureQuery("findata.getbskpis", PKIEntity.class)
				.registerStoredProcedureParameter("inentity", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("inyear", Integer.class, ParameterMode.IN)
				.registerStoredProcedureParameter("outretcursor", PKIEntity.class, ParameterMode.REF_CURSOR)
				.setParameter("inentity", entity).setParameter("inyear", year);
		procedure.execute();
		List<PKIEntity> pKIEntities = (List<PKIEntity>)procedure.getResultList();
		
		em.getTransaction().commit();
		em.close();
		
		return pKIEntities.size()>0?pKIEntities.get(0):null;
		
	}
	
}
