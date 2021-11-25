function renderControl(json) {
	var control = null;
	
	if (typeof json === "undefined" || typeof json.type === "undefined")
		return control;
	
	switch (json.type) {
		case "string" :
		case "number":
		case "date" :
		case "time":
		case "datetime" :
		case "datetime,datetime" :
		case "edatetime" :
		case "date,date" :
			control = $('<input type="text" class="form-control">');
			break;
		case "dropdown" :
		case "multiselect" :
			control = $('<select  class="form-control"/>');
			if (json && json.datasource){
				if(json.datasource instanceof Array)
					//array
					$.each(json.datasource, function(key, value) {
						//array
						control.append('<option value="' + value + '">' + value + '</option>');
					});
				else if (json.datasource instanceof Object && !json.datasource.a)
					$.each(json.datasource, function(key, value) {
						//Object 
						control.append('<option value="' + key + '">' + value + '</option>');
					});
				else
					$.each(json.datasource.a, function(key, value) {
								//Object with one key a 
						control.append('<option value="' + key + '">' + value + '</option>');
					});
			}
			break;
		case "checkbox":
			//control = $('<p/>');
			if(json.datasource instanceof Array)
				$.each(json.datasource, function(key, value) {
					//array
					if (control == null){
						var str = '<input class="" type="checkbox" placeholder="" value="' + value + '">' + value;
						control = $(str);
					}else{
						control.after('<input class="" type="checkbox" placeholder="" value="' + value + '">' + value);
					}
				});
			else
				$.each(json.datasource, function(key, value) {
							//map
					if (control == null){
						var str = '<input class="form-control" type="checkbox" placeholder="" value="' + key + '">' + value;
						control = $(str);
					}else{
						control.after('<input class="form-control" type="checkbox" placeholder="" value="' + key + '">' + value);
					}
				});
			break;

		case "number,number" :
			control = $('<input class="form-control" type="text" placeholder="Minimum"/>')
						.add($('<input type="text" placeholder="Maximum"/>'));
			break;
		default :
			console.log('not implemented');
	}
	
	//extra class or attributes
	switch(json.type) {
	case "date" :
		control.addClass('datePicker');
		break;
	case "time" :
		control.addClass('timePicker');
		break;
	case "datetime" :
		control.addClass('dateTimePicker');
		break;	
	case "datetime,datetime" :
		control.addClass('intervalPicker');
		break;
	case "edatetime" :
		control.addClass('eventIntervalPicker');
		break;
	case "date,date" :
		control.addClass('intervalDatePicker');
		break;
	case "number" :
	case "number,number" :
		control.addClass('form-amount');
		break;
	case "dropdown" :
		control.prepend('<option></option>')
		break;
	case "multiselect" :
		control.attr("multiple", "multiple");
		break;
	case "checkbox" :
//		$.each(json.datasource, function(key, value) {
//			$(control[key]).val(value);
//		});
		break;
	}
	
	//deselect bad selection???
	switch(json.type) {
	case "dropdown" :
	case "multiselect" :
		control.find('option:selected').prop('selected', false);//???
		break;
	}
	
	return control;
}