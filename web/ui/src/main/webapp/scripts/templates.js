//<![CDATA[

$(function(){
		function readTemplateValue(operation){
			var template = {};
			template.id = $('#id').val();
			template.txtemplatesconfig = {};
			template.txtemplatesdetaileds = fields;
			template.name= $("#templateName").val();
			var messageType = $('#messageType').select().val();
			if (!messageType){
				showError($('#messageType'), 'field required')
				return false;
			}else{
				hideError($('#messageType'))
			}
			template.txtemplatesconfig.id = messageType 
			template.type = $('input[name="type"]:checked').val();
	
			template.txtemplatesgroups = [];
			$("#multiselect_to > option").each(function(){
				var id = (this.id == 'undefined' ? null:(this.id == ''?null: this.id));
				template.txtemplatesgroups.push({groupid: this.value, id: id});
			});
	
			$.ajax({
				type: "POST",
				url: operation,
				data: {template: JSON.stringify(template)},
				dataType: 'json',
				beforeSend: function(xhr){
					var csrfToken = $('#_csrf').attr("content");
					var csrfHeader = $('#_csrf_header').attr("content");
		            xhr.setRequestHeader(csrfHeader, csrfToken);
		            showLoader()
			    },
				success: function(data) {
					location.reload();
				},
				error: function(data) {
					handleAPIError(data, $('form'));
				},
				complete: function(){
					hideLoader()
				}
			});
		}
		
		var table = $('<table >').addClass("fintpTable display").css("width", "100%");
		$('.table-wrapper').append(table);
		var fields = [];
		
		function checkValidity(){
			var rezult = []
			$("#xpathsArea").find("[id]").each(function(){
				var pattern = $(this).attr('pattern');
				if (this.nodeName == 'INPUT' && 'hidden' != this.getAttribute('type')){
						rezult.push(checkPattern($(this), pattern));
				}else if (this.nodeName == "SELECT"){
					rezult.push(checkPatternSelect($(this), pattern));
				}
				var id = $(this).attr('idd')
				id = id?id: null
				var field = {value: $(this).val(), id: id,txtemplatesconfigdetailed:{id:$(this).attr("data_id"), fieldid:$(this).attr("data_id")}};
				fields.push(field);
			})
			return rezult
		}
		
		var beforeAction = function BeforeCopyAction(){
			if (!$('#template_name').val().trim()){
				$('#template_name').parent().append('<span class=erroMessage>Can\'t be empty</span>')
				return true;
			}else{
				$('#template_name').parent().children().last().remove()
				return false;
			}
		}
		
		var copyAction = function copyTemplates(){
			$.ajax({
				type:'post',
				url: 'templates/copy',
				data: {
					id: $('#id').val(),
					name: $('#template_name').val(),
				},
				dataType: 'json',
				beforeSend: function(xhr){
					var csrfToken = $('#_csrf').attr("content");
					var csrfHeader = $('#_csrf_header').attr("content");
		            xhr.setRequestHeader(csrfHeader, csrfToken);
				},
				success: function(data){
					location.reload()
				},
				error: function(err){
					handleErrorList(err, 'form')
				}
				
			})
		}
		var button = {}
		button.add = {
				dialog:{
					title: "",
					size: 'wide', // we have 4 type of dialog normal -
									// default, wide, large and small see
									// dialog.js getDialogSize
				},
				url: 'templates/add',
				label: $$label,
				before: function(){
					var rezult = [];
					fields = []
					var max = $('#templateName').attr('pattern');
					rezult.push(checkPattern($('#templateName'), max));
					Array.prototype.push.apply(rezult, checkValidity())
					// $("#xpathsArea").find("[id]").each(function(){
					// if (this.nodeName == 'INPUT' && 'hidden' != this.getAttribute('type')){
					// var pattern = $(this).attr('pattern');
					// if (undefined != pattern){
					// rezult.push(checkPattern($(this), pattern));
					// }
					// }else if (this.nodeName == "SELECT"){
					// rezult.push(checkPatternSelect($(this), pattern));
					// }
					// var id = $(this).attr('idd')
					// id = id?id: null
					// var field = {value: $(this).val(), id:
					// id,txtemplatesconfigdetailed:{id:$(this).attr("data_id"),
					// fieldid:$(this).attr("data_id")}};
					// fields.push(field);
					// })
					return rezult.includes(false);
				},
				
			}
		
		button.edit = {
				url: 'templates/{rowid}/edit',
				before: function(){
					var rezult = checkValidity()
					// $("#xpathsArea").find("[id]").each(function(){
					// var pattern = $(this).attr('pattern');
					// if (undefined != pattern){
					// var reg = new RegExp(pattern);
					// if(false == reg.test($(this).val())){
					// if ($(this).parent().children().length > 1){
					// $(this).parent().children('span').remove();
					// }
					// $(this).parent().append('<span class="errorMessage">required field</span>');
					// window.rezult = false;
					// }
					// }
					// var id = $(this).attr('idd')
					// id = id?id: null
					// var field = {value: $(this).val(), id:
					// id,txtemplatesconfigdetailed:{id:$(this).attr("data_id"),
					// fieldid:$(this).attr("data_id")}};
					// fields.push(field);
					// })
					return rezult.includes(false);
				}
			}
		button.delete = {
				url: "templates/{rowid}/delete",
				confirm: $$messages["template.confirm.delete"] 
		}
		button.copy = {
				url: "templates/{id}/copy",
				saveText: 'Copy',
				before: beforeAction,
				saveAction: copyAction,
				dialog: {title: "Copy"}
		}
		
		if ($$addButtonView == 'false'){
			button.add = {};
			button.delete = {}
			button.copy = {}
			$$actionUpdate = undefined
		}
		
		table.datatablesInit({
			actions: {
				add: button.add,
				get: 'templates/page', 
				edit: button.edit,
				copy: button.copy,
				delete: button.delete,
			},
			saveAction:function(){
				readTemplateValue("templates/insert");
			},
			editAction:function(){
				readTemplateValue("templates/update");
			},
			columns: [ 
				{ "title": $$name, "data": "name" },
				{ "title": $$type, "data": "type" ,
					"name" :{"filterType":"number"},
					"className" : 'dt-body-center change-box',
					"headerExtra": {
						"data-filter": "dropdown",
						"data-datasource": '{"a" : ["Simple", "Multiple"]}',
					},
					render: function ( data, type, row, meta ) {
						return (row.type=="0"?'Simple':"Multiple");
					}
				},
			],
			
		});
		$('#multiselect').multiselect();
	});
//]]>
