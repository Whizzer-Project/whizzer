$(function(){
			var dropDownLists = {};
			var bussList = $("<select id='bussListEnrich' class='form-control'>");
			bussList.append('<option> </option>');			
			
			$.each(listsMetaData, function(listName, value){
					dropDownLists[listName] = $("<select class='form-control'>");
					$.each(value, function(key,field){
						dropDownLists[listName].append("<option value='"+field+"'>"+field+"</option>");
					});
				bussList.append("<option>"+listName+"</option>");
			});
			
			$("#bussListDivEnrich").empty()
			$("#bussListDivEnrich").append(bussList);
			if($$enrichData){
				$('#nameEnrich').val($$enrichData.name);
				$('#bussListEnrich').val($$enrichData.bussList);
				$('#txListFieldEnrich').val($$enrichData.txField);
				$('#enrichTxFieldTip').attr('title', $$enrichData.txField)
				$('label[data-xsd2html2xml-xpath="' + $$enrichData.txField + '"]').siblings("[name='test']").attr('checked', true);
				$('#patternEnrich').val($$enrichData.pattern)
				if ($$enrichData.conditions && !$$enrichData.conditions.hasOwnProperty('rules')){
					$$enrichData.conditions = null
				}
				if ($$enrichData.txtemplatesconfigoption){
					$('#predefinedEnrich').attr('checked', true)
				}
				else{
					$$enrichData.txtemplatesconfigoption = {}
				}
				if($$enrichData.mandatory === true){
					$('#mandatoryEnrichRule').prop('checked', true);
				}
				if ($$enrichData.bussList != null){
					setSelect($$enrichData.bussList);
				}					
			}
			
			$$options.forEach(function(value, item){
				if ($$enrichData && $$enrichData.txtemplatesconfigoption && value.id == $$enrichData.txtemplatesconfigoption.id){
					$('#optionidEnrich').append(new Option(value.name, item, true, true))
				}
				else{
					$('#optionidEnrich').append(new Option(value.name, item))
				}
			})
			
			$('#predefinedEnrich').click(function(){
				predefinedCheckers()
			})
			
			$('#optionidEnrich').click(function(){
				if ($('#optionidEnrich').val()){
					$('#patternEnrich').val($$options[$('#optionidEnrich').val()].pattern)
					if (!$$enrichData.txtemplatesconfigoption){
						$$enrichData.txtemplatesconfigoption = {}
					}
					$$enrichData.txtemplatesconfigoption.id = $$options[$('#optionidEnrich').val()].id
				}
			})
			
			predefinedCheckers()
			
			function predefinedCheckers(){
				if ($('#predefinedEnrich').is(':checked')){
					$('#bussListRow').hide()
					$('#bussListFieldRow').hide()
					$('#divConditionEnrich').hide()
					$('#optionRow').show()
					$('#patternRow').show()
					if ($('#optionidEnrich').val())
						$$enrichData.txtemplatesconfigoption.id = $$options[$('#optionidEnrich').val()].id
				}else{
					$('#bussListRow').show()
					$('#divConditionEnrich').show()
					$('#bussListFieldRow').show()
					$('#optionRow').hide()
					$('#patternRow').hide()
					$$enrichData.txtemplatesconfigoption = null
				}
			}

			function setSelect(fromEntity){
				$('#bussListFieldDivEnrich').empty();
				$('#bussListFieldDivEnrich').append($(dropDownLists[fromEntity]).clone().prop('id', 'busListFieldEnrich'));
				$('#busListFieldEnrich').val($$enrichData.bussListField);
				
			}
			
			bussList.change(function(){
				setSelect($(this).val());
				updateBuilderFilters($(this).val());
			})
			
			$('#SaveEnrich').html($$button)

			listsMetaData.xpath=["field"];
			var myFilters = queryBuilderFilter(listsMetaData, $$listEnrich, 'name')
			
			if(myFilters.length!=0){
				var $builder = $('#builderEnrich').queryBuilder({
						filters: myFilters,
						operators: ['equal', 'not_equal', 'contains', 'not_contains', 'is_not_null', 'is_null'],
						rules:($$enrichData.conditions),
						templates: {
				                rule: '\<div id="{{= it.rule_id }}" class="rule-container"> \
									    <div class="rule-header"> \
									      <div class="btn-group pull-right rule-actions"> \
									        <button type="button" class="btn btn-xs btn-danger" data-delete="rule"> \
									          <i class="{{= it.icons.remove_rule }}"></i> {{= it.translate("delete_rule") }} \
									        </button> \
									      </div> \
									    </div> \
									    {{? it.settings.display_errors }} \
									      <div class="error-container"><i class="{{= it.icons.error }}"></i></div> \
									    {{?}} \
									    <div class="rule-filter-container" ></div> \
				                		<input type="text" style="display:none;" onClick="addRuleXpath(this)" data-type="xpathCondition">\
								    	<div class="rule-operator-container"></div> \
									    <div class="rule-value-container"></div> \
									  </div>'
						 } 
				});
				var selectedFilter;
				$("input").find(".form-control").click(function(){
					$(".form-control").css("border","");
					$(this).css("border","solid green 5px");
					selectedFilter = $(this);
				})
				$("label, section").click(function(){
					 event.stopPropagation();
					console.log($(this).parent().attr("data-xsd2html2xml-xpath"));
					elem = getElementByAttr($(this).parent(),"data-xsd2html2xml-xpath")
					$(selectedFilter).val(elem.attr("data-xsd2html2xml-xpath"))
				})

				$builder.on("afterAddRule.queryBuilder", (event, rule) => {
			        console.log("on after add role");
				})
//				$builder.on('click', function(event){
//					addClickEvent(event, this)
//				})

				hideLoader();
				$builder.on('afterUpdateRuleFilter.queryBuilder', function(e, rule) {
					$("#"+rule.id).find("[data-type='xpathCondition']").css("display",(rule.filter!=undefined && rule.filter.field=="xpath-field")?"":"none")
				});
			}						
			
			$("#txListFieldEnrich").click(function(event){
				addXpathFromXsd($(this))
			}).dblclick(function(){
				if (confirm("Do you want clear the input?")){
					$(this).val('')
					$('#enrichTxFieldTip').attr('title','')
				}
			})
			
//			function addClickEvent(event, el){
//				$(el).find('input').on('click', function(){
//					$ruleName = $(this)
//				})		
//			}
			
			function updateBuilderFilters(bussListName){
				$.each(listsMetaData, function(listName, value) {
					if(listName === bussListName){
						let queryBuilderMap = {};
						queryBuilderMap[listName] = value;
						queryBuilderMap.xpath = ["field"];
						var myFiltersUpdated = queryBuilderFilter(queryBuilderMap, $$listEnrich, 'name');
						// change filters and delete orphan rules
						$('#builderEnrich').queryBuilder('setFilters', true, myFiltersUpdated);
						return false;
					}
				});	
			}
			
			$("#CancelEnrich").click(function(){
				getPageByUrl("enrich/view", '#enrich_rules')
			})
			
			$('#SaveEnrich').click(function(){
				var execute = true
				const enrichRules = {}
				enrichRules.name = $('#nameEnrich').val().trim();
				$('#nameEnrich').parent().find('span').remove()
				if (0 == enrichRules.name.length){
					execute = false
					$('#nameEnrich').parent().append('<span class="errorMessage"> Name is required </span>')
				}
				enrichRules.bussList = $('#bussListEnrich option:selected').val()
				enrichRules.bussListField = $('#busListFieldEnrich option:selected').val()								
				enrichRules.txField = $('#txListFieldEnrich').val() 
				enrichRules.conditions = $builder.queryBuilder('getRules')
				enrichRules.txType = template.messagetype
				enrichRules.id = $('#enrichId').val()
				enrichRules.mandatory = $('#mandatoryEnrichRule').is(':checked')
				enrichRules.txtemplatesconfig = {}
				enrichRules.txtemplatesconfig.id = $('#template').val()
				enrichRules.pattern = $('#patternEnrich').val()
				$('#builderEnrich').children().find('span').remove()
				$('#builderEnrich').find('span').remove()
				if ($$enrichData.txtemplatesconfigoption && $$enrichData.txtemplatesconfigoption.id){
					enrichRules.conditions = null
					enrichRules.txtemplatesconfigoption = {}
					enrichRules.txtemplatesconfigoption.id = $$enrichData.txtemplatesconfigoption.id
				}
				else if (!enrichRules.conditions){
					execute = false
					$('#builderEnrich').append('<span class="errorMessage"> Condition is required </span>')
				}
				if (execute){
					$.ajax({
						method: 'post',
						dataType: 'json',
						url: $('#enrichForm').prop('action'),
						data: {enrich: JSON.stringify(enrichRules)},
						beforeSend: function(xhr){
							xhr.setRequestHeader($('#_csrf_header').attr("content"),
							$('#_csrf').attr("content"));
							showLoader()
						},
						success: function(data){
							getPageByUrl("enrich/view", '#enrich_rules')
						},
						error: function(err){
							console.log('eroare' + err)
						},
						complete: function(){
							hideLoader();
						}
					})
				}
			})
			
			
})

function addRuleXpath(selected){
	addXpathFromXsd($(selected))
	
	if($(selected).parent().attr("id")!=undefined){
		var cond = $('#builderEnrich').queryBuilder('getRules');
		var condition = {}
		if (cond && cond.rules){
			condition = cond.rules[$(selected).parent().attr("id").split("rule_")[1]];
		
			if(condition.data == undefined){
				condition.data = {};			
			}
			condition.data.filterXpath = $(selected).val();
			
			$('#builderEnrich').queryBuilder("setRules", cond)
		}
		
	}
}

function addXpathFromXsd(input){
	
	var nodexPath = []
	nodexPath = (getxPath($("input[name='test']:checked").parent(), "data-xsd2html2xml-xpath"))
	
	var nodesSelect = new Set()
	nodexPath.forEach(function(value){
		nodesSelect.add(value)	
	})
	if (nodesSelect.size==0){
		alert("Please select the radiobox on the left!")
		return false;
	}
	
	var nodeSelectedChild
	
	if (nodesSelect.size > 1){
		$('#xPathOptions').show()
		$('#addxPath').empty()
		var uniqueAttribute =[]
		nodesSelect.forEach(function(value){
			if (!uniqueAttribute.includes(value.attr("data-xsd2html2xml-xpath"))){
				uniqueAttribute.push(value.attr("data-xsd2html2xml-xpath"))
				var inputxPath = $('<div><input type="radio" value="' + value.attr("data-xsd2html2xml-xpath") + '" id="radioX" name="radioX">' + value.attr("data-xsd2html2xml-xpath") + '</input></div>')
				inputxPath.click(function(){
					var nodeSelect = value
					$('#xPathOptions').hide()
					input.val(nodeSelect.attr("data-xsd2html2xml-xpath"))
				})
				$('#addxPath').append(inputxPath);
			}
		})
	}
	else{
		input.val(nodesSelect.values().next().value.attr("data-xsd2html2xml-xpath"))
	}
	
//	elem = getElementByAttr($("input[name='test']:checked").parent(),"data-xsd2html2xml-xpath")
//	if (undefined === elem){
//		alert("Please select the radiobox on the left!")
//		return false;
//	}
//	
//	input.val(elem.attr("data-xsd2html2xml-xpath"))
}

function getxPath(nodeElement, attr = "data-xsd2html2xml-xpath"){
	var nodexPath = []
	nodeElement.children().each(function(){
		if (!$(this).attr('hidden')){
			nodexPath = nodexPath.concat(getxPath($(this)))
		}
		if ($(this).attr(attr)) {
			nodexPath.push($(this))
		}
	})
	return nodexPath
}

function getElementByAttr(elem,  attr){
	if (elem.attr(attr)!==undefined){
		return $(elem)
	}else{
		var ret_elem = undefined 
		elem.children().each(function(){
			if ($(this).attr(attr)!==undefined){
				ret_elem = $(this)
				return false
			}
		})
		return ret_elem
	}
}		
