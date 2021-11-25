package ro.allevo.fintpui.dao;


import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import ro.allevo.fintpui.model.InternalEntity;

@Service
public interface InternalEntityDao {

	public InternalEntity[] getAllInternalEntities(Boolean isAdmin);
	public List<HashMap<String, Object>> getAllInternalEntities();
	public InternalEntity getInternalEntity(String id);
	public void insertInternalEntity(InternalEntity internalEntity);
	public void updateInternalEntity(InternalEntity internalEntity, String id);
	public void deleteInternalEntity(String id);
	public String[] getInternalsByRights(String messagetype);
	public InternalEntity[] getAllInternalEntities(LinkedHashMap<String, List<String>> params);
}
