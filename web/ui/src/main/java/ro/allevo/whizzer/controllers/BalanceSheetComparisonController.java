package ro.allevo.whizzer.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.validation.Valid;
import javax.ws.rs.QueryParam;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;

import ro.allevo.fintpui.service.InternalEntitiesService;
import ro.allevo.fintpui.utils.Filters;
import ro.allevo.fintpui.utils.JSONHelper;
import ro.allevo.fintpui.utils.PagedCollection;
import ro.allevo.fintpui.utils.datatables.DataTables;
import ro.allevo.whizzer.model.BalanceSheetComparison;
import ro.allevo.whizzer.model.BalanceSheetForecast;
import ro.allevo.whizzer.model.BalanceSheetLoadHistory;
import ro.allevo.whizzer.model.BalanceSheetReport;
import ro.allevo.whizzer.model.CashflowForecast;
import ro.allevo.whizzer.model.ProfitLossLoadHistory;
import ro.allevo.whizzer.service.BalanceSheetComparisonService;
import ro.allevo.whizzer.service.BalanceSheetForecastService;
import ro.allevo.whizzer.service.ConfigBsandPlService;
import ro.allevo.whizzer.service.ProfitLossLoadHistoryService;

@Controller()
@RequestMapping(value = "/whizzer/balanceSheetComparison")
public class BalanceSheetComparisonController {
	
	@Autowired
	private InternalEntitiesService internalEntitiesService;
	
	@Autowired
	private BalanceSheetComparisonService balanceSheetComparisonService;
	
	@Autowired
	private ConfigBsandPlService configBsandPlService;
	
	@Autowired
	private BalanceSheetForecastService balanceSheetForecastService;
	
	@Autowired
	private ProfitLossLoadHistoryService profitLossService;
	
	private static Logger logger = LogManager.getLogger(BalanceSheetComparisonController.class.getName());

	@GetMapping
	public String printBalanceSheetComparison(@RequestParam(required = false) Integer year,@RequestParam(required = false) String entity, 
			ModelMap model, OAuth2Authentication auth) {	
		model.addAttribute("hasModifyRole",true);//Roles.hasRoles(auth,Roles.BANKS_LIST_MODIFY));
		model.addAttribute("internalEntities", internalEntitiesService.getAllInternalEntityNames());

		LinkedHashMap<String, List<String>> params = new LinkedHashMap<String, List<String>>();
		if(!(year==null || entity ==null)) {
			params.put("filter_year_exact", new ArrayList<String>(){{add(String.valueOf(year));}});
			params.put("filter_entity_exact", new ArrayList<String>(){{add(entity);}});
			BalanceSheetComparison[] balanceSheetComparison = balanceSheetComparisonService.getPage(params).getItems();
			model.addAttribute("balanceSheetComparison",balanceSheetComparison);
		}
		
		
		HashMap<String,String> labels = new HashMap<String,String>();
		params.put("filter_reportingcategory_exact", new ArrayList<String>(){{add("Profit and Loss");}});
		labels.putAll(configBsandPlService.getLabels(params));
		model.addAttribute("year", year);
		model.addAttribute("entity", entity);
		model.addAttribute("labels", labels);
		return "whizzer/balanceSheetComparison";
	}

	@GetMapping("input")
	public String printBalanceSheetComparisonInput(
			@RequestParam(value = "entity", required = false) String entity,
			@RequestParam(value = "forecast", required = false ) Integer forecast, 	
			@RequestParam(value = "year", required = false) Integer year,
			@RequestParam(value = "historicalbs", required = false) Integer historicalbs,
				ModelMap model, OAuth2Authentication auth) {	
		LinkedHashMap<String, List<String>> paramsForecast = new LinkedHashMap<>();
		model.addAttribute("internalEntities", internalEntitiesService.getAllInternalEntityNames());
		paramsForecast.put("filter_forecast_exact", Filters.getFiltersParams(String.valueOf(forecast)));
		paramsForecast.put("filter_entity_exact", Filters.getFiltersParams(entity));
		
		if(entity!=null) {
			if(year!=null) {
				BalanceSheetForecast[] balanceSheetForecast = balanceSheetForecastService.getBalanceSheets(paramsForecast);
				if(balanceSheetForecast.length == 0) {
					profitLossService.computeBsForecast(entity, historicalbs, year, forecast);
					 balanceSheetForecast = balanceSheetForecastService.getBalanceSheets(paramsForecast);
				}
				model.addAttribute("balanceSheetForecast", balanceSheetForecast.length == 1?balanceSheetForecast[0]:new BalanceSheetForecast());
			
				LinkedHashMap<String, List<String>> paramsLoadHistory = new LinkedHashMap<>();
				paramsLoadHistory.put("filter_year_exact", Filters.getFiltersParams(String.valueOf(year)));
				paramsLoadHistory.put("filter_entity_exact", Filters.getFiltersParams(entity));
				ProfitLossLoadHistory[] profitLoss = profitLossService.getProfitLosses(paramsLoadHistory);
				model.addAttribute("profitLossLoadHistory", profitLoss.length == 1?profitLoss[0]:new ProfitLossLoadHistory());
			}
			model.addAttribute("profitLossAll", profitLossService.getAllProfitLosses());
		}
		
		LinkedHashMap<String, List<String>> paramsConfigBsAndPl = new LinkedHashMap<>();
		paramsConfigBsAndPl.put("filter_reportingcategory_exact", Filters.getFiltersParams("Profit and Loss"));
		model.addAttribute("plLabels", configBsandPlService.getLabels(paramsConfigBsAndPl));
		
		model.addAttribute("year", year);
		model.addAttribute("forecast", forecast);
		model.addAttribute("historicalbs", historicalbs);
		model.addAttribute("entity", entity);
		System.out.println(internalEntitiesService.getAllInternalEntityNames());
		
		return "whizzer/balanceSheetComparisonInput";
	}
	
	
	@PostMapping(value = "/update")
	public String updateCashflowForecast(ModelMap model, @ModelAttribute("balanceSheet") BalanceSheetForecast balanceSheet, BindingResult bindingResult,
			@RequestParam("id") String id,
			@RequestParam(value = "entity", required = false) String entity,
			@RequestParam(value = "forecast", required = false ) Integer forecast, 	
			@RequestParam(value = "year", required = false) Integer year,
			@RequestParam(value = "historicalbs", required = false) Integer historicalbs,
			@RequestParam(value = "fp", required = false) String[] fps,
			
			RedirectAttributes redirectAttributes) throws JsonProcessingException {
		logger.info("update cashflowForecast requested");
		/*redirectAttributes.addAttribute("entity", entity);
		redirectAttributes.addAttribute("forecast", forecast);
		redirectAttributes.addAttribute("year", year);
		redirectAttributes.addAttribute("historicalbs", historicalbs);*/
		//for (String fp : fps) 
		//	redirectAttributes.addAttribute("fp", fps);
		balanceSheetForecastService.updateBalanceSheet(balanceSheet, id);
		return "redirect:/whizzer/balanceSheetComparison/input?fp="+fps[0]+"&fp="+fps[1]+"&fp="+fps[2];
	}
	/*
	 * DISPLAY
	 */
//	@GetMapping(value = "report/page")
//	public @ResponseBody String getBalanceSheetComparisonJson(@RequestParam int draw) throws JsonProcessingException {
//		logger.info("/balanceSheetComparison required");
//		PagedCollection<BalanceSheetComparison> cashflowForecasts = balanceSheetComparisonService.getPage();
//
//		DataTables dt = new DataTables();
//		if (null != cashflowForecasts) {
//			dt.setData(cashflowForecasts.getItems());
//			dt.setRecordsFiltered(cashflowForecasts.getTotal());
//			dt.setRecordsTotal(cashflowForecasts.getTotal());
//		}
//		dt.setDraw(draw);
//		return JSONHelper.toString(dt);
//	}
}
