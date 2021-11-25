package ro.allevo.at.model;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TxProcessingTest {
	
	@NotNull
	private Long id;
	
	@NotNull
	private String name;
	
	private String description;
	private String txtype;
	private InterfaceConfig interfaceconfig;
	private InterfaceConfig interfaceconfigOutput;
	private List<TxProcessingTestLog> txprocessingtestlogs;
	private List<ExpectedOutputDataset> expectedoutputdatasets;
	private List<Object> callProcedureResult;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTxtype() {
		return txtype;
	}
	public void setTxtype(String txtype) {
		this.txtype = txtype;
	}
	
	@JsonGetter("rowid")
	public Long getRowid() {
		return id;
	}
	public InterfaceConfig getInterfaceconfig() {
		return interfaceconfig;
	}
	public void setInterfaceconfig(InterfaceConfig interfaceconfig) {
		this.interfaceconfig = interfaceconfig;
	}
	public List<TxProcessingTestLog> getTxprocessingtestlogs() {
		return txprocessingtestlogs;
	}
	public void setTxprocessingtestlogs(List<TxProcessingTestLog> txprocessingtestlogs) {
		this.txprocessingtestlogs = txprocessingtestlogs;
	}
	public List<Object> getCallProcedureResult() {
		return callProcedureResult;
	}
	public void setCallProcedureResult(List<Object> callProcedureResult) {
		this.callProcedureResult = callProcedureResult;
	}
	public List<ExpectedOutputDataset> getExpectedoutputdatasets() {
		return expectedoutputdatasets;
	}
	public void setExpectedoutputdatasets(List<ExpectedOutputDataset> expectedoutputdatasets) {
		this.expectedoutputdatasets = expectedoutputdatasets;
	}
	public InterfaceConfig getInterfaceconfigOutput() {
		return interfaceconfigOutput;
	}
	public void setInterfaceconfigOutput(InterfaceConfig interfaceconfigOutput) {
		this.interfaceconfigOutput = interfaceconfigOutput;
	}
}
