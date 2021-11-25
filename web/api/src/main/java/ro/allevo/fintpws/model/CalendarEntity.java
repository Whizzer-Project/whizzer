package ro.allevo.fintpws.model;



import java.sql.Date;
import java.text.SimpleDateFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import ro.allevo.fintpws.util.Invariants;
import ro.allevo.fintpws.util.annotations.URLId;


@Entity
@Table(schema = "FINCFG", name="NONBUSSDAYSCALENDAR")

@NamedQueries({
	@NamedQuery(name = "CalendarEntity.findAll", query = "select b from CalendarEntity b"),
	@NamedQuery(name = "CalendarEntity.findTotal", query = "select count(b.nonbusinessdate) from CalendarEntity b"),
	@NamedQuery(name = "CalendarEntity.deleteAllForId", query = "DELETE from CalendarEntity u where u.nonbusinessdate in :id"), // for list of delete
	})
	

public class CalendarEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	

	@Id
	@GeneratedValue(generator="AidGenerator")
	@Column(unique=true, nullable=false,name = "nonbusinessdate")
	@URLId
	private Date nonbusinessdate;
	

	@Column(length=50, name = "description")
	private String description;

	public Date getNonbusinessdate() {
		return nonbusinessdate;
	}

	public void setNonbusinessdate(String nonbusinessdate) {
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		this.nonbusinessdate = java.sql.Date.valueOf(nonbusinessdate);
	}
	
	public void setNonbusinessdate(Date nonbusinessdate) {
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		this.nonbusinessdate = (nonbusinessdate);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	@Override
	public String toString() {
		return "CalendarEntity [nonbusinessdate=" + nonbusinessdate + ", description=" + description + "]";
	}

	@Override
	public String getClassEvent() {
		return Invariants.configClassEvents.MANAGE.toString();
	}
	
	@Override
	public String getMessage() {
		return "calendar";
	}
}