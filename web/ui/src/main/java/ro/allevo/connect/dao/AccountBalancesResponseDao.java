package ro.allevo.connect.dao;

import org.springframework.stereotype.Service;

@Service
public interface AccountBalancesResponseDao {

	public String getBalanceAmount(String bic, String accountId);
}
