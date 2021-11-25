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

import static javax.persistence.ParameterMode.IN;
import static javax.persistence.ParameterMode.OUT;
import static javax.persistence.ParameterMode.REF_CURSOR;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.xml.bind.annotation.XmlRootElement;

import ro.allevo.fintpws.util.Invariants;


/**
 * The persistent class for the RICBIC database table.
 * 
 */

@Entity
@Table(schema = "FINCFG",name = "RICBIC")
@NamedStoredProcedureQueries({
		@NamedStoredProcedureQuery(name = "getbicinfo", procedureName = "getbicinfo", resultClasses = RicbicEntity.class, parameters = {

				@StoredProcedureParameter(name = "inRIC", type = String.class, mode = IN),
				@StoredProcedureParameter(name = "outRetCursor", type = void.class, mode = REF_CURSOR) }),
		@NamedStoredProcedureQuery(name = "getriccode", procedureName = "getriccode", resultClasses = String.class, parameters = {
				@StoredProcedureParameter(name = "inBIC", type = String.class, mode = IN), 
				@StoredProcedureParameter(name = "outRicCode", type = String.class, mode = OUT)})
		
})
@NamedQueries({
		@NamedQuery(name = "RicbicEntity.findAll", query = "SELECT r FROM RicbicEntity r ORDER BY r.bic ASC"),
		@NamedQuery(name = "RicbicEntity.findById", query = "SELECT r FROM RicbicEntity r WHERE r.ric=:id"),
		@NamedQuery(name = "RicbicEntity.findTotal", query = "SELECT count(r.ric) FROM RicbicEntity r") })
@XmlRootElement
@Cacheable(false)
public class RicbicEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	private String ric;

	private String bic;

	@GeneratedValue(generator = "RbidGenerator")
	@TableGenerator(name = "RbidGenerator", table = "FINCFG.IDGENLIST", pkColumnName = "TABCOLNAME", valueColumnName = "IDVALUE", pkColumnValue = "RICBIC_RIC")
	private Integer id;

	private String name;

	private String details;

	public RicbicEntity() {
	}

	public String getRic() {
		return this.ric;
	}

	public void setRic(String ric) {
		this.ric = ric;
	}

	public String getBic() {
		return this.bic;
	}

	public void setBic(String bic) {
		this.bic = bic;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	@Override
	public String toString() {
		String res = "";
		res += getRic();
		return res;
	}

	@Override
	public String getClassEvent() {
		return Invariants.configClassEvents.MANAGE.toString();
	}
	
	@Override
	public String getMessage() {
		return "ric bic";
	}

}