package ro.allevo.fintpui.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.dao.FilterRestApiDao;
import ro.allevo.fintpui.model.ReportFilter;

@Service
public class FilterService {

	@Autowired
	private FilterRestApiDao filterDao;
	
	public ResponseEntity<String> insertFilter(ReportFilter[] filters){
		return filterDao.saveFilter(filters);
	}
	
	public ReportFilter[] getFilters(String businessArea, Integer userId) {
		return filterDao.getFilters(businessArea, userId);
	}
	
	public ReportFilter[] getFilters(String businessArea, Integer userId, String template) {
		return filterDao.getFilter(businessArea, userId, template);
	}
	
	public ResponseEntity<String> deleteFilter(String businessArea, Integer userId , String templateName){
		return filterDao.deleteFilter(businessArea, userId, templateName);
	}

}
