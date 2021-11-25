package ro.allevo.fintpui.controllers;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.exception.NotAuthorizedException;
import ro.allevo.fintpui.model.Calendar;
import ro.allevo.fintpui.service.CalendarService;
import ro.allevo.fintpui.utils.Filters;
import ro.allevo.fintpui.utils.JSONHelper;
import ro.allevo.fintpui.utils.Roles;

@Controller
@RequestMapping(value = "Calendar")
public class CalendarController {

	@Autowired
	Config config;

	@Autowired
	private CalendarService calendarService;


	private static Logger logger = LogManager.getLogger(CalendarController.class.getName());

	@PostMapping(value = "/selWeekends")
	@Consumes(MediaType.APPLICATION_JSON)
	@ResponseBody
	public int selWeekends(@RequestParam("dates") String dates, ModelMap model) throws IOException {

		ObjectMapper objectMapper = new ObjectMapper();
		Calendar[] calendars = objectMapper.readValue(dates, Calendar[].class);
		ResponseEntity<String> stringResponseEntity = calendarService.insertFreeDate(calendars);
		return stringResponseEntity.getStatusCodeValue();

	}

	private String convertDateToSQLDate(String date) { // cast 23/06/2021 to 2021-06-23
		Pattern p = Pattern.compile("[-?0-9]+");
		Matcher dates = p.matcher(date);
		StringBuilder s = new StringBuilder();
		while (dates.find()) {
			if (s.length() != 0 )
				s.insert(0,"-");
			s.insert(0, dates.group());
		}
		return s.toString();
	}

	@PostMapping("/getDates")
	public int getDates(@RequestParam("dates") String dates, ModelMap model) throws ParseException {

		List<Calendar> saveCalendar = new ArrayList<>();
		List<Date> deleteCalendar = new ArrayList<>();
		List<String> freeDates = calendarService.getAllFreeDates(getFilters());
		String[] datesToSave = dates.split(",");
		for (String dateToSave : datesToSave) {
			String castDate = convertDateToSQLDate(dateToSave);
			Calendar calendar = new Calendar();
			Optional<String> dateInDB = freeDates.stream().filter(str -> str.equals(castDate)).findFirst();
			if (!dateInDB.isPresent()) {
				calendar.setNonbusinessdate(castDate);
				calendar.setDescription("free days");
				saveCalendar.add(calendar);
			}
			else{
				calendar.setNonbusinessdate(castDate);
				deleteCalendar.add(calendar.getNonbusinessdateDateFormat());
			}
		}
		
		if (!saveCalendar.isEmpty()) {
			calendarService.insertFreeDate(saveCalendar.toArray(new Calendar[0]));
		}
		if (!deleteCalendar.isEmpty()) {
			calendarService.deleteFreeDate(deleteCalendar);
		}
		return 0;
	}
	
	private String getDate(int year, int day, int mounth) {
		java.util.Calendar cal = java.util.Calendar.getInstance();
		cal.add(java.util.Calendar.YEAR, year);
		cal.set(java.util.Calendar.MONTH, mounth - 1);
		cal.set(java.util.Calendar.DAY_OF_YEAR, day);
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");

		return format1.format(cal.getTime());
	}
	
	//get filter for 7 years
	private LinkedHashMap<String, List<String>> getFilters() {
		String beginDate = getDate(-5, 31, 12);
		String endDate = getDate(5, 31, 12);
		LinkedHashMap<String, List<String>> filters = new LinkedHashMap<>();
		filters.put("filter_nonbusinessdate_ldate", Filters.getFiltersParams(beginDate));
		filters.put("filter_nonbusinessdate_udate",Filters.getFiltersParams(endDate));
		return filters;
	}

	@GetMapping(value = "")
	public String printCalendar(OAuth2Authentication auth, ModelMap model) throws JsonProcessingException {
		logger.info("/calendar required");
		
		if (!Roles.hasRoles(auth, Roles.CONFIGURATION_LISTS_VIEW)) {
			throw new NotAuthorizedException(); 
		}
		
		boolean showButton = true;
		if (!Roles.hasRoles(auth, Roles.CONFIGURATION_LISTS_MODIFY)) {
			showButton = false; 
		}
		
		LinkedHashMap<String, List<String>> filters = getFilters();
		model.addAttribute("allDates", JSONHelper.toString(calendarService.getAllFreeDates(filters)));
		model.addAttribute("apiUri", config.getAPIUrl());
		model.addAttribute("showButton", showButton);
		return "calendar";
	}

}
