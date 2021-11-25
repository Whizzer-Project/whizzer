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

package ro.allevo.fintpui.controllers;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;

import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.exception.NotAuthorizedException;
import ro.allevo.fintpui.model.Event;
import ro.allevo.fintpui.service.EventService;
import ro.allevo.fintpui.utils.Filters;
import ro.allevo.fintpui.utils.JSONHelper;
import ro.allevo.fintpui.utils.PagedCollection;
import ro.allevo.fintpui.utils.Roles;
import ro.allevo.fintpui.utils.datatables.DataTables;

@Controller
@RequestMapping(value="/events")
public class EventsController {
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	Config config;
	
	private static Logger logger = LogManager.getLogger(EventsController.class.getName());

	/*
	 * DISPLAY
	 */
	@GetMapping
	public String printEvents(OAuth2Authentication auth, ModelMap model) {
		logger.info("/event required");
		if (!Roles.hasRoles(auth,Roles.EVENTS_VIEW))
			throw new NotAuthorizedException();
		model.addAttribute("hasModifyRole", Roles.hasRoles(auth,Roles.BANKS_LIST_MODIFY));

		model.addAttribute("apiUri", config.getAPIUrl());		
		return "events";
	}

	/*
	 * DISPLAY
	 */
	@GetMapping(value = "/page")
	public @ResponseBody String getEventsJson(OAuth2Authentication auth,@RequestParam int draw, @RequestParam String correlationid) throws JsonProcessingException {
		logger.info("/events required");
		
		if (!Roles.hasRoles(auth,Roles.EVENTS_VIEW)) {
			DataTables dt = new DataTables();
			dt.setData(new String[] {});
			dt.setRecordsFiltered(0);
			dt.setRecordsTotal(0);
			dt.setDraw(draw);
			return JSONHelper.toString(dt);
		}
		
		LinkedHashMap<String, List<String>> param = new LinkedHashMap<>();
		if (null != correlationid) {
			param.put("filter_correlationid_exact", Arrays.asList(new String[]{correlationid}));
		}

		PagedCollection<Event> events = eventService.getPage(param);

		DataTables dt = new DataTables();
		if (null != events) {
			dt.setData(events.getItems());
			dt.setRecordsFiltered(events.getTotal());
			dt.setRecordsTotal(events.getTotal());
		}
		dt.setDraw(draw);
		return JSONHelper.toString(dt);
	}
	
	@GetMapping(value="/count")
	public @ResponseBody String countEvent(OAuth2Authentication auth, ModelMap model) throws JsonProcessingException{
		if (!Roles.hasRoles(auth,Roles.EVENTS_VIEW))
			throw new NotAuthorizedException();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		
		Map<String, Integer> map = new HashMap<>();
		LinkedHashMap<String, List<String>> filters = new LinkedHashMap<>();
		filters.put("filter_eventdate_ldate", Filters.getFiltersParams(sdf.format(cal.getTime())));
		
		cal.add(Calendar.HOUR_OF_DAY, -1);
		String date = sdf.format(cal.getTime());

		filters.put("filter_eventdate_udate", Filters.getFiltersParams(date));
		map.put("total_events", eventService.getCountEvent(filters).length);
		filters.put("filter_type_exact", Filters.getFiltersParams("Error"));
		map.put("error_events", eventService.getCountEvent(filters).length);				
		
		return JSONHelper.toString(map);
	}

}
