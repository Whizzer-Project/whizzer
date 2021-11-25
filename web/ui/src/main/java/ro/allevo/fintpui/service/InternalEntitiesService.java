package ro.allevo.fintpui.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.dao.InternalEntityRestApiDao;
import ro.allevo.fintpui.model.InternalEntity;
import ro.allevo.fintpui.utils.PagedCollection;

@Service
public class InternalEntitiesService {

	@Autowired
	private InternalEntityRestApiDao internalEntityDao;
	
	public InternalEntity[] getAllInternalEntities(Boolean isAdmin) {
		return internalEntityDao.getAllInternalEntities(isAdmin);
	}
	
	/**
	 * no rights required
	 * @return list of inernal entitites with Id, name
	 */
	public List<HashMap<String, Object>> getAllInternalEntities(){
		return internalEntityDao.getAllInternalEntities();
	}
	
	public InternalEntity[] getAllInternalEntities(LinkedHashMap<String, List<String>> params) {
		return internalEntityDao.getAllInternalEntities(params);
	}
	
	public PagedCollection<InternalEntity> getPage() {
		return internalEntityDao.getPage();
	}
	
	public PagedCollection<InternalEntity> getPage(LinkedHashMap<String, List<String>> param) {
		return internalEntityDao.getPage(param);
	}
	
	public InternalEntity getInternalEntity(String id) {
		return internalEntityDao.getInternalEntity(id);
	}

	public void insertInternalEntity(InternalEntity internalEntities) {
		internalEntityDao.insertInternalEntity(internalEntities);
	}

	public void updateInternalEntity(InternalEntity internalEntity, String id) {
		internalEntityDao.updateInternalEntity(internalEntity, id);
	}

	public void deleteInternalEntity(String id) {
		System.out.println("Service " + id);
		internalEntityDao.deleteInternalEntity(id);
	}
	
	public List<String> getAllInternalEntityNames() {
		 List<HashMap<String, Object>> internalEntities = getAllInternalEntities();
		List<String> result = new ArrayList<>();
 		for(HashMap<String, Object> internalEntity : internalEntities){
			result.add(internalEntity.get("name").toString());
		}
 		Collections.sort(result);
		return result;
	}
	
	public Map<Integer, String> getAllInternalEntityIdAndName(){
		 List<HashMap<String, Object>> internalEntities = getAllInternalEntities();
		Map<Integer, String> map = new HashMap<>();
		for(HashMap<String, Object> internalEntity: internalEntities) {
			map.put((int) internalEntity.get("id"), internalEntity.get("name").toString());
		}
		return map;
	}
	
	public String[] getAllInternalEntitiesByRights(String messagetype) {
		return internalEntityDao.getInternalsByRights(messagetype);
	}
	
}
