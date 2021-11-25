$(function(){
		
		$('#dateCashFlowReport').daterangepicker({
			singleDatePicker: true,
			showDropdowns: true,
			minYear: 1901,
			autoUpdateInput:true,
			startDate:$("#dateCashFlowReport").val()!=""? $("#dateCashFlowReport").val():new Date(),
			locale: {
				format: 'YYYY-MM-DD'
			}
		});
		
		const headers = ["Actual CashFlow","Forecasted CashFlow","Comparison"]

		if(cashflowComparison!=null){
				let indexHeader = 0; 
				let cashflow = cashflowComparison.map((row,index)=>[headers[row.iban!=null?indexHeader:indexHeader++],row.indicator,row.iban,addThousandsSeparator(row.amount),row.currency])
				$('#cashReportTable').dataTable( {
			    	 order: [],
			    	 paging: false,
			    	 ordering: false,
			    	 searching: false,
			    	 columns: [ 
			    		 	{ title: '', className: "dt-body-center" , visible:false},
			    	        { title: 'Indicatori (Indicators)'},
			    	        { title: 'IBAN'},
			    	        { title: 'Suma (Amount)', className: "dt-body-right"},
			    	        { title: 'Moneda (Currency)', className: "dt-body-center"}
			    	    ],
			    	    data:cashflow,
			    	    rowGroup: {
			                dataSrc: 0
			            },
			    	    pageLength: '20',
			    } );
				
				let div = $("#pie")
				let filterIbans = cashflowComparison.filter(row => (row.iban!=null && !row.indicator.startsWith("Suma"))).sort((a, b) => a.iban < b.iban ? -1 : (a.iban > b.iban ? 1 : 0))
				
				let x = filterIbans.map( row => (row.indicator.startsWith("Excedent")?"forecasted ":"actual ") + row.iban)
				let y = filterIbans.map( row => row.amount) 
				
				var pie = [{ x: x,y: y, type: 'bar' }];
	
				var layout = {
						  height: 340, 
						  width: 950,
						  marker: {
						    color: 'rgb(204,204,204)',
						    opacity: 0.5
						  },
						  title: "Comparison Actual Cashflow vs. Forecasted Cashflow",
						  xaxis: {
					          tickformat: "d",
					          automargin:true
					         // zeroline: false
					      },
						  yaxis: {
					          tickformat: ".3s",
					      }
				};
				var d3 = Plotly.d3;
				var pie_jpg= d3.select('#pieImage');
				Plotly.newPlot('pie', pie, layout, {showSendToCloud: true, displayModeBar: false}).then(function(gd){
					gd.on('plotly_relayout', () => {
					    var hiddenLabels = gd.layout.hiddenlabels;
					    var updatedLabels = getLabels(hiddenLabels, omfp);
					    var update = {				    		
					    		labels: [updatedLabels],
					         }
			            Plotly.restyle(div, update);
					})
					Plotly.toImage(gd,{height:530,width:800}).then(function(url){pie_jpg.attr("src", url);} )
			 });
		}
			
		$('#ToPDF').click(function(){

			var pdf = new jsPDF('p', 'pt', 'a3');

			pdf.text(40, 20, "Entity: " + $('#entity option:selected').text() + "\nDate: " + $('#dateCashFlowReport').val());
			
			var IDtable = document.getElementById('cashReportTable');
	        var sourceTable = pdf.autoTableHtmlToJson(IDtable);
	        var sourceTableData = sourceTable.data;
	        var sourceTableColums = sourceTable.columns;
	        pdf.autoTable(sourceTableColums, sourceTableData,  {startY: pdf.autoTableEndPosY() + 40, theme: 'striped',
	        	createdCell: function (cell, data) {
	        		if(data.column.index==2)
	        			cell.styles.halign="right"
	        	},
	        });
		        
		    pdf.addImage($("#pieImage").attr("src"), 'JPEG', 0, pdf.autoTableEndPosY()+2);
		    
		    pdf.save('Generate Cashflow comparison.pdf');
  
		});	

		function addThousandsSeparator(num) {
			const options = { 
					minimumFractionDigits: 2,
					maximumFractionDigits: 2 
			};
			return  num==null?"":Number(num).toLocaleString('en', options);
		}

});
	
