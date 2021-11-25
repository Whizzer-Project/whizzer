function addThousandsSeparator(num) {
	num = "" + num;
	var n = num.replace(/,/g, '');
    var x = n.split('.');
    var x1 = x[0];
    var x2 = x.length > 1 ? '.' + x[1] : '';
    var rgx = /(\d+)(\d{3})/;
    while (rgx.test(x1)) {
        x1 = x1.replace(rgx, '$1' + ',' + '$2');
    }
    return x1 + x2.substring(0, 3);
}

$(function() {	
	$('#myModal').modal({show:false})
	$('#myModal').on('show.bs.modal', function() {
		var myModal = $(this);
        clearTimeout(myModal.data('hideInterval'));
        myModal.data('hideInterval', setTimeout(function(){
            myModal.modal('hide');
        }, 3000));
	});
	
	$('#internalEntName').on('change', function(){
		window.location.href = "inputIndicators?fp=header.menuWhizzerInputIndicators&year=" + $('#bsplYear option:selected').text() + "&entity="+$('#internalEntName option:selected').text();
//		$("#searchLoadHistory").click();
	});
	
	$('#bsplYear').on('change', function(){
		var selectedOptionEntity = $('#internalEntName option:selected').text();
		if(selectedOptionEntity !== ""){
			window.location.href = "inputIndicators?fp=header.menuWhizzerInputIndicators&year=" + $('#bsplYear option:selected').text() + "&entity="+$('#internalEntName option:selected').text();
//			$("#searchLoadHistory").click();
		}
	});
	
	$("#bsplYear").multiselect('dataprovider', getLastNYear(10,new Date().getFullYear())); 
	if(year !== null){
		$("#bsplYear").val(year); 
		$("#bsplYear").val([year]).multiselect("refresh");
	}		
	
	var entityId;
	let loadType = "";
	$("#plFileLoad, #bsFileLoad").change(function(event) {
         let files = event.target.files;
		 let formData = new FormData(); 
		 formData.append("file", files[0]);
		 loadType = $(this).attr("id");
		 $.ajax({
				data:formData,
				method : 'POST',
				url : "./upload/"+loadType,
				cache: false,
			    contentType: false,
			    processData: false,
			    cache:false, 
			    beforeSend: function(xhr){
					var csrfToken = $('#_csrf').attr("content");
					var csrfHeader = $('#_csrf_header').attr("content");
		            xhr.setRequestHeader(csrfHeader, csrfToken);
			    },
				success: function(data, textStatus, xhr){
					if(typeof data === 'object'){
						$("#inputIndicatorsMyModalBody").html("File Upload Successful. Press save to persist info in database");
						$('#myModal').modal("show");
						jQuery.each(data, function(name, value) {
							if(name.startsWith("omfp"))
								$("#"+(loadType.startsWith("bs")?"bs":"pl")+name).val(value)
						});
					}else{
						document.getElementById('inputIndicatorsModalSave').style.visibility = 'hidden';
						$("#inputIndicatorsModalBody").html("<span style='color:red'>"+data+"</span>");
						$('#inputIndicatorsModalTitle').text("Upload error");
						$('#inputIndicatorsModal').modal("show");
						$('#inputIndicatorsModal').on('hidden.bs.modal', function () {
							$('#inputIndicatorsModalTitle').text("");
							location.reload();
						});
					}
					$("#plFileLoad, #bsFileLoad").val();
				},
				error: function (xhr, textStatus, error){
				}
		 });
	})
	
	var balanceSheetMandatoryOptionalFields = {};
	var profitLossMandatoryOptionalFields = {};
	var balanceSheetLabels = {};
	var profitLossLabels = {};
	var balanceSheetMandatoryPaired = {}
	var profitLossMandatoryPaired = {}
	
	$('#searchLoadHistory').click(function(e){

		if($('#internalEntName').val() == ''){
			createErrorDialog('Select entity');
		}
		
		var year = $('#bsplYear').val()
		var entityName = $('#internalEntName').val()
		

		$.ajax({
			method : 'GET',
			url : 'loadHistory?year=' + year + "&entityName=" + entityName,
			dataType : 'json',
			success : function(data) {
				
				balanceSheetMandatoryOptionalFields = data["bsMandOptionFields"];
				profitLossMandatoryOptionalFields = data["plMandOptionFields"];
				
				var balanceSheet = data["balanceSheetLoadHistory"];
				var profitLoss = data["profitLossLoadHistory"];
				balanceSheetLabels = data["bsLabels"];
				profitLossLabels = data["plLabels"];
				balanceSheetMandatoryPaired = data["bsSameMandatoryType"];
				profitLossMandatoryPaired = data["plSameMandatoryType"];				
				
				var bsDiv = $('#balanceSheetDiv');
				var plDiv = $('#profitLossDiv');
				bsDiv.empty();
				plDiv.empty();

				var bsForm = $('<form>').attr({
					'name': 'bsForm',
					'id': 'bsForm',
					'method': 'post'
				});
				
				var plForm = $('<form>').attr({
					'name': 'plForm',
					'id': 'plForm',
					'method': 'post'
				});				
				
				
				var bsh3 = '<h3>Balance sheet</h3>';
				//var plh3 = '<h3 = '<h3>Profit & loss</h3>';
				
				bsForm = getInputIndicatorForm(balanceSheet, "bs", bsForm, balanceSheetLabels);		
				plForm = getInputIndicatorForm(profitLoss, "pl", plForm, profitLossLabels);
				
				bsDiv.append(bsh3);
				bsDiv.append(bsForm);				
				bsDiv = appendButonsToDiv(bsDiv, "bs");				

				//plDiv.append(plh3);
				plDiv.append(plForm);				
				plDiv = appendButonsToDiv(plDiv, "pl");
				
				$("#plLoad").click(function(){
					firstClick("#plEditSave", "Excel");
					$("#plFileLoad").click();
				})
				$("#bsLoad").click(function(){
					firstClick("#bsEditSave", "Excel");
					$("#bsFileLoad").click();
				})
				$("#bsForecast").find("tbody").empty();
				let table = $("#bsForecast tbody");
				const fields = [2,3,12,15,50,60,16,21,30,40,57,61,62]
				
				const th = ["", "Indicatori bilant (Balance Sheet Indicators)", "Valoare previzionata (Forecasted Amount)","Valoare realizata (Realized Amount)","Difference between realised Amount and Forecasted Amount"]
				$("#bsComparisionTable thead tr").append(th.map(item=>$("<th>").html(item)))
				
		        //balanceSheetLabels["omfp"+item]
				//console.log(profitLoss);
				table.append(fields.map(item=>{
					const tdVaules = ["",profitLossLabels["omfp"+item],profitLoss["omfp"+item]]//profitLossLabels["omfp"+item], balanceSheetForecast["omfp"+item]]
					return $("<tr>").append(tdVaules.map((val,index)=> $("<td>").css("display",index==0?"none":"")
							.html(index!=2?val:"<input type='text' onkeyup='this.value = addThousandsSeparator(this.value);' style='text-align:right' readonly id='lpomfp"+item+"'  name='lpomfp"+item+"' value='"+(val!=null?addThousandsSeparator(val):"")+"'/>")))
				}))
				$("#bsForecast tfoot").find("td").append('<input type="hidden" name="lpId" id="lpId" value="' + profitLoss.id + '"/>');
				const groupRows =["Incasari (Collections)","Plati (Payments)","Profit"];
				groupRows.map((item,index)=>{ 
					table.find("tr:eq("+(index*6)+")").find("td:eq(0)").css("display","").attr("rowspan",6).html(item)
				})
				$('#collapseOne').collapse() 
				
				$("#bsForecast tfoot").css("display",$("#plomfp1").val()!=""?"none":"");
				$("#plContainer").css("display","")
			}
		});		
		
		
//		$("#lpdEditSave").click(function(){
//			firstClick('#lpdEditSave', "UI");	
//		})
		
		$(document).one('click','#lpdEditSave',function(){
			firstClick('#lpdEditSave', "UI");	
		});
		
		
		function getInputIndicatorForm(bsOrPl, startingChars, form, labels){			
				for(var field in bsOrPl){
					if(field!="level"){
						let tr = $('<tr>');
						
						if(field !== "id"){						
							var label = field in labels? labels[field]:field;
							var value = bsOrPl[field];
							if(field === "insertdate"){
								value = new Date(value).toISOString().replace(/.\d+Z$/g, "").replace("T", " ");								
							}	
							if(field === "entity" || field === "year"){
								var optionSelected;
								if(field === "entity"){
									var e = document.getElementById("internalEntName");
									optionSelected = e.value;
								}else{
									var e = document.getElementById("bsplYear");
									optionSelected = e.value;
								}
								tr.append('<input type="hidden" name="' + startingChars + field + '" id="' + startingChars + field + '" value="' + optionSelected + '"/>');
							}else{
								tr.append( '<td><label for="' + startingChars + field + '">' + label + ':</label>' + '</td><td>'+
										'<input type="text" onkeyup="this.value = addThousandsSeparator(this.value);" style="text-align:right" id="' + startingChars + field + '" name="' +startingChars + field + '" value="' + (value!=null?addThousandsSeparator(value):"") + '" readonly></td>');
								if(["username","source","username","insertdate"].includes(field)){
									tr.css("display","none")
								}
							}							
						}else{
							tr.append('<td><input type="hidden" name="' + startingChars + 'Id" id="' + startingChars + 'Id" value="' + bsOrPl["id"] + '"/></td>');
						}	
						form.append(tr)
					}
					
				}
			return form;
		}
		
		function appendButonsToDiv(div, startingChars){
			div.append('<button id="' + startingChars + 'EditSave" class="submitButton glossy-button glossy-button--purple" style="margin: 1%;">Edit</button>');
			div.append('<button id="' + startingChars + 'Load" class="submitButton glossy-button glossy-button--purple" style="margin: 1%;">Load</button>');
			
			$(document).one('click','#' + startingChars + 'EditSave',function(){
				firstClick('#' + startingChars + 'EditSave', "UI");	
			});
			
			return div;
		}

		function firstClick(buttonID, loadOrEdit) {
			$(buttonID).text("Save");
			var startingChars = buttonID.slice(1, 3);
			$("input[id^='"+ startingChars +"']").each(function (i, el) {				
				el.removeAttribute("readonly");						 
		     });
			
			$(document).one('click', buttonID, function(){
				if(loadOrEdit === "UI"){
					secondClickConfirm(buttonID, loadOrEdit);
				}else{
					secondClickSave(buttonID, loadOrEdit);
				}
			});
		}
		
		// if edit is clicked without loading data from excell - show second confirmation
		function secondClickConfirm(buttonID, loadOrEdit) {	
				$("#inputIndicatorsModalBody").html('Annual new values overwrite annual old values - second confirmation needed. Are you sure?');
				document.getElementById('inputIndicatorsModalSave').style.visibility = 'visible';
				$('#inputIndicatorsModal').modal('show');
				
				$(document).one('click', '#inputIndicatorsModalSave', function() {
				    $('#inputIndicatorsModal').modal('hide');
				    setTimeout(function() { $(buttonID).trigger("click"); }, 1000);
				    
					$(document).one('click', buttonID, function(){
						secondClickSave(buttonID, loadOrEdit);
					});					
				});
		}

		function secondClickSave(buttonID, loadOrEdit) {
			// buttonID starts with either bs or pl, same as form		
			var formElements = document.getElementById(buttonID.slice(1, 3) + "Form").elements;
			var isValid = isFormValid(formElements);
			var entId = $('#' + buttonID.slice(1, 3) + 'Id').val();
			console.log(buttonID)
			if(isValid){							
				// pass through ajax form data for update in DB
				var bsOrPlObject = {};

				for(var i = 0; i < formElements.length; i++){
					var element = formElements[i];
					bsOrPlObject[element.name.slice(2)] = element.value.replace(/,/g, '');	
				}
				bsOrPlObject["entity"] = $("#internalEntName").val();
				bsOrPlObject["year"] = $("#bsplYear").val();
				var url;
				if(entId === "null"){
					url = './updateInsertInputIndicators?inputIndicator=' + buttonID.slice(1, 3) + '&uiOrExcell=' + loadOrEdit;
				}else{
					url = './updateInsertInputIndicators?id=' + entId + '&inputIndicator=' + buttonID.slice(1, 3) + '&uiOrExcell=' + loadOrEdit;
				}
				console.log(bsOrPlObject);
				if(buttonID=="#plEditSave")
					bsOrPlObject["level"] = 'extended';
				else
					bsOrPlObject["level"] = 'limited';	
				$.ajax({
					method : 'POST',
					url : url,
					dataType: 'json',
					data : bsOrPlObject,
					beforeSend: function(xhr){
						var csrfToken = $('#_csrf').attr("content");
						var csrfHeader = $('#_csrf_header').attr("content");
			            xhr.setRequestHeader(csrfHeader, csrfToken);
//			            $('#loader').css("display", "block");
				    },
					success : function(data) {
						let year = $('#bsplYear').val()
						let entityName = $('#internalEntName').val()
						
						if(entId === "null"){
							$("#inputIndicatorsModalBody").html("Inserted");						
						}else{
							$("#inputIndicatorsModalBody").html("Updated");
						}
						$('#inputIndicatorsModal').modal('show');
						$('#inputIndicatorsModal').on('hidden.bs.modal', function () {
						    location.reload();
						});
					}
				});	
				
				var startingChars = buttonID.slice(1, 3);
				$("input[id^='"+ startingChars +"']").each(function (i, el) {				
					el.setAttribute("readonly", true);	 
			     });
				
				$(buttonID).text("Edit");
				$(document).one('click', buttonID, function(){
					firstClick(buttonID, loadOrEdit);							
				});
			}else{
				$(document).one('click', buttonID, function(){
					secondClickSave(buttonID, loadOrEdit);
				});
			}									
		}	
		
		function isFormValid(formElements){
			for(var i = 0; i < formElements.length; i++) {
				var element = formElements[i];
				var isValid = true;
				
				if(element.name.indexOf("omfp") !== -1){
					isValid = validateInputWithDecimals(element.value.replace(/,/g, ''), element.name);
				}	
				
				document.getElementById('inputIndicatorsModalSave').style.visibility = 'hidden';
				if(!isValid){	
					document.getElementById(element.name).focus();
					$('#inputIndicatorsModal').modal('show');
					return false;
				}						
			}
			return true;
		}				
		
		function validateInputWithDecimals(inputValue, fieldNameWithIndicatorType){
			// here field name comes with bs or pl in front of the real name			
			var fieldName = fieldNameWithIndicatorType.slice(2);
			var startingChars = fieldNameWithIndicatorType.substring(0, 2);
			var fieldLabel;
			if(startingChars === "bs"){
				fieldLabel = balanceSheetLabels[fieldName];
			}
			if(startingChars === "pl" || startingChars === "lp"){
				fieldLabel = profitLossLabels[fieldName];
			}

			// nr should have 17 digits with decimals, 18 because of the decimal separator, also check if there are no decimals -> should have max 15 digits
			if(inputValue.length > (inputValue.indexOf('.') > -1 ?18:15)){
				$("#inputIndicatorsModalBody").html('Too many digits (maximum 15 digits + "." + 2 decimals) in field: ' + fieldLabel);
				return false;
			}			
			console.log(startingChars, fieldName);
			// check only mandatory type = M
			if(isFieldM(startingChars, fieldName)){
				if(inputValue.length == 0){
					$("#inputIndicatorsModalBody").html("<span style='color:red'>"+fieldLabel+" is required</span>");
					return false;
				}
				if(!isInputNumeric(inputValue)){
					$("#inputIndicatorsModalBody").html('Please enter only numbers with max 2 decimals and "." (period) as decimal separator in field: ' + fieldLabel);						
					return false;
				}
			}
			
			// check only mandatory type = C(NR)
			// example for bs sameMandatoryTypeList = {"C5":["omfp45","omfp46"],"C6":["omfp44","omfp43"]}
			if(startingChars != 'lp' && isFieldC(startingChars, fieldName)){
				var mandatoryType = startingChars === "bs"?balanceSheetMandatoryOptionalFields[fieldName]:profitLossMandatoryOptionalFields[fieldName];
				var sameMandatoryTypeList = startingChars === "bs"?balanceSheetMandatoryPaired[mandatoryType]:profitLossMandatoryPaired[mandatoryType];
								
				var pair;
				for (index = 0; index < sameMandatoryTypeList.length; index++) {
					if(sameMandatoryTypeList[index] !== fieldName){
						pair = sameMandatoryTypeList[index];
					}
				}
				console.log(startingChars + pair);
				var pairValue = document.getElementById(startingChars + pair).value.replace(/,/g, '');
				if(inputValue === ""){
					if(pairValue === ""){
						$("#inputIndicatorsModalBody").html('Only one field must have value: "' + fieldLabel + '" or field: "' + (startingChars === "bs"?balanceSheetLabels[pair]:profitLossLabels[pair]) + '"');
						return false;
					}else{
						if(!isInputNumeric(pairValue)){
							$("#inputIndicatorsModalBody").html('Please enter only numbers with max 2 decimals and "." (period) as decimal separator in field: ' + (startingChars === "bs"?balanceSheetLabels[pair]:profitLossLabels[pair]));
							return false;
						}
					}
				}else{
					if(pairValue !== ""){
						$("#inputIndicatorsModalBody").html('Only one field must have value: "' + fieldLabel + '" or field: "' + (startingChars === "bs"?balanceSheetLabels[pair]:profitLossLabels[pair]) + '"');
						return false;
					}
					if(!isInputNumeric(inputValue)){						
						$("#inputIndicatorsModalBody").html('Please enter only numbers with max 2 decimals and "." (period) as decimal separator in field: ' + fieldLabel);
						return false;
					}
				}
			}
						
		    return true;
		}
		
		function isFieldM(startingChars, fieldName){
			if(startingChars === "bs"){
				return balanceSheetMandatoryOptionalFields[fieldName] === "M"? true: false;
			}
			if(startingChars === "pl" || startingChars === "lp"){
				return profitLossMandatoryOptionalFields[fieldName] === "M"? true: false;
			}
			return false;
		}
		
		function isFieldC(startingChars, fieldName){
			if(startingChars === "bs" && balanceSheetMandatoryOptionalFields[fieldName]){
				return balanceSheetMandatoryOptionalFields[fieldName].indexOf('C') > -1? true: false;
			}
			if((startingChars === "pl"|| startingChars === "lp") && profitLossMandatoryOptionalFields[fieldName]){
				return profitLossMandatoryOptionalFields[fieldName].indexOf('C') > -1? true: false;
			}
			return false;
		}		
		
		function isInputNumeric(inputValue){
			return jQuery.isNumeric(inputValue);
		}
		
	})
	
	if(year!=undefined && entity!=undefined){
		$("#searchLoadHistory").click();
	}
})
