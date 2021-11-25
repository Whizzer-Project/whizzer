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

package ro.allevo.fintpui.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.dao.MessageTypesDao;
import ro.allevo.fintpui.dao.QueueRestApiDao;
import ro.allevo.fintpui.dao.QueueTypeDao;
import ro.allevo.fintpui.dao.UserActionDao;
import ro.allevo.fintpui.model.MessageType;
import ro.allevo.fintpui.model.Queue;
import ro.allevo.fintpui.model.QueueMessageGroups;
import ro.allevo.fintpui.model.QueueMessageGroupsFilter;
import ro.allevo.fintpui.model.QueueType;
import ro.allevo.fintpui.model.QueuesCountEntity;
import ro.allevo.fintpui.model.QueuesDuplicate;
import ro.allevo.fintpui.model.RoutingJobParameters;
import ro.allevo.fintpui.model.UserAction;
import ro.allevo.fintpui.utils.PagedCollection;
import ro.allevo.fintpui.utils.Pair;

@Service
public class QueueServiceImpl implements QueueService {

	@Autowired
	QueueRestApiDao queueDao;

	@Autowired
	MessageTypesDao messageTypesDao;

	@Autowired
	UserActionDao userActionDao;

	@Autowired
	QueueTypeDao queueTypeDao;

	@Override
	public Queue getQueue(String queueName) {
		return queueDao.getQueue(queueName);
	}

	@Override
	public Queue getQueueByTransaction(String queueName) {
		return queueDao.getQueueByTransaction(queueName);
	}

	@Override
	public Queue[] getQueueList() {
		return queueDao.getQueueList();
	}

	@Override
	public PagedCollection<Queue> getPage() {
		return queueDao.getPage();
	}

	@Override
	public QueuesCountEntity[] getQueuesCount() {
		return queueDao.getQueuesCount();
	}

	@Override
	public void insertQueue(Queue queue) {
		queueDao.insertQueue(queue);
	}

	@Override
	public void updateQueue(String queueName, Queue queue) {

		queueDao.updateQueue(queueName, queue);
	}

	@Override
	public void deleteQueue(String queueName) {
		queueDao.deleteQueue(queueName);
	}

	@Override
	public List<Pair<String, String>> getQueueTypes() {
		QueueType[] types = queueTypeDao.getQueueTypes();

		List<Pair<String, String>> list = new ArrayList<>();

		for (QueueType type : types)
			if (type.getId() != 2)
				list.add(new Pair<>(type.getId() + "", type.getName()));

		return list;
	}

	@Override
	public List<Pair<String, String>> getAllQueueTypes() {
		QueueType[] types = queueTypeDao.getQueueTypes();

		List<Pair<String, String>> list = new ArrayList<>();

		for (QueueType type : types)
			list.add(new Pair<>(type.getId() + "", type.getName()));

		return list;
	}

	@Override
	public MessageType[] getMessageTypesInQueue(String queueName) {
		return messageTypesDao.getMessageTypesInQueue(queueName);
	}

	@Override
	public MessageType[] getMessageTypesInQueue(String queueName, LinkedHashMap<String, List<String>> params) {

		if (null == params || params.isEmpty())
			return getMessageTypesInQueue(queueName);

		return messageTypesDao.getMessageTypesInQueue(queueName, params);
	}
	
	@Override
	public QueueMessageGroupsFilter[] getQueueMessageGroupsFilter() {
		return queueDao.getQueueMessageGroupsFilter();
	}
	
	@Override
	public QueueMessageGroups[] getQueueMessageGroups() {
		return queueDao.getQueueMessageGroups();
	}
	
//	@Override
//	public ArrayList<Boolean> getIsParrentMessageInQueue(String queueName) {
//		ArrayList<Boolean> result = new ArrayList<>();
//		for(MessageType messageType : messageTypesDao.getMessageTypesInQueue(queueName)){
//			if(null != messageType.getParentMessageType() && !messageType.getParentMessageType().isEmpty()){
//				result.add(true);
//			}else{
//				result.add(false);
//			}
//		}
//		return result;		
//	}
//	
//	@Override
//	public ArrayList<String> getChildMessageTypes(String queueName) {
//		
//		ArrayList<String> result = new ArrayList<>();
//		for(MessageType messageType : messageTypesDao.getMessageTypesInQueue(queueName)){
//			if(null != messageType.getParentMessageType() && !messageType.getParentMessageType().isEmpty()){
//				result.add(messageType.getMessageType());
//				
//			}else{
//				result.add(null);
//			}
//		}
//		return result;		
//	}

	@Override
	public ArrayList<String> getQueuesNames() {
		ArrayList<String> names = new ArrayList<>();
		Queue[] queues = getQueueList();
		for (Queue queue : queues) {
			names.add(queue.getName());
		}
		return names;
	}

	public LinkedHashMap<Integer, String> getAllQueuesNamesAndId() { // LinkedHashMap storage a queue in a sort order
		LinkedHashMap<Integer, String> list = new LinkedHashMap<>();
		Queue[] queues = getQueueList();
		for (Queue queue : queues) {
			list.put(queue.getId(), queue.getName());
		}
		return list;
	}

	public List<HashMap<String, Object>> getTransactionsFinale() {
		//URI url = UriBuilder.from(getBaseUrl()).path("access").build();
		LinkedHashMap<String, List<String>> params = new LinkedHashMap<>();
		params.put("filter_queueTypeId_exact", Arrays.asList("2", "3", "4", "5", "6"));
		return queueDao.getTransaction(params);
	}

	// filter_queueTypeId_exact=1&filter_queueTypeId_exact=7
	public List<HashMap<String, Object>> getTransactionsInterm() {
		LinkedHashMap<String, List<String>> params = new LinkedHashMap<>();
		params.put("filter_queueTypeId_exact", Arrays.asList("1", "7"));
		return queueDao.getTransaction(params);
	}

	public List<HashMap<String, Object>> getTransactions(String category) {
		LinkedHashMap<String, List<String>> params = new LinkedHashMap<>();
		if (null != category) {
			params.put("filter_category_exact", Arrays.asList(category));
		}
		params.put("sort_label", Arrays.asList("asc"));
		return queueDao.getTransaction(params);
	}

	@Override
	public UserAction[] getSelectionActions(String queueName, String messageType) {
		return userActionDao.getSelectionUserActions(queueName, messageType);
	}

	@Override
	public UserAction[] getGroupActions(String queueName, String messageType) {
		return userActionDao.getGroupUserActions(queueName, messageType);
	}

	@Override
	public void sendRoutingJobs(String queueName, RoutingJobParameters parameters) {
		queueDao.sendRoutingJobs(queueName, parameters);
	}

	@Override
	public void updatePayload(String id, String correlationId, String payload) {
		queueDao.updatePayload(id, correlationId, payload);
	}

	@Override
	public QueuesDuplicate[] getDuplicateMSG(String id, String queue) {
		return queueDao.getDuplicatMSGDetails(id, queue);
	}
}
