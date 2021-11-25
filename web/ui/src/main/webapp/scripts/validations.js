//number

$(function() {
	registerValidations();
});

function registerValidations() {
	registerAmountValidation();
	registerIBANValidation();
}

function registerAmountValidation() {
	$('.form-amount').numeric({
		decimalPlaces : 2,
		negative : false,
		decimal : '.'
	});
}

// iban
function registerIBANValidation() {
	$("input#iban").on(
			// should be on class
			'keyup change',
			function() {
				var iban = $('#iban').val();
				$("#bic").empty();
				var control = $('#bic').append('<option></option>');
				var error = []
				if (iban.length > 7) {
					if (iban.substr(0, 2) == 'RO') {
						$.each(bics, function(key, value) {
							if (value.bic.substr(0, 4) == iban.substr(4, 4)) {
								control.append('<option value="' + value.bic
										+ '">' + value.bic + '</option>');
								error.push(true)
							}
							else 
								error.push(false)
						});
					} else {
						$.each(bics, function(key, value) {
							control.append('<option value="' + value.bic + '">'
									+ value.bic + '</option>');
						})
						error = []
					}
				} else {
					$.each(bics, function(key, value) {
						control.append('<option value="' + value.bic + '">'
								+ value.bic + '</option>');
					});
					error = []
				}
				if (error.length > 0 && !error.includes(true)){
					$(this).parent().children('span').remove()
					$(this).parent().append('<span class="errorMessage">Warning: Invalid IBAN</span>')
					$('.create').attr('disabled','disabled')
				}else{
					$(this).parent().children('span').remove()
					$('.create').removeAttr('disabled')
				}
				$("#bic").append(control);
				var bic = document.querySelector("#bic")
				if (2 == bic.length && bic.options[0].innerText.trim() == ''){
					bic.selectedIndex = 1
				}
				
			});
}

function accountValidation(bics) {
	control = $('#bic').append('<option></option>');
	$.each(bics, function(key, value) {
		// array
		control.append('<option value="' + value + '">' + value + '</option>');
	});

	$("#bic").append(control);
}

function selectBic(bic) {
	var iban = $('#iban').val();
	control = $('#bic').append('<option></option>');
	$.each(bics, function(key, value) {
		if (value.substr(0, 4) == iban.substr(4, 4))
			if (value == bic) {
				control.append('<option value="' + value + '" selected>'
						+ value + '</option>');
			} else{
				control.append('<option value="' + value + '">' + value
						+ '</option>');
			}
	});
}
function registrationCIFValidation(elem){
	elem.on('keyup change',	function() {
		if ($('select[name="country"]').val() ==""){
			$('select[name="country"]').next().remove()
			$('select[name="country"]').parent().append('<span class="errorMessage">Required field</span>')
			$('select[name="country"]').focus()
		}
		else{
			$('select[name="country"]').next().remove()
		}
		var country = $('select[name="country"]').val()
		var val = $(this).val().replaceAll(" ", "")
		$(this).val(val)
		var cifOnlyNumbers = val
		elem.parent().find('span').each(function(){$(this).remove()})
 		$('.create').removeAttr('disabled')
		   
	 	if(cifOnlyNumbers.toUpperCase().startsWith("RO") && country.toUpperCase() == "RO") {
	 		cifOnlyNumbers = cifOnlyNumbers.substring(2);
	 		
	 	}else{
	 		if (country.toUpperCase() != "RO"){
	 			return
	 		}
	 		cifOnlyNumbers = cifOnlyNumbers.trim();
	 	}
	 	
	 	// if more than 10 digits not valid
	 	if(cifOnlyNumbers.length > 10) {
	 		elem.parent.append('<span class="errorMessage">Warning: invalid CUI/CIF</span>')
			$('.create').attr('disabled','disabled')
	 		return true
	 	}
	 	
	 	// control number
	 	var controlNumber = 753217532;
	 	
	 	var cifAsInt;
	 	if (Number(cifOnlyNumbers)){
	 		cifAsInt = cifOnlyNumbers;
	 	}
	 		
	 	
	 	// extract control digit
	 	var controlDigit = parseInt(cifAsInt % 10);
	 	cifAsInt= parseInt( cifAsInt / 10);
	 	
	 	// execute digit operations
	 	var totalDigitSum = 0;
	 	while(cifAsInt > 0){
	 		totalDigitSum += parseInt((cifAsInt % 10) * (controlNumber % 10));
	 		cifAsInt = parseInt(cifAsInt / 10);
	 		controlNumber = parseInt(controlNumber / 10);
	 	}
	 	
	 	// sum resulted is multiplied with 10, and then is divided by 11
	 	var checkNumber = parseInt(totalDigitSum * 10 % 11);
	 	
	 	// If the check number is 10, then the check number is 0
	 	if(checkNumber == 10){
	 		checkNumber = 0;
	 	}
	 	
	 	// The CIF is valid when the check number resulted after the validation is
		// the same as the check number of the initial CIF
	 	if (controlDigit != checkNumber){
	 		elem.parent().append('<span class="errorMessage">Warning: invalid CUI/CIF</span>')
			$('.create').attr('disabled','disabled')
			return true
	 	}
	})
}

function isIBANValidated(iban){
	if(iban.length != 24) {
		
		return false;  //data is fail and data will not be saved
	}

	// 2. Move the four initial characters to the end of the string
	var moveROToEnd = iban.substring(4) + iban.substring(0, 4);
	
	// 3. Replace each letter in the string with two digits, thereby expanding the string, where A = 10, B = 11, ..., Z = 35
	var bigNumber = getStringAsNumber(moveROToEnd)
	
	if (bigNumber % BigInt(97) == 1)
		return true
	
	return false;
}
function getStringAsNumber(charsAndDigits) {
	
    var alphabeticOrderChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" ;
    var counter = 10;
    var digits = ''
    for(ind=0;ind<charsAndDigits.length;ind++){
    	var ch = charsAndDigits[ind]
    	if (alphabeticOrderChars.indexOf(ch) > -1){
    		digits = digits.concat(alphabeticOrderChars.indexOf(ch) + counter)
    	}
    	else{
    		digits +=ch	
    	}
    	
    }

    return BigInt(digits);
}
