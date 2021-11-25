package ro.allevo.fintpui.service;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.dao.EventsProcedureCallRestApiDao;
import ro.allevo.fintpui.model.EventsProcedureCallCursor;
import ro.allevo.fintpui.utils.PagedCollection;

@Service
public class EventsProcedureCallService {
	
	@Autowired
	private EventsProcedureCallRestApiDao eventsProcedureCallRestApiDao;
	
	public EventsProcedureCallCursor getCallProcedureResult(String guid) {
		return eventsProcedureCallRestApiDao.get(guid);
	}
	
	public PagedCollection<EventsProcedureCallCursor> getCallProcedureResult(LinkedHashMap<String, List<String>> params) {
		return eventsProcedureCallRestApiDao.getPage(params);
	}
}
