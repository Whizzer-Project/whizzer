package ro.allevo.fintpws.model;

import java.util.LinkedHashMap;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import ro.allevo.fintpws.util.Invariants;
import ro.allevo.fintpws.util.annotations.URLId;

@Entity
@Table(schema = "FINCFG", name="ENRICHRULES")

@NamedQueries({
	@NamedQuery(name = "EnrichRulesEntity.findAll", query = "select e from EnrichRulesEntity e"),
	@NamedQuery(name = "EnrichRulesEntity.findTotal", query = "select count(e.id) from EnrichRulesEntity e"),
	@NamedQuery(name = "EnrichRulesEntity.findById", query = "select e from EnrichRulesEntity e where e.id=:id")})
@Cacheable(false)
public class EnrichRulesEntity extends BaseEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(unique=true, nullable=false, updatable=false)
	@GeneratedValue(generator="ENRICH_ID_GENERATOR")
	@TableGenerator(name="ENRICH_ID_GENERATOR", table="FINCFG.IDGENLIST",
	pkColumnName="TABCOLNAME", valueColumnName="IDVALUE",
	pkColumnValue="ENRICH_ID") 
	@URLId
	private Integer id;
	@Column(length=50, nullable=false)
	private String name;
	@Column(name="txfield")
	private String txField;
	@Column(name="busslist")
	private String bussList;
	@Column(name="busslistfield")
	private String bussListField;
	@Column(name="txtype")
	private String txType;
	@Column(name="conditions")
	@Convert(converter = JpaConverterJson.class)
    private LinkedHashMap<String, Object> conditions;
	@Column(name="userid")
	private Integer userId;
	@ManyToOne
	@JoinColumn(name="optionid")
	private TxTemplatesConfigOptionEntity txtemplatesconfigoption;
	@Column(name="mandatory")
	private Boolean mandatory;
	
	@Column(name="pattern")
	private String pattern;
	
	//bi-directional many-to-one association to Txtemplatesconfig
	@ManyToOne
	@JoinColumn(name="configid", updatable=false)
	private TxTemplatesConfigEntity txtemplatesconfig;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTxField() {
		return txField;
	}

	public void setTxField(String txField) {
		this.txField = txField;
	}

	public String getBussList() {
		return bussList;
	}

	public void setBussList(String bussList) {
		this.bussList = bussList;
	}

	public String getBussListField() {
		return bussListField;
	}

	public void setBussListField(String bussListField) {
		this.bussListField = bussListField;
	}

	public String getTxType() {
		return txType;
	}

	public void setTxType(String txType) {
		this.txType = txType;
	}

	public LinkedHashMap<String, Object> getConditions() {
		return conditions;
	}

	public void setConditions(LinkedHashMap<String, Object> conditions) {
		this.conditions = conditions;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "Enrich [id=" + id + ", name=" + name + ", txField=" + txField + ", bussList=" + bussList
				+ ", bussListField=" + bussListField + ", txType=" + txType + ", pattern = " + pattern
				+ ", conditions=" + conditions + ", userId=" + userId+ "]";
	}

	@Override
	public String getClassEvent() {
		return Invariants.configClassEvents.MANAGE.toString();
	}

	public TxTemplatesConfigEntity getTxtemplatesconfig() {
		return txtemplatesconfig;
	}

	public void setTxtemplatesconfig(TxTemplatesConfigEntity txtemplatesconfig) {
		this.txtemplatesconfig = txtemplatesconfig;
	}

	public Boolean getMandatory() {
		return mandatory;
	}

	public void setMandatory(Boolean mandatory) {
		this.mandatory = mandatory;
	}

	@Override
	public String getMessage() {
		return "transaction enrich rule";
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	
	public TxTemplatesConfigOptionEntity getTxtemplatesconfigoption() {
		return txtemplatesconfigoption;
	}

	public void setTxtemplatesconfigoption(TxTemplatesConfigOptionEntity txtemplatesconfigoption) {
		this.txtemplatesconfigoption = txtemplatesconfigoption;
	}
	
}
