package ro.allevo.whizzer.controllers;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.QueryParam;

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
import ro.allevo.whizzer.model.ProfitLossReport;
import ro.allevo.whizzer.service.ConfigBsandPlService;
import ro.allevo.whizzer.service.ProfitLossReportService;

@Controller()
@RequestMapping(value = "/whizzer/profitLossReport")
public class ProfitLossReportController {

	@Autowired
	Config config;

	@Autowired
	private ProfitLossReportService profitLossReportService;
	
	@Autowired
	private ConfigBsandPlService configBsandPlService;

	@Autowired
	private InternalEntitiesService internalEntitiesService;
	
	private static Logger logger = LogManager.getLogger(ProfitLossReportController.class.getName());

	/*
	 * DISPLAY
	 */
	@GetMapping
	public String printProfitLossReports(@RequestParam(required = false) Integer year,@RequestParam(required = false) String entity, 
			ModelMap model, OAuth2Authentication auth)
			throws JsonProcessingException {
		logger.info("/profitLossReports required");
		if (!Roles.hasRoles(auth,Roles.BALANCE_SHEET_VIEW))
			throw new NotAuthorizedException();
		
		LinkedHashMap<String, List<String>> params = new LinkedHashMap<String, List<String>>();
		if(!(year==null || entity ==null)) {
			params.put("filter_year_exact", new ArrayList<String>(){{add(String.valueOf(year));}});
			params.put("filter_entity_exact", new ArrayList<String>(){{add(entity);}});
			ProfitLossReport[] profitLoss = profitLossReportService.getPage(params).getItems();
			model.addAttribute("profitLossReport", (profitLoss!=null && profitLoss.length>0)?profitLoss[0]:null);
			
			model.addAttribute("pkis", configBsandPlService.getPkis(entity, year));
			
			HashMap<String,String> labels = new HashMap<String,String>();
			params.put("filter_reportingcategory_exact", new ArrayList<String>(){{add("Profit and Loss");}});
			labels.putAll(configBsandPlService.getLabels(params));
			
			params = new LinkedHashMap<String, List<String>>();
			params.put("filter_reportingcategory", new ArrayList<String>(){{add("KPIs");}});
			labels.putAll(configBsandPlService.getLabels(params));
			model.addAttribute("labels", labels);
			
			
		}
		model.addAttribute("internalEntities", internalEntitiesService.getAllInternalEntityNames());
		model.addAttribute("year", year);
		model.addAttribute("entity", entity);
		model.addAttribute("apiUri", config.getAPIUrl());
		return "whizzer/profitLossReport";
	}

	

}	
