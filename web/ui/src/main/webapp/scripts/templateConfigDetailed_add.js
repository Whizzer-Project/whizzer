$(function(){
	var dropDownLists = {};
	var nodexPath = []
	var bussList = $("<select id='busslist' class='form-control'>");
	bussList.append('<option> </option>');
	$('#save').html(button)
	var $$optionId = undefined
	
	$('#optionid').change(function(){
		$$optionId = $('optionid :selected').val()
		if (6 == $$optionId || 7 == $$optionId){
			$('#fieldvalue').val('')
		}
	})
	
	function addXPath(el){
		let nodeSelected = $("input[name='test']:checked").parent().children().first().attr("data-xsd2html2xml-xpath");
		if(nodeSelected == undefined){
			alert("Please select the radiobox on the left!")
			return false;
		}else{
			$('[name='+el+']').val(nodeSelected)
		}
	}
	
	$.each(listsMetaData, function(listName, value){
		dropDownLists[listName] = $("<select class='form-control'>");
		$.each(value, function(key,field){
			dropDownLists[listName].append("<option value='"+field+"'>"+field+"</option>");
		});
		bussList.append("<option>"+listName+"</option>");
	});
	
	if(detailed.fieldxpath){
		$('label[data-xsd2html2xml-xpath="' + detailed.fieldxpath + '"]').siblings("[name='test']").attr('checked', true);
	}
	
	$("#bussListDiv").empty()
	$("#bussListDiv").append(bussList);
	if (detailed.busslist!=null){
		$(busslist).val(detailed.busslist)
		setSelect(detailed.busslist)
	}
	
	function setSelect(fromEntity){
		$('#bussListFieldDiv').empty();
		$('#bussListFieldDiv').append($(dropDownLists[fromEntity]).clone().prop('id', 'busslistfield'));
		$('#busslistfield').val(detailed.busslistfield)
	}
	
	bussList.change(function(){
		setSelect($(this).val())
		updateBuilderFilters($(this).val());
	})
	
	if (detailed.visible == false){
		$('#visible').removeAttr("checked")
	}
	
	$("#CancelRecord").click(function(){
		getPageByUrl("view",'#config_detailed')
	})
	
	$('#templatesConfigDetailedType0').click(function(){
		typeUserDefined();
	})
	
	$('#templatesConfigDetailedType1').click(function(){
		typePreDefined();
	})
	
	var style = 'inline-block'
	if (detailed.hasOwnProperty('conditions')){
		if (detailed.conditions != null && detailed.conditions.hasOwnProperty('rules')){
		}else{
			detailed.conditions = null;
		}
	}
	
	var myFilters = queryBuilderFilter(listsMetaData, $$fields, 'fieldlabel')
	
	if(myFilters.length!=0){
		var $builder = $('#templatesConfigDetailedBuilder').queryBuilder({
			filters: myFilters,
			operators: ['equal', 'not_equal', 'contains', 'not_contains'],
			rules:(detailed.conditions),
		})
		var selectedFilter;
		$("input").find(".form-control").click(function(){
			$(".form-control").css("border","");
			$(this).css("border","solid green 5px");
			selectedFilter = $(this);
		})
		$("label, section").click(function(){
			event.stopPropagation();
			$(selectedFilter).val($(this).parent().parent().attr("data-xsd2html2xml-xpath"))
		})
		
		$builder.click();
		hideLoader()
	}
	
	$('.rule-dropdown-container').css('display', style)
	
	$('#fieldxpath').click(function(){
		nodexPath = []
		nodexPath.push.apply(getxPath($("input[name='test']:checked").parent(), "data-xsd2html2xml-xpath"))
		
		nodesSelect = new Set()
		nodexPath.forEach(function(value){
			nodesSelect.add(value)	
		})
		if (nodesSelect.size==0){
			alert("Please select the radiobox on the left!")
			return false;
		}
		if (nodesSelect.size > 1){
			$('#xPathOptions').show()
			$('#addxPath').empty()
			uniqueAttribute =[]
			nodesSelect.forEach(function(value){
				if (!uniqueAttribute.includes(value.attr("data-xsd2html2xml-xpath"))){
					uniqueAttribute.push(value.attr("data-xsd2html2xml-xpath"))
					var input = $('<div><input type="radio" value="' + value.attr("data-xsd2html2xml-xpath") + '" id="radioX" name="radioX">' + value.attr("data-xsd2html2xml-xpath") + '</input></div>')
					input.click(function(){
						nodeSelect = value
						$('#xPathOptions').hide()
						setFieldxPath(nodeSelect)
					})
					$('#addxPath').append(input);
				}
			})
		}
		else{
			setFieldxPath(nodesSelect.values().next().value)
		}
	})
	
	function setFieldxPath(nodeSelect){
		$('#fieldxpath').val(nodeSelect.attr("data-xsd2html2xml-xpath"))
		$('#createTxFieldTip').attr('title',nodeSelect.attr("data-xsd2html2xml-xpath"))
		var element = nodeSelect.children().first()
		$('#fieldtype').val(element.attr('type'))
		if ($$optionId && (6 == $$optionId || 7 == $$optionId)){
			$('#fieldvalue').val('')
		}else{
			if (element.is('input')){
				$('#fieldvalue').val(element.attr('value'))
			}
			else{
				$('#fieldvalue').val(element.find('option:selected').text())
			}
				
		}
		$('#pattern').val(element.attr('pattern'))
		$('#mandatory').val(element.attr('required')?true:false)
		$('#editable').val(false)
		$('#fieldvisibility').val(1)
	}
	
	function getxPath(nodeElement, attr = "data-xsd2html2xml-xpath"){
		nodeElement.children().each(function(){
			if (!$(this).attr('hidden')){
				nodexPath.push.apply(getxPath($(this)))
			}
			if ($(this).attr(attr)) {
				nodexPath.push($(this))
			}
		})
		return nodexPath
	}
	
	$('#fieldxpath').dblclick(function(){
		if (confirm("Do you want clear the input?")){
			$('#fieldxpath').val('')
			$('#createTxFieldTip').attr('title', '')
			$('#fieldtype').val('')
			$('#fieldvalue').val('')
			$('#pattern').val('')
			$('#mandatory').val('')
			$('#editable').val('')
			$('#fieldvisibility').val(null)
		}
	})
	
	$("input[type=radio][name='conditionValueType']").click(function(){
		$("#ruleContainer").css("display",($(this).val()=="xpath")?"none":"")
	})
	
	function selectxPath(conditionValueType){
		//let conditionValueType = $("input[type=radio][name='conditionValueType']:checked").val();
		if(conditionValueType=="rule"){
			templateCondition.val($("#rules").val());
			templateCondition.change()
		}else{
			if (templateCondition!=null){
				let nodeSelected = getElementByAttr($("input[name='test']:checked").parent(),"data-xsd2html2xml-xpath");
				if(nodeSelected==undefined){
					alert("Please select the radiobox on the left!")
					return false;
				}
				templateCondition.val(nodeSelected.attr("data-xsd2html2xml-xpath"))
				templateCondition.change()
			//	addXpathToRule.disabled = true
			}else{
				alert($("input[name='test']:checked").parent().attr("data-xsd2html2xml-xpath"));
				
			}
		}
	}
	
	$('#fillInput').click(function(){
		selectxPath($("input[type=radio][name='conditionValueType']:checked").val())
		$('#conditionValue').modal('hide');
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
	
	function updateBuilderFilters(bussListName){
		$.each(listsMetaData, function(listName, value) {
			if(listName === bussListName){
				let queryBuilderMap = {};
				queryBuilderMap[listName] = value;
				var myFiltersUpdated = queryBuilderFilter(queryBuilderMap, $$fields, 'fieldlabel');
				// change filters and delete orphan rules
				$('#templatesConfigDetailedBuilder').queryBuilder('setFilters', true, myFiltersUpdated);
				return false;
			}
		});	
	}
	
	$("#save").click(function(e){
		var saveDetailed = {}
		$.each(detailed, function(key, value){
			saveDetailed[key] = $('#'+key).val()
		})
		saveDetailed.txtemplatesconfigoption = {}
		saveDetailed.txtemplatesconfig = {}
		saveDetailed.txtemplatesconfig.id = $('#template').val()
		saveDetailed['visible'] = $('#visible').is(':checked')
		if ($('input[name=templateType]:checked').val()==0){
			saveDetailed.conditions = $builder.queryBuilder('getRules')
			saveDetailed.editable = false
			delete saveDetailed.txtemplatesconfigoption
		}else{
			saveDetailed.txtemplatesconfigoption.id = $('#optionid').val()
			delete saveDetailed.conditions
			delete saveDetailed.busslist
			delete saveDetailed.busslistfield
			saveDetailed.editable = true
		}
		
		$.ajax({
			method: 'POST',
			dataType: 'json',
			url: $('#templateConfigForm').prop('action'),
			data: {detailed: JSON.stringify(saveDetailed) },
			beforeSend: function(xhr){
				xhr.setRequestHeader($('#_csrf_header').attr("content"),
				$('#_csrf').attr("content"));
				showLoader()
			},
			success: function(data){
				getPageByUrl("view",'#config_detailed')
				//window.location = "/fintp_ui/templates-config";
			},
			complete: function(){
				hideLoader()
			}
		});
	});
	
})