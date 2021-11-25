$(function(){
	hideAll();
	
	$('#templates').change(function(){
		var sel = $('#payments').val(); 
		dependency = {}
//		allElementId = {}
		switch (sel){
		case '1': 
			getSimple($(this).val(), $('#templates option:selected').text());
			break;
		case '2': 
			getTemplate($(this).val());
			$('#no').show();
			break;
		case '3': 
			$('#no').hide();
			getTemplate($(this).val());
			break;
		}
	})
	
	$('#elementShowHide').click(function(){
				$('#tabContent > div > div').each(function() {
					if (window.getComputedStyle(this).display == 'none'){
						$(this).show()
						$(this).children().each(function(){
							if (this.tagName == 'INPUT'){
								this.setAttribute('type','text')
							}
						})
					}
				})
			})
	
	$('#no').change(function(){
		var mult = ($(this).val());
		var el = $('#ulTab').children().length;
		if (mult - el > 0){
			multipliction($('#ulTab'), mult, el);
		}else{
			diminution($('#ulTab'), el - mult);
		}
	})
	
	$('#payments').change(function(){
		paymentType = $(this).val();
		hideAll();
		if ($(this).val() == 1 ){
			paymentSimple();
		}else if($(this).val() == 2 || ($(this).val() == 3 )){
			paymentComplex($(this).val());
		}
	});
	

	$('#createPayment').click(function() {
		window.savePoint = 0
		$('#container > ul > li').each(function() {
			var id = $(this).children().attr('href')
			const xml = []
			const rezult = []
			$(id).find('[id]').each(function() {
				var info = {}
				info.fieldxpath = this.name
				info.id = this.getAttribute('data_id')
				var pattern = $(this).attr('pattern')
				var mandatory = $(this).attr('mandatory')
				switch ((this.nodeName).toLowerCase()) {
				case 'input':
					info.fieldvalue = $(this).val()
					if (pattern.trim().length || mandatory )
						rezult.push(checkPattern($(this), pattern));
					break;
				case 'select':
					info.fieldvalue = $(this).find(' option:selected').text()
					if(pattern.trim().length || mandatory){
						rezult.push(checkPatternSelect($(this), pattern));
					}
					break
				default:
					break
				}
				xml.push(info)
			})
			window.savePoint++;
			if (rezult.includes(false)) {
				$(this).click()
				$(this).change()
			}else{
				savePayload(xml, this);
			}
			
		})
	});
})
var savePoint = 0;
var paymentType = -1;
var dependency = {}

function diminution(el, count) {
	for (var i = 0; i < count; i++) {
		el.children().last().remove();
	}
}

function multipliction(el, val_new, val_cur) {
	var mult = el.children().last().children();
	var init_title = el.children().first().children()
	
	var title = (init_title.text())
	
	for (var i = val_cur; i < val_new; i++) {
		var newElement = mult.clone();
		var newTitleLi = (title + (i + 1));
		newElement.text(newTitleLi);
		var newTitleHref = replaceNonAlphaNumericChar(newTitleLi.toLowerCase()).toLowerCase()
		var href = '#' + newTitleHref;
		newElement.attr('href', href);
		var li = $('<li></li>').append(newElement);
		el.append(li);
		var div = $('#' + replaceNonAlphaNumericChar(title.toLowerCase()).toLowerCase()).clone().attr('id', newTitleHref);
		div.removeClass('active in');
		$('#tabContent').append(div);
	}
}

function hideAll() {
	$('#div_sample').hide();
	$('#div_template').hide();
	$('#container').hide();
	$('#no').hide();
}

function getSimple(id, text) {
	$.ajax({
		type : "GET",
		url : "payment-create/" + id + '/config',
		dataType : "json",
		beforeSend : function() {
			showLoader()
		},
		success : function(data) {
			$('#container').show();
			$('#ulTab').empty();
			$('#tabContent').empty();
			$('#ulTab').append(
					'<li class="active"><a data-toggle="pill" href="#'
							+ data.messagetype + '"> new ' + text + '</a></li>');
			var divContent = $('#tabContent');
			var bloc = $('<div id="' + data.messagetype
					+ '" class="tab-pane fade in active"></div>');
			var width = screen.width / 3;
			$.each(data, function(item, txtemplatesconfigdetaileds) {
				txtemplatesconfigdetaileds.item = ""
				var element = createHTMLElementAndDependency(txtemplatesconfigdetaileds);
				if (element != undefined) {
					display = "block"
					if (element.attr('type')=="hidden")
						display = 'none'
					const div = $('<div style="margin-bottom:5px;width:' + width+ 'px;"></div>').append(
								'<div style="display:inline-block;width:25%">'
									+ txtemplatesconfigdetaileds.fieldlabel
								+ '</div>').append(element);
					div.css('display', display)
					bloc.append(div);
					divContent.append(bloc);
				}
			})
			registerDateTime();
			applyChange()
		},
		complete : function() {
			hideLoader()
		}
	})
}

function getTemplate(id) {
	$.ajax({type : "GET",
				url : "payment-create/" + id + '/template',
				dataType : "json",
				beforeSend : function() {
					showLoader()
				},
				success : function(data) {
					$('#container').show();
					$('#ulTab').empty();
					$('#tabContent').empty();
					var width = screen.width / 3;
					$.each(data,function(items, detail) {
						var activ = ""
						if (items == 0) {
							activ = "in active";
						}
						$('#ulTab').append('<li class="'
												+ activ
												+ '"><a data-toggle="pill" href="#'
												+ replaceNonAlphaNumericChar(detail.name).toLowerCase()
												+ '">'
												+ detail.name
												+ '</a></li>');
						var divContent = $('#tabContent');
						var bloc = $('<div id="'
								+ replaceNonAlphaNumericChar(detail.name).toLowerCase()
								+ '" class="tab-pane fade '
								+ activ + '"></div>');
						$.each(detail.txtemplatesdetaileds,function(item, obj) {
							obj.txtemplatesconfigdetailed.item = items
							var element = createHTMLElementAndDependency(
									obj.txtemplatesconfigdetailed,
									obj.value);
							if (element != undefined) {
								display = "block"
								if (element.attr('type')=="hidden")
									display = 'none'
								div = $('<div style="margin-bottom:5px;width:'
												+ width
												+ 'px;"></div>').css('display', display)
										.append('<div style="display:inline-block;width:25%">'
														+ obj.txtemplatesconfigdetailed.fieldlabel
														+ '</div>')
										.append(element);
								bloc.append(div);
								
							}
						})
						divContent.append(bloc);
						
					});
					applyChange()
					registerDateTime();
				},
				complete : function() {
					hideLoader()
				}
			})
}

function applyChange(){
	$('#tabContent > div').each(function() {
		$(this).find('[id]').each(function() {
			$(this).change()
		})
	})
}

function paymentSimple() {
	$.ajax({
		type : "GET",
		url : "payment-create/templates",
		dataType : "json",
		beforeSend : function() {
			showLoader()
		},
		success : function(data) {
			$('#templates').empty().append('<option></option>');
			$.each(data, function(item, value) {
				$('#templates').append(
						'<option value="' + value.id + '">' + value.label
								+ '</option>');
			})
			$('#div_template').show();
		},
		complete : function() {
			hideLoader()
		}
	});
}

function paymentComplex(id) {
	$.ajax({
		type : "GET",
		url : "payment-create/" + id + "/sample",
		dataType : "json",
		beforeSend : function() {
			showLoader()
		},
		success : function(data) {
			$('#templates').empty().append('<option></option>');
			$.each(data, function(item, value) {
				$('#templates').append(
						'<option value="' + value.id + '">' + value.name
								+ '</option>');
			})
			$('#div_template').show();
		},
		complete : function() {
			hideLoader()
		}
	});
}

function savePayload(xmlObject, liElement) {
	$.ajax({
		type : "POST",
		data : {
			payload : JSON.stringify(xmlObject),
		},
		url : "payment-create/create-payload",
		//dataType : "json",
		beforeSend : function(xhr) {
			showLoader()
			var csrfToken = $('#_csrf').attr("content");
			var csrfHeader = $('#_csrf_header').attr("content");
            xhr.setRequestHeader(csrfHeader, csrfToken);
		},
		success: function(mess){
			console.dir(mess.status)
			window.savePoint--;
			console.log(window.savePoint)
			if (window.savePoint == 0) {
				location.reload();
			}
			
			var id = $(liElement).children().attr('href')
			$(id).remove()
			if ($(liElement).prev().is('li')){
				$(liElement).prev().children().click();
			}
			else if ($(liElement).next().is ('li')){
				$(liElement).next().children().click()
			}
			$(liElement).remove();
		},
		error: function(err){
			console.dir(err);
			console.dir(err.status)
		},
		complete: function (){
			hideLoader()
		},
	})
}
