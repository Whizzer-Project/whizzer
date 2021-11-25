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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ro.allevo.fintpws.model.ExternalAccountEntity;
import ro.allevo.fintpws.model.ExternalEntity;
import ro.allevo.fintpws.model.InternalEntity;
import ro.allevo.fintpws.security.RolesUtils;
import ro.allevo.fintpws.util.Roles;

/**
 * Resource class implementing /alerts path methods and acting as /alerts/{name}
 * sub-resource locator to {@link AlertResource}.
 * 
 * @author andrei
 * @version $Revision: 1.0 $
 */
public class ExternalAccountsResource extends PagedCollection<ExternalAccountEntity> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2289763559055558099L;
	private ExternalEntity externalEntity;
	private String projectType;

	public ExternalAccountsResource(UriInfo uriInfo, EntityManager entityManagerList) {
		super(uriInfo,
				entityManagerList.createNamedQuery("ExternalAccountEntity.findAll", ExternalAccountEntity.class),
				entityManagerList.createNamedQuery("ExternalAccountEntity.findTotal", Long.class),
				entityManagerList,
				ExternalAccountEntity.class
				);
	}
	
	public ExternalAccountsResource(UriInfo uriInfo, EntityManager entityManagerList, ExternalEntity externalEntity) {
		super(uriInfo,
				entityManagerList
					.createNamedQuery("ExternalAccountEntity.findAllEntity", ExternalAccountEntity.class)
					.setParameter("entityname", externalEntity.getName()), 
				entityManagerList
					.createNamedQuery("ExternalAccountEntity.findTotalEntity", Long.class)
					.setParameter("entityname", externalEntity.getName()),
				entityManagerList,
				ExternalAccountEntity.class
				);
		
		this.externalEntity = externalEntity;
	}

	@Path("{accid}")
	public ExternalAccountResource getExternalAccount(@PathParam("accid") long externalAccountId) {
		return new ExternalAccountResource(getUriInfo(), getEntityManager(), externalAccountId);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.EXTERNAL_ENTITIES_LIST_VIEW})
	@JsonIgnore
	public PagedCollection<ExternalAccountEntity> getExternalAccountsAsJson() {
		return this;
	}

	@SuppressWarnings("resource")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@JsonIgnore
	@Path("/access")
	public PagedCollection<ExternalAccountEntity> getAccesExternalAccountEntity(@Context HttpServletRequest request) {
		RolesUtils utils = new RolesUtils();
		return new PagedCollection<>(getUriInfo(),
				getEntityManager().createNamedQuery("ExternalAccountEntity.findAll", ExternalAccountEntity.class), 
				getEntityManager().createNamedQuery("ExternalAccountEntity.findTotal", Long.class),
				getEntityManager(),
				ExternalAccountEntity.class,
				utils.getFilters(request, projectType, getEntityManager(), 7));
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.EXTERNAL_ENTITIES_LIST_MODIFY})
	public Response postForm(ExternalAccountEntity externalAccountEntity) {
		if (null != externalEntity)
			externalAccountEntity.setEntityName(externalEntity.getName());
		
		return post(externalAccountEntity);
	}
}
