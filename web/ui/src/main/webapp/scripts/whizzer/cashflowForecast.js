$(function(){
	
	const collection = "Incasare (Collection)";
	const payment = "Plata (Payment)";
	const withDocument = "cu document de plata (with payment document)";
	const withoutDocument = "fara document de plata (without payment document)";
	const informationFromPayment = "informatii din documentul de plata (information from payment document)"
		
	const typeOfRevenue = {"collectionDocument":["Venituri din exploatare (Operating revenues)", "Venituri financiare (Financial revenues)"],
						   "collectionNoDocument":["Venituri financiare (Financial revenues)"],
						   "paymentDocument":["Cheltuieli de exploatare (Operating expenses)"],
						   "paymentNoDocument":["Cheltuieli de personal (Personnel expenses)",
											   "Cheltuieli cu impozite si taxe (Tax expenses)",
											   "Cheltuieli financiare (Financial expenses)"]
	};
	
	$('#issueDate').daterangepicker({
		singleDatePicker: true,
	    showDropdowns: true,
	    minYear: 1901,
	    locale: {
	        format: 'YYYY-MM-DD'
	    }
    });
	
	$("#issueDate,#paymentDueDate").change(function(){
		$('#maturityDate').val(moment(moment($('#issueDate').val(), "YYYY-MM-DD").add($("#paymentDueDate").val(), 'days')).format('YYYY-MM-DD'));
	})
	
	$("#opType,#opSubType").off().change(function(){
		changeRevenue();
	})
	
	$("#opSubType").off().change(function(){
		changeTypeInformation();
	})
	
	changeTypeInformation();

	changeRevenue();
	
	function changeRevenue(){
		$('#typeRevenueExpense').empty();
		$.each(fillRevenue($("#opType").val(), $("#opSubType").val()), function(i, p) {
		    $('#typeRevenueExpense').append($('<option></option>').val(p).html(p));
		});
	}
	
	function changeTypeInformation(){
		if($("#opSubType").val()==withDocument && $("#opSubType").length==1)
			$('#typeInformation').append($('<option></option>').val(informationFromPayment).html(informationFromPayment));
		else
			$("#typeInformation option[value='"+informationFromPayment+"']").remove();
	}
	
	function fillRevenue(opType, opSubType){
		switch(opType){
			case collection:
				switch(opSubType){
					case withDocument: return typeOfRevenue.collectionDocument; break;
					case withoutDocument: return typeOfRevenue.collectionNoDocument; break;
				}break;
			case payment:
				switch(opSubType){
					case withDocument: return typeOfRevenue.paymentDocument; break;
					case withoutDocument: return typeOfRevenue.paymentNoDocument; break;
				}break;
		}
	}
	
	
})