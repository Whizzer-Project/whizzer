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

import ro.allevo.fintpws.model.ExternalEntity;
import ro.allevo.fintpws.util.Roles;

/**
 * Resource class implementing /alerts path methods and acting as /alerts/{name}
 * sub-resource locator to {@link AlertResource}.
 * 
 * @author andrei
 * @version $Revision: 1.0 $
 */
public class ExternalEntitiesResource extends PagedCollection<ExternalEntity> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5222498881324455281L;

	public ExternalEntitiesResource(UriInfo uriInfo, EntityManager entityManagerList) {
		super(uriInfo,
				entityManagerList.createNamedQuery("ExternalEntity.findAll", ExternalEntity.class), 
				entityManagerList.createNamedQuery("ExternalEntity.findTotal", Long.class),
				entityManagerList,
				ExternalEntity.class
				);
	}

	@Path("{entityid}")
	public ExternalEntityResource getExternalEntity(@PathParam("entityid") long entityId) {
		return new ExternalEntityResource(getUriInfo(), getEntityManager(), entityId);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.EXTERNAL_ENTITIES_LIST_VIEW})
	@JsonIgnore
	public PagedCollection<ExternalEntity> getExternalEntitiesAsJson() {
		return this;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("access")
	@JsonIgnore
	public List<HashMap<String, Object>> getAccesExternalEntities(@Context HttpServletRequest request) {
		PagedCollection<ExternalEntity> paged =  getExternalEntitiesAsJson();
		List<ExternalEntity> listItem = paged.getItems();
		List<HashMap<String, Object>> list = new ArrayList<>();
		
		for (ExternalEntity ie : listItem) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", ie.getId());
			map.put("name", ie.getName());
			list.add((HashMap<String, Object>) map);
		}
		Collections.sort(list, new Comparator<Map<String, Object>>() {

			@Override
			public int compare(Map<String, Object> entPrev, Map<String, Object> entNext) {
				return entPrev.get("name").toString().compareTo(entNext.get("name").toString());
			}
			
		});
		return list;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.EXTERNAL_ENTITIES_LIST_MODIFY})
	public Response postForm(ExternalEntity externalEntity) {
		return post(externalEntity);
	}
}
