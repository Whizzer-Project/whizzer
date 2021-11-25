package ro.allevo.fintpws.model;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import ro.allevo.fintpws.util.Invariants;
import ro.allevo.fintpws.util.annotations.URLId;

import java.util.LinkedHashMap;
import java.util.List;


/**
 * The persistent class for the txtemplatesconfigdetailed database table.
 * 
 */
@Entity
@Table(schema = "FINCFG", name="txtemplatesconfigdetailed")
@NamedQueries({
	@NamedQuery(name="TxTemplatesConfigDetailedEntity.findAll", query="SELECT t FROM TxTemplatesConfigDetailedEntity t order by t.id"),
	@NamedQuery(name="TxTemplatesConfigDetailedEntity.findAllByConfigId", query="SELECT t FROM TxTemplatesConfigDetailedEntity t where t.txtemplatesconfig.id=:txtemplatesconfig order by t.id"),
	@NamedQuery(name="TxTemplatesConfigDetailedEntity.findTotalByConfigId", query="SELECT count(t) FROM TxTemplatesConfigDetailedEntity t where t.txtemplatesconfig.id=:txtemplatesconfig"),
	@NamedQuery(name="TxTemplatesConfigDetailedEntity.findById", query="SELECT t FROM TxTemplatesConfigDetailedEntity t where t.id=:id"),
	@NamedQuery(name="TxTemplatesConfigDetailedEntity.deleteAllForId", query="DELETE FROM TxTemplatesConfigDetailedEntity t where t.id=:id")
})
@Cacheable(false)
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class TxTemplatesConfigDetailedEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@URLId
	@GeneratedValue(generator="TXTEMPLATESCONFIGDETAILED_ID_GENERATOR")
	@TableGenerator(name="TXTEMPLATESCONFIGDETAILED_ID_GENERATOR", table="FINCFG.IDGENLIST",
	pkColumnName="TABCOLNAME", valueColumnName="IDVALUE",
	pkColumnValue="TXTEMPLATESCONFIGDETAILED_ID") 
	private Integer id;
	@Column(nullable=false, length=35)
	private String fieldlabel;
	@Column
	private String fieldvalue;
	@Column
	private Integer fieldvisibility;
	@Column
	private String fieldxpath;
	@Column
	private String fieldtype;
	@Column
	private Boolean mandatory;
	@Column
	private String pattern;
	@Column
	private Boolean editable;
	@ManyToOne
	@JoinColumn(name="optionid")
	private TxTemplatesConfigOptionEntity txtemplatesconfigoption;
	@Column
	private Integer fieldid;
	//bi-directional many-to-one association to TxTemplatesConfigEntity
	@ManyToOne
	@JoinColumn(name="configid", updatable=false)
	private TxTemplatesConfigEntity txtemplatesconfig;
	//bi-directional many-to-one association to TxTemplatesDetailedEntity
	@OneToMany(mappedBy="txtemplatesconfigdetailed", cascade = { CascadeType.ALL })
	private List<TxTemplatesDetailedEntity> txtemplatesdetaileds;
	@Column
	private String busslist;
	@Column
	private String busslistfield;
	@Column
	@Convert(converter = DetailedConverter.class)
	private LinkedHashMap<String, Object> conditions;
	@Column
	private Boolean visible;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getFieldid() {
		return this.id;
	}

	public void setFieldid(Integer fieldid) {
		this.fieldid = this.id;
	}
	
	public String getFieldlabel() {
		return this.fieldlabel;
	}

	public void setFieldlabel(String fieldlabel) {
		this.fieldlabel = fieldlabel;
	}

	public String getFieldvalue() {
		return this.fieldvalue;
	}

	public void setFieldvalue(String fieldvalue) {
		this.fieldvalue = fieldvalue;
	}

	//@JsonManagedReference
	public TxTemplatesConfigOptionEntity getTxtemplatesconfigoption() {
		return txtemplatesconfigoption;
	}

	public void setTxtemplatesconfigoption(TxTemplatesConfigOptionEntity txtemplatesconfigoption) {
		this.txtemplatesconfigoption = txtemplatesconfigoption;
	}

	public Integer getFieldvisibility() {
		return this.fieldvisibility;
	}

	public void setFieldvisibility(Integer fieldvisibility) {
		this.fieldvisibility = fieldvisibility;
	}

	public String getFieldxpath() {
		return this.fieldxpath;
	}

	public void setFieldxpath(String fieldxpath) {
		this.fieldxpath = fieldxpath;
	}

	public String getFieldtype() {
		return fieldtype;
	}

	public void setFieldtype(String fieldtype) {
		this.fieldtype = fieldtype;
	}

	public Boolean getMandatory() {
		return this.mandatory;
	}

	public void setMandatory(Boolean mandatory) {
		this.mandatory = mandatory;
	}

	public String getPattern() {
		return this.pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	
	public Boolean getEditable() {
		return editable;
	}

	public void setEditable(Boolean editable) {
		this.editable = editable;
	}

	@JsonBackReference("config-detaileds")
	public TxTemplatesConfigEntity getTxtemplatesconfig() {
		return this.txtemplatesconfig;
	}

	public void setTxtemplatesconfig(TxTemplatesConfigEntity txtemplatesconfig) {
		this.txtemplatesconfig = txtemplatesconfig;
	}
	
	//@JsonManagedReference("configdetailed-templatedetailed")
	@JsonIgnore
	public List<TxTemplatesDetailedEntity> getTxtemplatesdetaileds() {
		return this.txtemplatesdetaileds;
	}
	
	public void setTxtemplatesdetaileds(List<TxTemplatesDetailedEntity> txtemplatesdetaileds) {
		this.txtemplatesdetaileds = txtemplatesdetaileds;
	}

	public TxTemplatesDetailedEntity addTxtemplatesdetailed(TxTemplatesDetailedEntity txtemplatesdetailed) {
		getTxtemplatesdetaileds().add(txtemplatesdetailed);
		txtemplatesdetailed.setTxtemplatesconfigdetailed(this);

		return txtemplatesdetailed;
	}

	public TxTemplatesDetailedEntity removeTxtemplatesdetailed(TxTemplatesDetailedEntity txtemplatesdetailed) {
		getTxtemplatesdetaileds().remove(txtemplatesdetailed);
		txtemplatesdetailed.setTxtemplatesconfigdetailed(null);

		return txtemplatesdetailed;
	}

	public String getBusslist() {
		return busslist;
	}

	public void setBusslist(String busslist) {
		this.busslist = busslist;
	}

	public String getBusslistfield() {
		return busslistfield;
	}

	public void setBusslistfield(String busslistfield) {
		this.busslistfield = busslistfield;
	}

	public LinkedHashMap<String, Object> getConditions() {
		return conditions;
	}

	public void setConditions(LinkedHashMap<String, Object> conditions) {
		this.conditions = conditions;
	}
	
	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	@Override
	public String toString() {
		return "TxTemplatesConfigDetailedEntity [id=" + id + ", fieldlabel=" + fieldlabel + ", fieldvalue=" + fieldvalue
				+ ", fieldvisibility=" + fieldvisibility + ", fieldxpath=" + fieldxpath + ", fieldtype=" + fieldtype
				+ ", mandatory=" + mandatory + ", pattern=" + pattern + ", txtemplatesconfigoption="
				+ txtemplatesconfigoption + ", fieldid=" + fieldid + ", txtemplatesconfig=" + txtemplatesconfig
				+ ", txtemplatesdetaileds=" + txtemplatesdetaileds + "]";
	}

	@Override
	public String getClassEvent() {
		return Invariants.configClassEvents.MANAGE.toString();
	}
	
	@Override
	public String getMessage() {
		return "transaction create rule";
	}

}