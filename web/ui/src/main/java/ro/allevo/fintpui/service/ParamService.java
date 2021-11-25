package ro.allevo.fintpui.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.dao.ParamRestApiDao;
import ro.allevo.fintpui.model.Params;
import ro.allevo.fintpui.utils.PagedCollection;

@Service
public class ParamService {
	
	@Autowired
	private ParamRestApiDao paramRestApiDao;
	
	public Params[] getAllParams() {
		return paramRestApiDao.getAll();
	}

	public PagedCollection<Params> getPage() {
		return paramRestApiDao.getPage();
	}
	
	public Params getParam(String code) {
		return paramRestApiDao.get(code);
	}

	public void updateParam(Params params, String code) {
		paramRestApiDao.put(code , params);	
	}
}
