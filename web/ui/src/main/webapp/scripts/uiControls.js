$(function(){
	registerUIControls();
});

function registerUIControls() {
	registerMultiSelect();
}

function registerMultiSelect() {
	$('select[multiple]').multiselect({
		buttonWidth: '80%',
		maxHeight: 200,
		enableFiltering: true,
		enableCaseInsensitiveFiltering: true,//am adaugat pentru filtru de la report
		//includeSelectAllOption: true
	});
}