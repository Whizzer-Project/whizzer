package ro.allevo.connect.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.connect.dao.AccountBalancesResponseRestApiDao;

@Service
public class AccountBalancesResponseService {
	
	@Autowired
	private AccountBalancesResponseRestApiDao accountResponseDao;
	
	public String getBalanceAmount(String bic, String accountId) {
		return accountResponseDao.getBalanceAmount(bic, accountId);
	}
	
}
