package ro.allevo.whizzer.controllers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;

import ro.allevo.fintpui.service.InternalAccountService;
import ro.allevo.fintpui.service.InternalEntitiesService;
import ro.allevo.fintpui.utils.JSONHelper;
import ro.allevo.fintpui.utils.PagedCollection;
import ro.allevo.fintpui.utils.datatables.DataTables;
import ro.allevo.whizzer.model.CashflowForecast;
import ro.allevo.whizzer.service.CashflowForecastService;

@Controller()
@RequestMapping(value = "/whizzer/cashflowForecast")
public class CashflowForecastController {

	@Autowired
	private InternalEntitiesService internalEntitiesService;
	
	@Autowired
	private InternalAccountService internalAccountService;
	
	@Autowired
	private CashflowForecastService cashflowForecastService;
	
	private static Logger logger = LogManager.getLogger(CashflowForecastController.class.getName());

	@GetMapping
	public String printCashReporting(@RequestParam(required = false) Integer year,@RequestParam(required = false) String entity, 
			ModelMap model, OAuth2Authentication auth) {	
		model.addAttribute("hasModifyRole",true);//Roles.hasRoles(auth,Roles.BANKS_LIST_MODIFY));
		model.addAttribute("internalEntities", internalEntitiesService.getAllInternalEntityNames());
		model.addAttribute("entity", entity);
		return "whizzer/cashflowForecast";
	}

	/*
	 * DISPLAY
	 */
	@GetMapping(value = "report/page")
	public @ResponseBody String getCashflowForecastsReportJson(@RequestParam int draw) throws JsonProcessingException {
		logger.info("/cashflowForecasts required");
		PagedCollection<CashflowForecast> cashflowForecasts = cashflowForecastService.getPage();

		DataTables dt = new DataTables();
		if (null != cashflowForecasts) {
			dt.setData(cashflowForecasts.getItems());
			dt.setRecordsFiltered(cashflowForecasts.getTotal());
			dt.setRecordsTotal(cashflowForecasts.getTotal());
		}
		dt.setDraw(draw);
		return JSONHelper.toString(dt);
	}
	
	@GetMapping(value = "/page")
	public @ResponseBody String getCashflowForecastsJson(@RequestParam int draw,@RequestParam(required = false) String entity) throws JsonProcessingException {
		logger.info("/cashflowForecasts required");
		LinkedHashMap<String, List<String>> params = new LinkedHashMap<>();
		if(entity !=null) {
			params.put("filter_entity_exact", new ArrayList<String>(){{add(entity);}});
		}else {
			return null;
		}
		PagedCollection<CashflowForecast> cashflowForecasts = cashflowForecastService.getPage(params);

		DataTables dt = new DataTables();
		if (null != cashflowForecasts) {
			dt.setData(cashflowForecasts.getItems());
			dt.setRecordsFiltered(cashflowForecasts.getTotal());
			dt.setRecordsTotal(cashflowForecasts.getTotal());
		}
		dt.setDraw(draw);
		return JSONHelper.toString(dt);
	}

	/*
	 * INSERT
	 */
	@GetMapping(value = "/add")
	public String addCashflowForecast(ModelMap model, @ModelAttribute("cashflowForecast") CashflowForecast cashflowForecast, @RequestParam(required = false) String entity) {
		logger.info("/addCashflowForecast required");
		model.addAttribute("cashflowForecast", cashflowForecast);
		model.addAttribute("formAction", "cashflowForecast/insert");
		
		LinkedHashMap<String, List<String>> params = new LinkedHashMap<>();
		params.put("filter_entityName_exact", new ArrayList<String>(){{add(entity);}});
		model.addAttribute("internalAccFilteredByName", internalAccountService.getAllInternalAccounts(params));		
		return "whizzer/cashflowForecast_add";
	}

	@PostMapping(value = "/insert")
	@ResponseBody
	public String insertCashflowForecast(ModelMap model, @Valid @ModelAttribute("cashflowForecast") CashflowForecast cashflowForecast, BindingResult bindingResult)
			throws JsonProcessingException {
		logger.info("insert cashflowForecast requested");
		if (bindingResult.hasErrors()) {
			return JSONHelper.toString(bindingResult.getAllErrors());
		}
		cashflowForecastService.insertCashflowForecast(cashflowForecast);
		return "[]";
	}

	/*
	 * DELETE
	 */
	@RequestMapping(value = "/{id}/delete")
	public String deleteCashflowForecast(@PathVariable String id) {
		logger.info("delete cashflowForecast required");
		cashflowForecastService.deleteCashflowForecast(id);
		return "redirect:/whizzer/cashflowForecast";
	}

	/*
	 * EDIT
	 */
	@GetMapping(value = "/{id}/edit")
	public String editCashflowForecast(ModelMap model, @ModelAttribute("cashflowForecast") CashflowForecast cashflowForecast, @PathVariable String id) {
		logger.info("/editCashflowForecast requested");
		cashflowForecast = cashflowForecastService.getCashflowForecast(id);
		model.addAttribute("cashflowForecast", cashflowForecast);
		model.addAttribute("formAction", "cashflowForecast/update");
		model.addAttribute("entityIban", cashflowForecast.getOperationiban());
		
		String entity = cashflowForecast.getEntity();
		LinkedHashMap<String, List<String>> params = new LinkedHashMap<>();
		params.put("filter_entityName_exact", new ArrayList<String>(){{add(entity);}});
		model.addAttribute("internalAccFilteredByName", internalAccountService.getAllInternalAccounts(params));	
		return "whizzer/cashflowForecast_add";
	}

	@PostMapping(value = "/update")
	@ResponseBody
	public String updateCashflowForecast(ModelMap model, @Valid @ModelAttribute("cashflowForecast") CashflowForecast cashflowForecast, BindingResult bindingResult) throws JsonProcessingException {
		logger.info("update cashflowForecast requested");
		if (bindingResult.hasErrors()) {
			return JSONHelper.toString(bindingResult.getAllErrors());
		}
		cashflowForecastService.updateCashflowForecast(cashflowForecast, cashflowForecast.getId().toString());
		return "[]";
	}

}
