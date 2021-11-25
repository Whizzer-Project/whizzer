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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import ro.allevo.fintpws.util.Invariants;

/**
 * The persistent class for the batchrequests database table.
 * 
 */
@Entity
@Table(schema = "FINDATA", name = "BATCHREQUESTS")
@NamedQueries({
		@NamedQuery(name = "BatchRequestEntity.findAllByGroupKey", query = "SELECT br FROM BatchRequestEntity br "
				+ "WHERE br.groupkey=:key"),
		@NamedQuery(name = "BatchRequestEntity.findGroupKeysByUser", query = "SELECT DISTINCT br.groupkey FROM BatchRequestEntity br "
				+ "WHERE br.userEntity.username = :user"),
		@NamedQuery(name = "BatchRequestEntity.findAllGroupKeys", query = "SELECT DISTINCT br.groupkey FROM BatchRequestEntity br ")

})
@NamedStoredProcedureQuery(name = "createBatchRequest", procedureName = "createbatchrequest", parameters = {
		@StoredProcedureParameter(type = String.class, name = "inqueuename", mode = ParameterMode.IN),
		@StoredProcedureParameter(type = String.class, name = "inmsgtype", mode = ParameterMode.IN),
		@StoredProcedureParameter(type = String.class, name = "ingroupkey", mode = ParameterMode.IN),
		@StoredProcedureParameter(type = String.class, name = "intimekey", mode = ParameterMode.IN),
		@StoredProcedureParameter(type = String.class, name = "infield1val", mode = ParameterMode.IN),
		@StoredProcedureParameter(type = String.class, name = "infield2val", mode = ParameterMode.IN),
		@StoredProcedureParameter(type = String.class, name = "infield3val", mode = ParameterMode.IN),
		@StoredProcedureParameter(type = String.class, name = "infield4val", mode = ParameterMode.IN),
		@StoredProcedureParameter(type = String.class, name = "infield5val", mode = ParameterMode.IN),
		@StoredProcedureParameter(type = String.class, name = "inusername", mode = ParameterMode.IN) })
@Cacheable(false)
public class BatchRequestEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "BatchRequestIdGenerator")
	@TableGenerator(name = "BatchRequestIdGenerator", table = "FINCFG.IDGENLIST", pkColumnName = "TABCOLNAME", valueColumnName = "IDVALUE", pkColumnValue = "BATCHREQUESTS_REQUESTID")
	private String requestid;
	@Column(length = 35)
	private String batchuid;
	@Column(length = 100)
	private String groupkey;
	@Column
	private Integer userid;

	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "USERID")
	private UserEntity userEntity;

	public BatchRequestEntity() {
	}

	public String getBatchuid() {
		return this.batchuid;
	}

	public String getGroupkey() {
		return this.groupkey;
	}

	public void setGroupkey(String groupkey) {
		this.groupkey = groupkey;
	}

	public UserEntity getUserEntity() {
		return userEntity;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	@Override
	public String toString() {
		return "BatchRequestEntity [requestid=" + requestid + ", batchuid=" + batchuid + ", groupkey=" + groupkey
				+ ", userid=" + userid + ", userEntity=" + userEntity + "]";
	}

	@Override
	public String getClassEvent() {
		return Invariants.messageClassEvents.BATCH.toString();
	}
	
	@Override
	public String getMessage() {
		return "batch request";
	}

}