package ro.allevo.fintpui.dao;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.utils.PagedCollection;

@Service
public class ReportsRestApiDao extends RestApiDao<ObjectNode> implements ReportsDao {

	@Autowired
	Config config;
	
	private static final String REPORTS = "reports";
	private static final String MESSAGETYPE = "messageType";
	private static final String QUEUES = "queues";
	private static final String BUSINESSAREA = "businessArea";

	public ReportsRestApiDao() {
		super(ObjectNode.class);
	}

	@Override
	public URI getBaseUrl() {
		return null;
	}

	@Override
	public ObjectNode[] getTransactionsTableHeaders(String messageType) {
		URI uri = UriBuilder.fromUri(config.getUiUrl()).path(QUEUES).path("table-headers")
				.queryParam(MESSAGETYPE, messageType).build();

		return getList(uri, ObjectNode.class);
	}

	@Override
	public ObjectNode[] getTransactionsGroupHeaders(String messageType) {
		URI uri = UriBuilder.fromUri(config.getUiUrl()).path(QUEUES).path("group-headers")
				.queryParam(MESSAGETYPE, messageType).build();

		return getList(uri, ObjectNode.class);
	}

	@Override
	public ArrayNode getTransasctionsInGroup(String queueName, String messageType) {
		URI uri = UriBuilder.fromUri(config.getUiUrl()).path(QUEUES).path("transactions-in-group")
				.queryParam("queueName", queueName).queryParam(MESSAGETYPE, messageType).build();

		return getObject(uri, ArrayNode.class);
	}

	@Override
	public ObjectNode[] getTransactionStates() {
		URI uri = UriBuilder.fromUri(config.getUiUrl()).path(REPORTS).path("transaction-states").build();

		return getList(uri);
	}

	@Override
	public ObjectNode[] getMessageCriteria(String businessArea) {
		URI uri = UriBuilder.fromUri(config.getUiUrl()).path(REPORTS).path("message-criteria")
				.queryParam(BUSINESSAREA, businessArea).build();

		return getList(uri);
	}

	@Override
	public ObjectNode[] getMessageResults(String businessArea) {
		URI uri = null;
		if (null == businessArea) {
			uri = UriBuilder.fromUri(config.getUiUrl()).path(REPORTS).path("message-results")
					.build();
		}
		else 
			uri = UriBuilder.fromUri(config.getUiUrl()).path(REPORTS).path("message-results")
				.queryParam(BUSINESSAREA, businessArea).build();
		return getList(uri);
	}
	
	public PagedCollection<ObjectNode> getPagedMesssageResults(String businessArea, LinkedHashMap<String, List<String>> parameters){
		URI uri = UriBuilder.fromUri(config.getUiUrl()).path(REPORTS).path("report")
				.path(businessArea).path("messages").build();
		return getPage(uri, parameters);
	}

	@Override
	public ObjectNode[] getFilterResults(String businessArea, String userName) {
		URI uri = UriBuilder.fromUri(config.getUiUrl()).path(REPORTS).path("filters")
				.queryParam(BUSINESSAREA, businessArea).queryParam("user", userName).build();

		return getList(uri);
	}
}
