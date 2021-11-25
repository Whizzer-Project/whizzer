package ro.allevo.tracker.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(schema = "FINTRACK", name="LIVEPERFORMANCE")
@NamedQueries({
	@NamedQuery(name = "LivePerformanceEntity.findById", query = "select l from LivePerformanceEntity l "
			+ "where l.id=:id"),
	@NamedQuery(name = "LivePerformanceEntity.findIntervalsByDate", query = "select l from LivePerformanceEntity l "
			+ "where l.date = cast(:date as date) "
			+ "order by l.interval"),
	})
public class LivePerformanceEntity {

	@Id
	private long id;
	
	@Column(name = "reportdate")
	private Date date;
	
	@Column(name = "timestampsinterval")
	private String interval;
	
	private int initialrj;
	
	private int finalrj;
	
	private int processedrj;
	
	private String listinitialrj;
	
	private String listfinalrj;
	
	private String listprocessedrj;
	
	@Column(name = "errorevents")
	private int errors;

	public long getId() {
		return id;
	}
	
	public String getDate() {
		return date.toLocalDate().toString();
	}

	public String getInterval() {
		return interval;
	}

	public int getErrors() {
		return errors;
	}
	
	interface IRoutingJobs {
		int getInitial();
		int getFinal();
		int getProcessed();
	}
	
	public IRoutingJobs getRoutingJobs() {
		return new IRoutingJobs() {
			public int getInitial() {
				return initialrj;
			}
			
			public int getFinal() {
				return finalrj;
			}
			
			public int getProcessed() {
				return processedrj;
			}
		};
	}
	
	interface IDetailedRoutingJobs {
		String[] getInitial();
		String[] getFinal();
		String[] getProcessed();
	}
	
	public IDetailedRoutingJobs getDetailedRoutingJobs() {
		return new IDetailedRoutingJobs() {
			public String[] getInitial() {
				if (null != listinitialrj)
					return listinitialrj.split("<br>");
				
				return null;
			}
			
			public String[] getFinal() {
				if (null != listfinalrj)
					return listfinalrj.split("<br>");
				
				return null;
			}
			
			public String[] getProcessed() {
				if (null != listprocessedrj)
					return listprocessedrj.split("<br>");
				
				return null;
			}
		};
	}
}
