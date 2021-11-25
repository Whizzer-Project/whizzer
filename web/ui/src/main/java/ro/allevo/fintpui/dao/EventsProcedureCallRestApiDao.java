package ro.allevo.fintpui.dao;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.model.EventsProcedureCallCursor;

@Service
public class EventsProcedureCallRestApiDao extends RestApiDao<EventsProcedureCallCursor> implements EventsProcedureCallDao{
	
	@Autowired
	Config config;

	public EventsProcedureCallRestApiDao() {
		super(EventsProcedureCallCursor.class);
	}
	
	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getAPIUrl()).path("events").build(); 
	}

	@Override
	public EventsProcedureCallCursor callProcedure(String guid) {
		return get(UriBuilder.fromUri(getBaseUrl()).path(guid).path("call-procedure").build()); //  events/4503/call-procedure
	}

}
