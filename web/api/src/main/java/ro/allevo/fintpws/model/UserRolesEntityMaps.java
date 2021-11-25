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

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import ro.allevo.fintpws.util.annotations.URLId;

@Entity
@Table(schema = "fincfg", name = "userrolesentitymaps")
@NamedQueries({
		@NamedQuery(name = "UserRolesEntityMaps.findByUserIdAndRole", query = "select distinct(ur.entityId) from UserRolesEntityMaps ur where ur.userId=:userId and ur.roleId = :roleId"),
		@NamedQuery(name = "UserRolesEntityMaps.findByUserId", query = "select ur from UserRolesEntityMaps ur where ur.userId=:userId"),
		@NamedQuery(name = "UserRolesEntityMaps.deleteAllForId", query = "DELETE from UserRolesEntityMaps ur where ur.userId=:id"),
//	@NamedQuery(name = "UserRoleEntity.findAllForUser", query = "SELECT ur FROM UserRoleEntity ur where ur.userId=:userid"),
//	@NamedQuery(name = "UserRoleEntity.findTotalForUser", query = "SELECT count(ur.id) FROM UserRoleEntity ur where ur.userId=:userid"),
//	@NamedQuery(name = "UserRoleEntity.deleteAllForId", query = "DELETE from UserRoleEntity ur where ur.userId=:id"),
//	@NamedQuery(name = "UserRoleEntity.isSuperviser", query = "SELECT count(udr) from RoleEntity udr join UserRoleEntity urm on udr.id = urm.roleId where urm.userId=:userid and udr.name='Superviser'"), 
})
@Cacheable(false)
public class UserRolesEntityMaps extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@GeneratedValue(generator = "GenRolesEntity")
	@TableGenerator(name = "GenRolesEntity", table = "FINCFG.IDGENLIST", pkColumnName = "TABCOLNAME", valueColumnName = "IDVALUE", pkColumnValue = "USERROLESENTITYMAPS_ID")
	@Id
	@URLId
	private long id;

	@Column(precision = 38, updatable = false, name = "userid")
	private long userId;

	@Column(precision = 38, updatable = false, name = "roleid")
	private long roleId;

	@Column(precision = 38, updatable = false, name = "entityid")
	private long entityId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	public long getEntityId() {
		return entityId;
	}

	public void setEntityId(long entityId) {
		this.entityId = entityId;
	}

	@Override
	public String toString() {
		return "UserRolesEntityMaps[id=" + id + ", userId=" + userId + ", roleId=" + roleId + ", entityId=" + entityId
				+ "]";
	}

	@Override
	public String getClassEvent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return null;
	}

}
