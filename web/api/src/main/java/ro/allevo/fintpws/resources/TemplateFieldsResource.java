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

/**
 * 
 */
package ro.allevo.fintpws.resources;

import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ro.allevo.fintpws.model.TxTemplatesDetailedEntity;


public class TemplateFieldsResource extends PagedCollection<TxTemplatesDetailedEntity> {

	private static final long serialVersionUID = 1L;
	private Integer templateId;

	public TemplateFieldsResource(UriInfo uriInfo, EntityManager entityManagerConfig, Integer templateId) {
		super(uriInfo, 
				entityManagerConfig.createNamedQuery("TxTemplatesDetailedEntity.findAll", TxTemplatesDetailedEntity.class)
					.setParameter("configId", templateId),
				entityManagerConfig.createNamedQuery("TxTemplatesDetailedEntity.findTotal", Long.class)
					.setParameter("configId", templateId),
				entityManagerConfig,
				TxTemplatesDetailedEntity.class
		);
		this.templateId = templateId;
	}

	@Path("{fieldId}")
	public TemplateFieldResource getTemplateResource(@PathParam("fieldId") Integer fieldId) {
		return new TemplateFieldResource(getUriInfo(), getEntityManager(), fieldId);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@JsonIgnore
	public PagedCollection<TxTemplatesDetailedEntity> getTemplatesAsJson() {
		return this;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response saveField(TxTemplatesDetailedEntity templateEntity) {
		return post(templateEntity);
	}

	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteAllFields(){
		return delete(this.templateId);
	}
}
