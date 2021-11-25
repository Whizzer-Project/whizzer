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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ro.allevo.fintpws.util.Invariants;
import ro.allevo.fintpws.util.annotations.URLId;

@Entity
@Table(schema = "FINCFG", name="USERROLESMAPS")
@NamedQueries({
	@NamedQuery(name = "UserRoleEntity.findById", query = "select ur from UserRoleEntity ur where ur.id=:id"), 
	@NamedQuery(name = "UserRoleEntity.findAllForUser", query = "SELECT ur FROM UserRoleEntity ur where ur.userId=:userid"),
	@NamedQuery(name = "UserRoleEntity.findTotalForUser", query = "SELECT count(ur.id) FROM UserRoleEntity ur where ur.userId=:userid"),
	@NamedQuery(name = "UserRoleEntity.deleteAllForId", query = "DELETE from UserRoleEntity ur where ur.userId=:id"),
	@NamedQuery(name = "UserRoleEntity.isSuperviser", query = "SELECT count(udr) from RoleEntity udr join UserRoleEntity urm on udr.id = urm.roleId where urm.userId=:userid and udr.name='Supervise'"), 
})
@Cacheable(false)
public class UserRoleEntity extends BaseEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public enum Action {
		VIEW("view"),
		MODIFY("modify"),
		OPERATE("operate"),
		CREATE_EDIT("createEdit");
		
		private String action;
		
		private Action(String action) {
			this.action = action;
		}
		
		@Override
		public String toString() {
			return action;
		}
	}


	@Id
	@GeneratedValue(generator="UserRoleGenerator")
	@TableGenerator(name="UserRoleGenerator", table="FINCFG.IDGENLIST",
	pkColumnName="TABCOLNAME", valueColumnName="IDVALUE",
	pkColumnValue="USERROLESMAPS_ID") 
	@Column(unique=true, nullable=false, precision=38, updatable=false)
	@URLId
	private long id;

	
	@Column(precision = 38, updatable = false, name="userid")
	private long userId;

	@Column(precision = 38, updatable = false, name="roleid")
	private long roleId;
	
	@Column(nullable=false, length = 35)
	private String action;
	
	@OneToOne(targetEntity = UserEntity.class, cascade = { CascadeType.REFRESH }, fetch = FetchType.LAZY)
	@JoinColumn(name = "userid", referencedColumnName = "id", insertable = false, updatable = false)
	@Fetch(FetchMode.JOIN)
	private UserEntity userEntity;
	
	@OneToOne(targetEntity = RoleEntity.class, cascade = { CascadeType.REFRESH }, fetch = FetchType.LAZY)
	@JoinColumn(name = "roleid", referencedColumnName = "id", insertable = false, updatable = false)
	@Fetch(FetchMode.JOIN)
	private RoleEntity roleEntity;
	
//	@OneToOne(targetEntity = UserRolesEntityMaps.class, cascade = { CascadeType.REFRESH }, fetch = FetchType.LAZY)
//	@JoinColumn(name = "roleid", referencedColumnName = "roleid", insertable = false, updatable = false)
//	@Fetch(FetchMode.JOIN)
//	private UserRolesEntityMaps userRolesEntityMaps;
//	
//	public UserRolesEntityMaps getUserRolesEntityMaps() {
//		return userRolesEntityMaps;
//	}

	public long getId(){
		return id;
	}
	
	public long getUserId(){
		return userId;
	}
	
	public void setUserId(long userid){
		this.userId = userid;
	}
	
	public long getRoleId(){
		return roleId;
	}
	
	public void setRoleId(long roleid){
		this.roleId = roleid;
	}
	
	public String getAction() {
		return action;
	}
	
	public void setAction(String action) {
		this.action = action;
	}

	@JsonIgnore
	public UserEntity getUserEntity() {
		return userEntity;
	}

	public RoleEntity getRoleEntity() {
		return roleEntity;
	}

	@Override
	public String toString() {
		return "UserRoleEntity [id=" + id + ", userId=" + userId + ", roleId=" + roleId + ", action=" + action
				+ ", userEntity=" + userEntity + ", roleEntity=" + roleEntity + "]";
	}
	
	@Override
	public String getClassEvent() {
		return Invariants.userClassEvents.MANAGE.toString();
	}

	@Override
	public String getMessage() {
		return "user roles";
	}
	
}
