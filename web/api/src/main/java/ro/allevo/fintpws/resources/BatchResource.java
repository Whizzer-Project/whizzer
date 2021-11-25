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

import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import ro.allevo.fintpws.model.BatchJobEntity;

/**
 * Resource class implementing /batchrequests path methods
 * @author remus
 * @version $Revision: 1.0 $
 */
public class BatchResource extends BaseResource<BatchJobEntity> {
	
	public BatchResource(UriInfo uriInfo, EntityManager entityManagerConfig, String id) {
		super(BatchJobEntity.class, uriInfo, entityManagerConfig, id);
	}
	
	/**
	 * GET Method : returns an application/json formatted batch
	 * 
	 * @return JSONObject the batch
	 */

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public BatchJobEntity getBatch() {
		return get();
	}
}
