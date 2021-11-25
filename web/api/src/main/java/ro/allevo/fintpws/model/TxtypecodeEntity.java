
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

import javax.persistence.*;



/**
 * The persistent class for the TXTYPECODES database table.
 * 
 */
@Entity
@Table(schema = "FINCFG", name="TXTYPECODES")
@NamedQueries({
@NamedQuery(name="TxtypecodeEntity.findAll", query="SELECT t FROM TxtypecodeEntity t"),
@NamedQuery(name = "TxtypecodeEntity.findTotal", query = "SELECT count(t.ttc) FROM TxtypecodeEntity t"),
@NamedQuery(name = "TxtypecodeEntity.findByTTC", query = "SELECT t FROM TxtypecodeEntity t WHERE t.ttc = :ttc")
})
@Cacheable(false)
public class TxtypecodeEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String ttc;

	private String description;

	public TxtypecodeEntity() {
	}

	public String getTtc() {
		return this.ttc;
	}

	public void setTtc(String ttc) {
		this.ttc = ttc;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
	@Override
	public String toString(){
		String res = "";
		res+=getTtc();
		return res;
	}

}