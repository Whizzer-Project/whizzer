package ro.allevo.at.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExpectedOutputDataset {
	
	private Integer id;
	private String dataset;
	private String uniquekeypath;
	private InputDataset inputdataset;
	private TxProcessingTest processingTest;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDataset() {
		return dataset;
	}
	public void setDataset(String dataset) {
		this.dataset = dataset;
	}
	public InputDataset getInputdataset() {
		return inputdataset;
	}
	public void setInputdataset(InputDataset inputdataset) {
		this.inputdataset = inputdataset;
	}
	public TxProcessingTest getProcessingTest() {
		return processingTest;
	}
	public void setProcessingTest(TxProcessingTest processingTest) {
		this.processingTest = processingTest;
	}
	public String getUniquekeypath() {
		return uniquekeypath;
	}
	public void setUniquekeypath(String uniquekeypath) {
		this.uniquekeypath = uniquekeypath;
	}

}
