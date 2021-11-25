package ro.allevo.fintpws.model;

import java.util.LinkedHashMap;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import ro.allevo.fintpws.util.Invariants;
import ro.allevo.fintpws.util.annotations.URLId;

/**
 * The persistent class for the validationrules database table.
 * 
 */
@Entity
@Table(schema = "FINCFG", name="VALIDATIONRULES")
@NamedQueries({
	@NamedQuery(name = "ValidationsRulesEntity.findAll", query = "select v from ValidationsRulesEntity v"),
	@NamedQuery(name = "ValidationsRulesEntity.findTotal", query = "select count(v.id) from ValidationsRulesEntity v"),
	@NamedQuery(name = "ValidationsRulesEntity.findById", query = "select v from ValidationsRulesEntity v where v.id=:id")})
@Cacheable(false)
public class ValidationsRulesEntity extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false, updatable=false)
	@GeneratedValue(generator="VALIDATIONRULES_ID_GENERATOR")
	@TableGenerator(name="VALIDATIONRULES_ID_GENERATOR", table="FINCFG.IDGENLIST",
	pkColumnName="TABCOLNAME", valueColumnName="IDVALUE",
	pkColumnValue="VALIDATIONRULES_ID") 
	@URLId
	private Integer id;
	
	@Column(length=50, nullable=false)
	private String name;
	
	@Column(length=12)
	private String type;

	@Column(length=50)
	private String algorithm;
	
	@Column(length=50, name="txfield")
	private String txField;

	@Column(length=50, name="txtype")
	private String txType;

	@Column(name="conditions")
	@Convert(converter = JpaConverterJson.class)
    private LinkedHashMap<String, Object> conditions;
	
	@Column(name="userid")
	private Integer userId;

	@Column(name="configid")
	private Integer configId;

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAlgorithm() {
		return this.algorithm;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	public LinkedHashMap<String, Object> getConditions() {
		return this.conditions;
	}

	public void setConditions(LinkedHashMap<String, Object> conditions) {
		this.conditions = conditions;
	}

	public Integer getConfigId() {
		return configId;
	}

	public void setConfigId(Integer configId) {
		this.configId = configId;
	}

	public String getName() {
		return this.name;
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

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	public String getTxType() {
		return txType;
	}

	public void setTxType(String txType) {
		this.txType = txType;
	}

	@Override
	public String toString() {
		return "ValidationsRulesEntity [id=" + id + ", algorithm=" + algorithm + ", conditions=" + conditions
				+ ", configid=" + configId + ", name=" + name + ", txfield=" + txField + ", txtype=" + txType
				+ ", type=" + type + ", userid=" + userId + "]";
	}
	
	@Override
	public String getClassEvent() {
		return Invariants.configClassEvents.MANAGE.toString();
	}

	@Override
	public String getMessage() {
		return "transaction validation rule";
	}
}
