package ro.allevo.fintpuiws.model;

import java.io.IOException;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import ro.allevo.fintpuiws.util.DatasourceParser;
import ro.allevo.fintpws.model.BaseEntity;

@Entity
@Table(schema = "FINCFG", name = "reportmessagecriteria")
@NamedQueries({
	@NamedQuery(name = "MessageCriterionEntity.findByBusinessArea", query = "SELECT c FROM MessageCriterionEntity c"
			+ " where c.businessArea=:businessarea order by c.displayOrder"),
	@NamedQuery(name = "MessageCriterionEntity.findAll", query = "SELECT c FROM MessageCriterionEntity c"),
	@NamedQuery(name = "MessageCriterionEntity.findTotal", query = "SELECT count(c) FROM MessageCriterionEntity c"),
	})
public class MessageCriterionEntity extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6984484510165029274L;

	@Id
	private long id;
	
	private String label;
	
	@Column(name = "displayorder")
	private long displayOrder;
	
	@Column(name = "messagetypesbusinessarea")
	private String businessArea;
	
	private String type;
	
	private String datasource;
	
	private String field;
	
	@Transient
	private Map<String, String> values;
	
	private String masterlabel;
	
	//bi-directional many-to-one association to Reportmessagecriteriatemplate
//	@OneToMany(mappedBy="reportmessagecriteriatemplates")
//	@JoinColumn(name="criterionid", referencedColumnName = "id")
//	private List<ReportMessageCriteriaTemplatesEntity> reportmessagecriteriatemplates;
	
	public String getMasterlabel() {
		return masterlabel;
	}

	public void setMasterlabel(String masterlabel) {
		this.masterlabel = masterlabel;
	}

	public String getLabel() {
		return label;
	}

	public String getType() {
		return type;
	}

	public Map<String, String> getDatasource() {
		return values;
	}
	
	public void fetchDatasource(EntityManager entityManager) throws JsonParseException, JsonMappingException, IOException {
		values = DatasourceParser.parseAndFetch(datasource, entityManager);
	}

	public String getField() {
		return field;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "MessageCriterionEntity [id="+id+", businessArea="+businessArea+", datasource="+datasource
				+",label="+label+", masterlabel="+masterlabel+", field="+field+", values="+values+"]";
	}

	@Override
	public String getClassEvent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return null;
	}

//	@JsonIgnore
//	public List<ReportMessageCriteriaTemplatesEntity> getReportmessagecriteriatemplates() {
//		return reportmessagecriteriatemplates;
//	}
//
//	public void setReportmessagecriteriatemplates(
//			List<ReportMessageCriteriaTemplatesEntity> reportmessagecriteriatemplates) {
//		this.reportmessagecriteriatemplates = reportmessagecriteriatemplates;
//	}
	
}
