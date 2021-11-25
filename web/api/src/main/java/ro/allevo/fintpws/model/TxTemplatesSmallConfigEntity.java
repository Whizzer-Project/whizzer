package ro.allevo.fintpws.model;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import ro.allevo.fintpws.util.Invariants;

import java.util.List;


/**
 * The persistent class for the txtemplatesconfig database table.
 * 
 */
@Entity
@Table(schema = "FINCFG", name="txtemplatesconfig")
@NamedQuery(name="TxTemplatesSmallConfigEntity.findById", query="SELECT t FROM TxTemplatesSmallConfigEntity t where t.id=:id")
@JsonIgnoreProperties(ignoreUnknown = true)
@Cacheable(false)
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class TxTemplatesSmallConfigEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TXTEMPLATESCONFIG_ID_GENERATOR")
	@TableGenerator(name="TXTEMPLATESCONFIG_ID_GENERATOR", table="FINCFG.IDGENLIST",
	pkColumnName="TABCOLNAME", valueColumnName="IDVALUE",
	pkColumnValue="TXTEMPLATESCONFIG_ID") 
	private Integer id;

	private String messagetype;
	
	private String validationxsd;
	
	private String type;
	
//	//bi-directional many-to-one association to TxTemplateEntity
//	@OneToMany(mappedBy="txtemplatesconfig")
//	private List<TxTemplateEntity> txtemplates;
//
//	//bi-directional many-to-one association to TxTemplatesConfigDetailedEntity
//	@OneToMany(mappedBy="txtemplatesconfig")
//	private List<TxTemplatesConfigDetailedEntity> txtemplatesconfigdetaileds;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public TxTemplatesSmallConfigEntity() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMessagetype() {
		return this.messagetype;
	}

	public void setMessagetype(String messagetype) {
		this.messagetype = messagetype;
	}
	
//	@JsonIgnore
//	public List<TxTemplateEntity> getTxtemplates() {
//		return this.txtemplates;
//	}
//
//	@JsonIgnore
//	public void setTxtemplates(List<TxTemplateEntity> txtemplates) {
//		this.txtemplates = txtemplates;
//	}

//	public TxTemplateEntity addTxtemplate(TxTemplateEntity txtemplates) {
//		getTxtemplates().add(txtemplates);
//		txtemplates.setTxtemplatesconfig(this);
//		return txtemplates;
//	}

//	public TxTemplateEntity removeTxtemplate(TxTemplateEntity txtemplates) {
//		getTxtemplates().remove(txtemplates);
//		txtemplates.setTxtemplatesconfig(null);
//		return txtemplates;
//	}
//
//	@JsonIgnore
//	public List<TxTemplatesConfigDetailedEntity> getTxtemplatesconfigdetaileds() {
//		return this.txtemplatesconfigdetaileds;
//	}
//
//	@JsonIgnore
//	public void setTxtemplatesconfigdetaileds(List<TxTemplatesConfigDetailedEntity> txtemplatesconfigdetaileds) {
//		this.txtemplatesconfigdetaileds = txtemplatesconfigdetaileds;
//	}

//	public TxTemplatesConfigDetailedEntity addTxtemplatesconfigdetailed(TxTemplatesConfigDetailedEntity txtemplatesconfigdetailed) {
//		getTxtemplatesconfigdetaileds().add(txtemplatesconfigdetailed);
//		txtemplatesconfigdetailed.setTxtemplatesconfig(this);
//		return txtemplatesconfigdetailed;
//	}

//	public TxTemplatesConfigDetailedEntity removeTxtemplatesconfigdetailed(TxTemplatesConfigDetailedEntity txtemplatesconfigdetailed) {
//		getTxtemplatesconfigdetaileds().remove(txtemplatesconfigdetailed);
//		txtemplatesconfigdetailed.setTxtemplatesconfig(null);
//		return txtemplatesconfigdetailed;
//	}

	public String getValidationxsd() {
		return validationxsd;
	}

	public void setValidationxsd(String validationxsd) {
		this.validationxsd = validationxsd;
	}

	@Override
	public String toString() {
		return "TxTemplatesSmallConfigEntity [id=" + id + ", messagetype=" + messagetype + ", validationxsd="
				+ validationxsd + ", type=" + type + "]";
	}

	@Override
	public String getClassEvent() {
		return Invariants.configClassEvents.MANAGE.toString();
	}
	
	@Override
	public String getMessage() {
		return "transaction templates small config";
	}

}