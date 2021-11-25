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

import ro.allevo.fintpws.model.AccountingCodeEntity;
import ro.allevo.fintpws.model.InternalEntity;
import ro.allevo.fintpws.model.LocationCodeEntity;
import ro.allevo.fintpws.util.Roles;

public class LocationCodesResource extends PagedCollection<LocationCodeEntity> {
	
	private static final long serialVersionUID = 1L;
	private InternalEntity internalEntity;
	
	public LocationCodesResource(UriInfo uriInfo, EntityManager entityManagerList) {
		super(uriInfo,
				entityManagerList.createNamedQuery("LocationCodeEntity.findAll", LocationCodeEntity.class), 
				entityManagerList.createNamedQuery("LocationCodeEntity.findTotal", Long.class),
				entityManagerList,
				LocationCodeEntity.class
				);
	}
	
	public LocationCodesResource(UriInfo uriInfo, EntityManager entityManagerList, InternalEntity internalEntity) {
		super(uriInfo,
				entityManagerList
					.createNamedQuery("LocationCodeEntity.findAllEntity", LocationCodeEntity.class)
					.setParameter("entityname", internalEntity.getName()), 
				entityManagerList
					.createNamedQuery("LocationCodeEntity.findTotalEntity", Long.class)
					.setParameter("entityname", internalEntity.getName()),
				entityManagerList,
				LocationCodeEntity.class
				);
		
		this.internalEntity = internalEntity;
	}

	@Path("{loccid}")
	public LocationCodeResource getLocationCode(@PathParam("loccid") long locationCodeId) {
		return new LocationCodeResource(getUriInfo(), getEntityManager(), locationCodeId);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed(Roles.INTERNAL_ENTITIES_LIST_VIEW)
	@JsonIgnore
	public PagedCollection<LocationCodeEntity> getLocationCodesAsJson() {
		return this;
	}
	
	@SuppressWarnings("resource")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@JsonIgnore
	@Path("/access")
	public List<HashMap<String, Object>> getAccesExternalAccountEntity(@Context HttpServletRequest request) {
		PagedCollection<LocationCodeEntity> paged =  new PagedCollection<>(getUriInfo(),
				getEntityManager().createNamedQuery("LocationCodeEntity.findAll", LocationCodeEntity.class), 
				getEntityManager().createNamedQuery("LocationCodeEntity.findTotal", Long.class),
				getEntityManager(),
				LocationCodeEntity.class,
				null);
		List<LocationCodeEntity> listItem = paged.getItems();
		List<HashMap<String, Object>> list = new ArrayList<>();
		
		for (LocationCodeEntity ie : listItem) {
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
	@RolesAllowed(Roles.INTERNAL_ENTITIES_LIST_MODIFY)
	public Response postForm(LocationCodeEntity locationCodeEntity) {
		if (null != internalEntity)
			locationCodeEntity.setEntityName(internalEntity.getName());
		
		return post(locationCodeEntity);
	}
}

