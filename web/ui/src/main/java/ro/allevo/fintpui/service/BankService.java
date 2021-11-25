package ro.allevo.fintpui.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.dao.BankRestApiDao;
import ro.allevo.fintpui.model.Bank;
import ro.allevo.fintpui.utils.PagedCollection;
import ro.allevo.fintpui.utils.SortListBankByName;

@Service
public class BankService {

	@Autowired
	private BankRestApiDao bankDao;

	public Bank[] getAllBanks() {
		return bankDao.getAllBanks();
	}
	
	public Bank[] getAllBanks(LinkedHashMap<String, List<String>> params) {
		return bankDao.getAll(params);
	}
	
	public PagedCollection<Bank> getPage() {
		return bankDao.getPage();
	}
	
	public PagedCollection<Bank> getPage(LinkedHashMap<String, List<String>> params){
		return bankDao.getPage(params);
	}

	public Bank getBank(String bic) {
		return bankDao.getBank(bic);
	}

	public void insertBank(Bank bank) {
		bankDao.insertBank(bank);
	}

	public void updateBank(Bank bank, String bic) {
		bankDao.updateBank(bank, bic);
	}

	public void deleteBank(String bic) {
		bankDao.deleteBank(bic);
	}
	
//	private List<HashMap<String, Object>> getAllBankBics(){
//		URI uri = UriBuilder.fromUri(getBaseUrl()).path("access").build();
//		String[] response = getList(uri, String.class);  
//		List<String> result = new ArrayList<>();
// 		for(HashMap<String, Object> internalEntity : internalEntities){
//			result.add(internalEntity.get("name").toString());
//		}
// 		Collections.sort(result);
//	}

	public List<HashMap<String, Object>> getAllBankBics() {
		List<HashMap<String, Object>> banks = bankDao.getAllBankBics();
		ArrayList<String> result = new ArrayList<>();
//		for (HashMap<String, Object> bank : banks) {
//			result.add(bank.get("bic").toString());
//		}
		Collections.sort(banks, new SortListBankByName());
		return banks;
	}
	
	public List<String> getAllListBankBics() {
		List<HashMap<String, Object>> banks = bankDao.getAllBankBics();
		ArrayList<String> result = new ArrayList<>();
		for (HashMap<String, Object> bank : banks) {
			result.add(bank.get("bic").toString());
		}
		Collections.sort(result);
		return result;
	}

	
}
