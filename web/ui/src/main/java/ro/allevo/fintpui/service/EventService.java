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

package ro.allevo.fintpui.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.dao.EventRestApiDao;
import ro.allevo.fintpui.model.Event;
import ro.allevo.fintpui.model.Status;
import ro.allevo.fintpui.utils.PagedCollection;

@Service
public class EventService {

	@Autowired
	private EventRestApiDao eventDao;
	
	public Event[] getAllEvents() {		
		return eventDao.getAllEvents();
	}
	
	public Event getEvent(String eventGuid, String type){
		return eventDao.getEvent(eventGuid, type);
	}
	
	public PagedCollection<Event> getPage(LinkedHashMap<String, List<String>> param) {
		return eventDao.getPage(param);
	}
	
	public Event[] getCountEvent(LinkedHashMap<String, List<String>> params) {
		return eventDao.getCountEvent(params);
	}
	
	public ResponseEntity<String> insertStatusData(Status status){
		return eventDao.insertStatusData(status);
	}
	
	public ArrayList<String> getAllEventsNames(){
		ArrayList<String> result = new ArrayList<>();
		for(Event event : getAllEvents()){
			result.add(event.getGuid());
		}
		return result;
	}	
}
