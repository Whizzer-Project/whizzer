package ro.allevo.fintpui.dao;

import java.net.URI;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.springframework.beans.factory.annotation.Autowired;

import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.model.ReportFilter;

public class FilterReportRestApiDao extends RestApiDao<ReportFilter> implements FilterReportDao {
	
	@Autowired
	Config config;

	public FilterReportRestApiDao() {
		super(ReportFilter.class);
	}

	@Override
	public Response getFilterByReportAndUser(String reportName, Integer userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response insertFilter(List<Map<String, String>> filter, String reportName, Integer userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getAPIUrl()).path("internal-accounts").build();
	}

}
