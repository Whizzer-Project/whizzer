$(function(){
			
			var dropDownLists = {};
			var bussList = $("<select id='bussListEditRule' class='form-control'>");
			bussList.append('<option> </option>');	
			$('#SaveEditRecord').html($$button)

			$.each(listsMetaData, function(listName, value){
					dropDownLists[listName] = $("<select class='form-control'>");
					$.each(value, function(key,field){
						dropDownLists[listName].append("<option value='"+field+"'>"+field+"</option>");
					});
				bussList.append("<option>"+listName+"</option>");
			});
			
			$("#bussListDivEditRule").empty();
			$("#bussListDivEditRule").append(bussList);
			$('#bussListFieldDivEditRule').empty();
			
			$('#editRulesName').val($$editRulesData.name);
			if($$editRulesData.mandatory === true){
				$('#mandatoryEditRule').prop('checked', true);
			}
						
			if ($$editRulesData.bussList != null){
				$('#txListFieldEditRule').val($$editRulesData.txField);	
				$('#editTxFieldTip').attr('title', $$editRulesData.txField)
				$('label[data-xsd2html2xml-xpath="' + $$editRulesData.txField + '"]').siblings("[name='test']").attr('checked', true);
				$('#bussListEditRule').val($$editRulesData.bussList);
				updateBussListField($$editRulesData.bussList);
			} 
			
			var $builder;
			filterQueryBuilderList(listsMetaData);			
			
			bussList.change(function(){
				updateBussListField($(this).val());
				updateBuilderFilters($(this).val());
			})
			
			function updateBussListField(fromEntity){									
				$('#bussListFieldDivEditRule').empty();
				$('#bussListFieldDivEditRule').append($(dropDownLists[fromEntity]).clone().prop('id', 'busListFieldProp'));
				$('#busListFieldProp').val($$editRulesData.bussListField);	
			}
			
			function updateBuilderFilters(bussListName){
				$.each(listsMetaData, function(listName, value) {
					if(listName === bussListName){
						let queryBuilderMap = {};
						queryBuilderMap[listName] = value;
						var myFiltersUpdated = queryBuilderFilter(queryBuilderMap, $$listEditRules, 'name');
						// change filters and delete orphan rules
						$('#builderEditRules').queryBuilder('setFilters', true, myFiltersUpdated);
						return false;
					}
				});	
			}

			function filterQueryBuilderList(queryBuilderMap){
				if(queryBuilderMap){
					var myFilters = queryBuilderFilter(queryBuilderMap, $$listEditRules, 'name');
					if(myFilters.length!=0){
						$builder = $('#builderEditRules').queryBuilder({
							filters: myFilters,
							operators: ['equal', 'not_equal', 'contains', 'not_contains'],
							rules:jQuery.isEmptyObject($$editRulesData.conditions)? "":($$editRulesData.conditions),
						});
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
						hideLoader();
					}					
				}
			}
			
			$("#txListFieldEditRule").click(function(){
				
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
							var input = $('<div><input type="radio" value="' + value.attr("data-xsd2html2xml-xpath") + '" id="radioX" name="radioX">' + value.attr("data-xsd2html2xml-xpath") + '</input></div>')
							input.click(function(){
								var nodeSelect = value
								$('#xPathOptions').hide()
								$('#txListFieldEditRule').val(nodeSelect.attr("data-xsd2html2xml-xpath"))
								
								setPattern(getElementByAttr(nodeSelect.children(),"pattern"))
							})
							$('#addxPath').append(input);
						}
					})
				}
				else{
					var next = nodesSelect.values().next().value
					$('#txListFieldEditRule').val(next.attr("data-xsd2html2xml-xpath"))
					setPattern( getElementByAttr(next.children(),"pattern"))
				}
				$('#editTxFieldTip').attr('title', $('#txListFieldEditRule').val())				
			})
			
			function setPattern(element){
				if(undefined !== element){
					$('#patternEditRule').val(element.attr("pattern"));
				}
			}
			
			$('#txListFieldEditRule').dblclick(function(){
				if (confirm("Do you want clear the input?")){
					$('#txListFieldEditRule').val("");
					$('#editTxFieldTip').attr('title','')
					$('#patternEditRule').val('');
				}
			})
			
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
				if ($(elem).children().length == 0){
					if (elem.attr(attr)!==undefined){
						return $(elem)
					}
					return $(elem)
				}
				else{
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
			
			$("#CancelEditRecord").click(function(){
				getPageByUrl("edit-rules/view", '#edit_rules')
			})
			
			$('#SaveEditRecord').click(function(){
				const editRules = {}
				editRules.name = $('#editRulesName').val()
				editRules.mandatory = $('#mandatoryEditRule').is(':checked')
				editRules.bussList = $('#bussListEditRule option:selected').val()
				editRules.bussListField = $('#busListFieldProp option:selected').val()
				editRules.txField = $('#txListFieldEditRule').val() 
				editRules.pattern = $('#patternEditRule').val()
				editRules.conditions = $builder.queryBuilder('getRules')
				editRules.txType = $$template.messagetype
				editRules.id = $('#editRulesId').val()
				$.ajax({
					method: 'post',
					dataType: 'json',
					url: $('#editRulesForm').prop('action'),
					data: {editRules: JSON.stringify(editRules)},
					beforeSend: function(xhr){
						xhr.setRequestHeader($('#_csrf_header').attr("content"),
						$('#_csrf').attr("content"));
						showLoader()
					},
					success: function(data){
						getPageByUrl("edit-rules/view", '#edit_rules')
					},
					error: function(err){
						console.log('eroare')
					},
					complete: function(){
						hideLoader()
					}
				})
			})
		})
