package ro.allevo.fintpui.dao;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import ro.allevo.fintpui.model.TemplateConfig;

@Service
public interface TemplateConfigDao {

	public TemplateConfig[] getAllTemplateConfings();
	public TemplateConfig[] getAllTemplateConfings(LinkedHashMap<String, List<String>> params);
	public TemplateConfig getTemplateConfigWithXsd(String id);
	public TemplateConfig getTemplateConfig(String id);
//	public void insertTemplateConsfing(TemplateConfig templateConfig);
//	public void updateTemplateConsfing(TemplateConfig templateConfig, Long id);
//	public void deleteTemplateConsfing(Long id);
}
