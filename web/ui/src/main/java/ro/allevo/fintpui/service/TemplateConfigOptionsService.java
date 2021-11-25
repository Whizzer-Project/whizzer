package ro.allevo.fintpui.service;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import ro.allevo.fintpui.dao.TemplateConfigOptionsRestApiDao;
import ro.allevo.fintpui.model.TemplateConfigOptions;
import ro.allevo.fintpui.utils.PagedCollection;

@Service
public class TemplateConfigOptionsService {
	
	@Autowired
	private TemplateConfigOptionsRestApiDao templateConfigOptionsDao;
	
//	@Autowired
//	private TemplateSmallConfigRestApiDao templateSmallConfigDao;
	
	public TemplateConfigOptions[] getAllTemplateConfig() {
		return templateConfigOptionsDao.getAllTemplateConfingOptions();
	}
	
	public TemplateConfigOptions[] getAllTemplateConfig(LinkedHashMap<String, List<String>> params) {
		return templateConfigOptionsDao.getAll(params);
	}
	
	public Map<Integer, List<String>> getAllConfingOptionsValues(){
		return templateConfigOptionsDao.getAllConfingOptionsValues();
	}
	
	public JSONObject getAllConfingOptionsValues(Set<String> setEntity){
		return templateConfigOptionsDao.getAllConfingOptionsValues(setEntity);
	}
	
	public PagedCollection<TemplateConfigOptions> getPage(LinkedHashMap<String, List<String>> param){
		return templateConfigOptionsDao.getPage(param);
	}
	
	public Object[] getConfigOption(String condition, String queryValue) throws JSONException, JsonGenerationException, JsonMappingException, IOException{
		return templateConfigOptionsDao.getConfigOption(condition, queryValue);
	}
		
//	public void insertTemplateConfig(TemplateConfig templateConfig) {
//		templateConfigDao.insertTemplateConsfing(templateConfig);
//	}
//	
//	public void updateTemplateConfig(TemplateConfig templateConfig, Long id) {
//		templateConfigDao.updateTemplateConsfing(templateConfig, id);
//	}
//	
//	public void deleteTemplateConfig(Long id) {
//		templateConfigDao.deleteTemplateConsfing(id);
//	}
}
