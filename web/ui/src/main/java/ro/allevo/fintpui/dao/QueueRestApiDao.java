/*
* FinTP - Financial Transactions Processing Application
* Copyright (C) 2013 Business Information Systems (Allevo) S.R.L.
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>
* or contact Allevo at : 031281 Bucuresti, 23C Calea Vitan, Romania,
* phone +40212554577, office@allevo.ro <mailto:office@allevo.ro>, www.allevo.ro.
*/

package ro.allevo.fintpui.dao;

import java.net.URI;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;

import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.model.Queue;
import ro.allevo.fintpui.model.QueueMessageGroups;
import ro.allevo.fintpui.model.QueueMessageGroupsFilter;
import ro.allevo.fintpui.model.QueuesCountEntity;
import ro.allevo.fintpui.model.QueuesDuplicate;
import ro.allevo.fintpui.model.RoutingJobParameters;
import ro.allevo.fintpui.utils.Utils;
import ro.allevo.fintpui.utils.datatables.DataTableRequest;

@Service
public class QueueRestApiDao extends RestApiDao<Queue> implements QueueDao {
	
	@Autowired
	private Config config;
	
	@Autowired
	private RestOperations client;
	
	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getAPIUrl()).path("queues").build();
	}
	
	public QueueRestApiDao() {
		super(Queue.class);
	}
	
	
	@Override
	public Queue getQueue(String queue) {
		URI uri = UriBuilder.fromUri(getBaseUrl()).path("by-name").path(queue).build();
		return get(uri);
	}
	
	@Override
	public Queue getQueueByTransaction(String queue) {
		URI uri = UriBuilder.fromUri(getBaseUrl()).path("by-name").path(queue).path("transaction").build();
		return get(uri);
	}

	@Override
	public Queue[] getQueueList() {
		return getAll();
	}

	@Override
	public void insertQueue(Queue queue) {
		post(queue);
	}

	
	@Override
	public void updateQueue(String queueName, Queue queue) {
		URI uri = UriBuilder.fromUri(getBaseUrl()).path("by-name").path(queueName).build();
		put(uri, queue);
	}

	@Override
	public void deleteQueue(String queueName) {
		URI uri = UriBuilder.fromUri(getBaseUrl()).path("by-name").path(queueName).build();
		delete(uri);
	}

	@Override
	public void sendRoutingJobs(String queueName, RoutingJobParameters parameters) {
		URI uri = UriBuilder.fromUri(getBaseUrl()).path("by-name").path(queueName).path("routing-jobs").build();
		
		post(uri, parameters);
	}

	@Override
	public QueuesCountEntity[] getQueuesCount() {
		URI uri = UriBuilder.fromUri(config.getUiUrl()).path("queues").path("queues-count").build();
		return getList(uri, QueuesCountEntity.class);
	}
	
	@Override
	public QueueMessageGroupsFilter[] getQueueMessageGroupsFilter() {
		URI uri = UriBuilder.fromUri(config.getUiUrl()).path("queues").path("queue-group-filters").build();
		return getList(uri, QueueMessageGroupsFilter.class);
	}
	
	@Override
	public QueueMessageGroups[] getQueueMessageGroups() {
		URI uri = UriBuilder.fromUri(config.getUiUrl()).path("queues").path("queue-group").build();
		return getList(uri, QueueMessageGroups.class);
	}

	@Override
	public void updatePayload(String id, String correlationId, String payload) {
		URI uri = UriBuilder.fromUri(config.getAPIUrl()).path("messages").path(id).queryParam("correlationId", correlationId).build();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", MediaType.APPLICATION_XML);
		HttpEntity<String> httpEntity = new HttpEntity<>(payload, headers);
		client.put(uri, httpEntity);
		
		//System.out.println(client.exchange(uri, HttpMethod.PUT, httpEntity, String.class));
	}

	public QueuesDuplicate[] getDuplicatMSGDetails(String id, String queue) {
		URI uri = UriBuilder.fromUri(config.getUiUrl()).path("queues")
				.path("queue-duplicates")
				.queryParam("id", id)
				.queryParam("queueName", queue).build();
		
		return getList(uri, QueuesDuplicate.class);
	}

	public List<HashMap<String, Object>> getTransaction(LinkedHashMap<String, List<String>> params) {
		UriBuilder uri = UriBuilder.fromUri(getBaseUrl()).path("access");
		
		if (null == params)
			params = new LinkedHashMap<>();

		// add datatable params
		params.putAll(new DataTableRequest(request).getApiParameters());
		for (String key : params.keySet())
			uri = uri.queryParam(key, params.get(key).toArray());
		
		URI url = uri.build();
		return Utils.getResourceByListHashMap(client.getForEntity(url, String.class));
	}
	
//	PagedCollection<Queue> getTransactionsFinale(LinkedHashMap<String, List<String>> params){
//		URIBuilder url = UriBuilder.fromUri(getBaseUrl())
//		return getPage(url, param);
//	}
}
