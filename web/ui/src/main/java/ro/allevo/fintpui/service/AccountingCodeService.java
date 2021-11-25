package ro.allevo.fintpui.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.dao.AccountingCodeRestApiDao;
import ro.allevo.fintpui.model.AccountingCode;
import ro.allevo.fintpui.utils.PagedCollection;

@Service
public class AccountingCodeService {

	@Autowired
	private AccountingCodeRestApiDao accountingCodeDao;

	public AccountingCode[] getAllAccountingCodes() {
		return accountingCodeDao.getAllAccountingCodes();
	}
	
	public PagedCollection<AccountingCode> getPage() {
		return accountingCodeDao.getPage();
	}

	public AccountingCode getAccountingCode(String id) {
		return accountingCodeDao.getAccountingCode(id);
	}

	public void insertAccountingCode(AccountingCode accountingCode) {
		accountingCodeDao.insertAccountingCode(accountingCode);
	}

	public void updateAccountingCode(AccountingCode accountingCode, String id) {
		accountingCodeDao.updateAccountingCode(accountingCode, id);
	}

	public void deleteAccountingCode(String id) {
		accountingCodeDao.deleteAccountingCode(id);
	}
}
