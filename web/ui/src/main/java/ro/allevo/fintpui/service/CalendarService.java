package ro.allevo.fintpui.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.dao.CalendarRestApiDao;
import ro.allevo.fintpui.model.Calendar;
import ro.allevo.fintpui.utils.PagedCollection;

@Service
public class CalendarService {
	
	@Autowired
	private CalendarRestApiDao calendarRestApiDao;
	
	
	
	public Calendar[] getAllCalendar() {
		return calendarRestApiDao.getAllCalendar();
	}
	
	public Calendar[] getAllCalendar(LinkedHashMap<String, List<String>> params) {
		return calendarRestApiDao.getAllCalendar(params);
	}
	
	public PagedCollection<Calendar> getPage() {
		return calendarRestApiDao.getPage();
	}

	public Calendar getCalendar(String nonbusinessdate) {
		return calendarRestApiDao.getCalendar(nonbusinessdate);
	}
	
	public ArrayList<String> getAllFreeDates(LinkedHashMap<String, List<String>> params) {
		Calendar[] calendars;
		if (null == params)
			calendars =  getAllCalendar();
		else
			calendars = getAllCalendar(params);
		ArrayList<String> result = new ArrayList<>();
 		for(Calendar calendar : calendars){
			result.add(calendar.getNonbusinessdate());
		}
		return result;
	}
	
	
	public ResponseEntity<String> deleteFreeDate(String date) {
		return calendarRestApiDao.deleteFreeDate(date);
	}
	
	public ResponseEntity<String> deleteFreeDate(List<Date> calendar) {
		return calendarRestApiDao.deleteFreeDate(calendar);
	}
	
	public ResponseEntity<String> insertFreeDate(Calendar[] calendar) {
		return calendarRestApiDao.selWeekends(calendar);
	}
	
}
