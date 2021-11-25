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
package ro.allevo.tracker.resources;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.springframework.stereotype.Repository;

@Path("/tracker-api")
@Singleton
@Repository
//@EnableTransactionManagement
public class TrackerApiResource {

	@Context
	private UriInfo uriInfo;
	
	@PersistenceContext(unitName="fintpTRACK")
	public EntityManager emt;
	
	@Path("complexity")
	public ComplexityResource getComplexity() {
		return new ComplexityResource(uriInfo, emt);
	}
	
	@Path("live")
	public LiveResource getLive() {
		return new LiveResource(uriInfo, emt);
	}
	
	@Path("overall")
	public OverallResource getOverall() {
		return new OverallResource(emt);
	}
	
	@Path("component")
	public ComponentResource getComponent() {
		return new ComponentResource(emt);
	}
}
