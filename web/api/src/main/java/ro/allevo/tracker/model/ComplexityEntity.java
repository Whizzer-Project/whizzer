package ro.allevo.tracker.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(schema = "FINTRACK", name="ROUTINGSCHEMAANALYSIS")
@NamedQueries({
	@NamedQuery(name = "ComplexityEntity.findAll", query = "select c from ComplexityEntity c")
	})
public class ComplexityEntity {

	@Id
	@Column(name = "routingschemaid")
	private long id;
	
	@Column(name = "routingschemaname")
	private String routingSchemaName;
	
	@Column(name = "updatedate")
	private String updateDate;
	
	private String details;
	
	@Column(name = "noroutingrules")
	private int routingRules;
	
	private int noactionslow;
	
	private int noactionsmedium;
	
	private int noactionshigh;
	
	private int novalidationslow;
	
	private int novalidationsmedium;
	
	private int novalidationshigh;
	
	private int noservicelow;
	
	private int noservicemedium;
	
	private int noservicehigh;

	public String getRoutingSchemaName() {
		return routingSchemaName;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public String getDetails() {
		return details;
	}

	public int getRoutingRules() {
		return routingRules;
	}
	
	interface ILowMediumHigh {
		int getLow();
		int getMedium();
		int getHigh();
	}
	
	public ILowMediumHigh getActions() {
		return new ILowMediumHigh() {
			public int getLow() {
				return noactionslow;
			}
			
			public int getMedium() {
				return noactionsmedium;
			}
			
			public int getHigh() {
				return noactionshigh;
			}
		};
	}
	
	public ILowMediumHigh getValidations() {
		return new ILowMediumHigh() {
			public int getLow() {
				return novalidationslow;
			}
			
			public int getMedium() {
				return novalidationsmedium;
			}
			
			public int getHigh() {
				return novalidationshigh;
			}
		};
	}
	
	public ILowMediumHigh getServices() {
		return new ILowMediumHigh() {
			public int getLow() {
				return noservicelow;
			}
			
			public int getMedium() {
				return noservicemedium;
			}
			
			public int getHigh() {
				return noservicehigh;
			}
		};
	}
}
