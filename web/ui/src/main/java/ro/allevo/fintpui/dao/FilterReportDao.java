package ro.allevo.fintpui.dao;

import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.springframework.stereotype.Service;

@Service
public interface FilterReportDao {
	
	public Response getFilterByReportAndUser(String reportName, Integer userId);
	public Response insertFilter(List<Map<String, String>> filter, String reportName, Integer userId);

}
