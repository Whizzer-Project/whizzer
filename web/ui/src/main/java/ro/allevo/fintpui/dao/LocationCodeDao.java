package ro.allevo.fintpui.dao;



import org.springframework.stereotype.Service;
import ro.allevo.fintpui.model.LocationCode;


@Service
public interface LocationCodeDao {

	public LocationCode[] getAllLocationCodes();
	public LocationCode getLocationCode(String id);
	public void insertLocationCode(LocationCode locationCode);
	public void updateLocationCode(LocationCode locationCode, String id);
	public void deleteLocationCode(String id);

}
