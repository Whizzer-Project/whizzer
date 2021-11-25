package ro.allevo.fintpui.dao;

import java.util.List;

import org.springframework.http.ResponseEntity;

import ro.allevo.fintpui.model.TemplateGroup;

public interface TemplateGroupDao {
	
	public ResponseEntity<String> deleteTemplateGroup(Integer id);
//	public ResponseEntity<String> updateTemplateGroup(TemplateGroup templateGroup);
	public ResponseEntity<String> insertTemplateGroup(List<TemplateGroup> templateGroup);
	//public TemplateGroup[] getTemplatesGroup(Integer templateId);
}
