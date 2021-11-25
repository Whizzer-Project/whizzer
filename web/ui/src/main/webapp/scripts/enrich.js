//<![CDATA[
	$(function(){
	 var deleteEnrich = {}
	 var editEnrich = undefined
	 
		if ([$enrich.button.add].includes(true)){
			deleteEnrich.url = "enrich/{rowid}/delete"
			deleteEnrich.confirm = $$messages["enrichRules.confirm.delete"] 
			deleteEnrich.callBack = function(url){getPageByUrl(url, '#enrich_rules')}
			editEnrich = {
					url: "enrich/{rowid}/edit",
					callBack: function(url){getPageByUrl(url, '#enrich_rules')},
			}
		}
		
			var table = $('<table>').addClass("fintpTable display").css('width', '100%')
			$('#tableWrapperEnrich').append(table);
			table.datatablesInit({
				actions: {
					get: 'enrich/page?templateId=' + $('#template').val(),
					edit: editEnrich,
					view: {
						 dialog:{
		                    	title : $enrich.validation.condition,
		                    },
		                 url: 'enrich/{rowid}/view', 
		                 viewBtn : function viewBtn(row){
		                        return !jQuery.isEmptyObject(row.conditions);
		                 }
					},
					delete: deleteEnrich,
				},
				sortColumn: 0,
				columns: [
					{ "title": $enrich.table.name, "data": "name"},
					{ "title": $enrich.txfield, "data": "txField",
						"render":function ( data, type, row, meta ) {
							return (data!==null?data.split('/').join('/ '):"");
						},
					},
					{ "title": $enrich.busslist, "data": "bussList"},
					{ "title": $enrich.busslistfield, "data": "bussListField"},
					{ "title": $enrich.global.mandatory, "data": "mandatory", 
						"name":  {"filterType": "boolean"}, 
						"className" : 'dt-body-center',
						"headerExtra": {
							"data-filter": "checkbox",
							"data-datasource": '["true","false"]'
						},
						"render":function ( data, type, row, meta ) {
							return (data===true?'<span class="glyphicon glyphicon-ok" style="color:green"/>':"");
							},
					},
					{ "title": $enrich.txtype, "data": "txType", "visible":false},
				]
			});
			
			$("#addEnrich").click(function(){
				getPageByUrl("enrich/add?templateId=" + $('#template').val(), '#enrich_rules')
			})
	})
	//]]>