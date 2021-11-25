package ro.allevo.fintpui.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.dao.ExternalEntityRestApiDao;
import ro.allevo.fintpui.model.ExternalEntity;
import ro.allevo.fintpui.utils.PagedCollection;

@Service
public class ExternalEntitiesService {

	@Autowired
	private ExternalEntityRestApiDao externalEntityDao;
	
	/**
	 * no rights required
	 * @return list of external entitites with Id, name
	 */
	public List<HashMap<String, Object>> getExternalEntitiesWithOutRules(){
		return externalEntityDao.getExternalEntitiesWithOutRules();
	}
	
	public ExternalEntity[] getAllExternalEntities() {
		return externalEntityDao.getAllExternalEntities();
	}
	
	public ExternalEntity[] getAllExternalEntities(LinkedHashMap<String, List<String>> params) {
		return externalEntityDao.getAll(params); 
	}
	
	public PagedCollection<ExternalEntity> getPage() {
		return externalEntityDao.getPage();
	}
	
	public PagedCollection<ExternalEntity> getPage(LinkedHashMap<String, List<String>> param) {
		return externalEntityDao.getPage(param);
	}
	
	public ExternalEntity getExternalEntity(String name) {
		return externalEntityDao.getExternalEntity(name);
	}

	public void insertExternalEntity(ExternalEntity externalEntities) {
		externalEntityDao.insertExternalEntity(externalEntities);
	}

	public void updateExternalEntity(ExternalEntity externalEntity, String name) {
		externalEntityDao.updateExternalEntity(externalEntity, name);
	}

	public void deleteExternalEntity(String name) {
		externalEntityDao.deleteExternalEntity(name);
	}
	
	public List<String> getAllExternalEntityNames() {
		ExternalEntity[] externalEntities = getAllExternalEntities();
		ArrayList<String> result = new ArrayList<>();
 		for(ExternalEntity externalEntity : externalEntities){
			result.add(externalEntity.getName());
		}
 		Collections.sort(result);
		return result;
	}

}
