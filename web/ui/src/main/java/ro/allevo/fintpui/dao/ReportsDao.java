package ro.allevo.fintpui.dao;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import ro.allevo.fintpui.utils.PagedCollection;

@Service
public interface ReportsDao {
	public ObjectNode[] getTransactionsTableHeaders(String messageType);
	public ObjectNode[] getTransactionsGroupHeaders(String messageType);
	public ArrayNode getTransasctionsInGroup(String queueName, String messageType);
	
	public ObjectNode[] getTransactionStates();
	
	public ObjectNode[] getMessageCriteria(String businessArea);
	public ObjectNode[] getMessageResults(String businessArea);
	public ObjectNode[] getFilterResults(String businessArea, String userName);
	public PagedCollection<ObjectNode> getPagedMesssageResults(String businessArea,
			LinkedHashMap<String, List<String>> parameters);
}
