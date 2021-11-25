package ro.allevo.whizzer.dao;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import ro.allevo.whizzer.model.ProfitLossLoadHistory;

@Service
public interface ProfitLossLoadHistoryDao {

	public ProfitLossLoadHistory getProfitLoss(String id);
	public ProfitLossLoadHistory[] getAllProfitLosses();
	public ProfitLossLoadHistory[] getProfitLosses(LinkedHashMap<String, List<String>> params);
	public void insertProfitLoss(ProfitLossLoadHistory profitLoss);
	public void updateProfitLoss(ProfitLossLoadHistory profitLoss, String id);
	public void deleteProfitLoss(String id);
	public String computeBsForecast(String entity, Integer historicalbs, Integer realisedbs, Integer forecast);
	
}
