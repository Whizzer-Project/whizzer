package ro.allevo.fintpws.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import ro.allevo.fintpws.util.Invariants;
import ro.allevo.fintpws.util.annotations.URLId;

/**
 * The persistent class for the params database table.
 * 
 */
@Entity
@Table(schema="FINCFG", name="PARAMS")
@NamedQueries({
	@NamedQuery(name = "ParamsEntity.findAll", query = "SELECT p from ParamsEntity p"),
	@NamedQuery(name = "ParamsEntity.findTotal", query = "SELECT count(p.code) from ParamsEntity p"),
	@NamedQuery(name = "ParamsEntity.findById", query = "select p from ParamsEntity p where p.code=:id")
	})
public class ParamsEntity extends BaseEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false, length=35, updatable=false)
	@URLId
	private String code;
	
	private String name;
	
	@Column(length=300)
	private String value;
	
	@Column(length=50, updatable=false)
	private String description;	
	
	@Column
	private String category;

	public ParamsEntity() {
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return "ParamsEntity [code=" + code + ", name=" + name + ", value=" + value + ", description=" + description
				+ "]";
	}

	@Override
	public String getClassEvent() {
		return Invariants.configClassEvents.MANAGE.toString();
	}
	
	@Override
	public String getMessage() {
		return "parameters";
	}

}
