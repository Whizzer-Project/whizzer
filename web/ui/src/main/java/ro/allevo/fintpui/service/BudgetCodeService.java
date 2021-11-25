package ro.allevo.fintpui.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.dao.BudgetCodeRestApiDao;
import ro.allevo.fintpui.model.BudgetCode;
import ro.allevo.fintpui.utils.PagedCollection;

@Service
public class BudgetCodeService {

	@Autowired
	private BudgetCodeRestApiDao budgetCodeDao;

	public BudgetCode[] getAllBudgetCodes() {
		return budgetCodeDao.getAllBudgetCodes();
	}
	
	public PagedCollection<BudgetCode> getPage() {
		return budgetCodeDao.getPage();
	}

	public BudgetCode getBudgetCode(String id) {
		return budgetCodeDao.getBudgetCode(id);
	}

	public void insertBudgetCode(BudgetCode budgetCode) {
		budgetCodeDao.insertBudgetCode(budgetCode);
	}

	public void updateBudgetCode(BudgetCode budgetCode, String id) {
		budgetCodeDao.updateBudgetCode(budgetCode, id);
	}

	public void deleteBudgetCode(String id) {
		budgetCodeDao.deleteBudgetCode(id);
	}
}
