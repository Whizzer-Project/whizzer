package ro.allevo.fintpui.dao;


import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import ro.allevo.fintpui.model.Bank;
@Service
public interface BankDao {
	
	public Bank[] getAllBanks();
	public Bank getBank(String bic);
	public void insertBank(Bank bank);
	public void updateBank(Bank bank, String bic);
	public void deleteBank(String bic);
	public List<HashMap<String, Object>> getAllBankBics();
}
