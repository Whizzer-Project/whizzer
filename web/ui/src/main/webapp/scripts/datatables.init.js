/*
 * options = {
 * 		actions: {
 * 			add: string | {
 * 				url: string,
 * 				label: string,
 * 				before: function
 * 			},
 * 			get: string,
 * 			open: string,
 * 			edit: string | {
 * 				url: string,
 * 				before: function
 * 			},
 * 			delete: string | {
 * 				url: string,
 * 				confirm: string //optional confirm delete with popup message
 * 			}
 * 		},
 * 		columns: [{..., //datatables columns format,
 * 			isActionsColumn: true, //the Actions column is inserted by default at the end of the row; 
 * 								   //use this placeholder to insert the Actions column in some intermediate position
 * 			beforeRender: function, //callback functions; used only on the Actions column during rendering
 * 			afterRender: function,
 * 			headerExtra: {} //inject attributes into the th tag
 * 		}],  
 * 		data: {}, //extra data for datatables ajax data
 * 		orderFixed: [] | {},//datatables orderFixed
 * 		drawCallback: function //datatables drawCallback
 * }
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 */
(function ( $ ) {
	var $$columns
 
    $.fn.datatablesInit = function( options ) {
 
        var actions = getOption(options, "actions");
        
        if (actions !== undefined) {
	        var get = actions.get;
	    	var open = actions.open;
	    	var edit = actions.edit;
	    	var deleteField = actions.delete;
	    	var view = actions.view;
	    	var token = actions.getToken;
	    	var add = actions.add;
	    	var start = actions.start;
	    	var stop = actions.stop;
	    	var addJob = actions.addJob;
	    	var copy = actions.copy
        }
        
        //var dialog = getOption(options, 'dialog')
        
        var getAction
        var addAction
        var editAction
        var openAction
        var deleteAction
        var viewAction
        var viewBtn = undefined
        var buttonView = undefined
        var tokenAction
        var addJobAction
        var addDialog
        var copyAction
        var viewDialog
        
        if (add !== undefined) {
        	var addButtonLabel = "Add"; //TODO use resources
        	
        	if (typeof add === "string")
        		addAction = add;
        	else if (typeof add === "object") {
        		addAction = add.url;
        		addButtonLabel = add.label;
        		var beforeInsert = add.before;
        		addDialog = formattingDialog(add.dialog)
        		
        	}
        }
        var beforeUpdate
        var viewBeforeUpdate
        var afterGet
        
        if (get !== undefined){
        	if (typeof get === 'string'){
        		getAction = get
        	}else if (typeof get === "object"){
        		getAction = get.url
        		afterGet = get.after
        	}
        }
        
        if (edit !== undefined) {
        	if (typeof edit === "string")
        		editAction = edit;
        	else if (typeof edit === "object") {
        		editAction = edit.url;
        		beforeUpdate = edit.before;
        		var callBack = edit.callBack;
        	}
        }
        
        if (open !== undefined) {
        	if (typeof open === "string")
        		openAction = open;
        	else if (typeof open === "object") {
        		openAction = open.url;
        		beforeUpdate = open.before;
//        		openDialog = formattingDialog(open.dialog) - 
        	}
        }
        
        if (deleteField !== undefined) {
        	if (typeof deleteField === "string")
        		deleteAction = deleteField;
        	else if (typeof deleteField === "object") {
        		deleteAction = deleteField.url;
        		var deleteMessage = deleteField.confirm;
        		var deleteCallBack = deleteField.callBack;
        	}
        }
        
		
        if (view !== undefined) {
        	if (typeof view === "string"){
        		viewAction = view;
        	}
        	else if (typeof view === "object") {
        		viewAction = view.url;
        		viewBeforeUpdate = view.beforeUpdate;
        		buttonView = view.button
        	    viewBtn = view.viewBtn;
        		viewDialog = formattingDialog(view.dialog)
        	}
        }
        
        if (start !== undefined) {
        	if (typeof start === "string")
        		var startAction = start;
        }
        
        if (stop !== undefined) {
        	if (typeof stop === "string")
        		var stopAction = stop;
        }
        
        if (token !== undefined){
        	if (typeof token === "string")
        		tokenAction = token;
        	else if (typeof token === 'object'){
        		tokenAction = token.url;
        		var columnNames = token.columnNames;
        		var tokenDialog = formattingDialog(token.dialog)
        	}
        }

        if (addJob !== undefined) {
        	if (typeof addJob === "string"){
        		addJobAction = addJob;
        	}else if (typeof addJob === 'object'){
        		addJobAction = addJob.url;
        		addJobBeforeAction = addJob.before
        	}     		
        }
        
        if (copy){
        	if (typeof copy === 'object'){
        		copyAction = copy.url
        		saveAction = copy.saveAction
        		copyDialog = copy.dialog
        	}
        }
        
        var sortColumn = getOption(options,'sortColumn')
       
        var defaultSortColumn;
        if(sortColumn == undefined){
        	defaultSortColumn = [0, "asc"];
        }else{  
        	if(Array.isArray(sortColumn)){
                if(sortColumn[1] == undefined){
                	defaultSortColumn = [sortColumn[0], "asc"];
                }else{
                	defaultSortColumn = [sortColumn[0], sortColumn[1]];
                }
        	}else{
        		defaultSortColumn = [sortColumn, "asc"];
        	}

        }          
        
        var canAdd = addAction !== undefined;
    	var canOpen = openAction !== undefined;
    	var canView = viewAction !== undefined;
    	var canModify = editAction !== undefined;
    	var canDelete = deleteAction !== undefined;
    	var canToken = tokenAction !== undefined;
    	var canStart = startAction !== undefined;
    	var canStop = stopAction !== undefined;
    	var candAddJob = addJobAction !== undefined;
    	var canCopy = copyAction !== undefined
    	
    	var hasActions = canOpen || canModify || canDelete || canView || canToken || canStart || canStop || candAddJob || canCopy;
    	
    	$$columns = getOption(options, "columns");
    	
        if (canAdd == true) {
        	//hasModifyRole is global
	    	if($$hasModifyRole==true){
	    		var addButton = $('<button>')
					.attr('id', 'addRecord')
					.addClass('submitButton glossy-button glossy-button--purple addButton')
					.css('width', 'auto')
					.html(addButtonLabel);
	    		addButton.click(function(){
	    			createDialog(addAction, $$actionCreate, beforeInsert, addDialog, getOption(options, "saveAction"));
	    		});
	    		this.before(addButton);
	    	}
//	    	else
//	    		addButton.attr("disabled", 'disabled');
	    	
	    	//add button before this
	    	
        }
    	
    	if (hasActions == true) {
    		//find actions column placeholder
    		var actionsIndex = -1;
    		$.each($$columns, function(i, item){
    			if (item.isActionsColumn !== undefined && item.isActionsColumn == true) {
    				actionsIndex = i;
    				return false;
    			}
    		});
    		
    		var beforeRender;
    		var afterRender;
    		
    		if (actionsIndex != -1) {
    			beforeRender = $$columns[actionsIndex].beforeRender;
    			afterRender = $$columns[actionsIndex].afterRender;
    		}
    		
    		var actionsColumn = {
        			"title": $$actions,
        			"className": 'dt-body-center',
                    "sortable": false,
                    "headerExtra": {
                    	"data-filter": "noFilter"
                    },
                    "render": function ( data, type, row, meta ) {
                    	//console.log(row);

                    	//set extra row info as hidden
                    	var urlTokens = ["rowid"]; //default id field
                    	
                    	var str = "";
                    	
                    	if (beforeRender !== undefined)
                    		str += beforeRender(data, type, row, meta);
                    	
                    	var canModifyButton = {cssClass:"edit", title:"Edit", icon:"pencil", action:editAction,}
                    	var canDeleteButton = {cssClass:"delete", title:"Delete", icon:"trash", action:deleteAction, disabled:!$$hasModifyRole}
                    	if (!$$hasModifyRole || (row.serviceType==0)){
                    		canModifyButton = undefined
                    		canDeleteButton = undefined
                    	}
                    	
                    	var buttons = {
                    		canOpen : {cssClass:"open", title:"Open", icon:"folder-open", action:openAction},                    		
                    		canView : {cssClass:"view", title: (typeof title === 'undefined')?"View":title, icon:"folder-open", action:viewAction},
                    		canToken : {cssClass:"token", title:"Token", icon:"link", action:tokenAction},
                    		canStart : {cssClass:"start", title:"Start", icon:"play", action:startAction},
                    		canStop : {cssClass:"stop", title:"Stop", icon:"pause", action:stopAction},
                    		canModify : canModifyButton,
                    		canCopy : {cssClass:"copy", title:"Copy", icon:"duplicate", action:copyAction},
                    		canDelete : canDeleteButton,
                    		candAddJob : {cssClass:"addJob", title:"Add Job", icon:"random", action:addJobAction},
                    		
                    	}
                    	
                    	$.each(buttons, function(key, btn){
                    		if(eval(key)){
                    			if ( btn && ((key!="canView") || (viewBtn === undefined || viewBtn(row)))){
                    				str += "<button class='"+btn.cssClass+" btn btn-xs' "+((btn.disabled!=undefined && btn.disabled)?"disabled":"")+" title='"+btn.title+"'>" +
										"<span class='glyphicon glyphicon-"+btn.icon+"'>" +
										"</span>"+
									"</button>";
				        			$.merge(urlTokens, getUrlTokens(btn.action));
                    			}
                    		}
                    	})
                    	
                    	if(canToken)
                    		$.merge(urlTokens, columnNames);
                    	
                    	if (afterRender !== undefined)
                    		str += afterRender(data, type, row, meta);
                    	
                    	//console.log(urlTokens);
                    	
                    	//url tokens as hidden fields
                    	$.each(urlTokens, function(i, item){
                    		str += "<input name='" + item + "' type='hidden' value='" + row[item] + "'/>";
                    	});
                    	
        				return  str;
        						
        				}
        		};
    		
    		if (actionsIndex != -1) {
    			$.extend(true, $$columns[actionsIndex], actionsColumn);
    			
    			if (actionsIndex == defaultSortColumn)
    				defaultSortColumn++;
    		}
    		else
    			$$columns.push(actionsColumn);
    	}
    	
    	$.each($$columns, function(i, column){
    		column["defaultContent"] = "";
    	});
    	
    	//defined how many row per page
    	var pageLength = getOption(options,"pageLength")?getOption(options,"pageLength"):[10, 25, 50, 100]
    	
    	var table = this.DataTable( {
    		processing: true,
            serverSide: true,
            language: $$table_language,
            "lengthMenu": pageLength,
            ajax : {
            	"url" : getAction,
            	"data" : function(d) {
            		var extraData = getOption(options, "data");
            		
            		if (typeof extraData !== "undefined") {
            			$.each(extraData, function(i, item){
            				d[i] = item;
            			});
            		}
            		
    				$.each(d.columns, function(i, value) {
    					d.columns[i]['type'] = value.name;
    					d.columns[i]['name'] = "";
    				});
    			},
    			beforeSend: function(){
    				showLoader()
    			},
    			error: function(data){
    				
    			},
    			complete: function(){
    				$('th').removeClass('dt-right');
    				Array.from(document.querySelectorAll('th')).forEach(th=>th.style.width = null)
    				hideLoader()
    				if (afterGet)
    					afterGet()
    			},
            },
            "order": defaultSortColumn,
            "orderFixed": getOption(options, "orderFixed"),
            "columns": $$columns,
            "drawCallback": getOption(options, "drawCallback"),
            "footerCallback": getOption(options, "footerCallback"),
            "dom" : 'rltip'
    	    });
    	
    	this.find('tbody').on('click', 'button.edit', function(e) {
//    		var id=$(this).parent().find("input:hidden").val();
    		if (callBack !== undefined){
    			callBack(prepareUrl(editAction, $(this).parent()))
    		}else{
	    		createDialog(
	    				prepareUrl(editAction, $(this).parent()), 
	    				$$actionUpdate,
	    				beforeUpdate,addDialog,getOption(options, "editAction")
	    		);
    		}
    	});
    	
    	this.find('tbody').on('click', 'button.copy', function(e){
    		createDialog(
    				prepareUrl(copyAction, $(this).parent()),
    				copy.saveText,
    				undefined,
    				copyDialog,
    				saveAction
    		)
    	})
    	
    	this.find('tbody').on('click', 'button.open', function(e) {
    		var val = $(this).parent().parent().find('td:eq(0)').text();
    		document.location.href = prepareUrl(openAction + 'fp=' + val, $(this).parent());
    	});
    	this.find('tbody').on('click', 'button.view', function(e) {
//    		var id=$(this).parent().find("input:hidden:eq(1)").val();
    		//createSimpleDialog(doAction(prepareUrl(openAction, $(this).parent())));
    		showLoader()
    		if (buttonView){
    			editDialog(
	    				prepareUrl(viewAction, $(this).parent()), 
	    				buttonView,
	    				viewBeforeUpdate, viewDialog
	    		);
    		}else{
    			createDialog(
	    				prepareUrl(viewAction, $(this).parent()), 
	    				buttonView,
	    				viewBeforeUpdate, viewDialog
	    		);
    		}
    	});
    	
    	this.find('tbody').on('click', 'button.start', function(e) {
   		
    		document.location.href = prepareUrl(startAction, $(this).parent());
    	});
    	
    	this.find('tbody').on('click', 'button.stop', function(e) {
   		
    		document.location.href = prepareUrl(stopAction, $(this).parent());
    	});   
    	
    	this.find('tbody').on('click', 'button.addJob', function(e) {
    		var time = $(this).parent().find('input:hidden[name=timeTrigger]').val();
    		    		
    		if(time !== "null" && time !== ""){
    			
    			var div = $('<div></div>');
    			div.load(prepareUrl(addJobAction, $(this).parent()), function(responseText, textStatus, XMLHttpRequest){  	   		     			
            		BootstrapDialog.show({
            			title: '',
            			draggable: true,
            			message: div,
            			buttons: [
            				createButton("Update"),
            				createButton("Delete"),         	        	
            	        	{
            					label: $$actionCancel,
            					cssClass : "cancel submitButton glossy-button glossy-button--purple",
            					action: function(me) {
            						me.close();
            					}
            				}
            	        ]            			
            		}).getModalHeader().css('background-color', '#442080');
    			});   		
    		}else{
        		createDialog(
        				prepareUrl(addJobAction, $(this).parent()), 
        				"Create",
        				addJobBeforeAction
        		);
    		}
    	});    	

    	this.find('tbody').on('click', 'button.delete', function(e) {
//    		var id=$(this).parent().find("input:hidden").val();
    		if (deleteMessage === undefined || confirm(deleteMessage)){
	    		if (deleteCallBack !== undefined){
	    			deleteCallBack(prepareUrl(deleteAction, $(this).parent()))
	    		}else{
	    			doAction(prepareUrl(deleteAction, $(this).parent()));
	    		}
    		}
    	});
    	this.find('tbody').on('click', 'button.token', function(e) {
//    		var id=$(this).parent().find("input:hidden").val();    
    		createDialog(
    				prepareUrl(tokenAction, $(this).parent()), 
    				undefined,
    				beforeUpdate, 
    				tokenDialog
    		);
//    		getToken(id);
//    		createDialog(
//    				prepareUrl(tokenAction, $(this).parent()), 
//    				"NOBUTTON",
//    				beforeUpdate, 'Message details');
    	});
    	
    	//DataTable is rendered now
    	//inject headerExtra to th
    	$.each($$columns, function(i, item){
    		if (item.headerExtra !== undefined)
    			$.each(item.headerExtra, function(key, value){
    				$(table.column(i).header()).attr(key, value);
    			});
    	});
    	    	    	
    	this.createFilters();
        
        return table;
    };
 
    $.fn.createFilters = function() {
//    	var columns = getOption(options, "columns");
    	var trFilter = $('<tr>');
    	var dataTable = this.DataTable();
    	this.find('thead tr:eq(0) th').each(
    		function(i) {
    			var filter = {};
    			var td = $('<td>').addClass('filterTh');
    			var filterType = $(this).attr("data-filter");
    			filter.type = (filterType!=undefined?filterType:"string");
    			switch(filterType){
    				case "checkbox": 
    				case "dropdown": 
    					var dataSource = $(this).data("datasource");
    					filter.datasource = dataSource;	
    					break; 
    			}
    			if(filterType!="noFilter"){
    				var control = renderControl(filter);
    				if (filterType == 'number'){
    					control.attr("pattern","[0-9]*\.?[0-9]{1,2}" );
    				}
    				control.attr("id","filter" + i);
    				control.attr("placeholder", $$messages["general.search"] +' '+ $(this).text())
    				   .attr("style", "background:white; width: 100%;"); 
    				control.attr("title", $$messages["general.search"] +' '+ $(this).text());
    				td.append(control);
    				control.on('keyup change blur',function(e) {
    					var id = Number(e.target.id.split("filter")[1]) 
    					if (e.target.type == 'checkbox'){
    						if (e.target.checked == true){
    							dataTable.column(id).search(this.value).draw();
    						}else{
    							dataTable.column(id).search('').draw();
    						}
    					}
    					else{
	    					if (dataTable.column(id).search() !== this.value) {
	    						dataTable.column(id).search(this.value).draw()
	    					}
    					}
    				});

    			}
    			trFilter.append(td);
    	});
    	trFilter.css("text-align", "center");
    	this.find('thead').append(trFilter);
    	
    	registerDateTime();
    	registerValidations();
    	registerUIControls();
    };
    
}( jQuery ));

function getUrlTokens(url) {
	var tokens = []; 
	var reg = /{([^}]*)}/g;
	
	while (match = reg.exec(url))
		tokens.push(match[1]);
	
	return tokens;
}

function prepareUrl(action, container) {
	var tokens = getUrlTokens(action);
	
	$.each(tokens, function(i, item){
		var value = container.find("input:hidden[name='" + item + "']").val();
		
		action = action.replace("{" + item + "}", value);
	});
	
	if (tokens.length > 0)
		return action;
	
	var rowid = container.find("input:hidden[name='rowid']").val();
	
	return "" + action + rowid;
}

function getOption(options, option) {
	if(typeof options !== "undefined" && typeof options[option] !== "undefined")
		return options[option];
	
	//return "undefined";
}

function createButton(label){
	return {
		label: label,
		cssClass: label.toLowerCase() + " submitButton glossy-button glossy-button--purple",
		action: function(me) {
			var form = me.$modalBody.find('form');
			
//			if (beforeSubmit !== undefined)
//				beforeSubmit(form);
			
			$.ajax({
				type: form.attr('method'),
				url:  (label === "Update") ? form.attr('action') : "delete-trigger",
				data: form.serialize(),					
				dataType: 'json',
				beforeSend: function(xhr){
					var csrfToken = $('#_csrf').attr("content");
					var csrfHeader = $('#_csrf_header').attr("content");
		            xhr.setRequestHeader(csrfHeader, csrfToken);
		            showLoader()
			    },
				success: function(data) {
					//console.log(data);
					var ok = handleErrorList(data, form);
					
					if (ok) {
						me.close();
						location.reload();
					}
					
				},
				error: function(data) {
					//console.log(data)
					handleAPIError(data, form);
				},
				complete: function(){
					hideLoader();
				}
			});
		}
	}
}
