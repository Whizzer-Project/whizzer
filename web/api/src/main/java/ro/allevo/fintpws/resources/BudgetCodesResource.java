package ro.allevo.fintpws.resources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.UriInfo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ro.allevo.fintpws.model.BudgetCodesEntity;
import ro.allevo.fintpws.model.ExternalAccountEntity;
import ro.allevo.fintpws.util.Roles;

public class BudgetCodesResource extends PagedCollection<BudgetCodesEntity> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public BudgetCodesResource(UriInfo uriInfo, EntityManager entityManagerList) {
		super(uriInfo, 
			entityManagerList.createNamedQuery("BudgetCodesEntity.findAll", BudgetCodesEntity.class),
			entityManagerList.createNamedQuery("BudgetCodesEntity.findTotal", Long.class),
			entityManagerList,
			BudgetCodesEntity.class
			);
	}
	
	@Path("{codid}")
	public BudgetCodeResource getBdudgetResource(@PathParam("codid") long budgetCodeId) {
		return new BudgetCodeResource(getUriInfo(), getEntityManager(), budgetCodeId);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.INTERNAL_ENTITIES_LIST_VIEW})
	@JsonIgnore
	public PagedCollection<BudgetCodesEntity> getBudgetCodesAsJson() {
		return this;
	}
	
	@SuppressWarnings("resource")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@JsonIgnore
	@Path("/access")
	public List<HashMap<String, Object>> getAccesExternalAccountEntity(@Context HttpServletRequest request) {
		PagedCollection<BudgetCodesEntity> paged =  new PagedCollection<>(getUriInfo(),
				getEntityManager().createNamedQuery("BudgetCodesEntity.findAll", BudgetCodesEntity.class), 
				getEntityManager().createNamedQuery("BudgetCodesEntity.findTotal", Long.class),
				getEntityManager(),
				BudgetCodesEntity.class,
				null);
		List<BudgetCodesEntity> listItem = paged.getItems();
		List<HashMap<String, Object>> list = new ArrayList<>();
		
		for (BudgetCodesEntity ie : listItem) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", ie.getId());
			map.put("code", ie.getCode());
			list.add((HashMap<String, Object>) map);
		}
		Collections.sort(list, new Comparator<Map<String, Object>>() {

			@Override
			public int compare(Map<String, Object> entPrev, Map<String, Object> entNext) {
				return entPrev.get("code").toString().compareTo(entNext.get("code").toString());
			}
			
		});
		return list;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.INTERNAL_ENTITIES_LIST_MODIFY})
	public Response postForm(BudgetCodesEntity budgetCodesEntity) {
		return post(budgetCodesEntity);
	}
	
}
