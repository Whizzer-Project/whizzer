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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.model.MessageType;
import ro.allevo.fintpui.model.Queue;
import ro.allevo.fintpui.model.QueueMessageGroups;
import ro.allevo.fintpui.model.QueueMessageGroupsFilter;
import ro.allevo.fintpui.model.QueuesCountEntity;
import ro.allevo.fintpui.model.QueuesDuplicate;
import ro.allevo.fintpui.model.RoutingJobParameters;
import ro.allevo.fintpui.model.UserAction;
import ro.allevo.fintpui.utils.PagedCollection;
import ro.allevo.fintpui.utils.Pair;


@Service
public interface QueueService {

	/**
	 * Inserts a new queue 
	 * @param Queue queue
	 */
	public void insertQueue(Queue queue);
	/**
	 * Returns a list of all queues
	 * @return Queue[] 
	 */
	public Queue[] getQueueList();
	
	public PagedCollection<Queue> getPage();
	
	/**
	 * Return list of names of queues
	 * @return
	 */
	public ArrayList<String> getQueuesNames();
	
	/**
	 * Deletes the queue with the specified name
	 * @param String queueName
	 */
	public void deleteQueue(String queueName);
	/**
	 * Returns queue with the given name 
	 * @param queueName
	 * @return Queue
	 */
	public Queue getQueue(String queueName);
	/**
	 * Modifies queue with specified name with new details
	 * @param queueName
	 * @param queue
	 */
	public void updateQueue(String queueName, Queue queue);
	/**
	 * Reutrns the distinct types of queues in the application
	 * @return 
	 */
	public List<Pair<String, String>> getQueueTypes();
	/**
	 * Reutrns the distinct types of messages in the specified queue
	 */
	public MessageType[] getMessageTypesInQueue(String queueName);
	public MessageType[] getMessageTypesInQueue(String queueName, LinkedHashMap<String, List<String>> params);
	//public ArrayList<Boolean> getIsParrentMessageInQueue(String queueName);
	//public ArrayList<String> getChildMessageTypes(String queueName);
	//public ArrayList<ArrayList<String>> getAllQueues();
	public List<HashMap<String, Object>> getTransactionsFinale();
	public List<HashMap<String, Object>> getTransactionsInterm();
	public UserAction[] getSelectionActions(String queueName, String messageType);
	public UserAction[] getGroupActions(String queueName, String messageType);
	public LinkedHashMap<Integer,String> getAllQueuesNamesAndId();
	public void sendRoutingJobs(String queueName, RoutingJobParameters parameters);
	public QueuesCountEntity[] getQueuesCount();
	public void updatePayload(String id, String correlationId, String payload);
	public QueuesDuplicate[] getDuplicateMSG(String id, String queue);
	public Queue getQueueByTransaction(String queueName);
	public List<HashMap<String, Object>> getTransactions(String category);
	public List<Pair<String, String>> getAllQueueTypes();
	QueueMessageGroupsFilter[] getQueueMessageGroupsFilter();
	QueueMessageGroups[] getQueueMessageGroups();
	
}
