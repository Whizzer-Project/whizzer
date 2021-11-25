package ro.allevo.whizzer.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sun.xml.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import ro.allevo.whizzer.dao.ConfigBsandPlRestApiDao;
import ro.allevo.whizzer.model.ConfigBsandPl;
import ro.allevo.whizzer.model.PKIEntity;

@Service
public class ConfigBsandPlService {

	@Autowired
	private ConfigBsandPlRestApiDao configBsandPlDao;

	public ConfigBsandPl[] getAllConfigBsandPls() {
		return configBsandPlDao.getAllConfigBsandPl();
	}
		
	public Map<String,String> getLabels(LinkedHashMap<String, List<String>> params){
		ConfigBsandPl[] configBsandPls = configBsandPlDao.getPage(params).getItems();
		Map<String,String> labels = new HashMap<String,String>();
		for(ConfigBsandPl configBsandPl:configBsandPls) {
			labels.put(configBsandPl.getName(), configBsandPl.getLabel());
		}
		return labels;
	}
	
	public List<ConfigBsandPl> getLabelsList(){
		return Arrays.asList(configBsandPlDao.getPage().getItems());
	}

	public PKIEntity getPkis(String entity, Integer year){
		return configBsandPlDao.getPkis(entity, year);
	}
	
	public Map<String,String> getMandatoryAndOptionalFields(LinkedHashMap<String, List<String>> params){
		ConfigBsandPl[] configBsandPls = configBsandPlDao.getPage(params).getItems();
		Map<String,String> mandatoryAndOptionalFields = new HashMap<String,String>();
		for(ConfigBsandPl configBsandPl:configBsandPls) {
			mandatoryAndOptionalFields.put(configBsandPl.getName(), configBsandPl.getMandatory());
		}
		return mandatoryAndOptionalFields;		
	}
}
