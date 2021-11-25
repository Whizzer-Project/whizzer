/**
 * http://usejsdoc.org/
 */

function queryBuilderFilter(listsMetaData, ruleData, ruleDataField) {
	var myFilters = []
	$.each(listsMetaData, function(listName, value) {
		$.each( value, function(key, field) {
			//console.log(field)
			var filter = {
				id : listName + key,
				label : listName + "-" + field,
				field : listName + "-" + field,
				input : function(rule, name) {
					var $container = rule.$el.find('.rule-value-container');
					window.type = ''
					$container.on( 'click', '[name=' + name + '_2]', function() {
						if (window.type == 'xpath') {
							let nodeSelected = $("input[name='test']:checked")
									.parent()
									.children()
									.first()
									.attr("data-xsd2html2xml-xpath");
							if (rule.$el.find('.rule-value-container [name$=_2]')[0] != undefined && nodeSelected == undefined) {
								alert("Please select the radiobox on the left!")
								return false;
							} else {
								$(this).val( nodeSelected).trigger('change')
							}
						}
					});
					$container.on('change','[name=' + name + '_1]', function() {
						var h = '';
						window.type = ''
						switch ($(this).val()) {
						case 'xpath':
							window.type = 'xpath'
						case 'text':
							h = '<input type="text" class="form-control" name="' + name + '_2" value="" autocomplete="off"/>';
							break;
						case 'rule':
							h = '<select class="form-control" name="' + name + '_2">'
							h += '<option value="">-</option>'
								ruleData.forEach(function(val) {
									if (val[ruleDataField])
										h += '<option class="form-control" value="' + val[ruleDataField] + '">' + val[ruleDataField] + '</option>'
								})
							h += '</select>'
							break;
						
						}
	
						$container.find('[name$=_2]').html(h).toggle(!!h)
					});
	
					return '<select class="form-control" name="' + name + '_1"> \
						    <option value="-1">-</option> \
						    <option value="xpath">xpath</option> \
						    <option value="rule">rule</option> \
						    <option value="text">text</option> \
						  </select> \
						  <div name="' + name + '_2" style="display:none;" ></div>';
				},
				valueGetter : function(rule) {
					
					if(rule.data !== undefined && rule.data.filterXpath !== undefined){
						rule.$el.find("[data-type='xpathCondition']").val(rule.data.filterXpath).css("display","")
					}
					if (rule.$el.find('.rule-value-container [name$=_2]')[0] != undefined && rule.$el.find('.rule-value-container [name$=_2]')[0].childNodes[0] != undefined) {
						return rule.$el.find('.rule-value-container [name$=_1]').val() + '.' + rule.$el.find('.rule-value-container [name$=_2]')[0].childNodes[0].value;
					} else{
						return rule.$el.find('.rule-value-container [name$=_1]').val() + '.' + undefined
					}
				},
				valueSetter : function(rule, value) {
					
					if(rule.data !== undefined && rule.data.filterXpath !== undefined){
						rule.$el.find("[data-type='xpathCondition']").val(rule.data.filterXpath)
					}
					
					if (rule.operator.nb_inputs > 0) {
						var val = value.split('.');
	
						rule.$el.find('.rule-value-container [name$=_1]').val(val[0]).trigger('change');
						if (rule.$el.find('.rule-value-container [name$=_2]')[0] != undefined && rule.$el.find('.rule-value-container [name$=_2]')[0].childNodes[0] != undefined)
							rule.$el.find('.rule-value-container [name$=_2]')[0].childNodes[0].value = (val[1]);
					}
				}
			};
			myFilters.push(filter);
		});
	});
	return myFilters
}
