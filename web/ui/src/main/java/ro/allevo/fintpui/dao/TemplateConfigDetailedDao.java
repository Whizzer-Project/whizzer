package ro.allevo.fintpui.dao;

import java.util.LinkedHashMap;
import java.util.List;

import javax.ws.rs.core.Response;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.model.TemplateConfigDetailed;
import ro.allevo.fintpui.utils.PagedCollection;

@Service
public interface TemplateConfigDetailedDao {
	
	public TemplateConfigDetailed[] getAllTemplateConfigDetailed(String configId);
	public TemplateConfigDetailed getTemplateConfigDetailedById(String configId, Integer fieldId);
	public PagedCollection<TemplateConfigDetailed> getTemplateConfigDetailedPage(String configId,LinkedHashMap<String, List<String>> param);
	public ResponseEntity<String> insertTemplatesConfigDetailed(Integer templateConfigId,TemplateConfigDetailed[] templateConfigDetailed);
//	public Response deleteTemplatesConfigDetailedsByConfigId(Integer configId);
	public Response deleteTemplatesConfigDetailedById(String id);
}
