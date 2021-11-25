package ro.allevo.fintpws.resources;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.UriInfo;


public class ValidationsResource {
	
//	code for CNP validation from : 
//	https://github.com/1Mihail/CNP-Validator/blob/master/cnp-validator/src/main/java/mihailproductions/com/cnp_validator/CNPUtils.java
	
	private UriInfo uriInfo;
	private EntityManager emc;
	
	public ValidationsResource(UriInfo uriInfo, EntityManager emc) {
		this.uriInfo = uriInfo;
		this.emc = emc;
	}
	
	@Path("rules")
	public ValidationsRulesResources validateRules(@QueryParam("value") String value) {	
		return new ValidationsRulesResources(uriInfo, emc);		
	}
	
	@GET
	@Path("cnp")
	public boolean validateCNP(@QueryParam("value") String value) {	
		return isCNPValid(value);		
	}
	
	@GET
	@Path("cif")
	public boolean validateCIF(@QueryParam("value") String value) {	
		return isCIFValid(value);		
	}
	
	@GET
	@Path("iban")
	public boolean validateIBAN(@QueryParam("value") String value) {	
		return isIBANValid(value);		
	}	
	
	@GET
	@Path("roiban")
	public boolean validateIBANRo(@QueryParam("iban") String iban, @QueryParam("debtor") String debtor) {	
		return isIBANRoValid(iban, debtor);		
	}	
	
	private boolean isIBANRoValid(String iban, String debtor) {
		 if(!iban.startsWith("RO"))
			 return true;
		 if(iban!=null && iban.length()>=8 && debtor!=null && debtor.length()>=4) 
			 return iban.substring(4, 7).equals(debtor.substring(0, 3));
		 else
			 return false;
    }
	
    private boolean isCNPValid(String cnp) {
        if (cnp != null) {
            cnp = initializeCNP(cnp);
        } else {
            return false;
        }
        return cnp.length() == 13 && cnp.matches("\\d+") && controlValidation(cnp) && structureValidation(cnp);
    }
    
    private String initializeCNP(String cnp) {
        return cnp.trim();
    }
    
    private boolean controlValidation(String cnp) {
        final String controlSequence = "279146358279";
        final int controlDivider = 11;
        int controlSum = 0;
        int controlDigit;
        for (int charIndex = 0; charIndex < controlSequence.length(); charIndex++) {
            controlSum += Character.getNumericValue(cnp.charAt(charIndex)) * Character.getNumericValue(controlSequence.charAt(charIndex));
        }
        if (controlSum % controlDivider == 10) {
            controlDigit = 1;
        } else {
            controlDigit = controlSum % 11;
        }
        return controlDigit + '0' == cnp.charAt(cnp.length() - 1);
    }
    
    private boolean structureValidation(String cnp) {
        String yymmdd = cnp.substring(1, 7);
        String county = cnp.substring(7, 9);
        String registerNumber = cnp.substring(9, 12);
        return cnp.charAt(0) != '0' && birthdateValidation(yymmdd) && countyValidation(county) && registerNumberValidation(registerNumber);
    }

    private boolean birthdateValidation(String yymmdd) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd", Locale.getDefault());
        sdf.setLenient(false);
        try {
            sdf.parse(yymmdd);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
    
    private boolean countyValidation(String county) {
        int countyIndex = Integer.parseInt(county);
        return (countyIndex >= 1 && countyIndex <= 46) || countyIndex == 51 || countyIndex == 52;
    }
    
    private boolean registerNumberValidation(String registerNumber) {
        return !registerNumber.equals("000");
    }
    
    private boolean isCIFValid(String cif) {
    	
    	// eliminate fiscal atribute RO and spaces
     	String cifOnlyNumbers = cif;
   
     	if(cifOnlyNumbers.toUpperCase().startsWith("RO")) {
     		cifOnlyNumbers = cifOnlyNumbers.substring(2);
     		cifOnlyNumbers = cifOnlyNumbers.trim();
     	}
     	
     	// if more than 10 digits not valid
     	if(cifOnlyNumbers.length() > 10) {
     		return false;
     	}
     	
     	// control number
     	int controlNumber = 753217532;
     	
     	int cifAsInt;
     	try {
     		cifAsInt = Integer.valueOf(cifOnlyNumbers);
		} catch (NumberFormatException e) {
			return false;
		}
     	
     	// extract control digit
     	int controlDigit = cifAsInt % 10;
     	cifAsInt= cifAsInt / 10;
     	
     	// execute digit operations 
     	int totalDigitSum = 0;
     	while(cifAsInt > 0){
     		totalDigitSum += (cifAsInt % 10) * (controlNumber % 10);
     		cifAsInt = cifAsInt / 10;
     		controlNumber = controlNumber / 10 ;
     	}
     	
     	// sum resulted is multiplied with 10, and then is divided by 11
     	int checkNumber = totalDigitSum * 10 % 11;
     	
     	// If the check number is 10, then the check number is 0
     	if(checkNumber == 10){
     		checkNumber = 0;
     	}
     	
     	// The CIF is valid when the check number resulted after the validation is the same as the check number of the initial CIF
     	return controlDigit == checkNumber;
    }
    
    private boolean isIBANValid(String iban) {
    	
    	// 1. For Romania, IBAN length is 24.
    	if(iban.length() != 24) {
    		return false;
    	}
    	
    	// 2. Move the four initial characters to the end of the string
    	String firstCharsMoveToEnd = iban.substring(4) + iban.substring(0, 4);
    	
    	// 3. Replace each letter in the string with two digits, thereby expanding the string, where A = 10, B = 11, ..., Z = 35
    	BigInteger bigNumber = getStringAsNumber(firstCharsMoveToEnd);
    	
    	// 4.Interpret the string as a decimal integer and compute the remainder of that number on division by 97
    	int result = getRemainderofDivision(bigNumber);

    	//  IBAN is valid if remainder is 1
    	return result == 1;
    }
    
    private BigInteger getStringAsNumber(String charsAndDigits) {
    	
        String alphabeticOrderChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" ;
        int counter = 10;
        char[] alphabeticArray = alphabeticOrderChars.toCharArray();
        Map<Character, Integer> charsWIthIntValues = new HashMap<Character, Integer>();

        for(char character : alphabeticArray) {
          charsWIthIntValues.put(character, counter);
          counter ++;
        }

        StringBuilder builder = new StringBuilder(35);

        char[] charsAndDigitsArray = charsAndDigits.toUpperCase().toCharArray();

        for(char character : charsAndDigitsArray) {
          if(charsWIthIntValues.get(character) != null) {
            builder.append(charsWIthIntValues.get(character)); 
          }
          else{
            builder.append(character);
          }
           		
        }
        return new BigInteger(builder.toString());
    }
    
    private int getRemainderofDivision(BigInteger bigInt){
		
	     // apply remainder() method 
	     BigInteger result = bigInt.remainder(new BigInteger("97")); 
		try {
			return Integer.valueOf(result.toString());
		} catch (NumberFormatException e) {
			return 77;
		} 
	}
   
}
