package ro.allevo.at.model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ro.allevo.fintpws.model.BaseEntity;
import ro.allevo.fintpws.util.Invariants;

import java.util.List;


/**
 * The persistent class for the txprocessingtests database table.
 * 
 */
@Entity
@Table(schema = "FINAT", name="txprocessingtests")
@NamedStoredProcedureQueries({
	@NamedStoredProcedureQuery(
		name = "TxProcessingTestEntity.gettestcontrollerdata",
		procedureName = "finat.gettestcontrollerdata",
		parameters = {
				@StoredProcedureParameter(name = "intestid", type = Integer.class, mode = ParameterMode.IN),
				@StoredProcedureParameter(name = "intxtype", type = String.class, mode = ParameterMode.IN),
				@StoredProcedureParameter(name = "outretcursor", type = void.class, mode = ParameterMode.REF_CURSOR),
		}
	)
})
@NamedQueries({
	@NamedQuery(name="TxProcessingTestEntity.findAll", query="SELECT t FROM TxProcessingTestEntity t"),
	@NamedQuery(name = "TxProcessingTestEntity.findTotal", query = "select count(t.id) from TxProcessingTestEntity t"),
	@NamedQuery(name = "TxProcessingTestEntity.findById", query = "select t from TxProcessingTestEntity t where t.id=:id")	
	})
public class TxProcessingTestEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false, updatable=false)
	@GeneratedValue(strategy=GenerationType.TABLE)
	private Integer id;
	
	@Column
	private String description;

	@Column(name = "name", nullable = false, length = 500)
	private String name;

	//bi-directional many-to-one association to InterfaceConfigEntity
	@ManyToOne
	@JoinColumn(name="outputinterfaceid")
	private InterfaceConfigEntity interfaceconfigOutput;

	@Column(length = 30)
	private String txtype;

	//bi-directional many-to-one association to TxProcessingTestLogEntity
	@OneToMany(mappedBy="txprocessingtest")
	private List<TxProcessingTestLogEntity> txprocessingtestlogs;

	//bi-directional many-to-one association to InterfaceConfigEntity
	@ManyToOne
	@JoinColumn(name="inputinterfaceid")
	private InterfaceConfigEntity interfaceconfig;
	
	//bi-directional many-to-one association to ExpectedOutputDatasetEntity
	@OneToMany(mappedBy="txprocessingtest")
	private List<ExpectedOutputDatasetEntity> expectedoutputdatasets;
	
	@Transient
	private List<Object> callProcedureResult;

	public TxProcessingTestEntity() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTxtype() {
		return this.txtype;
	}

	public void setTxtype(String txtype) {
		this.txtype = txtype;
	}

	@JsonIgnore
	public List<TxProcessingTestLogEntity> getTxprocessingtestlogs() {
		return this.txprocessingtestlogs;
	}

	public void setTxprocessingtestlogs(List<TxProcessingTestLogEntity> txprocessingtestlogs) {
		this.txprocessingtestlogs = txprocessingtestlogs;
	}

	public TxProcessingTestLogEntity addTxprocessingtestlog(TxProcessingTestLogEntity txprocessingtestlog) {
		getTxprocessingtestlogs().add(txprocessingtestlog);
		txprocessingtestlog.setTxprocessingtest(this);

		return txprocessingtestlog;
	}

	public TxProcessingTestLogEntity removeTxprocessingtestlog(TxProcessingTestLogEntity txprocessingtestlog) {
		getTxprocessingtestlogs().remove(txprocessingtestlog);
		txprocessingtestlog.setTxprocessingtest(null);

		return txprocessingtestlog;
	}

	public InterfaceConfigEntity getInterfaceconfig() {
		return this.interfaceconfig;
	}

	public void setInterfaceconfig(InterfaceConfigEntity interfaceconfig) {
		this.interfaceconfig = interfaceconfig;
	}

	@Override
	public String toString() {
		return "TxProcessingTestEntity [id=" + id + ", description=" + description + ", name=" + name
				+ ", txtype=" + txtype + ", txprocessingtestlogs="
				+ txprocessingtestlogs + ", interfaceconfig=" + interfaceconfig + "]";
	}

	@Override
	public String getClassEvent() {
		return Invariants.ATClassEvents.MANAGE.toString();
	}

	public List<Object> getCallProcedureResult() {
		return callProcedureResult;
	}

	public void setCallProcedureResult(List<Object> callProcedureResult) {
		this.callProcedureResult = callProcedureResult;
	}

	public InterfaceConfigEntity getInterfaceconfigOutput() {
		return interfaceconfigOutput;
	}

	public void setInterfaceconfigOutput(InterfaceConfigEntity interfaceconfigOutput) {
		this.interfaceconfigOutput = interfaceconfigOutput;
	}
	
	@Override
	public String getMessage() {
		return "auto testing transaction processing";
	}

}
