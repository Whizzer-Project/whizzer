package ro.allevo.whizzer.service;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.whizzer.dao.ProfitLossLoadHistoryRestApiDao;
import ro.allevo.whizzer.model.ProfitLossLoadHistory;

@Service
public class ProfitLossLoadHistoryService {
	
	@Autowired
	private ProfitLossLoadHistoryRestApiDao profitLossRestApiDao;
	
	public ProfitLossLoadHistory[] getAllProfitLosses() {
		return profitLossRestApiDao.getAllProfitLosses();
	}
	
	public ProfitLossLoadHistory[] getProfitLosses(LinkedHashMap<String, List<String>> params) {
		return profitLossRestApiDao.getProfitLosses(params);
	}
	
	public ProfitLossLoadHistory getProfitLoss(String id) {
		return profitLossRestApiDao.getProfitLoss(id);
	}
	
	public void insertProfitLoss(ProfitLossLoadHistory profitLoss) {
		profitLossRestApiDao.insertProfitLoss(profitLoss);
	}
	
	public void updateProfitLoss(ProfitLossLoadHistory profitLoss, String id) {
		profitLossRestApiDao.updateProfitLoss(profitLoss, id);
	}
	
	public void deleteProfitLoss(String id) {
		profitLossRestApiDao.deleteProfitLoss(id);
	}
	public String computeBsForecast(String entity, Integer historicalbs, Integer realisedbs, Integer forecast) {
		return profitLossRestApiDao.computeBsForecast(entity, historicalbs, realisedbs, forecast);
	}
}
