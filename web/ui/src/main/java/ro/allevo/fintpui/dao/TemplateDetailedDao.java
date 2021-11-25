package ro.allevo.fintpui.dao;

import java.util.List;

import org.springframework.http.ResponseEntity;

import ro.allevo.fintpui.model.TemplateDetailed;

public interface TemplateDetailedDao {

	public TemplateDetailed[] getAllTemplateDetailed(Integer configId);
	public TemplateDetailed getTemplateDetailed(Integer configId);
	public ResponseEntity<String> insertListTemplateDetailed(List<TemplateDetailed> templateDetailed);
	public ResponseEntity<String> updateTemplateDetailed(TemplateDetailed templateDetailed);
	public ResponseEntity<String> deleteTemplateDetailedByTemplateId(Integer templateId); 
}
