package ro.allevo.fintpws.model;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import ro.allevo.fintpws.util.Invariants;
import ro.allevo.fintpws.util.annotations.URLId;

import java.util.List;


/**
 * The persistent class for the txtemplates database table.
 * 
 */
@Entity
@Table(schema = "FINCFG", name="txtemplatesgroups")
@NamedQueries({
	@NamedQuery(name = "TxTemplatesGroupsEntity.findAll", query="SELECT t FROM TxTemplatesGroupsEntity t where t.txtemplates.id=:templateid"),
	@NamedQuery(name = "TxTemplatesGroupsEntity.findTotal", query = "select count(t.id) from TxTemplatesGroupsEntity t where t.txtemplates.id=:templateid"),
	@NamedQuery(name = "TxTemplatesGroupsEntity.findById", query = "select e from TxTemplatesGroupsEntity e where e.id=:id")	
})
@Cacheable(false)
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class TxTemplatesGroupsEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@URLId
	@GeneratedValue(generator="TXTEMPLATESGROUPS_ID_GENERATOR")
	@TableGenerator(name="TXTEMPLATESGROUPS_ID_GENERATOR", table="FINCFG.IDGENLIST",
	pkColumnName="TABCOLNAME", valueColumnName="IDVALUE",
	pkColumnValue="TXTEMPLATESGROUPS_ID") 
	private Integer id;

	@Column
	private String name;

	@Column
	private Integer userid;
	
	@Column
	private Integer groupid;

	//bi-directional many-to-one association to TxTemplatesConfigEntity
	@ManyToOne
	@JoinColumn(name="templateid")
	private TxTemplatesEntity txtemplates;

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getUserid() {
		return this.userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public Integer getGroupid() {
		return groupid;
	}

	public void setGroupid(Integer groupid) {
		this.groupid = groupid;
	}
	
	@JsonBackReference("template-group")
	public TxTemplatesEntity getTxtemplate() {
		return txtemplates;
	}

	public void setTxtemplate(TxTemplatesEntity txtemplates) {
		this.txtemplates = txtemplates;
	}

	@Override
	public String toString() {
		return "TxTemplatesGroupsEntity [id=" + id + ", name=" + name + ", userid=" + userid + ", groupid=" + groupid
				+ ", txtemplates=" + txtemplates + "]";
	}

	@Override
	public String getClassEvent() {
		return Invariants.configClassEvents.MANAGE.toString();
	}
	
	@Override
	public String getMessage() {
		return "transaction templates groups";
	}

}