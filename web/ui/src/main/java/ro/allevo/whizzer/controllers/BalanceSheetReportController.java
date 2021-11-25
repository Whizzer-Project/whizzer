package ro.allevo.whizzer.controllers;

import java.util.ArrayList;
import java.util.HashMap;
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
import ro.allevo.whizzer.service.BalanceSheetReportService;
import ro.allevo.whizzer.service.ConfigBsandPlService;

@Controller()
@RequestMapping(value = "/whizzer/balanceSheetReport")
public class BalanceSheetReportController {

	@Autowired
	Config config;

	@Autowired
	private BalanceSheetReportService balanceSheetReportService;
	
	@Autowired
	private ConfigBsandPlService configBsandPlService;

	@Autowired
	private InternalEntitiesService internalEntitiesService;
	
	private static Logger logger = LogManager.getLogger(BalanceSheetReportController.class.getName());

	/*
	 * DISPLAY
	 */
	@GetMapping
	public String printBalanceSheetReports(@RequestParam(required = false) Integer year,@RequestParam(required = false) String entity, 
			ModelMap model, OAuth2Authentication auth) {
		logger.info("/balanceSheetReports required");
		if (!Roles.hasRoles(auth,Roles.BALANCE_SHEET_VIEW))
			throw new NotAuthorizedException();
		
		LinkedHashMap<String, List<String>> params = new LinkedHashMap<>();
		if(!(year==null || entity ==null)) {
			params.put("filter_year_exact", new ArrayList<String>(){{add(String.valueOf(year));}});
			params.put("filter_entity_exact", new ArrayList<String>(){{add(entity);}});
			BalanceSheetReport[] balanceSheet = balanceSheetReportService.getPage(params).getItems();
			model.addAttribute("balanceSheetReport", (balanceSheet!=null && balanceSheet.length>0)?balanceSheet[0]:null);
			
			model.addAttribute("pkis", configBsandPlService.getPkis(entity, year));
			
			
			HashMap<String,String> labels = new HashMap<>();
			params.put("filter_reportingcategory_exact", new ArrayList<String>(){{add("Balance Sheet");}});
			labels.putAll(configBsandPlService.getLabels(params));
			
			params = new LinkedHashMap<>();
			params.put("filter_reportingcategory", new ArrayList<String>(){{add("KPIs");}});
			labels.putAll(configBsandPlService.getLabels(params));
			model.addAttribute("labels", labels);
			
			
			
			
		}
		model.addAttribute("internalEntities", internalEntitiesService.getAllInternalEntityNames());
		model.addAttribute("year", year);
		model.addAttribute("entity", entity);
		model.addAttribute("apiUri", config.getAPIUrl());
		return "whizzer/balanceSheetReport";
	}

	

}	
