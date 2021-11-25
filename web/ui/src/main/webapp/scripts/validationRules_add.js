$(function(){
		  	
	  	var conditionOrAlgoDiv = $('.condition-or-algo');
	  	var validationType;
		
	    $("input[name$='inlineRadioOptions']").click(function() {
	    	conditionOrAlgoDiv.empty();
	  		if($(this).val().localeCompare("userDefined") == 0){	  			
	  			addUserDefined();
	  			validationType = "user Defined";
	  		}else if($(this).val().localeCompare("predefined") == 0){
	  			addPredefined();
	  			validationType = "predefined";
	  		}	    	
	    });
		
		function addPredefined(){
			conditionOrAlgoDiv.append("<select id='predefinedList' class='form-control'>");
			conditionOrAlgoDiv.append("<input type='text' style='display:none' class='form-control' id='additionalXpath'/>");
			var predefined = $('#predefinedList');
			predefined.append('<option> </option>');
			predefined.append("<option value='IBAN syntax' name='syntax'>IBAN syntax</option>");
//			predefined.append("<option value='CNP syntax' name='syntax'>CNP syntax</option>");
			predefined.append("<option value='CIF syntax' name='syntax'>CIF syntax</option>");
			if ($$project.toLowerCase().trim()!='adpharma'){
				predefined.append("<option value='roIban' name='syntax'>RO IBAN Bank matching</option>");
			}
			if($$validationsRules.name != null){
				var optionSelectValue = $$validationsRules.algorithm;
				$("#predefinedList option[value='" + optionSelectValue + "']").prop('selected', true);
				if(optionSelectValue=="roIban"){
					$("#additionalXpath").val($$validationsRules.conditions.additionalXpath).css("display","")
				}
			}
			
			predefined.change(function(){
				$("#additionalXpath").css("display",$(this).val()=="roIban"?"":"none")
				
			})
			
			
			var selectedFilter;
			$("#additionalXpath").click(function(){
				elem = getElementByAttr($("input[name='test']:checked").parent(),"data-xsd2html2xml-xpath")
				if (undefined === elem){
					alert("Please select the radiobox on the left!")
					return false;
				}
				$(this).val(elem.attr("data-xsd2html2xml-xpath"))
				//$('#validationsTxFieldTip').attr('title', $('#txListFieldValidations').val())
			})
			
	  	}
	  	
		var $builder = null;
		
		function addUserDefined(){
			conditionOrAlgoDiv.append("<div style='margin: 0 auto; padding: 10px 10px; overflow-y: auto;'>" +
									"<div id='builderValidationRule'></div></div>"
									);
	  								
			var myFilters = queryBuilderFilter(listsMetaData, $$validationsRulesData, 'name')
			
			if(myFilters.length!=0){
				$builder = $('#builderValidationRule').queryBuilder({
					filters: myFilters,
					operators: ['equal', 'not_equal', 'contains', 'not_contains',
						{ type: '<', nb_inputs: 1, multiple: false, apply_to: ['string'] },
						{ type: '>', nb_inputs: 1, multiple: false, apply_to: ['string'] }
						],
					rules:($$validationsRules.conditions),
				});
				var selectedFilter;
				$("input").find(".form-control").click(function(){
					$(".form-control").css("border","");
					$(this).css("border","solid green 5px");
					selectedFilter = $(this);
					
				})
				$("label, section").click(function(){
					event.stopPropagation();
					elem = getElementByAttr($(this).parent(),"data-xsd2html2xml-xpath")
					$(selectedFilter).val(elem.attr("data-xsd2html2xml-xpath"))
				})

				$builder.on('afterUpdateRuleOperator.queryBuilder', (parent, rule) => {
					var normalOptions = '<option value="-1">-</option>' + '<option value="xpath">xpath</option>' + '<option value="rule">rule</option>>' + '<option value="text">text</option>';
					var specialOptions = '<option value="-1">-</option>' + '<option value="xpath">xpath</option>';
					
					var selectedElem = rule.$el.find('.rule-operator-container').find('[name$=_operator]');
					var selectedText = selectedElem.find(":selected").text();					
					var selectedElemRuleValue = rule.$el.find('.rule-value-container').find('[name$=_1]');
					
					if(selectedText === "<" || selectedText === ">"){						
						selectedElemRuleValue.children().remove();
						selectedElemRuleValue.append(specialOptions);
					}else{
						selectedElemRuleValue.children().remove();
						selectedElemRuleValue.append(normalOptions);
					}					
				})				
				hideLoader()
			}
		}
		
		if ($$validationsRules.name != null){
			$('#validationName').val($$validationsRules.name);
			$('#txListFieldValidations').val($$validationsRules.txField);
			$('#validationsTxFieldTip').attr('title', $('#txListFieldValidations').val())
			$('label[data-xsd2html2xml-xpath="' + $$validationsRules.txField + '"]').siblings("[name='test']").attr('checked', true);
			if($$validationsRules.algorithm){
				$("#inlineRadio2").prop("", true);
				$("#inlineRadio2").click();
			}
			else{
				$("#inlineRadio1").prop("checked", true);
				$("#inlineRadio1").click();
			}
		}
		
		$("#txListFieldValidations").click(function(){
			elem = getElementByAttr($("input[name='test']:checked").parent(),"data-xsd2html2xml-xpath")
			if (undefined === elem){
				alert("Please select the radiobox on the left!")
				return false;
			}
			$('#txListFieldValidations').val(elem.attr("data-xsd2html2xml-xpath"))
			$('#validationsTxFieldTip').attr('title', $('#txListFieldValidations').val())
		})
		
		$('#txListFieldValidations').dblclick(function(){
			if (confirm("Do you want clear the input?")){
				$('#txListFieldValidations').val('')
				$('#validationsTxFieldTip').attr('title', $('#txListFieldValidations').val())
			}
		})
		
		$('#SaveValidation').html($$button);
		
		$('#SaveValidation').click(function(){
			saveRecord();
		})
		
		function getElementByAttr(elem,  attr){
			if (elem.attr(attr)!==undefined){
				return $(elem)
			}else{
				var elem_ret
				elem.children().each(function(){
					if ($(this).attr(attr)!==undefined){
						elem_ret = $(this)
					}
				})
				return elem_ret
			}
		}
		
		function saveRecord(){
			const validation = {};
			validation.name = $('#validationName').val();
			validation.txField = $('#txListFieldValidations').val() ;

			if($builder !== null && $builder !== undefined){
				validation.conditions = $builder.queryBuilder('getRules');
			}			
			validation.txType = $$template.messagetype;
			if($('#predefinedList') !== null){
				validation.algorithm = $('#predefinedList').val();
			}			
			validation.type = validationType;
			validation.id = $('#validationId').val();
			validation.configId = $('#template').val()
			
			if(validation.algorithm=="roIban"){
				validation.conditions = {additionalXpath:$("#additionalXpath").val()}
			}
			$.ajax({
				method: 'post',
				dataType: 'json',
				url: $('#validationForm').prop('action'),
				data: {validationsRules: JSON.stringify(validation)},
				beforeSend: function(xhr){
					xhr.setRequestHeader($('#_csrf_header').attr("content"),
					$('#_csrf').attr("content"));
				showLoader()
				},
				success: function(data){
					getPageByUrl("validations/view", '#validation_rules')
				},
				error: function(err){
					console.log('error posting validation')
				},
				complete: function(){
					hideLoader()
				}
			})
		}
					
		$("#CancelValidation").click(function(){
			getPageByUrl("validations/view", '#validation_rules')
		})
		

})
