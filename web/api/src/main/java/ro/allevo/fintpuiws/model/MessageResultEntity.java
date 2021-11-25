package ro.allevo.fintpuiws.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(schema = "FINCFG", name = "reportmessageresults")
@NamedQueries({
	@NamedQuery(name = "MessageResultEntity.findByBusinessArea", query = "SELECT r FROM MessageResultEntity r"
			+ " where r.businessArea=:businessarea order by r.displayOrder"),
	@NamedQuery(name = "MessageResultEntity.findAll", query = "SELECT r FROM MessageResultEntity r")
	})
public class MessageResultEntity {

	@Id
	private long id;
	
	private String label;
	
	private String field;
	
	@Column(name = "displayorder")
	private long displayOrder;
	
	@Column(name = "messagetypesbusinessarea")
	private String businessArea;

	public String getLabel() {
		return label;
	}

	public String getField() {
		return field.split(",")[0];
	}
	
	public String getSortField() {
		String[] fields = field.split(",");
		
		return fields[fields.length - 1];
	}

	public String getBusinessArea() {
		return businessArea;
	}
}
