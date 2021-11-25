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

package ro.allevo.auth.model;

public class UserRoleEntity extends BaseEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long id;
	private long userId;
	private long roleId;
	private String action;
	private RoleEntity roleEntity;
	
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

	public RoleEntity getRoleEntity() {
		return roleEntity;
	}

	@Override
	public String toString() {
		return "UserRoleEntity [id=" + id + ", userId=" + userId + ", roleId=" + roleId + ", action=" + action
				+ ", roleEntity=" + roleEntity + "]";
	}
	
}
