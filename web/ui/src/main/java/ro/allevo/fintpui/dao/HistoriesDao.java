package ro.allevo.fintpui.dao;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import ro.allevo.fintpui.model.History;
import ro.allevo.fintpui.utils.PagedCollection;

@Service
public interface HistoriesDao {
	public History[] getAllHistories(LinkedHashMap<String, List<String>> params);
	public History getHistoryById(String id);
	public PagedCollection<History> getPagedAllHistories(LinkedHashMap<String, List<String>> params);
}
