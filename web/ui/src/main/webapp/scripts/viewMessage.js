$(function(){
//	var correlationId = 0;
	var pathPage = ""
	if (window.location.pathname.indexOf('reports')>-1){
		pathPage = "../../queues/"
	}
	
	var sort = []
	sort = [0, 'desc']
	
	if(entryQueueMessage!=undefined && entryQueueMessage.queueName=="Edit"){
		$(".bootstrap-dialog-footer-buttons").find(".create").after("<button id='reset' class='btn create submitButton glossy-button glossy-button--purple'>Cancel Edit</button>")
		$("#reset").click(function(){
			if (confirm("Discard the modifications?")){
				const item = {
					queueName:entryQueueMessage.queueName,
					action:"CancelEdit",
					details:"",
					reason:"",
					messageType:messageType,
					messageId:[id]
				};
				$.ajax({
	                type: "POST",
	                url: "message-routing-jobs",
	                data: {item: JSON.stringify(item)},  
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
	                				location.reload()
	                			}, 
	                		5000)
	                	}
	            		console.log('messasge routing jobs');
	                	console.log(data);
	                },
	                error: function (xhr, textStatus, error){
	                	console.log(xhr, textStatus, error)
	                }
	            });
			}
		})
//		correlationId = entryQueueMessage.correlationId;
	}
//	else{
//		correlationId = message.correlationid;
//	}
	
	getHistory()
	getDuplicate()
	getCorrelatedEvents()
	
	function getHistory(){
		var table = $('<table >').addClass("fintpTable display").css("width", "100%");
		$('#menu1').children().remove()
		$('#menu1').append('<span style="text-align:center"><b>Details of the related Original transaction</b></span>')
			.append(table);
		table.datatablesInit({
			language: $$table_language,
			sortColumn: sort,
			scroll: true,
			actions: {
				get: pathPage + 'page-history?&id=' + message.correlationid,
				view: {
					url : pathPage+'payload?type='+messageType+'&history=false&action=view&id={rowid}',
					dialog:{
						title : "View original transaction",
						size : "wide",
					},
				},
			},
			columns: [ 
				{ "title": historyLabels.dateTime, "data": "insertdate", "name": {filterName: "dateTimeLimit"},
					"width" : "150px",
					"render": function(data, type, row, meta){
						return moment(data).format('YYYY-MM-DD HH:mm:ss')
					},
				},
				{ "title": historyLabels.requestorService, "data": "requestorservice" },
				{ "title": historyLabels.corrId, "data": "correlationid" },
			]
		})
		
	}
	
	function getDuplicate(){
		var queueName = null
		if (entryQueueMessage){
			queueName = entryQueueMessage.queueName
		}
			
		var table = $('<table >').addClass("fintpTable display").css("width", "100%");
		$('#menu2').children().remove()
		$('#menu2').append('<span style="text-align:center"><b>Duplicate messages</b></span>')
			.append(table);
		
		table.datatablesInit({
			sortColumn: sort,
			actions: {
				get: pathPage + 'page-duplicate?queue=' + queueName+'&id=' + id, 
			},
			columns: [ 
				{ "title": queueLabels.reference, "data": "reference" },
				{ "title": queueLabels.status, "data": "feedback" },
				{ "title": queueLabels.name, "data": "sourcefilename" },
				{ "title": queueLabels.current, "data": "queuename"},
			]
		})
	}
	
	function getCorrelatedEvents(){
		var table = $('<table >').addClass("fintpTable display").css("width", "100%");
		$('#menu3').children().remove()
		$('#menu3').append('<span style="text-align:center"><b>Correlated events</b></span>')
			.append(table);
		
		var url = '../events/page?correlationid=';
		if( window.location.href.indexOf("reports") !== -1 ){
			url = '../../events/page?correlationid='
		}
		
		table.datatablesInit({
			language: $$table_language,
			sortColumn: sort,
			actions: {
				get: url + message.correlationid,
			},
			columns: [ 
				{ "title": eventLabels.dateTime, "name": {filterName: "dateTimeLimit"}, 
					  "data": "eventdate",
					  "render": function ( data, type, row, meta ) {
				  		 const date = new Date(data);
				  		 const offsetMs = date.getTimezoneOffset() * 60 * 1000;
				  		 const dateLocal = new Date(date.getTime() - offsetMs);
				  		 const formatedDate = dateLocal.toISOString().slice(0, 19).replace("T", " ");
				  	
				  		 return formatedDate;
					  },	
					},
				{ "title": eventLabels.message, "data": "message"},
				{ "title": eventLabels.additionalinfo, "data": "additionalinfo"},
				{ "title": eventLabels.correlationid, "data": "correlationid"},
				{ "title": eventLabels.user, "data": "username"},
			],
			error: function(err){
				console.log(err)
			},
			complete:function(){
				console.log("complete")
			}
		});
	}

	
	var dependency = {}
	
	function getElementConditions(element){
		if ($(element).attr('data-conditions')){
			return  $(element).attr('data-conditions')	
		}
		return undefined
	}
	
	function getElementName(element){
		if ($(element).attr('name')){
			return replaceNonAlphaNumericChar($(element).attr('name')).toLowerCase()
		}
		return undefined
	}
	
	function applyConditions(element){
		var spanValue = {}
		var conditions = JSON.parse(getElementConditions(element))
		if (!conditions || !conditions.hasOwnProperty('rules')){
			return spanValue
		}
		conditions.rules.forEach(function(rule){
			// let master = 'master'
			if (rule.value.indexOf('rule.', 0) == 0){
				var name = getElementName(element)
				var master = replaceNonAlphaNumericChar(rule.value.replace("rule.","")).toLowerCase()
				if(dependency.hasOwnProperty(master)){
					dependency[master].push(name)
				}else{
					dependency[master] = Array()
					dependency[master].push(name)
				}
			}
			else if(rule.value.indexOf('xpath', 0) == 0){
				var tempGetxPath = getxPathFromXMLTree(rule)
				for (const [key, value] of Object.entries(tempGetxPath)){
					spanValue[key] = value
				}
			}else if (rule.value.indexOf('text.', 0) == 0){
				var entity_field = rule.field.split('-')
//				var entity = entity_field[0]
				var field = entity_field[1]
				spanValue[field] = {}
				spanValue[field].value = rule.value.replace('text.', '')
				spanValue[field].rezult = rule.operator.includes('not')?false:true
			}
		})
		return spanValue
	}
	
	function getxPathFromXMLTree(rule){
		var spanValue = {} 
		var xpath = rule.value.replace('xpath.', '')
		$("#xmlTree").find("[data-path]").each(function(index) {
			if ($(this).attr('data-path') == xpath){
				var entity_field = rule.field.split('-')
//				var entity = entity_field[0]
				var field = entity_field[1]
				spanValue[field] = {}
				if ('SELECT' == this.tagName){
					spanValue[field].value = $(this).find(' option:selected').text()
				}
				else{
					spanValue[field].value = $(this).text()
				}
				spanValue[field].rezult = rule.operator.includes('not')?false:true
			}
		})
		return spanValue
	}
	
	function selectOnChange(){
			// const name = $(this).attr('name').toLowerCase()
		var valueChanged = $(this).val()
		var name = $(this).attr('id')
		if(dependency[name]){
			dependency[name].forEach(o => {
				var obj_name = '#'+o
//				document.querySelector(obj_name).value = ""
				$(obj_name).children().filter((i, node)=> {return $(node).hasClass('msgDetail')}).hide() // hide all options
				$(obj_name).children().filter((i, node)=> {return $(node).hasClass('msgDetail')}).hide() // hide all options
				var conditions = JSON.parse(getElementConditions(obj_name))
				if (conditions && conditions.rules){
					var down_elem = $(obj_name).attr('data-type-editid')
					dropDowns[down_elem].forEach(elem => {
						const rezultCheck = []
						conditions.rules.forEach(rule =>{
							let check = false
							switch (rule.operator){
							case 'contain': 
							case 'equal': 
								check = true
								break
							}
							var field = rule.field.split('-')[1]
							if (rule.value.indexOf('text.', 0) == 0){
								messChecked  = rule.value.replace('text.','')
								
							}
							else if (rule.value.indexOf('rule.', 0) == 0){
								messChecked = valueChanged
								
							}
//							else if (rule.value.indexOf('xpath', 0)  == 0 ){
//								
//							}
							if (rule.operator.includes('equal')){
								if ((elem[field] == messChecked) == check){
									rezultCheck.push(true)
								}else{
									rezultCheck.push(false)
								}
							}else{
								if (elem[field].includes(messChecked) == check){
									rezultCheck.push(true)
								}else{
									rezultCheck.push(false)
								}
							}
						})
						if( conditions.condition.toUpperCase() == 'AND' && !rezultCheck.includes(false)){
	// obj.append(new Option(o[value.fieldValue], ""))
							var field_param=$(obj_name).attr('data-field')
							$(obj_name).find(".msgDetail").filter((index, select)=>{
								return $(select).val().trim().toString().localeCompare(elem[field_param])==0
								})
								.css('display', 'block')
						}else if(conditions.condition.toUpperCase() == 'OR' && rezultCheck.includes(true)){
	// obj.append(new Option(o[value.fieldValue], ""))
							$(obj_name).find(".msgDetail").filter((index, select)=>{
								return $(select).val().trim().toString().localeCompare(elem[field])==0
								})
								.css('display', 'block')
						}
						return true
					})
					selectFirstAndSingleValue($(obj_name))
				}
			})
			
		}
	}
	
	function setAttribute(element){
		var attribute = {}
		attribute.pattern = $(element).attr("data-pattern")
		attribute['data-field'] = $(element).attr("data-buss-list")
		attribute.mandatory = $(element).attr('mandatory')
		attribute['data-path'] = $(element).attr("data-path")
		attribute['data-type-editid'] = $(element).attr("data-type-editid").trim()
		attribute['data-conditions'] = getElementConditions(element)
		attribute.text = $(element).text().trim()
		attribute.name = getElementName(element)
		return attribute
	}
	var selectedNode = {};
	$('#loader').css("display", "none");
	var tree = $('#xmlTree');
	tree.jstree({
		core : {
			"check_callback" : true,
		    themes : {
		      icons : false
		    }
		  }
	}).on('ready.jstree', function() {
		tree.jstree('open_all');
		if(action=="view"){
			$('#xmlTree li').filter(function(){
				return $(this).attr('aria-level') > 3;
			}).each(function(i, node){
				tree.jstree('close_node', node);
			});
		}else{
			$("#xmlTree").find("span[data-type-editid]").each(function(index) {
				var attr = setAttribute(this)
				var optionsId = attr["data-type-editid"]
				if(optionsId!='-1'){
					var bussList = attr["data-field"]
					var mandatory = attr.mandatory
					const valueText = attr.text
					
					var name = attr.name
					var spanValue = applyConditions(this)
					var edit = $("<input type='text' class='form-control' id='"+name+"'></input>");
					var existValue = true
					switch(optionsId){
						case 'timestamp':
							edit.addClass('dateTimePicker')
							break;
						case 'date':
							edit.addClass("datePicker") 
							break;
						case 'sequency':
						case 'fixvalue':
						case 'editvalue':
							break;
						default: 
							if (dropDowns[optionsId]){
//								$$field_compare = bussList
//								dropDowns[optionsId].sort(compareObjectByString)
								edit = $("<select id='"+name+"' class='form-control'></select>");
								existValue = false
								var options = []
								for(var i=0;i<dropDowns[optionsId].length;i++){
									 var entity = dropDowns[optionsId][i];
									 var check = true;
									 for (const [key, value] of Object.entries(spanValue)){
										 if ((entity[key] == value.value) == value.rezult){
											 continue
										 }
										 check = false
										 break
									 }
									 if (check){
										 var bussListValue = entity[bussList]
										 if (bussListValue){
											 bussListValue = bussListValue.trim()
											 if (!options.includes(bussListValue.trim())){
												 options.push(bussListValue)
											 }
										 }
										 
									 }
									 
								}

								if (options.length > 1){
									options.sort()
								}
								options.forEach((val)=>{
									 if (valueText.toLowerCase() == val.toLowerCase()){
										edit.append("<option selected='selected' class='msgDetail'>"+val+"</option>");
										existValue = true
									 }
									 else{
										 edit.append("<option class='msgDetail'>"+val+"</option>");
									 }
								})
								if (!existValue){
									edit.append("<option selected='selected' class='msgDetailDntExist'>"+valueText+"</option>");
									edit.attr('class', 'form-control')
									existValue = false
								}
								edit.on('change', selectOnChange)
								selectFirstAndSingleValue(edit, valueText)
							}
								break;
					}
					for (const [key, value] of Object.entries(attr)){
						if (!edit.is('INPUT') && key =='text')
							continue
						else
							edit.attr(key, value)
						
					}
					if (edit.is('INPUT'))
						edit.val($(this).html());
					edit.css('display', 'initial')
						.css('width', '35%')
						.css('margin', '-5px 0px 7px');
					$("#changeSelNode").css("display","");
					edit.change()
					
					$(this).parent().parent().append(edit)
					if (!existValue){
						$("<span> * </span>").insertAfter(edit)
					}
					if ('-1' == optionsId){
						edit.css('display', 'none')
					}else{
							$(this).remove();
					}
				}
			})
			registerDateTime();
			$('.modal select').each(function(){
				$(this).change();
			})
		}
		setTimeout(function(){
			var input = document.querySelector('input[text="[[#{remove}]]"]')
			if (input)
				input.value =""
		}, 1000)
	});
	
	function selectFirstAndSingleValue(obj, value){
		obj.children().filter(function(){
			return window.getComputedStyle(this).display == 'none'
		}).removeAttr("selected")
		var active_element = obj.children()
								.filter(function(){
									return window.getComputedStyle(this).display != 'none' && this.value != FIRSTOPTIONVALUE
								})
								.length
		if (active_element == 1){
			obj.children().filter(function(){
				return window.getComputedStyle(this).display != 'none' && this.value != FIRSTOPTIONVALUE
			})
				.last()
				.attr('selected', 'selected')
				.change()
		}
		else if (active_element == 0){
			var id = "#" + obj.get(0).id
			var element = document.querySelector(id)
			if (element){
				element.value = ''
			}
		}
		else if (undefined != value){
				var filter = obj.children().filter(function(){
					return window.getComputedStyle(this).display !== 'none' && this.value == value
				})
					if (filter.length >= 1){
						filter.first()
						.attr('selected', 'selected')
						.change()
					}else{
						filter = obj.children().filter(function(){
							return window.getComputedStyle(this).display !== 'none'
						})
						filter.first()
						.attr('selected', 'selected')
						.change()
					}
		}
		else{
				filter = obj.children().filter(function(){
					return window.getComputedStyle(this).display !== 'none'
				}).find(elmt=>elmt.attr('selected')=='selected')
				if (0 == filter.length)
					filter.first()
						.attr('selected', 'selected')
						.change()
		}
	}
								
	$('#changeSelNode').click(function(){
		$(selectedNode.el).html( $("#selNodeValue").val() )
		$('#xmlTree').jstree('rename_node', selectedNode.node , selectedNode.elementName.prop("outerHTML") + $(selectedNode.el).prop("outerHTML") )
	});
	
	$('#expand').click(function(){
		tree.jstree('open_all');
	});
	
	$('#collapse').click(function(){
		tree.jstree('close_all');
	});
	
	if(action=="edit"){
		$("#save").click(function(){
			
		})
	}
});
