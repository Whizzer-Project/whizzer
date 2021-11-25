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

import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import ro.allevo.fintpws.util.Invariants;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import ro.allevo.fintpws.util.annotations.URLId;


@Entity
@Table(schema = "FINCFG", name="USERS")
@NamedQueries({
	@NamedQuery(name = "UserEntity.findByUsername", query = "select q from UserEntity q where trim(q.username)=:id"),
	@NamedQuery(name = "UserEntity.findById", query = "select q from UserEntity q where q.id=:id"), 
	@NamedQuery(name = "UserEntity.findAll", query = "SELECT t FROM UserEntity t order by t.username asc"),
	@NamedQuery(name = "UserEntity.findTotal", query = "SELECT count(t.username) FROM UserEntity t"),
	@NamedQuery(name = "UserEntity.deleteAllForId", query = "DELETE from UserEntity u where u.id in :id")
})

@Cacheable(false)
public class UserEntity extends BaseEntity  {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator="UserIdGenerator")
	@TableGenerator(name="UserIdGenerator", table="FINCFG.IDGENLIST",
	pkColumnName="TABCOLNAME", valueColumnName="IDVALUE",
	pkColumnValue="USERS_ID") 
	@Column(unique=true, nullable=false, precision=38, updatable=false)
	private long id;
	
	@Column(length = 256, unique=true)
	@URLId
	private String username;
	
	@Column(length = 150)
	private String email;
	
	public long getId(){
		return id;
	}

	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username){
		this.username = username;
	}
	
	@Override
	public String toString(){
		String res = "";
		res+=getUsername();
		return res;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@OneToMany(mappedBy="userEntity")
	@Fetch(FetchMode.JOIN)
	private List<BatchRequestEntity> batchRequests;

	@Override
	public String getClassEvent() {
		return Invariants.userClassEvents.MANAGE.toString();
	}

	@Override
	public String getMessage() {
		return "user";
	}

}
