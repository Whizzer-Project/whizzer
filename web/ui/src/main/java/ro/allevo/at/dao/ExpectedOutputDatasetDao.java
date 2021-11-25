package ro.allevo.at.dao;

import org.springframework.stereotype.Service;

import ro.allevo.at.model.ExpectedOutputDataset;

@Service
public interface ExpectedOutputDatasetDao {
	
	public ExpectedOutputDataset[] getAllExpectedOutputDatasets(Long processingtestid);

}
