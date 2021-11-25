package ro.allevo.connect.model;

import java.io.Serializable;

import ro.allevo.fintpws.model.BaseEntity;
import ro.allevo.fintpws.util.Invariants;

public class AccountWrapperEntity extends BaseEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AccountEntity account;

	public AccountEntity getAccount() {
		return account;
	}

	public void setAccount(AccountEntity account) {
		this.account = account;
	}

	@Override
	public String toString() {
		return "AccountWrapperEntity [account=" + account + "]";
	}

	@Override
	public String getClassEvent() {
		return Invariants.connectClassEvents.MANAGE.toString();
	}
	
	@Override
	public String getMessage() {
		return "account wrapper";
	}
	
}
