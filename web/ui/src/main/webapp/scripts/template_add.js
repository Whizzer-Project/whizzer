/**
 * 
 */
$(function() {
	dependency = {}  //global variable for save dependency generate by createElement

	if (template.type == 1) {
		$("#xpathsArea").toggle();
		$('#multiselect').multiselect();
		getTemplates = "/groups";
	} else {
		$("#typeMultiple").toggle();
		if (template.txtemplatesconfig) // draw html element
			drawFields(template.txtemplatesconfig.txtemplatesconfigdetaileds);
		
		
		//set value for html elemen
		$.each(template.txtemplatesdetaileds, function(item, detailed) {
			
			var templateDetailed = detailed.txtemplatesconfigdetailed;
			if (templateDetailed) {
				if ($('#'+replaceNonAlphaNumericChar(templateDetailed.fieldlabel).toLowerCase()).is('input')){
					$('#'+replaceNonAlphaNumericChar(templateDetailed.fieldlabel).toLowerCase()).attr('idd', detailed.id)
					$('#'+replaceNonAlphaNumericChar(templateDetailed.fieldlabel).toLowerCase()).val(detailed.value)
				}else if ($('#'+replaceNonAlphaNumericChar(templateDetailed.fieldlabel).toLowerCase()).is('select')){
					$('#'+replaceNonAlphaNumericChar(templateDetailed.fieldlabel).toLowerCase()).attr('idd', detailed.id)
					$('#'+replaceNonAlphaNumericChar(templateDetailed.fieldlabel).toLowerCase()).children().each(function(){
						if ($(this).val().trim() == detailed.value.trim()) {
							$(this).attr('selected', true);
							$(this).change()
						}
					})
				}
			}
			registerDateTime();
		})
	}

	$("input[type=radio]").change(function() {
		$("#typeMultiple").toggle();
		$("#xpathsArea").toggle();
		getTemplates = "/fields";
		if ($(this).val() == "1") {
			getTemplates = "/groups";
			$('#multiselect').multiselect();
		}
		var id = ($("#messageType").val() == '' ? 0 : $("#messageType").val());
		loadTemplateData(id);
	});

	$("#messageType").change(function() {
		if ($(this).val() != "") {
			loadTemplateData($(this).val())
		}
	})

	function loadTemplateData(id) {
		$("#xpathsArea").empty();
		$("#multiselect").empty();
		$("#multiselect_to").empty();
		$.ajax({
			type : "GET",
			url : "templates/" + id + getTemplates,
			dataType : "json",
			beforeSend : function() {
				showLoader();
			},
			success : function(data) {
				if (getTemplates == '/groups') {
					$.each(data, function(item, value) {
						$('#multiselect').append('<option value="' + value.id + '" >'+ value.name + '</option>');
					});
				} else {
					drawFields(data.fields)
					registerDateTime();
					$$dropDowns[2] = (data.internal);
				}
			},
			complete : function() {
				hideLoader()
			}
		});
	}
	
	function drawFields(getElements){
		$$field_compare = "id"
		getElements.sort(compareObject)
		$.each(getElements,function(item, obj) {
			obj.item = "" // emptry string to create id html element
			var edit = createHTMLElementAndDependency(obj, obj.value)
			edit.css('width', '100%')
			var display = obj.visible?'block':'none'
			var div = $("<div style='margin-bottom: 5px;display:flex;'></div").css('display', display)
			.append("<div style='float: left;width: 50%;'><span>"+ obj.fieldlabel +"</span></div>")
			.append($('<div style="float: left;width: 50%;"></div>').append(edit));
			$("#xpathsArea").append(div)
		})
	}
	
	function showActivePayer(){
		var payer = document.querySelector('#payer')
		console.dir(payer)
	}
});