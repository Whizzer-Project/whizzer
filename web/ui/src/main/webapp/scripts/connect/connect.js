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
