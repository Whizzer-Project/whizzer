$(function() {	
		let entity = $("#entity").val();
		if(entity!=""){
			if(profitLossAll!=undefined){
				let years = profitLossAll.filter(item => item.entity==entity).map(item=>item.year).filter((e, i, a) => a.indexOf(e) === i).sort(function(a, b){return b-a});
				$("#year").multiselect('dataprovider', years.map(item=>{return {
					label:item,
					title:item,
		 			value:item}}))
		 		$("#historicalYear").multiselect('dataprovider', years.map(item=>{return {
					label:item,
					title:item,
		 			value:item}}))	
		 			
			}
				console.log(balanceSheetForecast);
			//$("#historicalYear").multiselect('dataprovider', getLastNYear(10, new Date().getFullYear()-1)); 
			$("#forecastYear").multiselect('dataprovider', getLastNYearMax(10, new Date().getFullYear()-1, new Date().getFullYear()+9)); 
			
			$("#year").multiselect('select', year!=null?year:new Date().getFullYear()).multiselect('refresh');  
			$("#forecastYear").multiselect('select', forecast!=null?forecast:new Date().getFullYear()+1).multiselect('refresh');  
			$("#historicalYear").multiselect('select',historicalbs!=null?historicalbs:new Date().getFullYear()-1).multiselect('refresh'); 
			
			
			
			$("#searchLoadHistory").click(function(){
				if($("#forecastYear").val()<=$("#year").val()){
					alert("The forecast date must be higher than the realised balance sheet date!")
					return false;
				}
				if($("#year").val()<=$("#historicalYear").val()){
					alert("The realised balance sheet date must be higher than the historical balance sheet date!")
					return false;
				}
			})
			let table = $("#bsForecast tbody");
			const fields = [2,3,12,15,50,60,16,21,30,40,57,61,62]
			if(balanceSheetForecast!=undefined){
						$("#bsForecast").css("display","")
						if( balanceSheetForecast.historicalbs != $("#historicalYear").val()*1 ||  balanceSheetForecast.realisedbs != $("#year").val()*1 ){
							balanceSheetForecast = {};
						}
						if(balanceSheetForecast != null){
							if(!table.find("td").length){
								const th = ["", "Indicatori bilant (Balance Sheet Indicators)", "Valoare previzionata (Forecasted Amount)","Valoare realizata (Realized Amount)",
																													"Difference between  realised Amount and Forecasted Amount",""]
								$("#bsComparisionTable thead tr").append(th.map(item=>$("<th>").html(item)))
								
					         	table.append(fields.map(item=>{
					         		const tdVaules = ["",profitLossLabels["omfp"+item], profitLoss["omfp"+item], 
										//addThousandsSeparator(Math.round(((balanceSheetForecast["omfp"+item]-profitLoss["omfp"+item])/profitLoss["omfp"+item]*100)* 100) / 100).replaceAll(/,/g, ''),
					         			balanceSheetForecast["omfp"+item+"f"],
					         			profitLoss["omfp"+item]*(1+balanceSheetForecast["omfp"+item+"f"]/100)
									]
					         		console.log(profitLoss["omfp"+item],balanceSheetForecast["omfp"+item+"f"],profitLoss["omfp"+item]*(1+balanceSheetForecast["omfp"+item+"f"]/100),
					         				addThousandsSeparator(profitLoss["omfp"+item]*(1+balanceSheetForecast["omfp"+item+"f"]/100)));
									return $("<tr>").append(tdVaules.map((val,index)=> $("<td>").attr("align",[2,3,4].includes(index)?"right":"left").css("display",index==0?"none":"")
												.html(
											[3].includes(index)? "<input type='text' name='omfp"+item+"f' style='text-align:right;width:100px' value='"+addThousandsSeparator(val)+"'/>%":
											[4].includes(index)? "<input type='text' readonly style='text-align:right;width:200px;border:0px' value='"+addThousandsSeparator(val)+"'/>":	
											[2].includes(index)? addThousandsSeparator(val):val)
											
									))
								}))
								
								const groupRows =["Incasari (Collections)","Plati (Payments)","Profit"];
								groupRows.map((item,index)=>{ 
									table.find("tr:eq("+(index*6)+")").find("td:eq(0)").css("display","").attr("rowspan",6).html(item)
								})
							}else{
								fields.map(item=>{
									const tdVaules = ["",profitLossLabels["omfp"+item], balanceSheetForecast["omfp"+item]]
									tdVaules.map((val,index)=> table.find()
										//$("<td>").attr("align",[2,3,4].includes(index)?"right":"left").css("display",index==0?"none":"")
										//		.html([2,3,4].includes(index)? "<input type='text' value='"+(val!=null?addThousandsSeparator(val):"")+"'/>":val)
												)
								})
							}
						}
						table.find("input").change(function(e){console.log(2)
							var code = e.keyCode || e.which;
						    if (code !== 9) {
								e.preventDefault();
								let tr = $(this).parent().parent();
								var realised = tr.find("td:eq(2)").html();
								tr.find("td:eq(4)").find("input").val(addThousandsSeparator(tr.find("td:eq(2)").html().replaceAll(/,/g, '')*(1+$(this).val().replaceAll(/,/g, '')/100)))
						    }
						}).keyup(function(e){console.log(1)
							var code = e.keyCode || e.which;
						    if (code !== 9) {
								e.preventDefault();
								let tr = $(this).parent().parent();
								var realised = tr.find("td:eq(2)").html();
								tr.find("td:eq(4)").find("input").val(addThousandsSeparator(tr.find("td:eq(2)").html().replaceAll(/,/g, '')*(1+$(this).val().replaceAll(/,/g, '')/100)))
						    }
					    })
						
						$('#collapseOne').collapse() 
					}
				$("#save").click(function(){
					$("input").each(function() {
				        $(this).val( $(this).val().replaceAll(/,/g, ''));
				    });
					
					//$('#formSearch').not(':submit').children().clone().appendTo('#bsValues');

					$("#bsValues").submit()
				})
				
		}else{
			$("[data-type='hidden']").css("display","none")
		}
		$("#entity").change(function(){
			$("#formSearch").submit()
		})
})
		
function addThousandsSeparator(num) {//console.log(num, isNaN(num))
	const options = { 
			minimumFractionDigits: 2,
			maximumFractionDigits: 2 
	};
	return  (num==null||Number.isNaN(num))?"":Number(num).toLocaleString('en', options);
}