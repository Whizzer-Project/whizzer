package ro.allevo.tracker.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(schema = "FINTRACK", name="COMPONENTPERFORMANCE")
@NamedQueries({
	@NamedQuery(name = "ComponentPerformanceEntity.findByTimestamp", query = "select o from ComponentPerformanceEntity o "
			+ "where o.date=cast(:date as date) and o.time=:time "
			+ "and (:name = '' or o.componentname = :name)"
			+ "and (:thread = '' or o.componentthread = :thread)"),
	
	@NamedQuery(name = "ComponentPerformanceEntity.findTimestampsByDate", query = "select distinct o.time "
			+ "from ComponentPerformanceEntity o "
			+ "where o.date = cast(:date as date) "
			+ "order by o.time"),
	})
public class ComponentPerformanceEntity {
	@Id
	private long id;
	
	@Column(name = "reportdate")
	private Date date;
	
	@Column(name = "timestamps")
	private String time;
	
	private String componentname;
	
	private String componentthread;
	
	private String componentcategory;
	
	@Column(name = "idletime")
	private long idleTime;
	
	@Column(name = "processingtime")
	private long processingTime;
	
	@Column(name = "processingrate")
	private float processingRate;
	
	private long noofevents;
	
	private long nooferrorevents;
	
	private long noofmanagementevents;
	
	private String processingtimeintervals;
	
	private long userid;
	
	public String getDate() {
		return date.toLocalDate().toString();
	}

	public String getTime() {
		return time;
	}

	public long getIdleTime() {
		return idleTime;
	}

	public long getProcessingTime() {
		return processingTime;
	}

	public float getProcessingRate() {
		return processingRate;
	}

	public String[] getProcessingTimeIntervals() {
		if (null != processingtimeintervals)
			return processingtimeintervals.split("; ");
		
		return null;
	}
	
	interface IEvents {
		long getNo();
		long getError();
		long getManagement();
	}
	
	public IEvents getEvents() {
		return new IEvents() {
			public long getNo() {
				return noofevents;
			}
			
			public long getError() {
				return nooferrorevents;
			}
			
			public long getManagement() {
				return noofmanagementevents;
			}
		};
	}
	
	interface IComponent {
		String getName();
		String getThread();
		String getCategory();
	}
	
	public IComponent getComponent() {
		return new IComponent() {
			public String getName() {
				return componentname;
			}
			
			public String getThread() {
				return componentthread;
			}
			
			public String getCategory() {
				return componentcategory;
			}
		};
	}
	
	protected void setDate(Date date) {
		this.date = date;
	}
	
	protected void setTime(String time) {
		this.time = time;
	}
}
