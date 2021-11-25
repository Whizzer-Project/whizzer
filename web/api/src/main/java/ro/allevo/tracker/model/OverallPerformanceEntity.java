package ro.allevo.tracker.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(schema = "FINTRACK", name="OVERALLPERFORMANCE")
@NamedQueries({
	@NamedQuery(name = "OverallPerformanceEntity.findByTimestamp", query = "select o from OverallPerformanceEntity o "
			+ "where o.date=cast(:date as date) and o.time=:time"),
	@NamedQuery(name = "OverallPerformanceEntity.findTimestampsByDate", query = "select distinct o.time "
			+ "from OverallPerformanceEntity o "
			+ "where o.date = cast(:date as date) "
			+ "order by o.time"),
	})
public class OverallPerformanceEntity {

	@Id
	private long id;
	
	@Column(name = "reportdate")
	private Date date;
	
	@Column(name = "timestamps")
	private String time;
	
	@Column(name = "businessarea")
	private String businessArea;
	
	@Column(name = "idletime")
	private long idleTime;
	
	@Column(name = "processingtime")
	private long processingTime;
	
	@Column(name = "processingrate")
	private float processingRate;
	
	@Column(name = "nooftrx")
	private long transactions;
	
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

	public String getBusinessArea() {
		return businessArea;
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
	
	public long getTransactions() {
		return transactions;
	}
	
	interface IEvents {
		long getError();
		long getManagement();
	}
	
	public IEvents getEvents() {
		return new IEvents() {
			public long getError() {
				return nooferrorevents;
			}
			
			public long getManagement() {
				return noofmanagementevents;
			}
		};
	}
	
	public String[] getProcessingTimeIntervals() {
		if (null != processingtimeintervals)
			return processingtimeintervals.split("; ");
		
		return null;
	}
	
	protected void setDate(Date date) {
		this.date = date;
	}
	
	protected void setTime(String time) {
		this.time = time;
	}
}
