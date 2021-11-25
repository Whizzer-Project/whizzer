package ro.allevo.connect.model;

import java.util.List;

import ro.allevo.fintpws.model.BaseEntity;
import ro.allevo.fintpws.util.Invariants;


public class AccountBalancesResponseEntity extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Account account;
	private List<BalanceEntity> balances;


	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public List<BalanceEntity> getBalances() {
		return balances;
	}

	public void setBalances(List<BalanceEntity> balances) {
		this.balances = balances;
	}

	@Override
	public String toString() {
		return "AccountResponseEntity [account=" + account + ", balances=" + balances + "]";
	}	

	@Override
	public String getClassEvent() {
		return Invariants.connectClassEvents.MANAGE.toString();
	}
	
	@Override
	public String getMessage() {
		return "account balances";
	}

}
