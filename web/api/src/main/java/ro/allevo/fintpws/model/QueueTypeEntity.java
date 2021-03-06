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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import ro.allevo.fintpws.util.Invariants;
import ro.allevo.fintpws.util.annotations.URLId;

/**
 * The persistent class for the QUEUETYPES database table.
 * 
 */
@Entity
@Table(schema = "FINCFG", name = "QUEUETYPES")
@NamedQueries({
		@NamedQuery(name = "QueueTypeEntity.findById", query = "SELECT t FROM QueueTypeEntity t "
				+ "WHERE t.id = :id"),
		@NamedQuery(name = "QueueTypeEntity.findAll", query = "SELECT t FROM QueueTypeEntity t order by t.name asc"),
		@NamedQuery(name = "QueueTypeEntity.findTotal", query = "SELECT count(t.name) FROM QueueTypeEntity t"), })
public class QueueTypeEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique = true, nullable = false, precision = 22)
	private long id;

	@Column(nullable = false, length = 50)
	@URLId
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}

	@Override
	public String toString() {
		String res = "";
		res += getName();
		return res;
	}

	@Override
	public String getClassEvent() {
		return Invariants.queueClassEvents.MANAGE.toString();
	}
	
	@Override
	public String getMessage() {
		return "queue type";
	}

}
