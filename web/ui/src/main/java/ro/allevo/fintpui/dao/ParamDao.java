package ro.allevo.fintpui.dao;

import org.springframework.stereotype.Service;

import ro.allevo.fintpui.model.Params;

@Service
public interface ParamDao {
	
	public Params[] getAllParams();
	public Params getParam(String code);
	public void updateParam(Params params, String code);

}
