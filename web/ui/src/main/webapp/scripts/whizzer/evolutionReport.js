$(function(){
	
	let labelsPL = labels.filter(function(label){
		return (label["reportingcategory"] == "Profit and Loss") 
	})
	
	let labelsBS = labels.filter(function(label){
		return (label["reportingcategory"] == "Balance Sheet")
	})
	let labelsPK = labels.filter(function(label){
		return (label["reportingcategory"] == "KPIs")
	})
	
	var currentYear = new Date().getFullYear();  
	$("#yearMin, #yearMax").append("<option></option>").change(function(){
		$('#entity'+$(this).attr("id")).html($(this).val());
	})
	$("#entity").change(function(){
		$('#entityPdf').html($(this).val());
	})
	
	
	$("#yearMin").multiselect('dataprovider', getLastNYear(10,yearMin)); 
	$("#yearMax").multiselect('dataprovider', getLastNYear(10,yearMax)); 

	const indicatorsPL = [3,2,1,8,11,12,15,16,17,18,19,21,24,30,40,41,42,45,48,50,54,56,57,58,59,60,61,62,63,64,67,68];
	const indicatorsBS = [1,2,3,"totalactimob",5,8,9,10,11,12,31,39,45,46,47,43,44,48,18,19,20,15,"totalcapstr","totalpas"];
	const indicatorsPK = ["emplcostsinturnover", "profitrate", "roe", "assetrurnoverratio", "debitratio", "currentratio"];
	
	var dataPl = indicatorsPL.map(function(index){	
		label = labelsPL.find(x => x.name === ("omfp"+index))
		return {
			label:label.label,//.replace("(","<br>("),
			value:label.id,
			name: label.name,
			selected:  selectedIndicatorsPL === null?false:selectedIndicatorsPL.includes(label.id)
		}
	})
	
	var dataBS = indicatorsBS.map(function(index){	
		label = labelsBS.find(x => x.name === (!isNaN(index)?"omfp"+index:index))
		return {
			label: label.label,
			value: label.id,
			name: label.name,
			selected: selectedIndicatorsBS === null?false:selectedIndicatorsBS.includes(label.id)
		}
	})
	
	var dataPK = indicatorsPK.map(function(index){	
		label = labelsPK.find(x => x.name === index)
		return {
			label: label.label,
			value: label.id,
			name: label.name,
			selected: selectedIndicatorsPK === null?false:selectedIndicatorsPK.includes(label.id)
		}
	})
	
	$("#indicatorsPL").multiselect('dataprovider', dataPl); 
	$("#indicatorsBS").multiselect('dataprovider', dataBS); 
	$("#indicatorsPK").multiselect('dataprovider', dataPK); 
	var d3 = Plotly.d3;
	
	function getYearInterval(){
		var minY = $('#yearMin').find(":selected").text();
		var maxY = $('#yearMax').find(":selected").text();
		var years = [];
		while(minY <= maxY){
			years.push(minY);
			minY++;
		}
		return years;
	}
	
	function setTable(table, report, selectedIndicators, data, div){
		if(selectedIndicators!=undefined){
			let pie = {values:[], labels:[], data:[], ids:[]}
			
			table.find("thead>tr>th").attr({style:"background:#CCFFCC;text-align:center;width:350px"}).text("Indicator - Year");
			
			var years = getYearInterval();
			years.forEach(function(year){
				table.find("thead>tr").append($("<th style='background:#CCFFCC;text-align:center;width:140px'>").text(year));
			})
			
			report.forEach(function(item){
//				table.find("thead>tr").append($("<th  0style='background:#CCFFCC;text-align:center'>").text(item.year));
				pie.labels.push(item.year);
			})
			
			selectedIndicators.forEach(function(indicator){
				let tr = $("<tr>")
				let ind = data.find(x => x.value === indicator);

				tr.append($("<td style='text-align:left'>").text(ind.label));
				pie.values = [];

				var arrayIndexWhenCreatingTds = 0;
				report.forEach(function(item){
					while(Number(years[arrayIndexWhenCreatingTds]) !== Number(item.year)){
						tr.append($("<td style='text-align:center'>").text("-"));
						arrayIndexWhenCreatingTds ++;						
					}
					tr.append($(isNaN(item[ind.name])?"<td style='text-align:center'>":"<td style='text-align:right'>")
							.text(isNaN(item[ind.name])?'-':formatNumberWithCommasPerThousand(Number(item[ind.name]).toFixed(2)) ));
					arrayIndexWhenCreatingTds ++;
//					tr.append($(isNaN(item[ind.name])?"<td style='text-align:center'>":"<td style='text-align:right'>")
//							.text(isNaN(item[ind.name])?'-':formatNumberWithCommasPerThousand(Number(item[ind.name]).toFixed(2)) ));
					pie.values.push(isNaN(item[ind.name])?'-':Number(item[ind.name]).toFixed(2) );
	 			})
	 			pie.data.push({x: pie.labels, y: pie.values, type: ([3,58,59].includes(indicator) && div=='pk')?'line':'bar', hoverlabel: {namelength: 0}, name: ind.label})
				table.find("tbody").append(tr);
			})
			

			if(table.attr("id")!="pkTable"){
				setPie(div, pie)
			}else{
	 			pie.data.forEach(function(data, index){
	 				let dataPie = [];
	 				data.name = data.name.replace("(","<br>(")
	 				dataPie.push(data)
	 				$("#pk_tab").append($("#pk").clone().attr({id:"pk"+index}).css("display","inline-block"));
	 				$("#images").append($("#pkImg").clone().attr({id:"pk"+index+"Img"}).css("display",""));
	 				setPie("pk"+index, {data:dataPie})
	 			})
			}
		}else{
			table.css("display","none")
		}	
	}
	
	function setPie(div, pie){
		var layout = {height: div.startsWith("pk")?400:(600+pie.data.length*40), width: div.startsWith("pk")?400:800,
			  marker: {
			    color: 'rgb(204,204,204)',
			    opacity: 0.5
			  },
			  showlegend: true,
			  legend: {"orientation": "h"},
			  xaxis: {
		          tickformat: "d",
		         // zeroline: false
		      },
			  yaxis: {
		          tickformat: ".3s",
		      }
		};
		
		Plotly.newPlot(div, pie.data, layout, {showSendToCloud: true, displayModeBar: false}).then(function(gd){
			      Plotly.toImage(gd,{height:530,width:800}).then(function(url){
			    	  d3.select('#'+div+"Img").attr("src", url);} )
		});
	}
	
	
	pkis = {};
	let index = 1;
	if(profitLoss!=null){
		profitLoss.forEach(function(pl){ 
			let pki = {
				emplcostsinturnover: pl.omfp21/pl.omfp1*100,
				profitrate: pl.omfp67/pl.omfp1*100,
				year:pl.year,
				name:pl.name,
				id:pl.id,
				id:index++
			}
			pkis[pl.year] = pki;
		})
	}
	if(balanceSheet!=null){
		console.log(balanceSheet)
		balanceSheet.forEach(function(bs){console.log(bs)
			let pki = {};
			if(pkis[bs.year]!=undefined)
				pki = pkis[bs.year];
			else
				pki.id = index++;
			pki.currentratio= bs.omfp11/bs.omfp15;
			pki.debitratio= bs.totalcapstr/bs.totalact*100;
			
			if(profitLoss!=null){
				let pl = profitLoss.find(x => x.year === bs.year)
				console.log(pl)
				if(pl!=undefined){
					pki.assetrurnoverratio = pl.omfp1/bs.totalact;
					pki.roe = pl.omfp67/bs.omfp48*100;
				}
				
			}
			pki.year = bs.year;
			pki.name = bs.ame;
			pki.id=bs.id;
			if(pkis[bs.year]==undefined)
				pkis[bs.year] = pki;
		})
	}

	let table = $("#table");
	var pkis1 = [];
	
	
	if(profitLoss!=null && profitLoss.length!=0 && selectedIndicatorsPL!=null){
		$("#tables").append($("#labelTable").clone().attr({id:"labelTablePL"}).css("display","").html("Profit and loss")).append(table.clone().removeClass("excludePdf").attr({id:"plTable"}).css("display",""));
	}
	
	if(balanceSheet!=null && balanceSheet.length!=0 && selectedIndicatorsBS!=null){
		$("#tables").append($("#labelTable").clone().attr({id:"labelTablePL"}).css("display","").html("Balance sheet")).append(table.clone().removeClass("excludePdf").attr({id:"bsTable"}).css("display",""));
	}
	if(pkis!=null && selectedIndicatorsPK!=null){
		$("#tables").append($("#labelTable").clone().attr({id:"labelTablePL"}).css("display","").html("KPIs")).append(table.clone().removeClass("excludePdf").attr({id:"pkTable"}).css("display",""));
	}
	for (var item in pkis) {
		pkis1.push( pkis[item] );
	}
	
	setTable($("#pkTable"), pkis1, selectedIndicatorsPK, dataPK, "pk");
	setTable($("#plTable"), profitLoss, selectedIndicatorsPL, dataPl, "pl");
	setTable($("#bsTable"), balanceSheet, selectedIndicatorsBS, dataBS, "bs");
	
	$('#pl_tab').tab('show');
	$("#entity").change(function(){
		$('#entityPdf').html($(this).val());
	})
	$('#ToPDF').click(function(){
		
		var pdf = new jsPDF('p', 'pt', 'a3')

		// source can be HTML-formatted string, or a reference
		// to an actual DOM element from which the text will be scraped.
		, source = $('#content')[0]

		// we support special element handlers. Register them with jQuery-style
		// ID selector for either ID or node name. ("#iAmID", "div", "span" etc.)
		// There is no support for any other type of selectors
		// (class, of compound) at this time.
		, specialElementHandlers = {
		    // element with id of "bypass" - jQuery style selector
		    '.excludePdf': function(element, renderer){
		        // true = "handled elsewhere, bypass text extraction"
		        return true
		    }
			//,'printHeaders': false
		}
		
		margins = {
		    top: 20,
		    bottom: 20,
		    left: 30,
		    width: 800
		  };
		  // all coords and widths are in jsPDF instance's declared units
		  // 'inches' in this case
		pdf.fromHTML(
			source // HTML string or DOM elem ref.
		    , margins.left // x coord
		    , margins.top // y coord
		    , {
		        'width': margins.width // max width of content on PDF
		        , 'elementHandlers': specialElementHandlers
		    },
		    function (dispose) {
		      // dispose: object with X, Y of the last line add to the PDF
		      //          this allow the insertion of new lines after html
		    	pdf.save('Evolution report.pdf');
		      },
		    margins
		  )
	});
	
	function formatNumberWithCommasPerThousand(number) {
	    return number.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
	}
})