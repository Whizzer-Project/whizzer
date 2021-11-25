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

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;

import ro.allevo.fintpws.config.Config;
import ro.allevo.fintpws.util.Invariants;
import com.fasterxml.jackson.annotation.JsonFormat;

import ro.allevo.fintpws.util.annotations.URLId;


/**
 * The persistent class for the TIMELIMITS database table.
 * 
 */
@Entity
@Table(schema = "FINCFG", name="TIMELIMITS")
@NamedQueries({
	@NamedQuery(name = "TimeLimitEntity.findById", query = "SELECT t FROM TimeLimitEntity t "
			+ "WHERE t.id = :id"),
	@NamedQuery(name = "TimeLimitEntity.findAll", query = "SELECT t FROM TimeLimitEntity t order by t.name asc"),
	@NamedQuery(name = "TimeLimitEntity.findTotal", query = "SELECT count(t.id) FROM TimeLimitEntity t"),
	@NamedQuery(name = "TimeLimitEntity.findAllRoutingSchema", query = "SELECT t FROM TimeLimitEntity t "
			+ "WHERE t.id=:startlimit or t.id=:stoplimit order by t.id asc"),
	@NamedQuery(name = "TimeLimitEntity.findTotalRoutingSchema", query = "SELECT count (t.id) FROM TimeLimitEntity t "
			+ "WHERE t.id=:startlimit or t.id=:stoplimit")
	
})
@Cacheable(false)
public class TimeLimitEntity extends BaseEntity{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator="TlGuidGenerator")
	@TableGenerator(name="TlGuidGenerator", table="FINCFG.IDGENLIST",
	pkColumnName="TABCOLNAME", valueColumnName="IDVALUE",
	pkColumnValue="TIMELIMITS_ID") 
	@Column(unique=true, nullable=false, precision=10)
	private long id;

/*	
	@OneToMany(targetEntity = RoutingSchemaEntity.class, cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY)
*/
	
	@Column(length=100)
	@URLId
	private String name;

	@Column
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private Timestamp cutoff;
	
	@OneToMany(mappedBy="startLimitEntity")
	private List<RoutingSchemaEntity> routingSchemasStart;

	@OneToMany(mappedBy="stopLimitEntity")
	private List<RoutingSchemaEntity> routingSchemasStop;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCutoff() {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm:ss");
		return cutoff.toLocalDateTime().toLocalTime().format(format);
	}

	public void setCutoff(Timestamp cutoff) {
		this.cutoff = cutoff;
	}
	
	public void setCutoff(String cutoff) {
		this.cutoff = Timestamp.valueOf(LocalDateTime.of(LocalDate.of(1970, 01, 01), LocalTime.parse(cutoff)));
	}

	@Override
	public String toString() {
		return "TimeLimitEntity [id=" + id + ", name=" + name + ", cutoff=" + cutoff + ", routingSchemasStart="
				+ routingSchemasStart + ", routingSchemasStop=" + routingSchemasStop + "]";
	}

	@Override
	public String getClassEvent() {
		return Invariants.configClassEvents.MANAGE.toString();
	}

	@Override
	public String getMessage() {
		return "routing time limit";
	}

	
}
