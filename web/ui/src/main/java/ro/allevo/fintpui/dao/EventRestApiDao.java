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

package ro.allevo.fintpui.dao;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.model.Event;
import ro.allevo.fintpui.model.Status;

@Service
public class EventRestApiDao extends RestApiDao<Event> implements EventDao {

	@Autowired
	Config config;
	
	public EventRestApiDao() {
		super(Event.class);
	}
	
	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getAPIUrl()).path("events").build();
	}
	
	@Override
	public Event getEvent(String eventGuid, String type) {
		URI uri = null;
		if(type.equals("archive")) {
			uri = UriBuilder.fromUri(config.getAPIUrl()).path("events").path(eventGuid).path("archive").build();
		}else {
			uri = UriBuilder.fromUri(config.getAPIUrl()).path("events").path(eventGuid).build();
		}
		
		return get(uri);
	}

	@Override
	public Event[] getAllEvents() {
		return getAll();
	}

	@Override
	public Event[] getCountEvent(LinkedHashMap<String, List<String>> params) {
		return getAll(params);
	}
	
	@Override
	public ResponseEntity<String> insertStatusData(Status status){
		URI uri = UriBuilder.fromUri(getBaseUrl()).path("status").build();
		return post(uri, status);
	}
}
