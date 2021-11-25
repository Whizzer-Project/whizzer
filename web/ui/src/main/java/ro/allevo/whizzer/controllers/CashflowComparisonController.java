package ro.allevo.whizzer.controllers;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
import org.springframework.web.util.HtmlUtils;

import com.fasterxml.jackson.core.JsonProcessingException;

import ro.allevo.fintpui.model.InternalAccount;
import ro.allevo.fintpui.model.User;
import ro.allevo.fintpui.service.InternalAccountService;
import ro.allevo.fintpui.service.InternalEntitiesService;
import ro.allevo.fintpui.service.UserService;
import ro.allevo.fintpui.utils.JSONHelper;
import ro.allevo.fintpui.utils.PagedCollection;
import ro.allevo.fintpui.utils.datatables.DataTables;
import ro.allevo.whizzer.model.CashflowComparison;
import ro.allevo.whizzer.service.CashflowComparisonService;

@Controller()
@RequestMapping(value = "/whizzer/cashflowComparison")
public class CashflowComparisonController {

	@Autowired
	private InternalEntitiesService internalEntitiesService;
	
	@Autowired
	private InternalAccountService internalAccountService;
	
	@Autowired
	private CashflowComparisonService cashflowComparisonService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	HttpServletRequest request;
	
	private static Logger logger = LogManager.getLogger(CashflowComparisonController.class.getName());

	@GetMapping("report")
	public String printCashReporting(@RequestParam(required = false) String date, @RequestParam(required = false) String entity, 
			ModelMap model, OAuth2Authentication auth) {	
		model.addAttribute("hasModifyRole",true);//Roles.hasRoles(auth,Roles.BANKS_LIST_MODIFY));
		model.addAttribute("internalEntities", internalEntitiesService.getAllInternalEntityNames());
		model.addAttribute("entity", entity);
		model.addAttribute("date", date);
		
		model.addAttribute("cashflowComparison", (entity!=null && date!=null)?cashflowComparisonService.getPageReport(HtmlUtils.htmlEscape(entity) , date):null);
		return "whizzer/cashflowComparisonReport";
	}

	@GetMapping
	public String printCashReportingReport(@RequestParam(required = false) String date, @RequestParam(required = false) String entity, 
			ModelMap model, OAuth2Authentication auth) {	
		model.addAttribute("hasModifyRole",true);//Roles.hasRoles(auth,Roles.BANKS_LIST_MODIFY));
		model.addAttribute("internalEntities", internalEntitiesService.getAllInternalEntityNames());
		model.addAttribute("entity", entity);
		model.addAttribute("date", date);
		if(date!=null) {
			try {
				model.addAttribute("dateISO", new SimpleDateFormat("yyyy-MM-dd").parse(date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		LinkedHashMap<String, List<String>> params = new LinkedHashMap<>();
		if(!(date==null && entity ==null)) {
			params.put("filter_accountbalancedate_exact", new ArrayList<String>(){{add(String.valueOf(date));}});
			params.put("filter_entity_exact", new ArrayList<String>(){{add(entity);}});
			model.addAttribute("amount", Arrays.asList(cashflowComparisonService.getPage(params).getItems()).stream().map(o -> o.getRonaccountbalanceamount()).reduce(BigDecimal.ZERO, (a, b) -> a.add(b)));	
		}
		return "whizzer/cashflowComparison";
	}
	
	
	@GetMapping(value = "/page")
	public @ResponseBody String getCashflowComparisonsJson(@RequestParam int draw, @RequestParam(required = false) String date, @RequestParam(required = false) String entity) throws JsonProcessingException {
		logger.info("/cashflowComparison required");
		
		LinkedHashMap<String, List<String>> params = new LinkedHashMap<>();
		if(!(date==null || entity ==null)) {
			params.put("filter_accountbalancedate_exact", new ArrayList<String>(){{add(String.valueOf(date));}});
			params.put("filter_entity_exact", new ArrayList<String>(){{add(entity);}});
		}
		
		PagedCollection<CashflowComparison> cashflowComparison = cashflowComparisonService.getPage(params);
		
		DataTables dt = new DataTables();
		if (null != cashflowComparison) {
			dt.setData(cashflowComparison.getItems());
			dt.setRecordsFiltered(cashflowComparison.getTotal());
			dt.setRecordsTotal(cashflowComparison.getTotal());
		}
		dt.setDraw(draw);
		return JSONHelper.toString(dt);
	}

	/*
	 * INSERT
	 */
	@GetMapping(value = "/add")
	public String addCashflowComparison(ModelMap model, @ModelAttribute("cashflowComparison") CashflowComparison cashflowComparison, @RequestParam("entity") String entity)  {
		logger.info("/addCashflowComparison required");
		model.addAttribute("cashflowComparison", cashflowComparison);
		model.addAttribute("formAction", "cashflowComparison/insert");
		
		InternalAccount[] internalAccFilteredByName = getInternalAccountFilteredByEntityName(entity);
		model.addAttribute("internalAccFilteredByName", internalAccFilteredByName);	
		
		return "whizzer/cashflowComparison_add";
	}
	
	@GetMapping(value = "/getCorespCurrency")
	@ResponseBody
	public String getDefaultAccount(@RequestParam("iban") String iban, @RequestParam("entity") String entity) throws Exception {
		
		InternalAccount[] internalAccFilteredByName = getInternalAccountFilteredByEntityName(entity);

		Map<String, String> internalAccPairIbanCurrency = new HashMap<String, String>();
		for(InternalAccount internalAcc : internalAccFilteredByName) {
			internalAccPairIbanCurrency.put(internalAcc.getAccountNumber(), internalAcc.getCurrency());
		}
		return internalAccPairIbanCurrency.get(iban);
	}

	@PostMapping(value = "/insert")
	@ResponseBody
	public String insertCashflowComparison(ModelMap model, @Valid @ModelAttribute("cashflowComparison") CashflowComparison cashflowComparison, BindingResult bindingResult)
			throws JsonProcessingException {
		logger.info("insert cashflowComparison requested");
		if (bindingResult.hasErrors()) {
			return JSONHelper.toString(bindingResult.getAllErrors());
		}
		cashflowComparison.setUserid(getUserId());
		cashflowComparisonService.insertCashflowComparison(cashflowComparison);
		return "[]";
	}
	private int getUserId() {
		HttpSession session = request.getSession();
		String userName = session.getAttribute("userName").toString();
		User user = userService.getUser(userName);
		return user.getId();
	}
	/*
	 * DELETE
	 */
	@RequestMapping(value = "/{id}/delete")
	public String deleteCashflowComparison(@PathVariable String id) {
		logger.info("delete cashflowComparison required");
		cashflowComparisonService.deleteCashflowComparison(id);
		return "redirect:/whizzer/cashflowComparison";
	}

	/*
	 * EDIT
	 */
	@GetMapping(value = "/{id}/edit")
	public String editCashflowComparison(ModelMap model, @ModelAttribute("cashflowComparison") CashflowComparison cashflowComparison, @PathVariable String id, @RequestParam("entity") String entity) {
		logger.info("/editCashflowComparison requested");
		cashflowComparison = cashflowComparisonService.getCashflowComparison(id);
		model.addAttribute("cashflowComparison", cashflowComparison);
		model.addAttribute("formAction", "cashflowComparison/update");
		InternalAccount[] internalAccFilteredByName = getInternalAccountFilteredByEntityName(entity);
		model.addAttribute("internalAccFilteredByName", internalAccFilteredByName);	
		return "whizzer/cashflowComparison_add";
	}

	@PostMapping(value = "/update")
	@ResponseBody
	public String updateCashflowComparison(ModelMap model, @ModelAttribute("cashflowComparison") @Valid CashflowComparison cashflowComparison, BindingResult bindingResult,
			@RequestParam("id") String id) throws JsonProcessingException {
		logger.info("update cashflowComparison requested");
		if (bindingResult.hasErrors()) {
			return JSONHelper.toString(bindingResult.getAllErrors());
		}
		cashflowComparison.setUserid(getUserId());
		cashflowComparisonService.updateCashflowComparison(cashflowComparison, id);
		return "[]";
	}
	
	private InternalAccount[] getInternalAccountFilteredByEntityName(String entityName) {
		LinkedHashMap<String, List<String>> params = new LinkedHashMap<>();
		params.put("filter_entityName_exact", new ArrayList<String>(){{add(entityName);}});
		return internalAccountService.getAllInternalAccounts(params);
	}

}
