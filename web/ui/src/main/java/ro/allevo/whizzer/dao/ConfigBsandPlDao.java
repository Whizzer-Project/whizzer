package ro.allevo.whizzer.dao;

import org.springframework.stereotype.Service;

import ro.allevo.whizzer.model.ConfigBsandPl;
import ro.allevo.whizzer.model.PKIEntity;
@Service
public interface ConfigBsandPlDao {
	
	public ConfigBsandPl[] getAllConfigBsandPl();
	PKIEntity getPkis(String entity, Integer year);
	
}
