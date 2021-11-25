package ro.allevo.fintpws.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import ro.allevo.fintpws.util.Invariants;
import ro.allevo.fintpws.util.annotations.URLId;

@Entity
@Table(schema = "FINCFG", name = "MESSAGETYPES")
@NamedQueries({ 
	@NamedQuery(name = "BusinessAreaEntity.findById", query = "select new ro.allevo.fintpws.model.BusinessAreaEntity(m.businessArea) from MsgTypeListEntity m where m.businessArea = :id"),
	@NamedQuery(name = "BusinessAreaEntity.findAll", query = "select distinct m.businessArea from MsgTypeListEntity m order by m.businessArea asc")
	})
public class BusinessAreaEntity extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="businessarea")
	@URLId
	private String name;

	public BusinessAreaEntity() {
		
	}
	
	public BusinessAreaEntity(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "BusinessAreaEntity [name=" + name + "]";
	}

	@Override
	public String getClassEvent() {
		return Invariants.configClassEvents.MANAGE.toString();
	}
	
	@Override
	public String getMessage() {
		return "business area";
	}
}
