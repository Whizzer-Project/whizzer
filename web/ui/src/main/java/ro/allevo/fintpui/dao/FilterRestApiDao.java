package ro.allevo.fintpui.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.model.ReportFilter;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

@Service
public class FilterRestApiDao extends RestApiDao<ReportFilter> implements FilterDao {

	@Autowired
	Config config;
	
	public FilterRestApiDao() {
		super(ReportFilter.class);
	}

	@Override
	public ResponseEntity<String> saveFilter(ReportFilter[] listFilter) {
		return  post(listFilter);
	}

	@Override
	public ReportFilter[] getFilters(String businessArea, Integer userId) {
		URI uri = UriBuilder.fromUri(getBaseUrl()).queryParam("businessArea", businessArea).queryParam("userId", userId).build();
		return getAll(uri);
	}

	@Override
	public ReportFilter[] getFilter(String businessArea, Integer userId , String templateName) {
		URI uri = UriBuilder.fromUri(getBaseUrl()).queryParam("businessArea", businessArea).queryParam("userId", userId).queryParam("templateName", templateName).build();
		return getAll(uri);
	}

	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getAPIUrl()).path("report-filters").build();
	}

	@Override
	public ResponseEntity<String> deleteFilter(String businessArea, Integer userId , String templateName){
		URI uri = UriBuilder.fromUri(getBaseUrl()).queryParam("businessArea", businessArea).queryParam("userId", userId).queryParam("templateName", templateName).build();
		return delete(uri);
	}
}
