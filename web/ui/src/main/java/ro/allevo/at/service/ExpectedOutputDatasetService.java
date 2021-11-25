package ro.allevo.at.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.at.dao.ExpectedOutputDatasetRestApiDao;
import ro.allevo.at.model.ExpectedOutputDataset;

@Service
public class ExpectedOutputDatasetService {

	@Autowired
	private ExpectedOutputDatasetRestApiDao expectedOutputDatasetRestApiDao;
	
	public ExpectedOutputDataset[] getAllInputDatasets(Long processingtestid) {
		return expectedOutputDatasetRestApiDao.getAllExpectedOutputDatasets(processingtestid);
	}
}
