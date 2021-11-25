var businessArea = new URL(window.location.href).searchParams.get("businessArea");
var columns = [];
var checkeds = [];
var $$table = $('<table>').addClass("fintpTable display").css("width", "100%");
function getParams(){
	return JSON.parse($('#params > input[name="params"]').val())
}

function saveParams(params){
	$('#params > input[name="params"]').val(JSON.stringify(params))
}

$(function() {
	$("#tableHeaders").hide();
	$("#transactionType").hide();
	$('#showTableHere').append($$table);
	
	getData();
	
	$('#ToPDF').click(function(){
		getExport("toPDF")
	});
	
	
	
	function getExport(typeToExport){
		var params = getParams()
		Object.getOwnPropertyNames(params).filter(key=>key.includes("sort")).map(key=>delete params[key])
		var order = $('.fintpTable').DataTable().order()
		var column = order[0][0] - 1
		var sort = order[0][1]
		var columns = params.columns
		params['sort_' + columns[column]] = []
		params['sort_' + columns[column]].push(sort)
		saveParams(params);
		$('#export').attr('action', typeToExport).submit();
	}
	
	$('#ToExcel').click(function(){
//			var params = JSON.parse($('#params > input[name="params"]').val())
//			params.order =  $('.datatable').DataTable().order()
//			$('#params > input[name="params"]').val(JSON.stringify(params));
		getExport("toExcel")
//		$('#export').attr('action', 'toExcel').submit();
	});
	
	$('#ToShowHide').click(function(){
		completModal(columns, [0]);
	});
	$('.panel-group').on('shown.bs.collapse',function(){
		$(this).each(function(){
			var div = $(this).find(".in");
			$.each(div, function(i, val){
				if (i > 0){
					var id = val.id.replace("filter-", "");
					var span = $('#'+id).children().find('span');
					$(span[0]).removeClass('down');
					$(span[0]).addClass('up');
				}
				
			});
			if ($(this).hasClass('in')){
				alert('ura');
			}
		})
	});
	$('.panel-group').on('hide.bs.collapse',function(){
		$(this).each(function(){
			var div = $(this).find(".in");
			$.each(div, function(i, val){
				if (i > 0){
					var id = val.id.replace("filter-", "");
					var span = $('#'+id).children().find('span');
					$(span[0]).removeClass('up');
					$(span[0]).addClass('down');
				}
				
			});
			/* if ($(this).hasClass('in')){
				alert('ura');
			} */
		})
	});
		

	
	var type = getType()
	/*
	 * $("#tableHeaders").show(); $("#closeHeadersButton").show();
	 * $("#transactionType").hide();
	 */
	$("input[type=radio]").click(function () {
		type = getType()
		
		if(document.URL.indexOf("?") > - 1){
			var query = location.search.substring(1);
			var params = query.split("&");
			var containsType = false;
			for (var i = 0; i < params.length; i++){
				var parameterName = (params[i].split("="))[0];
				if (parameterName == "type"){
					containsType = true;
				}
			}
			if(containsType){
				var text =  window.location.href;
				// replace value between 'type=' and '&' with new value
				var newUrl =  text.replace(/(type=).*?(&)/,'$1' + type + '$2');
				// treat the case when type is last parameter
				if(newUrl == window.location.href){
					text+= "&";
					newUrl =  text.replace(/(type=).*?(&)/,'$1' + type + '$2');
					newUrl = newUrl.substring(0, newUrl.length -1);
				}	
				window.location.href = newUrl;
			}else{
				window.location.href = document.URL + "&type=" + type;
			}
			
		}else{
			window.location.href = "?type=" + type;
		}
	});
		
	$('#rst').click(function(e){
		e.preventDefault()
		$('form').find('[type=checkbox]').each(function(){
			if ($(this).prop('checked')){
				$(this).click()
			}
		})
		$('form').find('.multiselect-clear-filter').each(function(){
			($(this).click())
		})
		$('form')[0].reset()
	})
	
	// ?????
	if (businessArea != "Statements") {
		$("#transactionType").show();
		var messageTypes = $('#0');

		$.ajax({
			data : {businessArea : businessArea},
			method : 'GET',
			url : '../message-types',
			dataType : 'json',
			success : function(data) {
				messageTypes.children('option:not(:first)').remove();

				$.each(data, function(key, value) {
					messageTypes.append('<option value="' + value.friendlyName
							+ '">' + value.friendlyName + '</option>');
				});
			}
		});
	}
	
	$.ajax({
		data : {businessArea : businessArea},
		method : 'GET',
		url : '../message-criteria',
		dataType : 'json',
		beforeSend: function(){
			showLoader()
		},
		success : function(data) {
			$.each(data,function(key, value) {
				var masterDiv;
				var label = value.masterlabel.replace(
						" ", "-").toLowerCase();
				if ($('#' + label).length) {
					// div exist
					masterDiv = $('#' + label);
				} else {
					// div is not exist
					masterDiv = $('#masterlabel')
							.clone().prop('id', label)
							.removeAttr('style');
					masterDiv.children('div :first')
							.children()
							.children(':first')
							.prop('href','#filter-' + label)
							.html('<span style ="position: absolute;margin-left: 400px;" class="arrow down"></span>'
											+ value.masterlabel);
					$('.panel-group').append(masterDiv);
				}
				masterDiv.children('div :last').prop('id', 'filter-' + label)
						.children().append(renderFilter(value));
				// i++;
			});

			registerDateTime();
			$('form input[id="filter_insertdate_idate"]').prop(
					'autocomplete', 'off');
			var val_param = $('input[name=params]').val()
			if (val_param){
				var params = JSON.parse(val_param);
				$('#filter_insertdate_idate').val(
					params['filter_insertdate_idate'][0]);
			}
			registerValidations();
			registerUIControls();
		},
		complete: function(){
			hideLoader()
		}
	});

	$.ajax({
		data : {businessArea : businessArea},
		method : 'GET',
		url : '../message-results',
		dataType : 'json',
		beforeSend: function(){
			showLoader()
		},
		success : function(data) {
			var table = $('.results-row > table');

			var firstRow = table.find('tr:first');
			var secondRow = table.find('tr:nth-child(2)');

			firstRow.children().remove();
			secondRow.children().remove();

			$.each(data, function(key, value) {
				var id = "column_" + value.field;
				firstRow.append($('<td></td>')
						.append($('<label></label>')
						.attr('for', id)
						.text(value.label)));
				secondRow.append($('<td></td>')
						.append($('<input type="checkbox"></input>')
						.attr('id', id)
						.attr('name', 'columns')
						.addClass('reportsHeader')
						.val(value.field)
						.prop('checked', true)));
			});
		},
		complete: function(){
			hideLoader()
		}
	});
	
// $.ajax({
// data : {
// 'report' : businessArea
// },
// method : 'GET',
// url : '../message-filter',
// dataType : 'json',
// success : function(data) {
// $("#filtername").empty().append('<option></option>');
// $.each(data, function(item, value) {
// $('#filtername').append('<option value='+value+'>'+value+'</option>');
// });
// },
//
// complete : {
//
// }
// });
	


	$('#filtername').change(function() {
		$.ajax({
			data : {
				'report' : businessArea,
				'templateName' : $('#filtername option:selected').text()
			},
			method : 'GET',
			url : '../message-filter',
			dataType : 'json',
			beforeSend: function(){
				showLoader()
			},
			success : function(data) {

				$('#rst').click()

				$.each(data, function(item, value) {
					var elem = {}
					elem.elem = $('.' + value.criterionId)
					elem.tag = elem.elem.prop('tagName')
					elem.type = elem.elem.attr('type');
					if (elem.tag.toLowerCase() =='input' && elem.type == 'text') {
						element = $('.' + value.criterionId) 
						if (elem.elem.length > 1){
							var values = value.value.trim().split(',', elem.elem.length)
							values.forEach(function(val, item){
								$(elem.elem.get(item)).val(val)
							})
						}else{
							elem.elem.val(value.value.trim())
						}
					} else if(elem.tag.toLowerCase() == 'select'){
						if (value.value.trim().includes(':')){
							var val = value.value.split(":")[1].split(',');
							$.each(val, function(key, select){
						        $('.' + value.criterionId)
						            .next()
						            .children()
						            .find('input[value="' + select.trim() + '"]')
	                                .click();
						    })
						}else{
						elem.elem.val(value.value.trim())
						}
					}
				});
			},
			complete : function(){
				hideLoader();	
			}
		});
	})

	$("#viewHeadersButton").click(function(e) {
		$("#toggle").toggle({
			direction : "down"
		});
		$(this).hide();
	});

	$("#closeHeadersButton").click(function(e) {
		$("#toggle").toggle({
			direction : "down"
		});
		$("#viewHeadersButton").show();
	});
	
	$('#removeFilter').click(function(){
		var filterName = $('#filtername option:selected').text()
		if (filterName.trim().length == 0){
			alert('Select filter name')
		}else{
			$.ajax({
				data:{
					report : businessArea,
					type : type,
					templateName: filterName
				},
				method: 'DELETE',
				url: '../message-filter',
				beforeSend: function(xhr){
					xhr.setRequestHeader($('#_csrf_header').attr("content"), $('#_csrf').attr("content"));
					showLoader()
				},
				success: function(data){
					$('#filtername option:selected').hide()
					$('#filtername option:selected').text('')
					console.log(data)
				},
				error: function(err){
					console.log(err)
				},
				complete: function(){
					hideLoader()
				}
			})
		}
	})

	$("#saveFilter").click(function() {
		var filterName = prompt("Filter Name", $('#filtername :selected').text());
		if (filterName != null) {
			var map = [];
			$("#messagesForm").each(function() {
				// map1 = $('form').serializeArray();
				var elements = $(this).find(':input');
				$.each(elements, function(item, element) {
					if (element.id != '' && !isNaN(element.id) && element.value !='') {
						var obj = {}
						for(item = 0; item<map.length;item++){
							var oldObj= map[item]
							if (oldObj.criterionId === element.id){
								obj = oldObj
								map.splice(item, 1)
								break
							}
						}
						obj.criterionId = element.id;
						obj.messagetypebusinessarea = businessArea;
						obj.templateName = filterName;
						obj.userid = 1;
						if ($(element).prop('multiple')) {
							obj.value = 'm:' + $(element).next().children('button').prop('title');
						} else {
							if (obj.hasOwnProperty('value')){
								obj.value += ',' + element.value 
							}else{
								obj.value =(element.value);
							}
						}
						map.push(obj);
					}
				})
			});
			
			var filterNamesAlreadyInOptions = [];
			$("#filtername option").each(function(){
				filterNamesAlreadyInOptions.push($(this).val());
			});
			
			$.ajax({
				data : {
					filter : JSON.stringify(map),
					report : businessArea,
					type : type,
					templateName: filterName
				},
				method : 'POST',
				url : '../message-filter',
				beforeSend : function(xhr) {
					xhr.setRequestHeader($('#_csrf_header').attr("content"), $('#_csrf')
							.attr("content"));
					showLoader()
				},
				success : function(data) {
					var setFilter = new Set(filterNamesAlreadyInOptions)
					setFilter.add(filterName)
					$('#filtername').empty()
					setFilter.forEach(function(value){
						$('#filtername').append('<option>' + value + '</option>');
					})
				},
				error: function(err){
					console.log(err)
				},
				complete : function() {
					hideLoader()
				}
			});
		} else {
			alert("filter not saved");
		}
	});

	$("#messagesForm").submit(function(e) {
		e.preventDefault();
		if ($('form').attr('action').includes('general/to', 0)) {
			exit();
		}
		var formParams = {};
		var paramJson = getParams()

		var tempParam = $('form').serializeArray();
		
		var multiselectElements = [];
		$("[multiple='multiple']").each(function() {
			var selectName = $(this).attr("name");	
			multiselectElements.push(selectName);
		});

		// clear old values in paramJson[value.name] for multiselect elements ->
		// if is first choosen in filter for example : currency : EUR, MDL, RON
		// -> when that filter is changed with only RON -> the old values eur
		// and mdl remains
		for (var i = 0; i < multiselectElements.length; i++) {
			paramJson[multiselectElements[i]] = [];
		}
		
		$.each(tempParam, function(key, value) {
//			if (value.value!=""){
				if (paramJson.hasOwnProperty(value.name)){
					
					if(jQuery.inArray(value.name, multiselectElements) === -1) {
					    // is not multiselect
						paramJson[value.name] = []
						paramJson[value.name].push( value.value)
					}else if(jQuery.inArray(value.value, paramJson[value.name]) === -1) {
						// add value in paramJson[value.name] only if is not
						// already there, otherwise it will have multiple times
						// same value
						paramJson[value.name].push(value.value);
					}							
				}else{
					paramJson[value.name] = []
					paramJson[value.name].push( value.value)
				}
//			}
			if (value.name in formParams) {
				formParams[value.name].push(value.value);
			} else {
				formParams[value.name] = [];
				formParams[value.name].push(value.value);
			}
		});				
		
		delete paramJson.params;
		saveParams(paramJson)
		
		$.ajax({
			data : {
				businessArea : businessArea
			},
			method : 'GET',
			url : '../message-criteria',
			dataType : 'json',
			beforeSend: function(){
				showLoader()
			},
			success : function(data) {
				var searchCriteria = $('#dynamicSearch');
				var fields = {};

				// set report search criteria
				for ( var k in formParams) {
					var prop = "";
					if (k.startsWith("filter_"))
						prop = k.replace("filter_", "");
					if (prop != "") {
						prop = prop.split("_")[0];

						if (!fields.hasOwnProperty(prop))
							fields[prop] = [];
						if (formParams[k][0] != "")
							fields[prop].push(formParams[k]);
					}
				}
				searchCriteria.html('');
//				var table = $('.datatable').DataTable();
				var columns = $$table.settings().init().columns;

				$.each(data, function(key, value) {
					if (fields.hasOwnProperty(value.field)
							&& fields[value.field] != "") {
						searchCriteria.append(' ' + value.label
								+ ': <b>' + fields[value.field]
								+ '</b>');
					}
				});
				delete formParams.params;
				formParams['businessArea'] = [ businessArea ];
				formParams['type'] = [ type ];
				formParams['columns'] = [];
				$.each(columns, function(key, value) {
					if (key > 0) {
						formParams['columns'].push(value.name);
					}
				});
				
				initDatatable(columns, formParams, businessArea);
			},
			error: function(err){
				console.log(err)
			},
			complete: function(){
				hideLoader('message-criteria')
			}
		});
		closeNav();
	});
});

function getData(){
	$.ajax({
		data : {
			businessArea : businessArea
		},
		method : 'GET',
		url : '../message-results',
		dataType : 'json',
		beforeSend: function(){
			showLoader()
		},
		success : function(data) {
			columns.push({title: 'No', orderable: false, data: function(){
				return '';
			}})
			$.each(data, function(key, value){
				// if (selectedColumns.includes(value.field))
					columns.push({
						'title' : value.label,
		          		'data' : value.field,
		          		'name': {filterName: value.sortField},
		          		'className': (value.field == 'amount')? 'dt-right':'',
		          		"render": function(data, type, row, meta){
                                 switch (value.field){
                                     case 'amount':
                                         console.log(data)
                                         return formatNumberWithCommasPerThousand(Number(data))
                                     case 'insertdate':
                                     case 'eventdate':
                                         return  data.includes("-")? moment(data).format('YYYY-MM-DDTHH:mm:ss'):""
                                     case 'sourcefilename':
                                         return data!=null?data:""
                                     default:
                                         return data
                                 }
                           },
		          		'defaultContent': "",
		          		
					});
			});
			initDatatable(columns, params, businessArea);
			$.each(columns, function(item, column){
				if (item > 0){
					params.columns.push(column.data);
					params.columnsSel.push(column.data)
				}
			});
			saveParams(params)
			checkIfAllChecked();
		},
		error: function(error){
			console.dir(error)
		},
		complete: function(){
			hideLoader()
			Array.from(document.querySelectorAll('th')).forEach(th=>th.style.width = null)
		}
	});
}

function completModal(columns, withOut){
	var div = $('#selectedColumns').clone().prop('id','selectedColumns1').css("display", "block");
	div.find("#showColumnsFirst").prop('id', 'showColumnsAll').prop('name', 'showColumnsAll');
	div.find('label:first-child').prop('for', 'showColumnsAll');
	var label = $("#showColumns").clone().prop('id','');
	$.each(columns, function(index, value){
		debugger
		var newLabel = label.clone();		
		var id = "showColumns" + index;
		newLabel.prop("for", id).css('display', 'block');
		var check = newLabel.children();
		var checked = true;
		if (checkeds.indexOf(index) > -1){
			checked = false;
		}
		check.prop('checked', checked).prop("name", id).prop("id", id).prop('value', index);
		//columnsSelected.push(""+index);
		newLabel.append(value.title);
		if (-1 == withOut.indexOf(index)){	
			div.append(newLabel);
			//$("#selectedColumns").append(newLabel);					
		}
	});
	createCloseDialog(div, actionColumnShowHide);
	checkIfAllChecked();
	
}

function checkIfAllChecked(){
	var checkedValue = false;
	if (0 == checkeds.length)
		checkedValue = true;
	$("#showColumnsAll").prop("checked", checkedValue);
	$("#showColumnsFirst").prop("checked", checkedValue);
	columnsSelectedSave()
}

function columnsSelectedSave(){
	var param = getParams()
	var selectColumns = param.columns.slice()
	checkeds = checkeds.sort(function( a , b){ //reverse sort
	    if(a < b) return 1;
	    if(a > b) return -1;
	    return 0;
	})
	for (var item of checkeds) {
		selectColumns.splice((item - 1), 1)
	}
	param['columnsSel'] = selectColumns
	saveParams(param)
}

function checkEvent(event){
	var id = event.id;
	if (id === 'showColumnsAll'){
		if (event.checked){
			checkeds.forEach(ind=>$$table.column(ind).visible(event.checked))
		}
		checkeds = Array.from(document.querySelector("#selectedColumns1")
				.querySelectorAll("input[type=checkbox]"))
				.map(inpt=>{inpt.checked=event.checked; return inpt})
				.filter(inpt=>!inpt.checked && inpt.value != "on")
				.map(inpt=>Number(inpt.value))
		checkeds.forEach(ind=>$$table.column(ind).visible(event.checked))
		debugger
	}else{
		var column = $$table.column(Number(event.value));
		var visible = checkeds.includes(Number(event.value)) 
		column.visible(visible);
		if (visible){
			var index = checkeds.indexOf(Number(event.value));
			if (index>-1)
				checkeds.splice(index, 1);
		}
		else{
				checkeds.push(Number(event.value));
		}
	}
	checkIfAllChecked();
}

function getType(){
	return $('input[name=schema]:checked').val()==='Live'?"live":"archive";
}

function renderFilter(json) {
	var div = $('<div></div>').addClass('form-group');
	var label = $('<label></label>').addClass('col-md-4 control-label').text(
			json.label);
	var controlDiv = $('<div></div>').addClass('col-md-8');
	div.append(label);
	div.append(controlDiv);

	var field = "";

	$.each(json, function(key,action) {
		var arrayAction = [];
		if(key == "datasource"){
			$.each( action, function(key,value) {
				arrayAction.push(value);
			});
			json.datasource = arrayAction;
		}
	});
	
	var control = renderControl(json);

	if (control != null) {

		// compute filter field
		switch (json.type) {
		case "string":
		case "date":
			field = "filter_" + json.field;
			break;
		case "dropdown":
		case "multiselect":
			field = "filter_" + json.field + "_exact";
			break;
		case "datetime,datetime":
		case "time":
		case "date,date":
			field = "filter_" + json.field + "_idate";
			break;
		case "number,number":
			control.first().attr('id', json.id).attr('name', "filter_" + json.field + "_lnum");
			control.eq(1).attr('id', json.id).attr('name', 'filter_' + json.field + '_unum');
			break;
		}

		label.attr("for", field);

		if (field != "")
			control.attr({
				'id' : json.id,
				'name' : field
			});

		control.addClass('form-control ' + json.id);

		controlDiv.append(control);
	}

	return div;
}

function initDatatable(columns, params, businessArea) {
	// datatable init
	if ($.fn.dataTable.isDataTable('.fintpTable')) {
		var table = $('.fintpTable').DataTable();
		table.destroy();
	}
	var sort = [[1, 'desc']]
	
	$$table = $('.fintpTable').DataTable({
		processing : true,
		'serverSide' : true,
		'filter' : false,
		'columns' : columns,
		'order' :sort ,
		language: $$table_language,
		'ajax' : {
			url : 'page',
			type : 'POST',
// dataSrc: "",
			data : function(d) {
				$.each(d.columns,function(i, value) {
					d.columns[i]['type'] = (value.name == 'amountchar') ? 'amount': value.name;
					d.columns[i]['className'] = (value.data == 'amount') ? 'dt-right': '';
					d.columns[i]['name'] = "";
				});

				d["params"] = JSON.stringify(params);
			},
			beforeSend : function(xhr) {
				xhr.setRequestHeader($('#_csrf_header')
								.attr("content"), $('#_csrf')
								.attr("content"));
				showLoader()
			},
			error: function(err){
				console.log(err)
			},
			complete : function() {
				hideLoader()
			},
		},
		'createdRow' : function(row, data, index) {
			$('th').removeClass('dt-right');
			var td = $('td', row).eq(0);
			td.html($$table.page.len() * $$table.page.info().page + index + 1);
			var queryParams = {businessArea: params.businessArea[0], id: data.correlationid, type: getType()};
			
			switch(businessArea){
				case "Events":
					queryParams.id = data.guid;
					appendBtn(td, "view-event", queryParams,"Event details");
				break;
				case "Outstanding":
					queryParams.type = "live";
					appendBtn(td, "view-message-outstanding", queryParams, "Message details");
				break;
				case "Payments":
					appendBtn(td, "view-message", queryParams, "Message details");
					if ($$project.toLowerCase() != "adpharma"){
						appendBtn(td, "message-status", queryParams, "Status");
					}
					
				break;
				default :
					appendBtn(td, "view-message", queryParams, "Message details");
				break;
			}
			
		},

	});
	for (let item of Array.from(checkeds).reverse()) {
		var column = $$table.column(item);
		column.visible(false)
	}
	$('#showTableHere').append($$table);
}

function appendBtn(td, url, queryParams, title) {
	
	td.append($('<span title="View"></span>')
		.addClass("glyphicon " + ((title=="Status")?"glyphicon-info-sign":"glyphicon-chevron-right view"))
		.css('cursor', 'pointer')
		.click(function() {
// var dialog = createObjectDialog('Message details', 'wide');
			createDialog("../"+url +"?"+ new URLSearchParams(queryParams).toString(),
					false,
					null,
					{title:"Message details",size:"wide"});
		})
	);
}
