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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import ro.allevo.fintpws.util.Invariants;
import ro.allevo.fintpws.util.annotations.MergeEntity;
import ro.allevo.fintpws.util.annotations.URLId;

//import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(schema = "FINCFG", name="ROLES")
@Cacheable(false)
@NamedQueries({
	@NamedQuery(name = "RoleEntity.findAll", query = "SELECT r FROM RoleEntity r order by r.id asc"),
	@NamedQuery(name = "RoleEntity.findTotal", query = "SELECT count(r) FROM RoleEntity r"),
	@NamedQuery(name = "RoleEntity.findById", query = "SELECT r FROM RoleEntity r WHERE r.id=:id"),
	@NamedQuery(name = "RoleEntity.findUserAuthorities", 
		query = "SELECT r " +
			"FROM RoleEntity r, UserRoleEntity ur " +
			"WHERE ur.userId= :userid AND r.id = ur.roleId " +
			"order by r.id asc"),
	@NamedQuery(name = "RoleEntity.findTotalUserAuthorities", 
		query = "SELECT count(r.id) " +
			"FROM RoleEntity r, UserRoleEntity ur " +
			"WHERE ur.userId = :userid AND r.id = ur.roleId ")
})

public class RoleEntity extends BaseEntity{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="RoleGenerator")
	@TableGenerator(name="RoleGenerator", table="FINCFG.IDGENLIST",
		pkColumnName="TABCOLNAME", valueColumnName="IDVALUE",
		pkColumnValue="ROLES_ID")
	@Column(unique=true, nullable=false, updatable=false)
	@URLId
	private long id;
	
	@Column(length = 100, nullable = false)
	private String name;
	
	@Column(updatable=false)
	private long userDefined = 1;
	
	@Column(name="listofactions")
	private String actions;
	
	@OneToMany(targetEntity = UserDefinedRoleEntity.class, cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "roleid")
	@MergeEntity
	private List<UserDefinedRoleEntity> userDefinedRoles;
	
	@Column(name="listofentities")
	private Boolean listOfEntities;
	
	public Boolean getListOfEntities() {
		return listOfEntities;
	}

	public void setListOfEntities(Boolean listOfEntities) {
		this.listOfEntities = listOfEntities;
	}

	public long getId(){
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getUserDefined() {
		return userDefined;
	}
	
	public String[] getActions() {
		if (null != actions && !actions.isEmpty())
			return actions.split(",");
		
		return null;
	}
	
	public List<UserDefinedRoleEntity> getUserDefinedRoles() {
		return userDefinedRoles;
	}

	public void setUserDefinedRoles(List<UserDefinedRoleEntity> userDefinedRoles) {
		this.userDefinedRoles = userDefinedRoles;
	}

	@Override
	public String toString() {
		return "RoleEntity [id=" + id + ", name=" + name + ", userDefined=" + userDefined + ", actions=" + actions
				+ ", userDefinedRoles=" + userDefinedRoles + "]";
	}

	@Override
	public String getClassEvent() {
		return Invariants.userClassEvents.MANAGE.toString();
	}
	
	@Override
	public String getMessage() {
		return "role";
	}
}
