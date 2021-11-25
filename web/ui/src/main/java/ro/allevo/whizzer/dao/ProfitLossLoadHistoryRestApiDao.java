package ro.allevo.whizzer.dao;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.dao.RestApiDao;
import ro.allevo.whizzer.model.ProfitLossLoadHistory;

@Service
public class ProfitLossLoadHistoryRestApiDao extends RestApiDao<ProfitLossLoadHistory> implements ProfitLossLoadHistoryDao{
	
	@Autowired
	Config config;
	
	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getWhizzerURL()).path("profit-loss").build();
	}
	
	public ProfitLossLoadHistoryRestApiDao() {
		super(ProfitLossLoadHistory.class);
	}

	@Override
	public ProfitLossLoadHistory getProfitLoss(String id) {
		return get(id);
	}

	@Override
	public ProfitLossLoadHistory[] getAllProfitLosses() {
		return getAll();
	}

	@Override
	public ProfitLossLoadHistory[] getProfitLosses(LinkedHashMap<String, List<String>> params) {
		return getAll(params);
	}

	@Override
	public void insertProfitLoss(ProfitLossLoadHistory profitLoss) {
		post(profitLoss);		
	}

	@Override
	public void updateProfitLoss(ProfitLossLoadHistory profitLoss, String id) {
		put(id, profitLoss);
	}

	@Override
	public void deleteProfitLoss(String id) {
		delete(id);
	}
	
	@Override
	public String computeBsForecast(String entity, Integer historicalbs, Integer realisedbs, Integer forecast) {
		URI uri = UriBuilder.fromUri(config.getWhizzerURL()).path("profit-loss").path("compute-bs-forecast")
				.queryParam("entity", entity).queryParam("historicalbs", historicalbs).queryParam("realisedbs", realisedbs).queryParam("forecast", forecast).build();
		return getObject(uri, String.class);
	}
}
