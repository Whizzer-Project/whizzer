var reasonsEnum = {
	DB_ForeignKey: "DB_ForeignKey",
	DB_PrimaryKey: "DB_PrimaryKey",
	DB_UniqueKey: "DB_UniqueKey",
	DB_Other: "DB_Other"
};

function handleErrorList(json, form) {
	var ok = json.length == 0;
	
	form.find('*[class="errorMessage"]').remove();
	
	$.each(json, function(i, item){
		var field = item.field;
		var error = $('<span class="errorMessage">' + item.defaultMessage + '</span>');
		form.find("*[name='" + field + "']").after(error);
	});
	return ok;
}


function handleAPIError(data, form) {
	var noForm = true;
	
	var column, errorMessage, detailedErrorMessage = null;
	
	if (typeof form !== "undefined") {
		form.find('*[class="errorMessage"]').remove();
		noForm = false;
	}
	
	//console.log(data.status);
	
	switch(data.status){
		case 500: 
			if(form.find('*[class="time-picker"]').val() === ""){
				detailedErrorMessage = "Choose time";				
			}
			break;
		case 302: //ajax goes on error handler??
		case 200: //show message? 
			location.reload();
			return;
		case 409: //switch reason and prepare message
			var response = data.responseJSON;
			
			switch(response.reason) {
			case reasonsEnum.DB_ForeignKey :
				var screen;
				
				if (typeof response.extraInfo.screen !== "undefined")
					screen = response.extraInfo.screen;
				else
					screen = response.extraInfo.table;
				
				errorMessage = 'Used in ' + screen;
				column = [{name: response.extraInfo.column, value: response.extraInfo.value}];
				break;
			case reasonsEnum.DB_UniqueKey :
			case reasonsEnum.DB_PrimaryKey :
				errorMessage = 'Already exists';
				
				var columns, values;
				
				$.each(response.extraInfo.columns, function(i, item) {
					columns += ", " + item.name;
					values += ", " + item.value;
				});
				
				columns = columns.substr(2);
				values = values.substr(2);
				
				detailedErrorMessage = errorMessage + ' [' + columns + ']=[' + values + ']'
				column = response.extraInfo.columns; //unique keys can have multiple columns
				break;
			case reasonsEnum.DB_Other :
			default :
				errorMessage = '[' + response.message + ']';
				break;
			}
			
	        break;
	}
	
	if (noForm) { //show dialog
		if (detailedErrorMessage != null)
			createErrorDialog(detailedErrorMessage);
		else
			createErrorDialog(errorMessage);
	}
	else { //show span in form
		var showDetailed = true;
		
		$.each(column, function(i, item){
			var field = $('*[data-column="' + item.name + '"]');
			
			if (field.length == 0)
				field = $('*[name="' + item.name + '"]');
			
			if (field.length > 0) {
				showDetailed = false;
				field.after($('<span class="errorMessage">' + errorMessage + '</span>'));
			}
		});		
		
		if(showDetailed){
			if (detailedErrorMessage != null){				
				if($('.bootstrap-dialog-message').find('*[class="errorMessage"]').val() === undefined){
					form.before($('<span class="errorMessage">' + detailedErrorMessage + '</span>'));
				}				
			}				
			else
				form.before($('<span class="errorMessage">' + errorMessage + '</span>'));
		}
	}
}

function createDialog(url, saveText, beforeSubmit, dialog, saveAction) {
	var div = $('<div></div>');
	dialog = formattingDialog(dialog)
	showLoader()
	
	div.load(url, function(responseText, textStatus, XMLHttpRequest){
		if(XMLHttpRequest.status == 403){
			createErrorDialog('Not enough rights to perform this action!');
			return false;
		}
		if (saveText){
			openDialog(div, saveText, beforeSubmit, saveAction, dialog);
		}else{
			createCloseDialog(div, dialog);
		}
	});
}

function doAction(url) {
	$.ajax({
		type: 'GET',
		url: url,
		dataType: 'json',
		beforeSend: function(){
			showLoader()
		},
		success: function(data) {
			//TODO
			//hook-up callback? : datatables reload ...
			//console.log('ok');
			location.reload();
		},
		error: function(data) {
			//console.log('error');
			handleAPIError(data);
		},
		complete: function(){
			hideLoader()
		}
	});
}

function editDialog(url, saveText, beforeSubmit, dialog, saveAction){
	var div = $('<div></div>');
	dialog = formattingDialog(dialog)
	showLoader()
	
	div.load(url, function(responseText, textStatus, XMLHttpRequest){
		if(XMLHttpRequest.status == 403){
			createErrorDialog('Not enough rights to perform this action!');
			return false;
		}
		BootstrapDialog.show({
			title: dialog.title,
	        draggable: dialog.draggable,
	        size: dialog.size,
	        closeByBackdrop: dialog.closable,
	        closable: false,
			message: div,
			onshown: function(dialogRef){
				hideLoader()
			},
			buttons:[
	        	{
	        		label: saveText,
	        		cssClass: "create submitButton glossy-button glossy-button--purple",
	        		action: function(me) {
						var form = me.$modalBody.find('form');
						
						if (beforeSubmit !== undefined)
							var rez = beforeSubmit(form) 
						if (true == rez){
							return false;
						}else{
					
							if (saveAction == undefined){
								$.ajax({
									type: form.attr('method'),
									url: form.attr('action'),
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
										hideLoader()
									},
									complete: function(){
										hideLoader()
									}
								});
							}else{
								showLoader()
								saveAction();
							}
							
						}
					}
	        	}],
		}).getModalHeader().css('background-color', '#442080'); // aici de adaugat css cu setarile de scroll
	});
}

function openDialog(div, saveText, beforeSubmit, saveAction, dialog) {
		dialog = formattingDialog(dialog)
	
	BootstrapDialog.show({
        title: dialog.title,
        draggable: dialog.draggable,
        size: dialog.size,
        closeByBackdrop: dialog.closable,
		message: div,
		onshown: function(dialogRef){
			hideLoader()
		},
        buttons: [
        	{
        		label: saveText,
        		cssClass: "create submitButton glossy-button glossy-button--purple",
        		action: function(me) {
					var form = me.$modalBody.find('form');
					
					if (beforeSubmit !== undefined)
						var rez = beforeSubmit(form) 
					if (true == rez){
						return false;
					}else{
				
						if (saveAction == undefined){
							$.ajax({
								type: form.attr('method'),
								url: form.attr('action'),
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
									hideLoader()
								},
								complete: function(){
									hideLoader()
								}
							});
						}else{
							showLoader()
							saveAction();
						}
						
					}
				}
        	},
        	{
				label: $$actionCancel,
				cssClass : "cancel submitButton glossy-button glossy-button--purple",
				action: function(me) {
					me.close();
				}
			}
        ]
    }).getModalHeader().css('background-color', '#442080'); // aici de adaugat css cu setarile de scroll
	
}


function createErrorDialog(message) {
	var div = $('<div></div>');
	
	div.append("<div style='color:red'>" + message + "</div>")
	
	BootstrapDialog.show({
		type: BootstrapDialog.TYPE_DANGER,
		title: '',
		draggable: true,
		message: div
	}).getModalHeader().css('background-color', '#442080');
}

function createSimpleDialog(content, dialog) {
	var div = $('<div class="modal-body-scroll"></div>');
//	if (title === null){
//		title=""
//	}
	div.append(content);
	dialog = formattingDialog(dialog)
	
	BootstrapDialog.show({
		size: BootstrapDialog.SIZE_WIDE,
		title: dialog.title,
		draggable: dialog.draggable,
		//closable: dialog.closable,
		message: div,
		onshown: function(dialogRef){
			hideLoader()
		},
	})
	.getModalHeader().css('background-color', '#442080');
}

function createCloseDialog(content, dialog) {
	var div = $('<div></div>');
//	if (typeof dialog === 'string'){
//		var title = dialog
//		this.dialog = createObjectDialog(title)
//	}else if(typeof dialog === 'object'){
//			this.dialog = createObjectDialog(dialog.title, dialog.size, dialog.draggable, dialog.closable)
//	}
	
	div.append(content);
	dialog = formattingDialog(dialog)
	
	BootstrapDialog.show({
		title: dialog.title,
		draggable: dialog.draggable,
		closeByBackdrop: dialog.closable,
		size: dialog.size,
		message: div,
		onshown: function(dialogRef){
			hideLoader()
		},
		buttons: [
			{
				label: $$actionClose,
				cssClass : "cancel dialogButton glossy-button glossy-button--purple",
				action: function(me) {
					me.close();
				}
			}
		]
	})
	.getModalHeader().css('background-color', '#442080');
	
}

function createConfirmDialog(content, callback) {

	var div = $('<div></div>');
	
	div.append(content);
	
	BootstrapDialog.confirm({
		message: div,
		draggable: true,
		title: '',
		type: BootstrapDialog.TYPE_WARNING,
		closable: true,
		callback: function(result) {
			if (result == true)
				callback();
		},
	}).getModalHeader().css('background-color', '#442080');
}

function confirmDelete(msg, url) {
	if(confirm(msg)){
		if(confirm("Are you really sure? ")){
			$.ajax({
				type: 'GET',
				url: url,
				success: function() {
					location.reload();
				}
			}).getModalHeader().css('background-color', '#442080');
		}
	}
}
