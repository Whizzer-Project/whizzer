package ro.allevo.fintpui.dao;


import org.springframework.stereotype.Service;

import ro.allevo.fintpui.model.AccountingCode;

@Service
public interface AccountingCodeDao {
	
	public AccountingCode[] getAllAccountingCodes();
	public AccountingCode getAccountingCode(String id);
	public void insertAccountingCode(AccountingCode accountingCode);
	public void updateAccountingCode(AccountingCode accountingCode, String id);
	public void deleteAccountingCode(String id);

}