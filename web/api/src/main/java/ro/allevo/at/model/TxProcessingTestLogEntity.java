package ro.allevo.at.model;

import javax.persistence.*;

import ro.allevo.fintpws.model.BaseEntity;
import ro.allevo.fintpws.util.Invariants;

import java.sql.Timestamp;


/**
 * The persistent class for the txprocessingtestlog database table.
 * 
 */
@Entity
@Table(schema = "FINAT", name="txprocessingtestlog")
@NamedQueries({
	@NamedQuery(name="TxProcessingTestLogEntity.findAll", query="SELECT t FROM TxProcessingTestLogEntity t"),
	@NamedQuery(name ="TxProcessingTestLogEntity.findTotal", query = "select count(t.id) from TxProcessingTestLogEntity t")

	})
public class TxProcessingTestLogEntity extends BaseEntity {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator="ProcessingTestLogidGenerator")
    @TableGenerator(name="ProcessingTestLogidGenerator", table="FINCFG.IDGENLIST",
         pkColumnName="TABCOLNAME", valueColumnName="IDVALUE",
         pkColumnValue="TXPROCESSINGTESTLOG_ID")
	@Column(unique=true, nullable=false)
	private Integer id;
		
	private Timestamp insertdate;

	private Integer status;
	
	@Column(name ="txtype")
	private String txtype;

	//bi-directional many-to-one association to InputDatasetEntity
	@ManyToOne
	@JoinColumn(name="inputdatasetid")
	private InputDatasetEntity inputdataset;

	//bi-directional many-to-one association to TxProcessingTestEntity
	@ManyToOne
	@JoinColumn(name="testid")
	private TxProcessingTestEntity txprocessingtest;
	

	public TxProcessingTestLogEntity() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Timestamp getInsertdate() {
		return this.insertdate;
	}

	public void setInsertdate(Timestamp insertdate) {
		this.insertdate = insertdate;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public InputDatasetEntity getInputdataset() {
		return this.inputdataset;
	}

	public void setInputdataset(InputDatasetEntity inputdataset) {
		this.inputdataset = inputdataset;
	}

	public TxProcessingTestEntity getTxprocessingtest() {
		return this.txprocessingtest;
	}

	public void setTxprocessingtest(TxProcessingTestEntity txprocessingtest) {
		this.txprocessingtest = txprocessingtest;
	}

	public String getTxType() {
		return txtype;
	}

	public void setTxType(String txtype) {
		this.txtype = txtype;
	}
	
	@Override
	public String getClassEvent() {
		return Invariants.ATClassEvents.MANAGE.toString();
	}	

	@Override
	public String toString() {
		return "TxProcessingTestLogEntity [id=" + id + ", insertdate=" + insertdate + ", status=" + status + ", txtype="
				+ txtype + ", inputdataset=" + inputdataset + ", txprocessingtest=" + txprocessingtest + "]";
	}
	
	@Override
	public String getMessage() {
		return "auto testing transaction processing log";
	}

}