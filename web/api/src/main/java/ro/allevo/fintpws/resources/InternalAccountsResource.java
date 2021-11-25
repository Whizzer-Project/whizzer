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

package ro.allevo.fintpws.resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ro.allevo.fintpws.model.InternalAccountEntity;
import ro.allevo.fintpws.model.InternalEntity;
import ro.allevo.fintpws.util.Roles;

/**
 * Resource class implementing /alerts path methods and acting as /alerts/{name}
 * sub-resource locator to {@link AlertResource}.
 * 
 * @author andrei
 * @version $Revision: 1.0 $
 */
public class InternalAccountsResource extends PagedCollection<InternalAccountEntity> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2567509226006225451L;
	private InternalEntity internalEntity;
	private String projectType;
	
	public InternalAccountsResource(UriInfo uriInfo, EntityManager entityManagerList) {
		super(uriInfo,
				entityManagerList.createNamedQuery("InternalAccountEntity.findAll", InternalAccountEntity.class), 
				entityManagerList.createNamedQuery("InternalAccountEntity.findTotal", Long.class),
				entityManagerList,
				InternalAccountEntity.class
				);
	}
	
	public InternalAccountsResource(UriInfo uriInfo, EntityManager entityManagerList, InternalEntity internalEntity) {
		super(uriInfo,
				entityManagerList
					.createNamedQuery("InternalAccountEntity.findAllEntity", InternalAccountEntity.class)
					.setParameter("entityname", internalEntity.getName()), 
				entityManagerList
					.createNamedQuery("InternalAccountEntity.findTotalEntity", Long.class)
					.setParameter("entityname", internalEntity.getName()),
				entityManagerList,
				InternalAccountEntity.class
				);
		
		this.internalEntity = internalEntity;
	}

	public InternalAccountsResource(UriInfo uriInfo, EntityManager eml, String projectType) {
		super(uriInfo, eml, InternalAccountEntity.class);
//		this.uriInfo = uriInfo;
//		this.entityManager = eml;
		this.projectType = projectType;
	}

	@Path("{accid}")
	public InternalAccountResource getInternalAccount(@PathParam("accid") long internalAccountId) {
		return new InternalAccountResource(getUriInfo(), getEntityManager(), internalAccountId);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.INTERNAL_ENTITIES_LIST_VIEW})
	@JsonIgnore
	public PagedCollection<InternalAccountEntity> getInternalAccountsAsJson() {
		return this;
	}
	
	@Path("access")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@JsonIgnore
	public List<HashMap<String, Object>> getInternalAccountsWithOutRules() {
		List<InternalAccountEntity> accountEntities = this.getItems();
		List<HashMap<String, Object>> list = new ArrayList<>();
		for (InternalAccountEntity ice : accountEntities) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", ice.getId());
			map.put("accountNumber", ice.getAccountNumber());
			list.add((HashMap<String, Object>) map);
		}
		return list;
	}
	
	

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.INTERNAL_ENTITIES_LIST_MODIFY})
	public Response postForm(InternalAccountEntity internalAccountEntity) {
		if (null != internalEntity)
			internalAccountEntity.setEntityName(internalEntity.getName());
		
		return post(internalAccountEntity);
	}
}
