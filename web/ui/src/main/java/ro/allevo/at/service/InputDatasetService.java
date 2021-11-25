package ro.allevo.at.service;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.at.dao.InputDatasetRestApiDao;
import ro.allevo.at.model.InputDataset;
import ro.allevo.fintpui.utils.PagedCollection;

@Service
public class InputDatasetService {
	
	@Autowired
	private InputDatasetRestApiDao inputDatasetRestApiDao;
	
	public InputDataset[] getAllInputDatasetsById(Long commonId, Long processingtestid) {
		return inputDatasetRestApiDao.getAllInputDatasetsById(commonId, processingtestid);
	}
	
	public InputDataset[] getAllInputDatasets() {
		return inputDatasetRestApiDao.getAllInputDatasets();
	}
	
	public InputDataset getInputDataset(Long id, Long processingtestid) {
		return inputDatasetRestApiDao.getInputDataset(id, processingtestid);
	}
	
	public PagedCollection<InputDataset> getPage(){
		return inputDatasetRestApiDao.getPage();
	}
	
	public PagedCollection<InputDataset> getPage(LinkedHashMap<String, List<String>> param){
		return inputDatasetRestApiDao.getPage(param);
	}

}
