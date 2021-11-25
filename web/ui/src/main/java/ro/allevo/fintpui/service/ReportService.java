package ro.allevo.fintpui.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import ro.allevo.fintpui.dao.ReportsDao;
import ro.allevo.fintpui.model.MessagesGroup;
import ro.allevo.fintpui.utils.PagedCollection;

@Service
public class ReportService {

	@Autowired
	ReportsDao reportsDao;

	public ObjectNode[] getTableHeaders(String messageType) {
		return reportsDao.getTransactionsTableHeaders(messageType);
	}

	public ObjectNode[] getGroupHeaders(String messageType) {
		return reportsDao.getTransactionsGroupHeaders(messageType);
	}

	public List<MessagesGroup> getTransactionsInGroup(String queueName, String messageType) {
		List<MessagesGroup> transactions = new ArrayList<>();

		ArrayNode result = reportsDao.getTransasctionsInGroup(queueName, messageType);

		result.forEach(node -> transactions.add(new MessagesGroup((ArrayNode) node)));

		return transactions;
	}
	
	public Set<String> getReportName(){
		ObjectNode[] allReports = getMessageResults();
		Set<String> reports = new HashSet<>();
		for (ObjectNode report : allReports) {
			reports.add(report.get("businessArea").asText());
		}
		return reports;
	}

	public List<String> getTransactionStatuses() {
		ObjectNode[] nodes = reportsDao.getTransactionStates();

		List<String> statuses = new ArrayList<>();

		for (ObjectNode node : nodes)
			statuses.add(node.get("status").asText());

		return statuses;
	}

	public ObjectNode[] getMessageCriteria(String businessArea) {
		return reportsDao.getMessageCriteria(businessArea);
	}
	
	public ObjectNode[] getMessageResults() {
		return reportsDao.getMessageResults(null);
	}
	
	public ObjectNode[] getMessageResults(String businessArea) {
		return reportsDao.getMessageResults(businessArea);
	}
	
	public ObjectNode[] getFilterResults(String businessArea, String userName) {
		return reportsDao.getFilterResults(businessArea, userName);
	}

	public PagedCollection<ObjectNode> getPagedMessageResults(String businessArea,
			LinkedHashMap<String, List<String>> parameters) {
		if (null == parameters) {
			parameters = new LinkedHashMap<>();
		}
		return reportsDao.getPagedMesssageResults(businessArea, parameters);
	}
}
