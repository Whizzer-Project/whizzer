package ro.allevo.fintpui.service;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.dao.LocationCodeRestApiDao;
import ro.allevo.fintpui.model.InternalEntity;
import ro.allevo.fintpui.model.LocationCode;
import ro.allevo.fintpui.utils.PagedCollection;

@Service
public class LocationCodeService {
	
	@Autowired
	private LocationCodeRestApiDao locationCodeDao;
	
	public LocationCode[] getAllLocationCodes() {
		return locationCodeDao.getAllLocationCodes();
	}
	
	public PagedCollection<LocationCode> getPage() {
		return locationCodeDao.getPage();
	}
	
	public LocationCode getLocationCode(String id) {
		return locationCodeDao.getLocationCode(id);
	}

	public void insertLocationCode(LocationCode locationCode) {
		locationCodeDao.insertLocationCode(locationCode);
	}

	public void updateLocationCode(LocationCode locationCode, String id) {
		locationCodeDao.updateLocationCode(locationCode, id);
	}

	public void deleteLocationCode(String id) {
		locationCodeDao.deleteLocationCode(id);
	}
	
	public LocationCode[] getLocationCode(LinkedHashMap<String, List<String>> param) {
		return locationCodeDao.getAll(param);
	}

}
