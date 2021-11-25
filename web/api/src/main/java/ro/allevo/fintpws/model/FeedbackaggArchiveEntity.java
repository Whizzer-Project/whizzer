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

package ro.allevo.fintpws.model;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the FEEDBACKAGG database table.
 * 
 */
@Entity
@Table(schema = "FINARCH", name = "FEEDBACKAGG")

@NamedQueries({
	@NamedQuery(name = "FeedbackaggArchiveEntity.findByCorrelId", query = "select f from FeedbackaggArchiveEntity f where f.correlationId=:id")
	})

@Cacheable(false)
public class FeedbackaggArchiveEntity  implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(length = 30, name="correlationid")
	private String correlationId;

	@Lob()
	private String payload;

	public String getPayload() {
		return this.payload;
	}

	public String getCorrelationId() {
		return correlationId;
	}
}
