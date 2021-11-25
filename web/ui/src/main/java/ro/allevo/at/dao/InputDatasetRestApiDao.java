package ro.allevo.at.dao;

import java.net.URI;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.at.model.InputDataset;
import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.dao.RestApiDao;

@Service
public class InputDatasetRestApiDao extends RestApiDao<InputDataset> implements InputDatasetDao {
	
	@Autowired
	Config config;
	
	public InputDatasetRestApiDao() {
		super(InputDataset.class);
	}
	
	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getAtURL()).path("tests").build();
	}

	@Override
	public InputDataset[] getAllInputDatasets() {
		return getAll();
	}

//  in api res
//  {id}/datasetinput/{entityUniqueId}
	@Override
	public InputDataset getInputDataset(Long id, Long processingtestid) {
		URI uri = UriBuilder.fromUri(getBaseUrl()).path("" + processingtestid).path("datasetinput").path("" + id).build();
		return get(uri);
	}
	
//	in resource api 
//	{id}/datasetinput/getbycommonid/{commonid}
	
	@Override
	public InputDataset[] getAllInputDatasetsById(Long commonId, Long processingtestid) {
		URI uri = UriBuilder.fromUri(getBaseUrl()).path("" + processingtestid).path("datasetinput").path("getbycommonid").path("" + commonId).build();		
		return getAll(uri);
	}	
}
