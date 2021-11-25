$(document).ready(function() {
	var qs = window.location.search;
	qs = qs.split('+').join(' ');
	var params = {}, tokens, re = /[?&]?([^=]+)=([^&]*)/g;
	while (tokens = re.exec(qs)) {
		if (!params.hasOwnProperty(decodeURIComponent(tokens[1]))){
			params[decodeURIComponent(tokens[1])] = []
		}
		params[decodeURIComponent(tokens[1])].push(decodeURIComponent(tokens[2]));
	}

	// var url = new URL(window.location.href);
	// preluare date calenderistice din controller
	var today = new Date();
	var y = today.getFullYear();
//					var x = $('#id1').val();
	// console.log($('#id1').val())

	var rawDates = JSON.parse(allDates);// x.split(",");
	var selDates = [];
	
	var fp = "&fp="+params.fp.join("&fp=");
	params.year = params.year?params.year.join():null
	params.act = params.act?params.act.join():""
	// console.log(xx);

	// setare ani pentru dropdown
	var years = [];
	for (var i = 4; i > 0; i--) {
		years.push(y - i);
	}
	for (i = 0; i < 5; i++) {
		years.push(y + i);
	}
	if (params["year"] == null) {
		drawStatusOption()
	}

	else if (params["year"] != y) {
		for (i = 0; i < 9; i++) {
			if (years[i] == params["year"]) {
				$('#status').append(new Option(years[i], years[i], true, true));
			} else {
				$('#status').append(new Option(years[i], years[i]));
			}
		}
	} 
	else {
		drawStatusOption();
	}
	
	function drawStatusOption(){
		for ( i = 0; i < 9; i++) {
			if (i == 4) {
				$('#status').append(new Option(years[i], years[i], true, true))
//						"<option value='" + years[i]
//								+ "' selected='selected'>"
//								+ years[i] + "</option>");
			} else {
				$('#status').append(
						new Option(years[i], years[i]));
			}
		}
	}

	for (i = 0; i < rawDates.length; i++) {
		selDates.push(new Date(rawDates[i]));
	}

	// creare calendar si adaugare zile libere
	if (params["year"] != null) {
		y = params["year"];
	}

	$('#datepicker').multiDatesPicker({
		addDates : selDates.length > 0 ? selDates : null,
		numberOfMonths : [ 3, 4 ],
		defaultDate : '1/1/' + y
	});

	$('#status').change(function() {
		var year = $(this).val();
		selDates = [];
		$("#datepicker").datepicker("destroy");
		for (i = 0; i < rawDates.length; i++) {
			var d = new Date(rawDates[i]);
			if (year == d.getFullYear())
				selDates.push(d);

		}
		$('#datepicker').multiDatesPicker(
			{
				addDates : selDates.length > 0 ? selDates: null,
				numberOfMonths : [3, 4 ],
				defaultDate : '1/1/'+ $('#status').find(":selected").text()
			}
		);
		}
	);

	// trimiterea weekend-urilor din an spre controller spre
	// inserare in baza de date
	$('#selWeekends').click(function() {
//							var dropDown = document.getElementById('status');
//							selectedYear = dropDown.options[dropDown.selectedIndex].value;
			function addZ(n) {
				return n < 10 ? '0' + n : '' + n;
			}

			var satsun = [];
			// functie pentru a obtine datele
			// weekend-urilor din an
			function weekends(year) {

				var day, date, month;

				day = 1;
				month = 0;

				date = new Date(year, month, day);

				while (date.getFullYear() == year) {

					if (date.getDay() === 0 || date.getDay() === 6) { // Sat=6,
						// Sun=0,
						// Mon=1,
						// Tue=2,
						// etc.

						satsun.push(date.getFullYear()
										+ "-"
										+ addZ(date.getMonth()+ 1)
										+ "-"
										+ addZ(date.getDate()));

					}
					day += 1;
					date = new Date(year, month,day);
				}

				return satsun;
			}

			var selectedYear = $('#status').find(":selected").text();
			var rawDates2 = [];
			rawDates2 = weekends(selectedYear);
			var weekend = [];
//							var descriptionWeekend = [];

			for (i = 0; i < rawDates2.length; i++) {
				if (!rawDates.includes(rawDates2[i]) ) {
					var object = {};
					object['nonbusinessdate'] = rawDates2[i];
					object['description'] = "weekend";
					weekend.push(object);

				}
			}
			

			$.ajax({
						method : 'POST',
						dataType : 'json',
						url : './Calendar/selWeekends',
						data : {
							dates : JSON.stringify(weekend),
							year : selectedYear
						},

						beforeSend : function(xhr) {
							xhr.setRequestHeader(
											$('#_csrf_header').attr("content"),
											$('#_csrf').attr("content"));
							showLoader()
						},

						success : function(data) {
							// window.document.location
							// =
							// "Calendar.htm?act=wend"+
							// "&year=" +
							// selectedYear;
							hideLoader()
							window.location = "/fintp_ui/Calendar?act=save&year="+ selectedYear+fp;
							
						},
						error : function(e) {
							hideLoader()
							window.location = "/fintp_ui/Calendar?act=error&year="+ selectedYear+fp;
							// window.document.location
							// =
							// "Calendar.htm?act=wend"+
							// "&year=" +
							// selectedYear;
						}
					});

		});
	
	function castDate(date){
		var dates = date.split("/")
		if (dates.length >= 3)
			return dates[2]+"-"+dates[0]+"-"+dates[1]
	}
	
	function castAllDates(allDates){
		var newDates = Array();
		allDates.forEach(value => newDates.push(castDate(value)))
		return newDates
	}
	// trimiterea datelor selectate catre controller
	$('#getDates').click(function() {
		var dropDown = document
				.getElementById('status');
		var selectedYear = dropDown.options[dropDown.selectedIndex].value;
		var rawDates1 = castAllDates($('#datepicker').multiDatesPicker('getDates'));
		var newDates = [];
		for (i = 0; i < rawDates1.length; i++) {
			if (!rawDates.includes(rawDates1[i])
					&& rawDates1[i] != "NaN/NaN/NaN"
					&& rawDates1[i] != "") {
				newDates.push(rawDates1[i]);
			}

		}

		for (i = 0; i < rawDates.length; i++) {
			if (!rawDates1.includes(rawDates[i])
					&& (rawDates[i] != "")) {
				newDates.push(rawDates[i]);
			}
		}

		$.ajax({
				type : "post",
				url : "./Calendar/getDates",
				data : {
					dates : JSON.stringify(newDates),
				},
				beforeSend : function(xhr) {
					xhr.setRequestHeader(
									$('#_csrf_header').attr("content"),
									$('#_csrf').attr("content"));
					showLoader()
				},
				// dataType : 'json',
				success : function(data) {
					$('#id1').val(newDates)
					location.href = window.location.href;
					hideLoader()
				},
				error : function(e) {
					var loc;
					if (params["act"] == "wend") {

						var adress = window.location.href;
						loc = adress.substring(0,adress.indexOf("?"))+ "?act=save";
						location.href = loc+ "&year="+ selectedYear + fp;

					} else {
						if (params["act"] != "save") {
							loc = '//'+ location.host+ location.pathname+ "?act=save";
							location.href = loc+ "&year="+ selectedYear+fp;
						}

						else {
							loc = window.location.href;
							location.href = loc.substring(0,loc.indexOf("&"))+ "&year="+ selectedYear+fp;
						}

					}
					hideLoader()
				}
			});

	});

	$('#message').puigrowl();

	if (params["act"] === "save") {

		$('#message').puigrowl('show', [ {
			severity : 'info',
			summary : 'Successfully saved',
			detail : 'Action was completed successfully!'
		} ]);
	}

	if (params["act"] === "wend") {

		$('#message').puigrowl('show', [ {
			severity : 'info',
			summary : 'The weekends were set successfully',
			detail : 'Action was completed successfully!'
		} ]);
	}
});
