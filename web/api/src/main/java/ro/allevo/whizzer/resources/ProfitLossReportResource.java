package ro.allevo.whizzer.resources;

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

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.fasterxml.jackson.annotation.JsonIgnore;


import ro.allevo.fintpws.resources.PagedCollection;
import ro.allevo.fintpws.util.Roles;
import ro.allevo.whizzer.model.ProfitLossReportEntity;


public class ProfitLossReportResource extends PagedCollection<ProfitLossReportEntity> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ProfitLossReportResource(UriInfo uriInfo, EntityManager entityManagerData) {
		super(uriInfo, 
				entityManagerData.createNamedQuery("ProfitLossReportEntity.findAll", ProfitLossReportEntity.class),
				entityManagerData.createNamedQuery("ProfitLossReportEntity.findTotal", Long.class),
				entityManagerData,
				ProfitLossReportEntity.class
				);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.BALANCE_SHEET_VIEW})
	@JsonIgnore
	public PagedCollection<ProfitLossReportEntity> getProfitLossAsJson() {
		return this;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({Roles.BALANCE_SHEET_VIEW})
	public Response postForm(ProfitLossReportEntity profitLossReport) {
		return post(profitLossReport);
	}

}
