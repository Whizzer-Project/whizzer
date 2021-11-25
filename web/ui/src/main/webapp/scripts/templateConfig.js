/**
 * 
 */

function getPageByUrl(url, id_element){
	$.ajax({
		method: 'get',
		url: url,
		beforeSend: function(){
			showLoader()
		},
		success: function(data){
			$(id_element).empty()
			$(id_element).append(data);
		},
		complete: function(){
			hideLoader()
		}
	})
}

function getAllTabRecords(){
	if ($('#template').val()!== undefined && $('#template').val()!==""){
		getPageByUrl("enrich/view", '#enrich_rules')
		getPageByUrl("validations/view", "#validation_rules");
		getPageByUrl("edit-rules/view", "#edit_rules");
		getPageByUrl("view", '#config_detailed')
	}
}

$(function() {

	$("section").each(function(item){
		$(this).append("<input type='radio' name='test' class='form-check-input' id='check"+item+"' />")
	})

	$("select").change(function() {
		var id = $(this).val();
//		console.log(document.location.origin + '/' + document.location.pathname  + '/' + id + document.location.search)
//		document.location = document.location.origin + '/' + document.location.pathname  + '/' + id + document.location.search
		if (id > 0) {
			var newpath = getPathWithOutLastParam(window.location.pathname, window.location.origin)
			newpath += '/' + id + document.location.search
			//document.location = newpath
			window.location.assign(newpath)
		}
	});
	
	function getPathWithOutLastParam(pathname, origin){
		var path = pathname.split('/')
		var len = path.length - 1
		if (!path[len].includes('config')){
			len -= 1
		}
		for(var i=1; i<= len; i++){
			origin += '/' + path[i]
		}
		return origin;
	}
	
	getAllTabRecords()
	
	$("label, section").css({
		border : "1px solid pink"
	});
	
});
