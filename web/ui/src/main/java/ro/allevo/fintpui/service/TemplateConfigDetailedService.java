package ro.allevo.fintpui.service;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.dao.TemplateConfigDetailedRestApiDao;
import ro.allevo.fintpui.model.TemplateConfigDetailed;
import ro.allevo.fintpui.utils.PagedCollection;

@Service
public class TemplateConfigDetailedService {
	
	@Autowired
	TemplateConfigDetailedRestApiDao templateConfigDetailedRestApiDao;
	
	public TemplateConfigDetailed getTemplateConfigDetailedByFieldId(String configId, Integer fieldId) {
		return templateConfigDetailedRestApiDao.getTemplateConfigDetailedById(configId, fieldId);
	}
	
	public ResponseEntity<String> insertTemplateConfigDetailed(Integer templateConfigId, TemplateConfigDetailed[] templateConfigDetailed) {
		return templateConfigDetailedRestApiDao.insertTemplatesConfigDetailed(templateConfigId, templateConfigDetailed);
	}

	public TemplateConfigDetailed[] getAllTemplateConfigDetailed(String configId) {
		return templateConfigDetailedRestApiDao.getAllTemplateConfigDetailed(configId);
	}

	public ResponseEntity<String> updateTemplateConfigDetailed(TemplateConfigDetailed templateConfigDetailed) {
		return templateConfigDetailedRestApiDao.updateTempatesConfigDetailed(templateConfigDetailed);
	}

	public void deleteTemplateConfigDetailed(String id) {
		templateConfigDetailedRestApiDao.deleteTemplatesConfigDetailedById(id);
		
	}
	
	public PagedCollection<TemplateConfigDetailed> getPage(LinkedHashMap<String, List<String>> param){
		return templateConfigDetailedRestApiDao.getPage(param);
	}
	
	public PagedCollection<TemplateConfigDetailed> getTemplateConfigDetailedPage(String configId, LinkedHashMap<String, List<String>> param){
		return templateConfigDetailedRestApiDao.getTemplateConfigDetailedPage(configId, param);
	}

}
