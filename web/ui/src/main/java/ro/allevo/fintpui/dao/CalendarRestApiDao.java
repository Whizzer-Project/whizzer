package ro.allevo.fintpui.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import ro.allevo.fintpui.model.Bank;
import ro.allevo.fintpui.model.Calendar;
import java.net.URI;
import java.sql.Date;
import java.util.LinkedHashMap;
import java.util.List;

import javax.ws.rs.core.UriBuilder;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.config.Config;


@Service
public class CalendarRestApiDao extends RestApiDao<Calendar> implements CalendarDao{
	
	@Autowired
	Config config;
	
	
	
	
	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getAPIUrl()).path("calendar").build();
	}
	
	public CalendarRestApiDao() {
		super(Calendar.class);
	}
	
	@Override
	public Calendar[] getAllCalendar() {
		return getAll();
	}
	
	@Override
	public Calendar[] getAllCalendar(LinkedHashMap<String, List<String>> params) {
		return getAll(params);
	}

	@Override
	public Calendar getCalendar(String nonbusinessdate) {
		//DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		return get(nonbusinessdate);
	}

	@Override
	public ResponseEntity<String> selWeekends(Calendar[] calendar) {
		//URI uri = UriBuilder.fromUri(config.getAPIUrl()).path("calendar").build();
		return post(calendar);
	}
	

	@Override
	public ResponseEntity<String> deleteFreeDate(String date) {
		URI uri = UriBuilder.fromUri(config.getAPIUrl()).path("calendar").build();
		return delete(uri);
	}
	
	@Override
	public ResponseEntity<String> deleteFreeDate(List<Date> calendar) {
		URI uri = UriBuilder.fromUri(config.getAPIUrl()).path("calendar").build();
		return delete(uri, calendar);
	}

}
