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

import org.springframework.stereotype.Service;
import ro.allevo.fintpui.model.Queue;
import ro.allevo.fintpui.model.QueueMessageGroups;
import ro.allevo.fintpui.model.QueueMessageGroupsFilter;
import ro.allevo.fintpui.model.QueuesCountEntity;
import ro.allevo.fintpui.model.RoutingJobParameters;

@Service
public interface QueueDao {

	public Queue getQueue(String queue);
	public Queue[] getQueueList();
	public void insertQueue(Queue queue);
	public void updateQueue(String queueName, Queue queue);
	public void deleteQueue(String queueName);
	public void updatePayload(String id, String correlaionId, String payload);
	public QueuesCountEntity[] getQueuesCount();
	public void sendRoutingJobs(String queueName, RoutingJobParameters parameters);
	public Queue getQueueByTransaction(String queue);
	public QueueMessageGroupsFilter[] getQueueMessageGroupsFilter();
	public QueueMessageGroups[] getQueueMessageGroups();
}
