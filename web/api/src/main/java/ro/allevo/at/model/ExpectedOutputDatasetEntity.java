package ro.allevo.at.model;

import javax.persistence.*;

import ro.allevo.fintpws.model.BaseEntity;
import ro.allevo.fintpws.util.Invariants;


/**
 * The persistent class for the expectedoutputdatasets database table.
 * 
 */
@Entity
@Table(schema = "FINAT", name="expectedoutputdatasets")
@NamedQueries({
	@NamedQuery(name="ExpectedOutputDatasetEntity.findAll", query="SELECT e FROM ExpectedOutputDatasetEntity e"),
	@NamedQuery(name ="ExpectedOutputDatasetEntity.findTotal", query = "select count(e.id) from ExpectedOutputDatasetEntity e")
	})
public class ExpectedOutputDatasetEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	private Integer id;

	private String dataset;
	
	private String uniquekeypath;

	//bi-directional many-to-one association to InputDatasetEntity
	@ManyToOne
	@JoinColumn(name="inputdatasetid")
	private InputDatasetEntity inputdataset;

	//bi-directional many-to-one association to TxProcessingTestEntity
	@ManyToOne
	@JoinColumn(name="testid")
	private TxProcessingTestEntity txprocessingtest;

	public ExpectedOutputDatasetEntity() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDataset() {
		return this.dataset;
	}

	public void setDataset(String dataset) {
		this.dataset = dataset;
	}

	public InputDatasetEntity getInputdataset() {
		return this.inputdataset;
	}

	public void setInputdataset(InputDatasetEntity inputdataset) {
		this.inputdataset = inputdataset;
	}

	public TxProcessingTestEntity getProcessingTest() {
		return this.txprocessingtest;
	}

	public void setProcessingTest(TxProcessingTestEntity processingTest) {
		this.txprocessingtest = processingTest;
	}

	@Override
	public String toString() {
		return "ExpectedOutputDatasetEntity [id=" + id + ", dataset=" + dataset + ", inputdataset=" + inputdataset
				+ ", processingTest=" + txprocessingtest + "]";
	}

	@Override
	public String getClassEvent() {
		return Invariants.ATClassEvents.MANAGE.toString();
	}

	public String getUniquekeypath() {
		return uniquekeypath;
	}

	public void setUniquekeypath(String uniquekeypath) {
		this.uniquekeypath = uniquekeypath;
	}
	
	@Override
	public String getMessage() {
		return "auto testing expected output";
	}

}