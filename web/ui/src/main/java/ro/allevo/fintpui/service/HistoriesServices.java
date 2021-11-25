package ro.allevo.fintpui.service;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.dao.HistoriesRestApiDao;
import ro.allevo.fintpui.model.History;
import ro.allevo.fintpui.utils.PagedCollection;

@Service
public class HistoriesServices {

	@Autowired
	private HistoriesRestApiDao historiesDao;
	
	public History[] getAllHistories(LinkedHashMap<String, List<String>> params) {
		return historiesDao.getAll(params);
	}
	
	public History getHistory(String id) {
		return historiesDao.getHistoryById(id);
	}
	
	public PagedCollection<History> getAllHistoriesPaged(LinkedHashMap<String, List<String>> params){
		return historiesDao.getPage(params);
	}
}
