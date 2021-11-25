package ro.allevo.fintpui.dao;

import org.springframework.stereotype.Service;

@Service
public interface ValidationsDao {
	
	public boolean validateIban(String iban);
	public boolean validateCif(String cif);
	public boolean validateCNP(String cnp);
	public boolean validateIbanRo(String iban, String debtor);

}
