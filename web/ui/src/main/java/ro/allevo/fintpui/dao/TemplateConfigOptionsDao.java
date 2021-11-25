package ro.allevo.fintpui.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;

import ro.allevo.fintpui.model.TemplateConfigOptions;

@Service
public interface TemplateConfigOptionsDao {

	public TemplateConfigOptions[] getAllTemplateConfingOptions();

	public Map<Integer, List<String>> getAllConfingOptionsValues();
//	public Map<String, List<Object>> getAllConfingOptionsValues(Set<String> setEntity);
	public JSONObject getAllConfingOptionsValues(Set<String> setEntity);
}
