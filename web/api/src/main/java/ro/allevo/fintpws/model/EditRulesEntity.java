package ro.allevo.fintpws.model;

import java.util.LinkedHashMap;

import javax.persistence.*;

import ro.allevo.fintpws.util.Invariants;
import ro.allevo.fintpws.util.annotations.URLId;


/**
 * The persistent class for the editrules database table.
 * 
 */
@Entity
@Table(schema = "FINCFG", name="EDITRULES")
@NamedQueries({
	@NamedQuery(name = "EditRulesEntity.findAll", query = "select e from EditRulesEntity e"),
	@NamedQuery(name = "EditRulesEntity.findTotal", query = "select count(e.id) from EditRulesEntity e"),
	@NamedQuery(name = "EditRulesEntity.findById", query = "select e from EditRulesEntity e where e.id=:id"),
	@NamedQuery(name = "EditRulesEntity.findByTxType", query = "select e from EditRulesEntity e where e.txType=:message_type")
	})
@Cacheable(false)
public class EditRulesEntity extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(unique=true, nullable=false, updatable=false, name="id")
	@GeneratedValue(generator="EDITRULES_ID_GENERATOR")
	@TableGenerator(name="EDITRULES_ID_GENERATOR", table="FINCFG.IDGENLIST",
	pkColumnName="TABCOLNAME", valueColumnName="IDVALUE",
	pkColumnValue="EDITRULES_ID") 
	@URLId
	private Integer id;
	
	@Column(name="name", length=50, nullable=false)
	private String name;
	
	@Column(name="busslist")
	private String bussList;
	
	@Column(name="busslistfield")
	private String bussListField;

	@Column(name="conditions")
	@Convert(converter = JpaConverterJson.class)
    private LinkedHashMap<String, Object> conditions;

	@Column(name="mandatory")
	private Boolean mandatory;
	
	@Column(name="txfield")
	private String txField;

	@Column(name="txtype")
	private String txType;
	
	@Column(name="configid")
	private Integer configId;
	
	@Column(name="userid")
	private Integer userId;
	
	@Column(name="pattern")
	private String pattern;

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

	public LinkedHashMap<String, Object> getConditions() {
		return conditions;
	}

	public void setConditions(LinkedHashMap<String, Object> conditions) {
		this.conditions = conditions;
	}

	public Boolean getMandatory() {
		return mandatory;
	}

	public void setMandatory(Boolean mandatory) {
		this.mandatory = mandatory;
	}

	public String getTxField() {
		return txField;
	}

	public void setTxField(String txField) {
		this.txField = txField;
	}

	public String getTxType() {
		return txType;
	}

	public void setTxType(String txType) {
		this.txType = txType;
	}

	public Integer getConfigId() {
		return configId;
	}

	public void setConfigId(Integer configId) {
		this.configId = configId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	@Override
	public String toString() {
		return "EditRulesEntity [id=" + id + ", name=" + name + ", bussList=" + bussList + ", bussListField="
				+ bussListField + ", conditions=" + conditions + ", mandatory=" + mandatory + ", txField=" + txField
				+ ", txType=" + txType + ", configId=" + configId + ", userId=" + userId + ", pattern=" + pattern + "]";
	}

	@Override
	public String getClassEvent() {
		return Invariants.configClassEvents.MANAGE.toString();
	}

	@Override
	public String getMessage() {
		return "transaction edit rule";
	}	
}
