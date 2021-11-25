/**
 * http://usejsdoc.org/
 * 
 */

var FIRSTOPTIONVALUE = '----------'
	
Date.prototype.myGetDate = function(){
	if (this.getDate()<10){
		return '0' + this.getDate()
	}
	return this.getDate()
}

Date.prototype.myGetHours = function(){
	if (this.getHours()<10){
		return '0' + this.getHours()
	}
	return this.getHours()
}

Date.prototype.myGetMinutes = function(){
	if (this.getMinutes()<10){
		return '0' + this.getMinutes()
	}
	return this.getMinutes()
}

Date.prototype.myGetSeconds = function(){
	if (this.getSeconds()<10){
		return '0' + this.getSeconds()
	}
	return this.getSeconds()
}

Date.prototype.myGetMonth = function(){
	if (this.getMonth()<9){
		return '0' + (this.getMonth() + 1)
	}
	return (this.getMonth() + 1)
}
	
String.prototype.isEmpty = function(){
	if (0  == this.trim().length){
		return true
	}
	return false
}

// $$field_compare need to be global
function compareObjectByString(obj1, obj2){
	const field1 = obj1[$$field_compare].toUpperCase()
	const field2 = obj2[$$field_compare].toUpperCase()
	
	if (field1 > field2)
		return 1
	else if (field1 < field2)
		return -1
	else return 0
}

function compareObject(obj1, obj2){
	const field1 = obj1[$$field_compare]
	const field2 = obj2[$$field_compare]
	
	if (field1 > field2)
		return 1
	else if (field1 < field2)
		return -1
	else return 0
}

function hideError(elem){
	if (elem){
		if (elem.parent().children().length > 1){
			elem.parent().children('span').remove();
		}
	}
}

function showError(elem, message="mandatory field"){
	if (elem){
		if (elem.parent().children().length > 1){
			elem.parent().children('span').remove();
		}
		elem.parent().css('display', 'block')
		if ('hidden' == elem.attr('type')){
			elem.attr('type','text')
		}
		elem.parent().append('<span class="errorMessage">' + message + '</span>');
		return false;
	}
	return null;
}

function getMandatory(elem){
	if (elem.attr('required'))
	{
		if (elem.attr('required')=='false'){
			return false
		}
		return Boolean(elem.attr('required'))
	}	
	if(elem.hasOwnProperty('required')){
		if (elem.hasOwnProperty('required')=='false'){
			return false
		}
		return Boolean(elem.hasOwnProperty('required'))
	}
	if (elem.hasOwnProperty('mandatory')){
		if (elem.hasOwnProperty('mandatory')=='false'){
			return false
		}
		return Boolean(elem.hasOwnProperty('mandatory'))
	}
	if (elem.attr('mandatory')){
		if (elem.attr('mandatory')=='false'){
			return false
		}
		return Boolean(elem.attr('mandatory'))
	}
	return false
}

function changePattern(pattern){
	return pattern.replace('[-]?', '([-]?)')
	
}

function checkPattern(elem, pattern){
	var reg = new RegExp((pattern));
	var mandatory = getMandatory(elem)
	var value = replaceNonAlphaNumericChar(elem.val(), '');
	if (mandatory){
		if (value.isEmpty()){
			return showError(elem, 'mandatory field')
		}
	}
	if((mandatory || 0 < value.trim().length) && false == reg.test(value)){
		return showError(elem, 'required field')
	}
	hideError(elem)
	return true;
}

function checkPatternSelect(elem, pattern){
	var reg = new RegExp((pattern));
	var mandatory = getMandatory(elem)
	var value = replaceNonAlphaNumericChar(elem.find(' option:selected').text(), '')
	if (mandatory){
		if (value.isEmpty()){
			return showError(elem, 'mandatory field')
		}
	}
	
	if((mandatory || 0 < value.trim().length) && false == reg.test(value)){
		if (elem.parent().children().length > 1){
			elem.parent().children('span').remove();
		}
		return showError(elem, 'required field')
	}
	hideError(elem)
	return true;
}

var regExpId = /\W/g  // regular expresion select all non alpha numeric
						// characters

function replaceNonAlphaNumericChar(value, replace_value = '_'){
	return value.replaceAll(regExpId, replace_value)
}

function getAttributesByValue(element){
	var attribute = {};
	attribute.name = element.fieldxpath;
	attribute.id = replaceNonAlphaNumericChar(element.fieldlabel).toLowerCase()+element.item
	attribute.data_id = element.id;
	attribute.pattern = changePattern(element.pattern);
	attribute.mandatory = element.mandatory;
	attribute.label = element.fieldlabel;
	attribute.type = element.visible?"text":"hidden"
	if (element.txtemplateconfigoption == null){
		attribute.conditions = JSON.stringify(element.conditions)
	}
	return attribute
}

function makeValueByPattern(value, pattern){
	var reg = new RegExp(pattern)
	return value.match(reg)
}

function addValueIfNotExist(value, addValue){
	if ("" == value){
		return addValue
	}
	return value
}

function getDateTimeValueFromSequence(sequence){
	// s:3+d:YYMM... or  YYYYMMDDHHmmss...
	
	var stringValue = '';
	var dateTimeSequence;
	
	if(sequence.indexOf("js:") !== -1){
		var jsCode = getStringAfterSpecificChar(sequence, "js:");
		return new Function(jsCode)();
	}else{
		if(sequence.indexOf("+") !== -1){
			var fields = sequence.split('+');
			if(fields[0].indexOf("s:") !== -1){
				stringValue = getStringAfterSpecificChar(fields[0], ":");
				dateTimeSequence = getStringAfterSpecificChar(fields[1], ":");
			}else if(fields[0].indexOf("d:") !== -1){
				stringValue = getStringAfterSpecificChar(fields[1], ":");
				dateTimeSequence = getStringAfterSpecificChar(fields[0], ":");
			}
		}else{
			dateTimeSequence = sequence;
		}
	}	

	var dateTime = moment().format(dateTimeSequence);
	
	return stringValue + dateTime;
}

function getStringAfterSpecificChar(value, char){
	return value.substring(value.indexOf(char) + 1);
}

function createHTMLElementAndDependency(element, value ){
		var edit;
		var attribute = getAttributesByValue(element)
		var options = element.txtemplatesconfigoption
// const multipleTab = element.item
		if ('' == value || undefined == value) {
			value = element.fieldvalue;
		}
		// attribute.value = value;
		var optionId = -1;
		var dataSource = element.fieldtype;
		if (null != options) {
			dataSource = options.datasource;
			optionId = options.id;
		} else {
			optionId = element.busslist.toLowerCase()
		}
		edit = $("<input type='text' class='form-control'></input>");
		var data = new Date()
		var dateTimeValue = data.getFullYear() + '-' + (data.myGetMonth()) + '-' + data.myGetDate() + 'T' + data.myGetHours() + ':' +data.myGetMinutes() + ':' + data.myGetSeconds()
		var dateValue = dateTimeValue.split(' ')[0]
		switch (dataSource.trim()) {
			case 'date':
				edit.addClass("datePicker")
				edit.val(addValueIfNotExist(value, dateValue))
				break;
			case 'timestamp':
				edit.addClass("dateTimePicker")
				edit.val(addValueIfNotExist(value, dateTimeValue))
				break
			case 'sequency':				
				edit.val(addValueIfNotExist(value, getDateTimeValueFromSequence(options.description)));
				break;
			case 'fixvalue':
				edit.val(value)
				break
			case 'editvalue':
			case 'visible':
				if (attribute.type == 'hidden'){
					edit.val(addValueIfNotExist(value, 
							makeValueByPattern(data.myGetSeconds() +  '' + data.myGetMinutes() + '' + data.myGetHours() + '' + data.myGetDate() + '' + (data.myGetMonth()) + '' + data.getFullYear(), attribute.pattern)
							))
				}
			break;
			default:
				edit = $("<select style='width:auto;' class='form-control' data-type=''><option>" + FIRSTOPTIONVALUE + "</option></select>");
				var setOptions = new Set()
				$$field_compare = element.busslistfield
				$$dropDowns[optionId].sort(compareObjectByString).forEach(ent => {
					const returnCheck = []
					let logicalCondition = 'AND'
					const conditions = JSON.parse(attribute.conditions)
					if (conditions.hasOwnProperty('rules')){
						logicalCondition = conditions.condition
						conditions.rules.forEach(function(rule){
							var master
							if (rule.value.indexOf('rule.', 0) == 0){
								master = replaceNonAlphaNumericChar(rule.value.replace("rule.","")).toLowerCase() + element.item
								var obj = {}
								obj.id = attribute.id
								obj.operator = rule.operator
								obj.optionId = optionId
								obj.fieldValue = element.busslistfield
								obj.checkField = rule.field.split('-')[1]
								if(dependency.hasOwnProperty(master)){
									dependency[master].push(obj)
								}else{
									dependency[master] = Array()
									dependency[master].push(obj)
								}
								removeDuplicateFromDependency()
							}else if (rule.value.indexOf('text.',0) == 0 ){
								const text = rule.value.replace('text.','').toLowerCase()
								let check = false
								switch(rule.operator){
								case 'equal':
								case 'contains':
									check = true
									break
								}
								if (rule.operator.includes('equal')){
									const field = rule.field.split('-')[1]
									if ((ent[field].toLowerCase() === text) === check)
										returnCheck.push(true)
									else
										returnCheck.push(false)
								}
								if (rule.operator.includes('contain')){
									if ((ent[field].toLowerCase().includes(text)) === check)
										returnCheck.push(true)
									else
										returnCheck.push(false)
								}
							}
						})
						
					}
					var option
					if (!setOptions.has(ent[element.busslistfield])){
						if (value == ent[element.busslistfield]){
							option = new Option( value, value,true, true)
						}else{
							option = new Option(ent[element.busslistfield], ent[element.busslistfield])
						}
						if (ent.hasOwnProperty('defaultAccount') && element.busslistfield.toUpperCase() == 'iban'){
							if (ent['defaultAccount'].toUpperCase() == 'Y'){
								$(option).attr('default_value', true)
							}
						}
						if(returnCheck.length == 0 ){
							edit.append(option)
							setOptions.add(ent[element.busslistfield])
						}else
						{
							if (logicalCondition.toUpperCase() === "OR" && returnCheck.includes(true)){
								edit.append(option)
								setOptions.add(ent[element.busslistfield])
							}
							if (logicalCondition.toUpperCase() === "AND" && !returnCheck.includes(false)){
								edit.append(option)
								setOptions.add(ent[element.busslistfield])
							}
						}
					}
					
				})
//				selectFirstAndSingleValue(edit)
				edit.change(function(){
					const master = this.attributes.id.value
					let text = $(this).find("option:selected").text()
					if (dependency.hasOwnProperty(master)){
						dependency[master].forEach(function(master_value){
							const obj = $('#'+ master_value.id)
							if (!$(obj).attr('conditions')){
								return
							}
							const conditions = JSON.parse($(obj).attr('conditions'))
							var setOptions = new Set()
							const items = master_value.optionId
							$$dropDowns[items].forEach(ent => {
								const rezultCheck = []
								conditions.rules.forEach(rule =>{
									let check = false
									switch (rule.operator){
									case 'contain': 
									case 'equal': 
										check = true
										break
									}
									const field = rule.field.split('-')[1]
									if (rule.value.indexOf('text.', 0) == 0){
										messChecked  = rule.value.replace('text.','')
									}
									else if (rule.value.indexOf('rule.', 0) == 0){
										messChecked = text
									}
									if (rule.operator.includes('equal')){
										if ((ent[field] == messChecked) == check){
											rezultCheck.push(true)
										}else{
											rezultCheck.push(false)
										}
									}else{
										if (ent[field].includes(messChecked) == check){
											rezultCheck.push(true)
										}else{
											rezultCheck.push(false)
										}
									}
								})
								if (!setOptions.has(ent[master_value.fieldValue])){
									if( conditions.condition.toUpperCase() == 'AND' && !rezultCheck.includes(false)){
										elementShowOrHide(obj, ent[master_value.fieldValue], 'block')
										setOptions.add(ent[master_value.fieldValue])
	// obj.append(new Option(ent[value.fieldValue], ""))
									}else if(conditions.condition.toUpperCase() == 'OR' && rezultCheck.includes(true)){
										elementShowOrHide(obj, ent[master_value.fieldValue], 'block')
										setOptions.add(ent[master_value.fieldValue])
									}else{
										elementShowOrHide(obj, ent[master_value.fieldValue], 'none')
									}
								}
							})
							selectFirstAndSingleValue(obj, value)
						})
						
					}
				})
		}
		
		//selectFirstAndSingleValue(edit, value)
		
		if (edit != undefined) {
			for (const key in attribute) {
				edit.attr(key, attribute[key]);
			}
			edit.css({'display': " inline-block", width : "60%"});
			if (null != options && options.datasource == 'editvalue') {
				edit.removeAttr('disabled');
			}
		}
		// elementsId.push(attribute['id']) put after return edit
		return edit;
	}

function removeDuplicateFromDependency(){
	
	for (const [ , value] of Object.entries(dependency)){
		for (var item = 0; item < value.length; item++){
			var objFirst = value[item]
			for (var ind = item + 1; ind < value.length; ind++){
				var objSecond = value[ind]
				var checking = []
				for (const [n_key, n_value] of Object.entries(objSecond)){
					if (objFirst.hasOwnProperty(n_key) && objFirst[n_key] == n_value){
						checking.push(true)
					}
					else{
						checking.push(false)
						break
					}
				}
				if (!checking.includes(false)){
					value.splice(ind, 1)
					ind--
				}
			}
		}
	}
	
}

function selectFirstAndSingleValue(obj, value){
	obj.children()
	.filter(function(){
		return window.getComputedStyle(this).display == 'none'
	})
	.removeAttr('selected')

	var active_element = obj.children()
							.filter(function(){
								return window.getComputedStyle(this).display != 'none' && this.value != FIRSTOPTIONVALUE
							})
							.length
	if (active_element == 1){
		obj.children()
		.filter(function(){
			return window.getComputedStyle(this).display != 'none' && this.value != FIRSTOPTIONVALUE
		})
		.last()
		.attr('selected', 'selected')
		.change()
	}else if (undefined != value){
			obj.children().filter(function(){
				return window.getComputedStyle(this).display !== 'none' && this.value == value
			})
				.first()
				.attr('selected', 'selected')
				.change()
	}else{
		if (obj.children().filter(function(){
				return this.getAttribute('default_value') && window.getComputedStyle(this).display !== 'none'
			}).length == 1){
			obj.children().filter(function(){
				return this.getAttribute('default_value') && window.getComputedStyle(this).display !== 'none'
			})
			.attr('selected', 'selected')
			.change()
		}
	}
}

function elementShowOrHide(parentElement, whyFind, whatToDo){
	parentElement.find('option').each(function(){
		if ($(this).text().trim() == whyFind.trim()){
			$(this).css('display',whatToDo)
			return false
		}
	})
}

function getLastNYear(n, selectedYear){
	let currentYear = new Date().getFullYear();  
	return getLastNYearMax(n, selectedYear, currentYear)
}

function getLastNYearMax(n, selectedYear, maxYear){
	let data = [];
	for (let i = 1; i <= 10; i++, maxYear-- ) {
		data.push({
 			label:maxYear,
 			title:maxYear,
 			value:maxYear,
 			selected:(selectedYear==maxYear)
 		})
 	}
	return data;
}
