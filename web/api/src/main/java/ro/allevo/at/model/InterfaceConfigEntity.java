package ro.allevo.at.model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ro.allevo.fintpws.model.BaseEntity;
import ro.allevo.fintpws.util.Invariants;

import java.util.List;


/**
 * The persistent class for the interfaceconfigs database table.
 * 
 */
@Entity
@Table(schema = "FINAT", name="interfaceconfigs")
@NamedQuery(name="InterfaceConfigEntity.findAll", query="SELECT i FROM InterfaceConfigEntity i")
public class InterfaceConfigEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	private Integer id;

	private String description;

	private String inputtype;

	private String location;

	private String name;

	private String txtype;

	//bi-directional many-to-one association to InputDatasetEntity
	@OneToMany(mappedBy="interfaceconfig")
	private List<InputDatasetEntity> inputdatasets;

	//bi-directional many-to-one association to TxProcessingTestEntity
	@OneToMany(mappedBy="interfaceconfig")
	private List<TxProcessingTestEntity> txprocessingtests;
	
	//bi-directional many-to-one association to TxProcessingTestEntity
	@OneToMany(mappedBy="interfaceconfig")
	private List<TxProcessingTestEntity> txprocessingtestsOutput;

	public InterfaceConfigEntity() {
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

	public String getInputtype() {
		return this.inputtype;
	}

	public void setInputtype(String inputtype) {
		this.inputtype = inputtype;
	}

	public String getLocation() {
		return this.location;
	}

	public void setLocation(String location) {
		this.location = location;
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
	public List<InputDatasetEntity> getInputdatasets() {
		return this.inputdatasets;
	}

	public void setInputdatasets(List<InputDatasetEntity> inputdatasets) {
		this.inputdatasets = inputdatasets;
	}

	public InputDatasetEntity addInputdataset(InputDatasetEntity inputdataset) {
		getInputdatasets().add(inputdataset);
		inputdataset.setInterfaceconfig(this);

		return inputdataset;
	}

	public InputDatasetEntity removeInputdataset(InputDatasetEntity inputdataset) {
		getInputdatasets().remove(inputdataset);
		inputdataset.setInterfaceconfig(null);

		return inputdataset;
	}

	@JsonIgnore
	public List<TxProcessingTestEntity> getTxprocessingtests() {
		return this.txprocessingtests;
	}

	public void setTxprocessingtests(List<TxProcessingTestEntity> txprocessingtests) {
		this.txprocessingtests = txprocessingtests;
	}

	public TxProcessingTestEntity addTxprocessingtest(TxProcessingTestEntity txprocessingtest) {
		getTxprocessingtests().add(txprocessingtest);
		txprocessingtest.setInterfaceconfig(this);

		return txprocessingtest;
	}

	public TxProcessingTestEntity removeTxprocessingtest(TxProcessingTestEntity txprocessingtest) {
		getTxprocessingtests().remove(txprocessingtest);
		txprocessingtest.setInterfaceconfig(null);

		return txprocessingtest;
	}

	@Override
	public String toString() {
		return "InterfaceConfigEntity [id=" + id + ", description=" + description + ", inputtype=" + inputtype
				+ ", location=" + location + ", name=" + name + ", txtype=" + txtype + ", inputdatasets="
				+ inputdatasets + ", txprocessingtests=" + txprocessingtests + "]";
	}

	@Override
	public String getClassEvent() {
		return Invariants.ATClassEvents.MANAGE.toString();
	}

	@JsonIgnore
	public List<TxProcessingTestEntity> getTxprocessingtestsOutput() {
		return txprocessingtestsOutput;
	}

	public void setTxprocessingtestsOutput(List<TxProcessingTestEntity> txprocessingtestsOutput) {
		this.txprocessingtestsOutput = txprocessingtestsOutput;
	}
	
	public TxProcessingTestEntity addTxprocessingtestOutput(TxProcessingTestEntity txprocessingtestsOutput) {
		getTxprocessingtests().add(txprocessingtestsOutput);
		txprocessingtestsOutput.setInterfaceconfigOutput(this);

		return txprocessingtestsOutput;
	}

	public TxProcessingTestEntity removeTxprocessingtestOutput(TxProcessingTestEntity txprocessingtestsOutput) {
		getTxprocessingtests().remove(txprocessingtestsOutput);
		txprocessingtestsOutput.setInterfaceconfigOutput(null);

		return txprocessingtestsOutput;
	}
	
	@Override
	public String getMessage() {
		return "auto testing interface config";
	}

}
