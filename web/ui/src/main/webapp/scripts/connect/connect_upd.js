$(function() {
	var deleteMessage = undefined;

	$('#DeleteConsent').click(
			function() {
				deleteMessage = $$messages["consentEntity.confirm.delete"];
				if (deleteMessage !== undefined && confirm(deleteMessage)) {
					$.ajax({
						type : 'POST',
						url : "./consent/" + $('#id').val() + "/delete",
						beforeSend : function(xhr) {
											xhr.setRequestHeader($('#_csrf_header').attr("content"), $('#_csrf')
													.attr("content"));
										},
						}).success(function(body) {
							console.dir(body)
								if (body == '200') {
									$('#consentId').val('');
									$('#consentExpir').val('');
								}
						}).error(function(body){
							console.dir(body)
						});
				}
			})
	$('#UpdateConsent').click(function() {
		$.ajax({
			type : 'GET',
			url : "./consent/" + $('#id').val() + "/update",
		// beforeSend: function(xhr){
		// xhr.setRequestHeader($('#_csrf_header').attr("content"),
		// $('#_csrf').attr("content"));
		// },
		}).success(function(body) {
			var response = $.parseJSON(body);
			if (null == response.consentId){
				alert('Error update consents')
			}else{
				$('#consentId').val(response.consentId);
				$('#consentExpir').val(response.validUntil);
			}
		}).error(function(data, status, headers, config) {
			console.dir(data)
		});
	})

	/* delete token */

	$('#DeleteToken').click(function() {
		deleteMessage = $$messages["token.confirm.delete"];
		if (deleteMessage !== undefined && confirm(deleteMessage)) {
			$.ajax({
				type : 'GET',
				url : "./token/" + $('#id').val() + "/delete",
			}).success(function(body) {
				if (body == "200") {
					$('#token').val('');
					$('#tokenExpir').val('');
				}
			});
		}
	})
	$('#UpdateToken').click(function() {
		$.ajax({
			type : 'GET',
			url : "./get_redirect?id=" +  $('#id').val(),
		}).success(function(body) {
			console.log(body);
			window.open(body);
		});
	});
})
