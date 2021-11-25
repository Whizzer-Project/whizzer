$(function(){
	
	$('#entity').on('change', function(){
		$('form#balanceSheetReportForm').submit();
	});
	
	$('#year').on('change', function(){
		var selectedOptionEntity = $('#entity option:selected').text();
		if(selectedOptionEntity !== ""){
			$('form#balanceSheetReportForm').submit();
		}
	});
	
	var d3 = Plotly.d3;
	var revenues_jpg= d3.select('#revenues');
	var costs_jpg= d3.select('#costs');

	function setPie(omfp, div, image){
		var data = [{
			  values:  omfp.map(function(e){
					return balanceSheetReport["omfp"+e]
			  }),
			  labels: getLabels([], omfp),
			  type: 'pie',
			  textposition: 'inside',
			  hovertemplate: "%{percent:.2%}<extra></extra>"		  
		}];

		var layout = {
			  title: div === "revenuesDiv"? 'TOTAL ASSETS STRUCTURE<br>(STRUCTURA ACTIV YTD)':'TOTAL LIABILITIES AND SHAREHOLDERS\' EQUITY STRUCTURE<br>(STRUCTURA PASIV YTD)',
			  height: 530,
			  width: 900 ,
			  showlegend: true,
			  legend: {"orientation": "h"}
		};
		
		Plotly.newPlot(div, data, layout, {showSendToCloud: true, displayModeBar: false}).then(function(gd){
				gd.on('plotly_relayout', () => {
				    var hiddenLabels = gd.layout.hiddenlabels;
				    var updatedLabels = getLabels(hiddenLabels, omfp);
				    var update = {				    		
				    		labels: [updatedLabels],
				         }
		            Plotly.restyle(div, update);
				})
				
		        Plotly.toImage(gd,{height:530,width:800}).then(function(url){image.attr("src", url);} )
		 });
	}
	if(balanceSheetReport!=null){
		setPie([1,2,3,5,8,9,10,12], "revenuesDiv", revenues_jpg)
		setPie([31,39,45,46,47,44,43,18,15,19,20], "costsDiv", costs_jpg)
	}	
	
	function getLabels(hiddenLabels, omfp){
		var total = 0;
		var labelsWithPercentage = [];
		
		var bsValues = omfp.map(function(e){
						return balanceSheetReport["omfp"+e]
			    	});
		var bsLabels = omfp.map(function(e){
						return labels["omfp"+e];
				 	  });
		
		var hiddenLabelsIndexes= {};
		var percentage;	
		
		if(hiddenLabels.length == 0){					
			for(var i=0; i<bsValues.length; i++){
				total = total + bsValues[i];
			}			
		
			for(var j=0; j<bsLabels.length; j++){
				percentage = (bsValues[j]/total)*100;
				labelsWithPercentage[j] = bsLabels[j] + "| " + (percentage === 0? percentage:percentage.toFixed(2)) + "%";
			}			
		}else{
			for(var i=0; i<bsLabels.length; i++){
				for(var k=0; k<hiddenLabels.length; k++){
					if(hiddenLabels[k].substr(0, hiddenLabels[k].indexOf('|')) === bsLabels[i]){
						hiddenLabelsIndexes['' + i] = hiddenLabels[k];
					}
				}								
			}	
			
			for(var l=0; l<bsValues.length; l++){
				if(!hiddenLabelsIndexes.hasOwnProperty(l)){
					total = total + bsValues[l];
				}				
			}

			for(var j=0; j<bsLabels.length; j++){					
				if(hiddenLabelsIndexes.hasOwnProperty(j)){
					labelsWithPercentage[j] = hiddenLabelsIndexes[j];
				}else{
					percentage = (bsValues[j]/total)*100;
					labelsWithPercentage[j] = bsLabels[j] + "| " + (percentage === 0? percentage:percentage.toFixed(2)) + "%";
				}
			}			
		}
		return labelsWithPercentage;
	}	
	
//	var currentYear = new Date().getFullYear();  
	$("#year").append("<option></option>").change(function(){
		$('#entityYear').html($(this).val());
	})
	$("#entity").change(function(){
		$('#entityPdf').html($(this).val());
	})

	$("#year").multiselect('dataprovider', getLastNYear(10,year)); 
	$('#entityYear').html(year);
	$('#ToPDF').click(function(){

		var pdf = new jsPDF('p', 'pt', 'a3');

		// source can be HTML-formatted string, or a reference
		// to an actual DOM element from which the text will be scraped.
		var source = $('#content')[0];
		var piesGraphs = $('#piesGraphsBS')[0];
		
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
		pdf.text(margins.left + 10, margins.top, "Entity: " + $('#entity option:selected').text() + "\nYear: " + $('#year option:selected').text());
		
		var width = document.getElementById('content').getBoundingClientRect().width;
		if(width > 1620){
			pdf.internal.scaleFactor = 1.1;
		}	
		
		// addHTML keeps css only for table - pie graphs must be copied with fromHTML
		pdf.addHTML(
				source // HTML string or DOM elem ref.
			    , margins.left // x coord
			    , margins.top + 20 // y coord
			    , {
			        'width': margins.width // max width of content on PDF
			        , 'elementHandlers': specialElementHandlers
			        , pagesplit: true
			        , "background": '#FFFFFF'
			    },
			    function (dispose) {
			      // dispose: object with X, Y of the last line add to the PDF
			      //          this allow the insertion of new lines after html
					pdf.fromHTML(
							piesGraphs// HTML string or DOM elem ref.
						    , margins.left // x coord
						    , margins.top // y coord
						    , {
						        'width': margins.width // max width of content on PDF
						        , 'elementHandlers': specialElementHandlers
						    },
						    function (dispose) {
						      // dispose: object with X, Y of the last line add to the PDF
						      //          this allow the insertion of new lines after html
						    	pdf.save('Balance Sheet Report.pdf');
						      },
						    margins
						  )
			      },
			    margins
			  )			  
	});
	
})