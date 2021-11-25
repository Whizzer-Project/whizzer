package ro.allevo.fintpui.dao;

import java.sql.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.model.Calendar;

@Service
public interface CalendarDao {
	
	public Calendar[] getAllCalendar();
	public Calendar[] getAllCalendar(LinkedHashMap<String, List<String>> params);
	public Calendar getCalendar(String nonbusinessdate);
	public ResponseEntity<String> deleteFreeDate(String date);
	public ResponseEntity<String> selWeekends(Calendar[] calendar);
	public ResponseEntity<String> deleteFreeDate(List<Date> calendar);

}
