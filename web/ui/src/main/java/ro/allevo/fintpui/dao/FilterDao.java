package ro.allevo.fintpui.dao;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.model.ReportFilter;

@Service
public interface FilterDao {

	public ResponseEntity<String> saveFilter(ReportFilter[] listFilter);
	public ResponseEntity<String> deleteFilter(String businessArea, Integer userId , String templateName);
	public ReportFilter[] getFilters(String businessArea, Integer userId);
	public ReportFilter[] getFilter(String businessArea, Integer userId, String templateName);
}
