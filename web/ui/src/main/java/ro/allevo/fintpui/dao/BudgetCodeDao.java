package ro.allevo.fintpui.dao;


import org.springframework.stereotype.Service;

import ro.allevo.fintpui.model.BudgetCode;


@Service
public interface BudgetCodeDao {
	
	public BudgetCode[] getAllBudgetCodes();
	public BudgetCode getBudgetCode(String id);
	public void insertBudgetCode(BudgetCode budgetCode);
	public void updateBudgetCode(BudgetCode budgetCode, String id);
	public void deleteBudgetCode(String id);

}