$(function(){
		$("#year").multiselect('dataprovider', getLastNYear(10,year))
			
			if(balance!=null){
				const balanceSheetComparison = balance[0];
				
				let table = $("#bsComparisionTable tbody");
				const fields = [2,3,12,15,50,60,16,21,30,40,57,61,62]
				
				const th = ["", "Indicatori bilant (Balance Sheet Indicators)", "Valoare previzionata (Forecasted Amount)","Valoare realizata (Realized Amount)",
					"Diferenta intre realizat si previzionat (Difference between realised and forecasted)"]
				$("#bsComparisionTable thead tr").append(th.map((item,index)=>$("<th>").attr("width",index==4?"200px":"").html(item)))
				
	            
				table.append(fields.map(item=>{
					const tdVaules = ["", labels["omfp"+item], balanceSheetComparison["omfp"+item+"f"], balanceSheetComparison["omfp"+item], balanceSheetComparison["omfp"+item+"diff"]]
					return $("<tr>").append(tdVaules.map((val,index)=> $("<td>").attr("align",[2,3,4].includes(index)?"right":"left")
							.css("display",index==0?"none":"")
							.attr("width",index==4?"200px":"")
							.html([2,3,4].includes(index)? addThousandsSeparator(val):val)))
				}))
				const groupRows =["Incasari (Collections)","Plati (Payments)","Profit"];
				groupRows.map((item,index)=>{ 
					table.find("tr:eq("+(index*6)+")").find("td:eq(0)").css("display","").attr("rowspan",6).html(item)
				})
				
				let x = fields.map(item=>labels["omfp"+item]);
				let y = fields.map(item=>labels["omfp"+item]);
				console.log(fields)
				var balanceItems = ["f","","diff"];
				var data = balanceItems.map((item,index)=>{
					return {
						  x: x,
						  y: fields.map(item=>balanceSheetComparison["omfp"+item+balanceItems[index]]),
						  name: th[index+2],
						  type: 'bar'
					};
				})
				
			
				$("#year").append("<option></option>").change(function(){
					$('#entityYear').html($(this).val());
				})
				$("#entity").change(function(){
					$('#entityPdf').html($(this).val());
				})
				
				$("#year").val(year); 
				$('#entityYear').html(year);
				
				var layout = {
						  height: 740, 
						  width: 950,
						  marker: {
						    color: 'rgb(204,204,204)',
						    opacity: 0.5
						  },
						  showlegend: true,
						  bargap :0.1,
						 // title: 'Excedent/ Deficit disponibilitati(Excedent/ Deficit) ',
						  legend: {"orientation": "v", xanchor : "center", x : 1.1, y: 1.3},
						  xaxis: {
					          tickformat: "d",
					         // zeroline: false
					          automargin:true
					      },
						  yaxis: {
					          tickformat: ".3s",
					      }
				};
				
				var d3 = Plotly.d3;
				var pie_jpg= d3.select('#pieImage');
				Plotly.newPlot('pie', data, layout, {showSendToCloud: true, displayModeBar: false}).then(function(gd){
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
				
				$('#ToPDF').click(function(){

					var pdf = new jsPDF('p', 'pt', 'a3');

					pdf.text(40, 20, "Entity: " + $('#entity option:selected').text() + "\nYear: " + $('#year').val());
					
					var IDtable = document.getElementById('bsComparisionTable');
			        var sourceTable = pdf.autoTableHtmlToJson(IDtable);
			        var sourceTableData = sourceTable.data;
			        var sourceTableColums = sourceTable.columns;
			        pdf.autoTable(sourceTableColums, sourceTableData,  {startY: pdf.autoTableEndPosY() + 40, theme: 'striped',
			        	styles: {overflow:'linebreak'}, 
			        	createdCell: function (cell, data) {
			        		if([2,3,4].includes(data.column.index)){
			        			cell.styles.halign="right"
			        		}
			        	},
			        });
				        
				    pdf.addImage($("#pieImage").attr("src"), 'JPEG', 0, pdf.autoTableEndPosY()+2);
				    
				    pdf.save('Generate Cashflow comparison.pdf');
		  
				});	
			}

		function addThousandsSeparator(num) {
			const options = { 
					minimumFractionDigits: 2,
					maximumFractionDigits: 2 
			};
			return  (num==null||Number.isNaN(num))?"":Number(num).toLocaleString('en', options);
		}
			
	});