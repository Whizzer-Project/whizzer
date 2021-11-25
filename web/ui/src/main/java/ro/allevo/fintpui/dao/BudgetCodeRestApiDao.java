package ro.allevo.fintpui.dao;

import java.net.URI;
import javax.ws.rs.core.UriBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.model.BudgetCode;


@Service
public class BudgetCodeRestApiDao extends RestApiDao<BudgetCode> implements BudgetCodeDao{

	@Autowired
	Config config;
	
	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getAPIUrl()).path("budget-codes").build();
	}
	
	public BudgetCodeRestApiDao() {
		super(BudgetCode.class);
	}
	
	@Override
	public BudgetCode[] getAllBudgetCodes() {
		return getAll();
	}

	@Override
	public BudgetCode getBudgetCode(String id) {
		return get(id);
	}

	@Override
	public void insertBudgetCode(BudgetCode budgetCode) {
		post(budgetCode);
	}

	@Override
	public void updateBudgetCode(BudgetCode budgetCode, String id) {
		put(id, budgetCode);
	}

	@Override
	public void deleteBudgetCode(String id) {
		delete(id);
	}
}

