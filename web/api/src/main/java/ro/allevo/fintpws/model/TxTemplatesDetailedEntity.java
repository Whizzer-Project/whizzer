package ro.allevo.fintpws.model;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import ro.allevo.fintpws.util.Invariants;
import ro.allevo.fintpws.util.annotations.URLId;


/**
 * The persistent class for the txtemplatesdetailed database table.
 * 
 */
@Entity
@Table(schema = "FINCFG", name="txtemplatesdetailed")
@NamedQueries({
	@NamedQuery(name = "TxTemplatesDetailedEntity.findAll", query="SELECT t FROM TxTemplatesDetailedEntity t where t.txtemplates.id=:configId"),
	@NamedQuery(name = "TxTemplatesDetailedEntity.findTotal", query = "select count(t.id) from TxTemplatesDetailedEntity t where t.txtemplates.id=:configId"),
	@NamedQuery(name = "TxTemplatesDetailedEntity.findById", query = "select t from TxTemplatesDetailedEntity t where t.id=:id"),
	@NamedQuery(name = "TxTemplatesDetailedEntity.deleteAllForId", query = "delete from TxTemplatesDetailedEntity t where t.txtemplates.id=:id")
	//@NamedQuery(name = "TxTemplateEntity.findByDet", query = "select tcd.optionid,tcd.fieldxpath from TxTemplateEntity t,TxTemplateDetailedEntity td ,TxTemplatesConfigDetailedEntity tcd where "+
	//		 " t.id=tcd.templateid and tcd.fieldid = td.fieldid and t.templateid=4")

})
@Cacheable(false)
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class TxTemplatesDetailedEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@URLId
	@GeneratedValue(generator="TXTEMPLATESDETAILED_ID_GENERATOR")
	@TableGenerator(name="TXTEMPLATESDETAILED_ID_GENERATOR", table="FINCFG.IDGENLIST",
	pkColumnName="TABCOLNAME", valueColumnName="IDVALUE",
	pkColumnValue="TXTEMPLATESDETAILED_ID") 
	private Integer id;
	
	@Column
	private String value;

	//bi-directional many-to-one association to TxTemplateEntity
	@ManyToOne
	@JoinColumn(name="templateid", insertable=true, updatable=true)
	private TxTemplatesEntity txtemplates;

	//bi-directional many-to-one association to TxTemplatesConfigDetailedEntity
	@ManyToOne
	@JoinColumn(name="fieldid", referencedColumnName="id")
	private TxTemplatesConfigDetailedEntity txtemplatesconfigdetailed;
	
//	@JoinColumn(name="fieldid", insertable=false, updatable=false)
//	private String fieldid;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getValue() {
		return this.value;
	}
	public void setValue(String value) {
		this.value = value;
	}

	@JsonBackReference("template-detailed")
	public TxTemplatesEntity getTxtemplate() {
		return this.txtemplates;
	}

//	@JsonIgnore
	public void setTxtemplate(TxTemplatesEntity txtemplates) {
		this.txtemplates = txtemplates;
	}

	//@JsonBackReference("configdetailed-templatedetailed")
	public TxTemplatesConfigDetailedEntity getTxtemplatesconfigdetailed() {
		return this.txtemplatesconfigdetailed;
	}

	public void setTxtemplatesconfigdetailed(TxTemplatesConfigDetailedEntity txtemplatesconfigdetailed) {
		this.txtemplatesconfigdetailed = txtemplatesconfigdetailed;
	}
	@Override
	public String toString() {
		return "TxTemplatesDetailedEntity [id=" + id + ", value=" + value + ", txtemplates=" + txtemplates
				+ ", txtemplatesconfigdetailed=" + txtemplatesconfigdetailed + "]";
	}
	@Override
	public String getClassEvent() {
		return Invariants.configClassEvents.MANAGE.toString();
	}
	
	@Override
	public String getMessage() {
		return "transaction templates";
	}

}