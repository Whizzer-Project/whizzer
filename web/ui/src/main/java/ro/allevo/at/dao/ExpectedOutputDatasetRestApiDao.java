package ro.allevo.at.dao;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.at.model.ExpectedOutputDataset;
import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.dao.RestApiDao;

@Service
public class ExpectedOutputDatasetRestApiDao extends RestApiDao<ExpectedOutputDataset> implements ExpectedOutputDatasetDao {

	@Autowired
	Config config;
	
	public ExpectedOutputDatasetRestApiDao() {
		super(ExpectedOutputDataset.class);
	}

	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getAtURL()).path("tests").build();
	}
	
	@Override
	public ExpectedOutputDataset[] getAllExpectedOutputDatasets(Long processingtestid) {
		URI uri = UriBuilder.fromUri(getBaseUrl()).path("" + processingtestid).path("datasetoutput").build();
		return getAll(uri);
	}
}
