$(function(){

	$('#dateCashFlowReport').daterangepicker({
		singleDatePicker: true,
	    showDropdowns: true,
	    minYear: 1901,
	    locale: {
	        format: 'YYYY-MM-DD'
	    }
    });
		
	if(date !== null){
		$("#dateCashFlowReport").data('daterangepicker').setStartDate(date);
		$("#dateCashFlowReport").data('daterangepicker').setEndDate(date);
	}		
	
	var d3 = Plotly.d3;
	var barGraphcfr_jpg= d3.select('#barGraphcfr');

	// arrays of object with fields : amt , operationiban
//	accountDeficitExcedent
//	companyDeficitExcedent
//  need: 1 array with operationiban(x) + 1 array with amt (y)
	
	if(accountDeficitExcedent !== null && companyDeficitExcedent !== null){
		var operationIbans = [];
		var amounts = [];
		
		for (const cashFlowGenData of accountDeficitExcedent) {
			operationIbans.push(cashFlowGenData.operationiban);
			amounts.push(cashFlowGenData.amt);
		}
		operationIbans.push("TOTAL");
		amounts.push(companyDeficitExcedent[0].amt);
				
		var trace1 = {
			    type: 'bar',
			    x: operationIbans,
			    y: amounts,		
			    
			    textinfo: 'none',
			    textposition: 'inside',
			    marker: {
			        color: ['#33ccff', '#ff9900', '#808080'],			       		    			        
			    }
			};

		var data = [ trace1 ];
		
		var layout = {
		  title: 'Excedent/ Deficit disponibilitati(Excedent/ Deficit) ',
		  height: 530,
		  width: '100%',
		  autosize: false,
		  bargap :0.1,
		  xaxis: {
			  automargin: true,
			  }
		};

		Plotly.newPlot('barGraphcfrdiv', data, layout, {showSendToCloud: true, displayModeBar: false}).then(function(gd){			
	        Plotly.toImage(gd,{height:530,width:800}).then(function(url){barGraphcfr_jpg.attr("src", url);} )
	 });
	}
		
	$('#ToPDF').click(function(){

		var pdf = new jsPDF('p', 'pt', 'a3');

		// source can be HTML-formatted string, or a reference
		// to an actual DOM element from which the text will be scraped.
		var source = $('#content').html();
		
		// we support special element handlers. Register them with jQuery-style
		// ID selector for either ID or node name. ("#iAmID", "div", "span" etc.)
		// There is no support for any other type of selectors
		// (class, of compound) at this time.
		var specialElementHandlers = {
		    // element with id of "bypass" - jQuery style selector
		    '.excludePdf': function(element, renderer){
		        // true = "handled elsewhere, bypass text extraction"
		        return true
		    }
		}
		
	    // all coords and widths are in jsPDF instance's declared units
		// 'inches' in this case
		var margins = {
		    top: 20,
		    bottom: 20,
		    left: 30,
		    width: pdf.internal.pageSize.getWidth()
		  };

		// add as first line info from selected entity and year
		pdf.text(margins.left + 10, margins.top, "Entity: " + $('#entityCashFlowReport option:selected').text() + "\nDate: " + date);
		
		var width = document.getElementById('content').getBoundingClientRect().width;
//		if(width > 1620){
//			pdf.internal.scaleFactor = 1.1;
//		}	
		
		pdf.fromHTML(
                source, // HTML string or DOM elem ref.
                margins.left, // x coord
                margins.top + 15, { // y coord
                    'width': margins.width, // max width of content on PDF
                    'elementHandlers': specialElementHandlers
                },

                function(dispose) {


                    // dispose: object with X, Y of the last line add to the PDF 
                    //          this allow the insertion of new lines after html
                    pdf.save('Cashflow Forecast Report.pdf');
                }, margins);		  
	});	

	
})