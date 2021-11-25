package ro.allevo.fintpui.dao;

import org.springframework.stereotype.Service;

import ro.allevo.fintpui.model.EventsProcedureCallCursor;

@Service
public interface EventsProcedureCallDao {
	
	public EventsProcedureCallCursor callProcedure(String guid);

}
