package ro.allevo.fintpws.model;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
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
@Table(schema = "FINCFG", name="txtemplates")
@NamedQuery(name="TxTemplatesEntity.findAll", query="SELECT t FROM TxTemplatesEntity t")
@NamedQueries({
	@NamedQuery(name = "TxTemplatesEntity.findAll", query="SELECT t FROM TxTemplatesEntity t"),
	@NamedQuery(name = "TxTemplatesEntity.findTotal", query = "select count(t.id) from TxTemplatesEntity t"),
	@NamedQuery(name = "TxTemplatesEntity.findById", query = "select e from TxTemplatesEntity e where e.id=:id")	
})
@Cacheable(false)
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class TxTemplatesEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@URLId
	@GeneratedValue(generator="TXTEMPLATES_ID_GENERATOR")
	@TableGenerator(name="TXTEMPLATES_ID_GENERATOR", table="FINCFG.IDGENLIST",
	pkColumnName="TABCOLNAME", valueColumnName="IDVALUE",
	pkColumnValue="TXTEMPLATES_ID") 
	private Integer id;

	@Column
	private String entity;

	@Column
	private String name;

	@Column
	private Integer userid;
	
	@Column
	private Integer type;

	//bi-directional many-to-one association to TxTemplatesConfigEntity
	@ManyToOne
	@JoinColumn(name="configid")
	private TxTemplatesConfigEntity txtemplatesconfig;

	//bi-directional many-to-one association to TxTemplatesDetailedEntity
	@OneToMany(mappedBy="txtemplates", cascade = CascadeType.ALL)
	@JoinColumn(name = "templateid", referencedColumnName = "id")
	private List<TxTemplatesDetailedEntity> txtemplatesdetaileds;
	
	@OneToMany(mappedBy="txtemplates", cascade = CascadeType.PERSIST)
	@JoinColumn(name="templateid", referencedColumnName = "id")
	private List<TxTemplatesGroupsEntity> txtemplatesgroups;

	public Integer getId() {
		return this.id;
	}
	
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEntity() {
		return this.entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
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

	//@JsonBackReference("template-config")
	public TxTemplatesConfigEntity getTxtemplatesconfig() {
		return this.txtemplatesconfig;
	}

	public void setTxtemplatesconfig(TxTemplatesConfigEntity txtemplatesconfig) {
		this.txtemplatesconfig = txtemplatesconfig;
	}

	@JsonManagedReference("template-detailed")
	public List<TxTemplatesDetailedEntity> getTxtemplatesdetaileds() {
		return this.txtemplatesdetaileds;
	}

	public void setTxtemplatesdetaileds(List<TxTemplatesDetailedEntity> txtemplatesdetaileds) {
		this.txtemplatesdetaileds = txtemplatesdetaileds;
	}

	public TxTemplatesDetailedEntity addTxtemplatesdetailed(TxTemplatesDetailedEntity txtemplatesdetailed) {
		getTxtemplatesdetaileds().add(txtemplatesdetailed);
		txtemplatesdetailed.setTxtemplate(this);

		return txtemplatesdetailed;
	}

	public TxTemplatesDetailedEntity removeTxtemplatesdetailed(TxTemplatesDetailedEntity txtemplatesdetailed) {
		getTxtemplatesdetaileds().remove(txtemplatesdetailed);
		txtemplatesdetailed.setTxtemplate(null);

		return txtemplatesdetailed;
	}

	@JsonManagedReference("template-group")
	public List<TxTemplatesGroupsEntity> getTxtemplatesgroups() {
		return txtemplatesgroups;
	}

	public void setTxtemplatesgroups(List<TxTemplatesGroupsEntity> txtemplatesgroups) {
		this.txtemplatesgroups = txtemplatesgroups;
	}
	
	public TxTemplatesGroupsEntity addTxtemplatesgroup(TxTemplatesGroupsEntity txtemplatesgroup) {
		getTxtemplatesgroups().add(txtemplatesgroup);

		return txtemplatesgroup;
	}

	public TxTemplatesDetailedEntity removeTxtemplatesgroup(TxTemplatesDetailedEntity txtemplatesgroup) {
		getTxtemplatesdetaileds().remove(txtemplatesgroup);
		txtemplatesgroup.setTxtemplate(null);

		return txtemplatesgroup;
	}

	@Override
	public String toString() {
		return "TxTemplatesEntity [id=" + id + ", entity=" + entity + ", name=" + name + ", userid=" + userid
				+ ", type=" + type + ", txtemplatesconfig=" + txtemplatesconfig + ", txtemplatesdetaileds="
				+ txtemplatesdetaileds + ", txtemplatesgroups=" + txtemplatesgroups + "]";
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