package ro.allevo.whizzer.controllers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;

import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.exception.NotAuthorizedException;
import ro.allevo.fintpui.service.InternalEntitiesService;
import ro.allevo.fintpui.utils.Roles;
import ro.allevo.whizzer.model.BalanceSheetReport;
import ro.allevo.whizzer.model.ProfitLossReport;
import ro.allevo.whizzer.service.BalanceSheetReportService;
import ro.allevo.whizzer.service.ConfigBsandPlService;
import ro.allevo.whizzer.service.ProfitLossReportService;

@Controller()
@RequestMapping(value = "/whizzer/evolutionReport")
public class EvolutionReportController {

	@Autowired
	Config config;

	@Autowired
	private BalanceSheetReportService balanceSheetReportService;
	
	@Autowired
	private ProfitLossReportService profitLossReportService;
	
	@Autowired
	private ConfigBsandPlService configBsandPlService;

	@Autowired
	private InternalEntitiesService internalEntitiesService;
	
	private static Logger logger = LogManager.getLogger(EvolutionReportController.class.getName());

	/*
	 * DISPLAY
	 */
	@GetMapping
	public String evolutionReports(@RequestParam(required = false) Integer yearMin,@RequestParam(required = false) Integer yearMax,@RequestParam(required = false) String entity,
			@RequestParam(required = false) Integer[] indicatorsPL, @RequestParam(required = false) Integer[] indicatorsBS, @RequestParam(required = false) Integer[] indicatorsPK,
			ModelMap model, OAuth2Authentication auth)
			throws JsonProcessingException {
		logger.info("/profitLossReports required");
		if (!Roles.hasRoles(auth,Roles.KPI_DYNAMIC_REPORTS_VIEW))
			throw new NotAuthorizedException();
		
		LinkedHashMap<String, List<String>> params = new LinkedHashMap<String, List<String>>();
		if(!(yearMin==null || entity ==null)) {

			params.put("filter_year_unum", new ArrayList<String>(){{add(String.valueOf(yearMin-1));}});
			params.put("filter_year_lnum", new ArrayList<String>(){{add(String.valueOf(yearMax+1));}});
			params.put("filter_entity_exact", new ArrayList<String>(){{add(entity);}});
			params.put("sort_year", new ArrayList<String>(){{add("");}});
			
			if(indicatorsBS!=null || indicatorsPK!=null) {
				BalanceSheetReport[] balanceSheet = balanceSheetReportService.getPage(params).getItems();
				model.addAttribute("balanceSheet", balanceSheet);
			}
			
			if(indicatorsPL!=null || indicatorsPK!=null) {
				ProfitLossReport[] profitLoss = profitLossReportService.getPage(params).getItems();
				model.addAttribute("profitLoss", profitLoss);
			}
			
		}
		model.addAttribute("labels", configBsandPlService.getLabelsList());
		
		model.addAttribute("internalEntities", internalEntitiesService.getAllInternalEntityNames());
		model.addAttribute("yearMin", yearMin);
		model.addAttribute("yearMax", yearMax);
		model.addAttribute("entity", entity);
		model.addAttribute("indicatorsBS", indicatorsBS);
		model.addAttribute("indicatorsPL", indicatorsPL);
		model.addAttribute("indicatorsPK", indicatorsPK);
		model.addAttribute("apiUri", config.getAPIUrl());
		return "whizzer/evolutionReport";
	}

	

}	
