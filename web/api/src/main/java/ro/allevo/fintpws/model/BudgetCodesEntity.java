package ro.allevo.fintpws.model;

import javax.persistence.*;

import ro.allevo.fintpws.util.Invariants;
import ro.allevo.fintpws.util.annotations.URLId;

/**
 * The persistent class for the budgetcodes database table.
 * 
 */
@Entity
@Table(schema = "FINLIST", name="BUDGETCODES")

@NamedQueries({
	@NamedQuery(name = "BudgetCodesEntity.findAll", query = "SELECT b FROM BudgetCodesEntity b"),
	@NamedQuery(name = "BudgetCodesEntity.findTotal", query = "select count(b.id) from BudgetCodesEntity b"),
	@NamedQuery(name = "BudgetCodesEntity.findById", query = "select b from BudgetCodesEntity b where b.id=:id") })
public class BudgetCodesEntity extends BaseEntity {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="BudgetCodesEntityIdGenerator")
	@TableGenerator(name="BudgetCodesEntityIdGenerator", table="FINCFG.IDGENLIST",
	pkColumnName="TABCOLNAME", valueColumnName="IDVALUE",
	pkColumnValue="BUDGETCODES_ID") 
	@Column(unique=true, nullable=false, precision=38, updatable=false)
	@URLId
	private long id;
	
	@Column(unique=true, nullable=false, length=35)
	private String code;

	@Column(nullable=false, length=35)
	private String name;

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClassEvent() {
		return Invariants.configClassEvents.MANAGE.toString();
	}

	@Override
	public String toString() {
		return "BudgetCodesEntity [id=" + id + ", code=" + code + ", name=" + name + "]";
	}
	
	@Override
	public String getMessage() {
		return "entry in list budget code";
	}

}