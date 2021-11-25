
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

package ro.allevo.fintpws.config;

import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.springframework.context.annotation.Configuration;

//import com.owlike.genson.Genson;
//import com.owlike.genson.GensonBuilder;
//import com.owlike.genson.ext.jaxrs.GensonJaxRSFeature;

@Configuration
public class JerseyConfig extends ResourceConfig{
	
	public JerseyConfig() {
	    packages("ro.allevo.fintpws.resources", "ro.allevo.fintpuiws.resources", "ro.allevo.tracker.resources", "ro.allevo.connect.resources", "ro.allevo.whizzer.resources");
	    property(ServerProperties.RESPONSE_SET_STATUS_OVER_SEND_ERROR, true);
	    register(LoggingFeature.class);
		register(RolesAllowedDynamicFeature.class);
		
		/*Genson skipNull = new GensonBuilder()
				.setSkipNull(true)
				.create();
		register(new GensonJaxRSFeature().use(skipNull));*/
	}
}
