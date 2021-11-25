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

package ro.allevo.fintpui.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class QueueMoveMaps {
	private static final long serialVersionUID = 1L;

	private Integer id;

	private Integer sourcequeueid;
	
	private Integer destinationqueueid;
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSourcequeueid() {
		return sourcequeueid;
	}

	public void setSourcequeueid(Integer sourcequeueid) {
		this.sourcequeueid = sourcequeueid;
	}

	public Integer getDestinationqueueid() {
		return destinationqueueid;
	}

	public void setDestinationqueueid(Integer destinationqueueid) {
		this.destinationqueueid = destinationqueueid;
	}

	@Override
	public String toString() {
		return "QueueMoveMaps [id=" + id + ", sourcequeueid=" + sourcequeueid + ", destinationqueueid="
				+ destinationqueueid + "]";
	}


}

