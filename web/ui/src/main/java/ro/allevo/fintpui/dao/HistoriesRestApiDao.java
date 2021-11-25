package ro.allevo.fintpui.dao;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.model.History;
import ro.allevo.fintpui.utils.PagedCollection;

@Service
public class HistoriesRestApiDao extends RestApiDao<History> implements HistoriesDao{
	
	@Autowired
	private Config config;

	public HistoriesRestApiDao() {
		super(History.class);
	}

	@Override
	public History[] getAllHistories(LinkedHashMap<String, List<String>> params) {
		return getAll(getBaseUrl(), params);
	}

	@Override
	public History getHistoryById(String id) {
		return get(id);
	}

	@Override
	public PagedCollection<History> getPagedAllHistories(LinkedHashMap<String, List<String>> params) {
		return getPagedAllHistories(params);
	}

	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getAPIUrl()).path("histories").build();
	}

}
