
var $$suspend = false
	var $$det = undefined
	var idsAttributes = 0;
	changedDropDowns = {}
	
$(function() {
	
	if (!window.location.pathname.includes("admin")){
		changedDropDowns = {
				banks : $$dropDowns.bank,
				internalaccounts: $$dropDowns.internalaccount,
				externalaccounts: $$dropDowns.externalaccount,
				internalentities: $$dropDowns.internalentity,
				externalentities: $$dropDowns.externalentity,
				locationcodes: $$dropDowns.locationcode,
				budgetcodes: $$dropDowns.budgetcode,
				accountingcodes: $$dropDowns.accountingcode
				
		}
		if(changedDropDowns){
			messageTypes.forEach( message => {
				var filterGorup = groupFilters.filter(function (msg){
					return msg.messagetype === message.messageType;
			    });
				filterGorup.forEach(elem => {
					var div
					if(bussinesAreas[message.messageType]=="Payments"){
						div = createElementForFilters(elem, message)
						$("#tabs-"+message.messageType).find("[data-type='globalFilters']").append(div)
					}else if(bussinesAreas[message.messageType]=="Statements"){
						div = createElementForFilters(elem, message)
						$("#tabs-"+message.messageType).find("[data-type='globalFilters']").append(div)
						Array.from(document.querySelectorAll("#beneficiary")).forEach(el=>el.style.display='none')
					}
					else{
						$("#tabs-"+message.messageType).find("[data-type='globalFilters']").css("display","none")
					}
				})
				getActionButton(message.messageType)
			});
			registerValidations();
			registerDateTime();
			$('select[multiple]').multiselect({
				buttonWidth: '100%',
				maxHeight: 200,
				enableFiltering: true,
				enableCaseInsensitiveFiltering: true,//am adaugat pentru filtru de la report
				//includeSelectAllOption: true
			});
		}
	}
	
	function createElementForFilters(elem, message){
		let control;
		switch(elem.type){
			case "string":control = getInputFilter(elem, message);break;
			case "multiselect":control = getDropdownFilter(elem, message); break;
			case "date": control = getDateFilter(elem, message); break;
		}
		var label = document.createElement("label")
		label.classList.add("col-sm-3")
		label.classList.add("col-form-label")
		label.setAttribute("for", "attr" + (idsAttributes++))
		label.innerText = elem.label
		var div = document.createElement("div")
		div.classList.add("col-md-12")
		div.appendChild(label)
		var labelDiv = document.createElement("div")
		labelDiv.classList.add("col-md-3")
		labelDiv.appendChild(control[0])
		div.appendChild(labelDiv)
		return div
	}
	
	function getInputFilter(elem, message){
		var input = $('<input>')
			.attr('id', "attr"+idsAttributes)
			.attr('data-messageType', message.messageType)
			.attr('data-level', elem.level)
			.addClass('form-control')
		input.on('change', function (e) { 
			changeFilters(this,elem, message)
		});
		return input;
	}
	function getDateFilter(elem, message){
		var input = $('<input>')
					.attr('id', "attr"+idsAttributes)
					.attr('data-messageType', message.messageType)
					.attr('data-type', 'dateInterval')
					.attr('data-level', elem.level)
					.addClass('form-control')
		input.daterangepicker({
			"showDropdowns": true,
			autoUpdateInput: false,
			"locale": {
		        "format": $$dateFormat ,
		        "separator": " ",
		        "firstDay": 1,
		        "cancelLabel": 'Clear',
		    },
		}).on('apply.daterangepicker', function(ev, picker) {
		      $(this).val(picker.startDate.format('YYYY-MM-DD') + ' ' + picker.endDate.format('YYYY-MM-DD'));
		      changeFilters(this,elem, message)
		}).on('cancel.daterangepicker', function(ev, picker) {
		      $(this).val('');
			  changeFilters(this,elem, message)
		});
		return input;
	}
	function getDropdownFilter(elem, message){
		if(elem.datasource.indexOf("[")!=-1){
			var select = $('<select>')
					.attr('id', "attr"+idsAttributes)
					.attr('data-messageType', message.messageType)
					.attr('data-level', elem.level)
					.attr('data-type', 'multiselect')
					.css('width','100%')
					.attr("multiple", "multiple")
					.addClass('form-control');
			$.each(JSON.parse(elem.datasource), function(key, value) {
				select.append('<option value="' + value + '">' + value + '</option>');
			});
			select.change(function(){
				changeFilters(this,elem, message)
			});
			return select;
		}else{
			const lists = elem.datasource.split(".");
			if(lists[1]!=undefined){
				var select = $('<select>')
							.attr('id', "attr"+idsAttributes)
							.attr('data-messageType', message.messageType)
							.attr('data-level', elem.level)
							.attr('data-type', 'multiselect')
							.css('width','100%')
							.attr("multiple", "multiple")
							.addClass('form-control');
				var entity = lists[1]
				let dropDownList = changedDropDowns[entity]
				dropDownList.sort((a, b) => a[lists[2]].toLowerCase() > b[lists[2]].toLowerCase() ? 1 : -1);
				select.append(dropDownList.map(elem => { return "<option>"+elem[lists[2]]+"</option>"}))
				select.change(function(){
					changeFilters(this,elem, message)
				})
				return select;
			}
		}
	}
	function changeFilters(_this,elem, message){
		let messageType = $(_this).attr("data-messageType");
		groupsMap[messageType].forEach( item => {
			$("[data-groupKey='"+item.groupKey+"']").css("display","")
			$("#"+item.groupKey).css("display","")
			item.fields.forEach(function(value,key){
				let filterField = $(".tab-pane.active").find("[data-level='"+(key+1)+"']");
				const val = filterField.val();
				if (val.length == 0)
					return true
//				let isDate = filterField.attr("data-type")=='dateInterval';
				let hideTheGroup = false;
				switch (filterField.attr("data-type")){
				case 'dateInterval':
					let dates = val.split(" ");
					let filterDates = {startDate:moment(dates[0],"YYYY-MM-DD"), endDate:moment(dates[1],"YYYY-MM-DD"),groupDate:moment(item.fields[key],"YYMMDD")}
					hideTheGroup = (filterDates.groupDate<filterDates.startDate || filterDates.groupDate>filterDates.endDate)
					break
				case 'multiselect' :
					hideTheGroup = !val.includes(value)
					break
				default:
					hideTheGroup = (val!=undefined && val!="" && val!=item.fields[key])
					break
				}
				if(hideTheGroup){
					$("[data-groupKey='"+item.groupKey+"']").css("display","none");
					$("#"+item.groupKey).css("display","none")
				}
			})
		});
		$("#tabs-"+messageType).find(".batchCheckbox").prop("checked",false)
		$("#tabs-"+messageType).find("[data-type='selectAll']").prop("checked",false)
	}

	$( ".batchCheckbox" ).on( "click", checkBoxClicked);
	var allCheckBoxGroup = document.querySelector('.tab-pane.active').querySelectorAll(".batchCheckbox")
	
	function checkBoxClicked(){
		var checked = $(this).prop('checked');
		$(this).parent().find("a").click()
		$('#' + $(this).val()).find("input[type=checkbox]").each(function(){
			if($(this).prop('checked'))
				$(this).trigger("click");
			if(checked)
				$(this).trigger("click");
		});
		getCheckedValueOfCheckBox()
	}
	$('button.messageAction').click(function(e) {	
		buttonMessageAction(this)
	});
	
	function buttonMessageAction(_this){
		if($(_this).attr("data-hasDetails")==1){
			if ($$suspend){
				if ($$det){
					actionSubmit(_this, $$det)
				}else{
					createConfirmDialog("<span>Details:</span><textarea id='det'/>", function(){
						$$det = $("#det").val()
						actionSubmit(_this, $$det);
					});
				}
			}else{
				if (!getCheckedCheckBox()){
					alert($$messages["transaction.suspend.unselect.any"])
					return
				}
				createConfirmDialog("<span>Details:</span><textarea id='det'/>", function(){
					actionSubmit(_this, $("#det").val());
				});
			}
			
		}else{
			actionSubmit(_this, "");
		}
	}
	
	function getCheckedCheckBox(){
		var checked = false
		document.querySelectorAll(".tab-pane.active").forEach(t=>t.querySelectorAll("input.routeCheckbox:checked[type=checkbox]").forEach(ch=>checked=ch.checked))
		return checked;
	}
	
	function actionSubmit(_this, details){
		var item = {};

		item["queueName"] =queueName; 

		item["messageId"]= [];

		var currentTab = $(_this).closest('div.tab-pane');
		var currentGroup = $(_this).closest('table');

		item["action"] = $(_this).val();
		item["reason"] = currentGroup.find("div#"+ item["action"]+" option").filter(":selected").val();		
		item["messageType"] =  currentTab.find('input:hidden[name="messageType"]').val();
		
		if(item["action"]=="MoveTo")
			item["reason"] = currentGroup.find("[data-type='moveTo'] option").filter(":selected").val();	
		
		if ($(_this).val() !== 'Authorize' && item.reason === ''){
			return alert($$messages["transaction.suspend.unselect"]);
		}
		
		item["details"] = details;
			
		currentGroup.find("input.routeCheckbox:checked[type=checkbox]").each(function(){
			item["messageId"].push($(this).val());
		});

		if (item["messageId"].length >0) {

			$.ajax({
                type: "POST",
                url: "message-routing-jobs",
                data: {item: JSON.stringify(item)},  
                // dataType: "json",
                beforeSend: function(xhr){
                	showLoader()
    	            xhr.setRequestHeader($('#_csrf_header').attr("content"),
    	            					 $('#_csrf').attr("content"));
                },
                success: function (data) {
                	if(data=="Edit"){
                		setTimeout(function(){
                			document.location.href = "./Edit"
                			}, 
                		10000)
                		
                	}else{
                		setTimeout(function(){
                			hideLoader()
                			//$(".tab-pane.active").find(".fintpTable").DataTable().draw();
                				location.reload()
                		}, 5000)
                	}
                },
                error: function (xhr, textStatus, error){
                	console.log(xhr, textStatus, error)
                },
                complete: function(){
//                	hideLoader()
                	document.querySelectorAll('div.btn-group').forEach(elem=>elem.style.width="100%")
                }
            });
        }
	}

	$('button.groupAction').click(function(e) {	
//		var item = {};
//		item["queueName"] = queueName;		
//		item["groupKeys"]= [];
//		item["timeKeys"]=[];
//		item["fieldValues"]=[];
//
//		item["action"] = $(this).val();	
//
		var currentTab = $(this).closest('div.tab-pane');
//
//		item["messageType"] =  currentTab.find('input:hidden[name="messageType"]').val();
//		
//		item["messageId"] = []	
//
//		currentTab.find("input.batchCheckbox:checked[type=checkbox]").each(function(){
//			 var groupKey = $(this).val();
//			 item["groupKeys"].push(groupKey);
//			 item["timeKeys"].push(groups[groupKey].timekey);
//			 item["fieldValues"].push(groups[groupKey].values);
//			 item["messageId"].push(Array.from(document.querySelector("div[id='" + groupKey + "']").querySelectorAll(".routeCheckbox"))
//					 .filter(ch=>ch.checked).map(ch=>ch.value))
//		});
//		if (item["groupKeys"].length >0) {
//            $.ajax({
//                type: "POST",
//                url: "group-routing-jobs",
//                data: {item: JSON.stringify(item)},  
//               // dataType: "json",
//                beforeSend: function(xhr){
//                	showLoader()
//    	            xhr.setRequestHeader($('#_csrf_header').attr("content"),
//    	            					 $('#_csrf').attr("content"));
//                },
//                success: function (data) {
//                    console.log('group routing jobs');
//                	console.log(data);
//                	setTimeout(function(){
////            			hideLoader()
//            			//$(".tab-pane.active").find(".fintpTable").DataTable().draw();
//            				location.reload()
//            		}, 5000)
//                },
//                error: function (xhr, textStatus, error){
//                	console.log(xhr, textStatus, error)
//                },
//                complete: function(){
////                	hideLoader()
//                }
//            });
//        }
		checkByTransaction(this.value, currentTab)
		
	});
	
	function checkByTransaction(buttonValue, currentTab){
//		var batchElement = currentTab.find("input.batchCheckbox:not(:checked)[type=checkbox]")
		var batchElement = currentTab.find("input.batchCheckbox[type=checkbox]")
		Array.from(batchElement).forEach(elem=>{
//			if (!elem.checked){
				var id = elem.value
				var item = {}
				item["messageId"] = []
				Array.from(document.querySelector("div[id='" + id + "']").querySelectorAll(".routeCheckbox")).forEach(ch=>ch.checked?item["messageId"].push(ch.value):"")
				item["queueName"] = queueName

				item["action"] = buttonValue;//=="Batch"?"Route":buttonValue;
				item["reason"] = "";		
				item["messageType"] =  document.querySelector('.tab-pane.active').querySelector('input[name="messageType"]').value
				
				item["details"] = "";
				if (item["messageId"].length > 0){
					$.ajax({
		                type: "POST",
		                url: "message-routing-jobs",
		                data: {item: JSON.stringify(item)},  
		                // dataType: "json",
		                beforeSend: function(xhr){
		                	showLoader()
		    	            xhr.setRequestHeader($('#_csrf_header').attr("content"),
		    	            					 $('#_csrf').attr("content"));
		                },
		                success: function (data) {
		                	if(data=="Edit"){
		                		setTimeout(function(){
		                			document.location.href = "./Edit"
		                			}, 
		                		10000)
		                		
		                	}else{
		                		setTimeout(function(){
//		                			hideLoader()
		                			//$(".tab-pane.active").find(".fintpTable").DataTable().draw();
		                				location.reload()
		                		}, 5000)
		                	}
		                	document.querySelectorAll('div.btn-group').forEach(elem=>elem.style.width="100%")
		                },
		                error: function (xhr, textStatus, error){
		                	console.log(xhr, textStatus, error)
		                },
		                complete: function(){
//		                	hideLoader()
		                }
		            })
				}
//			}
		})
	}
	
	function groupSubmit(object) {		
		var item = {};
		item["queueName"] = queueName	
		item["groupKeys"]= [];
		item["timeKeys"]=[];
		item["fieldValues"]=[];

		item["action"] = object.action;	
		item["details"] = object.message
		var s = document.querySelector(".tab-pane.active").querySelector("select")
		item["reason"] = s.options[s.selectedIndex].text

//		var currentTab = $(this).closest('div.tab-pane');

		item["messageType"] =  object.parent.querySelector('input[name=messageType]').value;

//		currentTab.find("input.batchCheckbox:checked[type=checkbox]").each(function(){
			 var groupKey = object.elem.value;
			 item["groupKeys"].push(groupKey);
			 item["timeKeys"].push(groups[groupKey].timekey);
			 item["fieldValues"].push(groups[groupKey].values);
//		});

		if (item["groupKeys"].length >0) {
            $.ajax({
                type: "POST",
                url: "group-routing-jobs",
                data: {item: JSON.stringify(item)},  
               // dataType: "json",
                beforeSend: function(xhr){
                	showLoader()
    	            xhr.setRequestHeader($('#_csrf_header').attr("content"),
    	            					 $('#_csrf').attr("content"));
                },
                success: function (data) {
                    console.log('group routing jobs');
                	console.log(data);
                	showLoader()
                	setTimeout(function(){
            				location.reload()
            			}, 
            		5000)
                },
                error: function (xhr, textStatus, error){
                	console.log(xhr, textStatus, error)
                }
            });
        }
	}
	
	 
	$("[data-type='globalFilter']").change(function(){
		var searchValue = Array.from(this.nextElementSibling.querySelectorAll("li.active")).map(elem=>elem.querySelector("input").value).join("|")
		$(".tab-pane.active").find(".fintpTable").DataTable().columns(1).search( searchValue).draw();
		$("[data-id='selAmount']").html("0.00");
		$("[data-id='selCount']").html("0");
		$(this).parent().parent().find(".batchCheckbox").prop("checked",false)
		$(this).parent().parent().find("[data-type='selectAll']").prop("checked",false)
	})
	
	
	
	/*var selectAll = document.querySelector("[data-type='selectAll']")
	if(selectAll){
		selectAll.addEventListener("change", changeSelect)
		var allCheckBoxGroup = document.querySelectorAll(".batchCheckbox")
	}*/
	
	$("[data-type='selectAll']").change(function(){
		
		var selected = this.checked;
//		$(".tab-pane.active").find( ".batchCheckbox" ).click();
		
		allCheckBoxGroup.forEach(elem => {
			if (elem.parentElement.style.display != 'none'){
				if (!elem.checked && selected){
					elem.click()
				}
				if (!selected && elem.checked){
					elem.click()
				}
			}
		})
	})
	
	function getCheckedValueOfCheckBox(){
		var checkedItem = 0
		allCheckBoxGroup.forEach(elem => elem.checked?checkedItem++:"")
		if (checkedItem == allCheckBoxGroup.length){
			document.querySelector(".tab-pane.active").querySelector('#selectAll').checked = allCheckBoxGroup[0].checked
		}else{
			document.querySelector(".tab-pane.active").querySelector('#selectAll').checked = false
		}
	}
	
	function getActionButton(messageType){
		var tabActiveElement = $("#tabs-"+messageType)//document.querySelector(".tab-pane.active")
		if (tabActiveElement){
			//var messageType = tabActiveElement.querySelector("input[name=messageType]").value
			var action = selActions[messageType]
			var div = document.createElement("div")
			div.setAttribute("id","groupButton")
			action.forEach(btn=>{
				switch (btn.label){
				case "Reject":
				case "Investigate":
				case "Edit":
					break;
				default:
						var divContainer = createDivElement()
						divContainer.appendChild(createButton(btn))
						
						if (btn.userActionCodeEntity.length > 0){
							divContainer.appendChild(createSelect("userActionCodeEntity", formattingByKeyValue(btn.userActionCodeEntity,"code,label")))
						}
						if(btn.queueMoveMaps){
							divContainer.appendChild(createSelect("data-type,moveTo",formattingByKeyValue(btn.queueMoveMaps,"key, value")))
						}
						if (0 != Object.entries(queuesDestination).length){
							var select = document.createElement("select")
							select.setAttribute("data-type",'moveTo')
							select.style.marginLeft = "15px"
							select.style.width = "auto"
							select.classList.add("form-control");
							select.addEventListener("change", function(){
								 Array.from(document.querySelectorAll("[data-type=moveTo]")).forEach(element=>element.selectedIndex = this.selectedIndex)
							})
							for ([key, value] of Object.entries(queuesDestination)){
									select.appendChild(new Option(key, value))
								}
							divContainer.appendChild(select)
						}
						div.appendChild(divContainer)
				}
				
			})
			
			if (tabActiveElement.has("#activateAllGroup").length>0){
				tabActiveElement.find("#activateAllGroup").prepend(div)
				$("#batchBtn").prependTo("#groupButton")
			}
		}
	}
	
	function createDivElement(){
		var div = document.createElement("div")
		div.setAttribute("id","groupButton1")
		div.style.display = "inline-block"
		div.style["margin-left"] = "20px";
		div.style["vertical-align"] = "top";
		return div
	}
	
	function formattingByKeyValue(values, keyValue){
		var key,val
		[key, val] = keyValue.split(",")
		var rezult = []
		values.forEach(v=>{
			var obj = {}
			obj.key = v[key]
			obj.value = v[val]
			rezult.push(obj)
		})
		return rezult
	}
	
	function createButton(btn){
		const button = document.createElement('button')
		
		button.setAttribute("value", btn.name)
		button.setAttribute("data-hasdetails", btn.detailsInput)
		button.setAttribute("class", "allButton submitButton glossy-button glossy-button--purple addButton")
		button.innerText = btn.label
		if(btn.name=="MoveTo" || btn.name=="Suspend"){
//			button.style["margin-bottom"] = 0;
		}
		button.addEventListener("click", clickAllSelectedButton)
		return button
	}
	
	function clickAllSelectedButton(){
		var object = {}
		object.action = this.value
		var selectors = this.parentNode.querySelector("select")
		if (selectors && selectors.name){
			Array.from(document.querySelectorAll("[name="+selectors.name+"]")).forEach(elem=>elem.selectedIndex=selectors.selectedIndex)
		}
		if (selectors && selectors.getAttribute("data-type")){
			var attr = selectors.getAttribute("data-type") 
			Array.from(document.querySelectorAll("[data-type="+attr+"]")).forEach(elem=>elem.selectedIndex=selectors.selectedIndex)
		}
		object.parent = document.querySelector(".tab-pane.active")
		var detailInput = this.getAttribute("data-hasdetails")
		if (1 == detailInput){
			$$suspend = true
			createConfirmDialog("<span>Details:</span><textarea id='det'/>", function(){
				$$det = $("#det").val()
				if (!$$det){
					$$det = " "
				}
				object.message = $$det
				clickGroupOrButton(object)
				$$suspend = false
				$$det = undefined
			});
		}
		else{
			clickGroupOrButton(object)
		}
	}
	
	function clickGroupOrButton(object){
		Array.from(object.parent.querySelectorAll("input.batchCheckbox")).forEach(function(elem){
			//to select the group if needed
//			if (true == elem.checked){
			//execute selected group
//				groupSubmit({message:object.message, action: object.action, parent:object.parent, elem:elem})
//			}
//			else{
			//execute selected transaction
				var id = elem.value;
				document.querySelector("div[id='" + id + "']").querySelector("button[value="+object.action+"]").click();
//			}
		})
	}
	
	function createSelect(name, options){
		var select = document.createElement("select")
		select.style.marginLeft = "15px"
		var nameAttribute = name.split(",")
		if (nameAttribute.length>=2){
			select.setAttribute(nameAttribute[0], nameAttribute[1])
		}else{
			select.setAttribute("name", name)
		}
		select.addEventListener("change", function(){
			var key = this.selectedIndex
			Array.from(document.querySelectorAll("[name="+this.name+"]")).forEach(element=>element.selectedIndex =key )
		})
		select.classList.add("form-control");
		select.style.width = "auto"
		select.appendChild(createOption({key:"", value:""}))
		options.forEach(value=>select.appendChild(createOption(value)))
		return select
	}
	
	function createOption(keyValue){
		var option = document.createElement("option")
		option.text =  keyValue.value
		option.value = keyValue.key
		return option
	}
});
