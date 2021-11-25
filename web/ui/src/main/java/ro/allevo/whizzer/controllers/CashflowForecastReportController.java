package ro.allevo.whizzer.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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

import ro.allevo.fintpui.service.InternalEntitiesService;
import ro.allevo.whizzer.model.CashFlowForecastGenData;
import ro.allevo.whizzer.service.CashflowForecastReportService;

@Controller()
@RequestMapping(value = "/whizzer/cashflowForecastReport")
public class CashflowForecastReportController {
	
	@Autowired
	private InternalEntitiesService internalEntitiesService;
	
	@Autowired
	private CashflowForecastReportService cashflowForecastReportService;
	
	private static Logger logger = LogManager.getLogger(CashflowForecastReportController.class.getName());

	@GetMapping
	public String printCashReporting(@RequestParam(name = "dateCashFlowReport", required = false) String date, @RequestParam(name = "entityCashFlowReport", required = false) String entity, 
			ModelMap model, OAuth2Authentication auth) {
		
		logger.info("/cashflowForecastReports required");
		model.addAttribute("hasModifyRole",true);//Roles.hasRoles(auth,Roles.BANKS_LIST_MODIFY));
		model.addAttribute("internalEntities", internalEntitiesService.getAllInternalEntityNames());
		model.addAttribute("title", "Cashflow Forecast Report");
		
		LinkedHashMap<String, List<String>> params = new LinkedHashMap<>();
		if(!(date==null || entity ==null)) {
			params.put("filter_cfforecastdate_exact", new ArrayList<String>(){{add(date);}});
			params.put("filter_entity_exact", new ArrayList<String>(){{add(entity);}});
			CashFlowForecastGenData[] cashFlowForecastGenData = cashflowForecastReportService.getPage(params).getItems();
			model.addAttribute("cashFlowForecastGenDataReport", (cashFlowForecastGenData!=null && cashFlowForecastGenData.length>0)?cashFlowForecastGenData:null);
			
			
			// must be displayed in a certain order (check xsls, sheet "A.1 Output Forecast CF" )
			// some arrays can have multiple object for same indicators that must be sorted by iban
			CashFlowForecastGenData[] initialAccountBalance = getCashFlowGenDataFilteredAndSorted(cashFlowForecastGenData, "Initial Account Balance");
			CashFlowForecastGenData[] companyInitialBalance = getCashFlowGenDataFilteredAndSorted(cashFlowForecastGenData, "SOLD INITIAL COMPANIE");
			CashFlowForecastGenData[] totalCollInAccount = getCashFlowGenDataFilteredAndSorted(cashFlowForecastGenData, "Total Collections in Account");
			CashFlowForecastGenData[] totalCollInCompany = getCashFlowGenDataFilteredAndSorted(cashFlowForecastGenData, "TOTAL COLLECTIONS IN COMPANY");
			CashFlowForecastGenData[] totalPaymFromAccount = getCashFlowGenDataFilteredAndSorted(cashFlowForecastGenData, "Total Payments from Account");
			CashFlowForecastGenData[] totalPaymFromCompany = getCashFlowGenDataFilteredAndSorted(cashFlowForecastGenData, "TOTAL PAYMENTS FROM COMPANY");
			CashFlowForecastGenData[] accountDeficitExcedent = getCashFlowGenDataFilteredAndSorted(cashFlowForecastGenData, "Account Excedent/ Deficit");
			CashFlowForecastGenData[] companyDeficitExcedent = getCashFlowGenDataFilteredAndSorted(cashFlowForecastGenData, "COMPANY EXCEDENT/ DEFICIT");
			
			model.addAttribute("initialAccountBalance", (initialAccountBalance!=null && initialAccountBalance.length>0)?initialAccountBalance:null);
			model.addAttribute("companyInitialBalance", (companyInitialBalance!=null && companyInitialBalance.length>0)?companyInitialBalance:null);
			model.addAttribute("totalCollInAccount", (totalCollInAccount!=null && totalCollInAccount.length>0)?totalCollInAccount:null);
			model.addAttribute("totalCollInCompany", (totalCollInCompany!=null && totalCollInCompany.length>0)?totalCollInCompany:null);
			model.addAttribute("totalPaymFromAccount", (totalPaymFromAccount!=null && totalPaymFromAccount.length>0)?totalPaymFromAccount:null);
			model.addAttribute("totalPaymFromCompany", (totalPaymFromCompany!=null && totalPaymFromCompany.length>0)?totalPaymFromCompany:null);
			model.addAttribute("accountDeficitExcedent", (accountDeficitExcedent!=null && accountDeficitExcedent.length>0)?accountDeficitExcedent:null);
			model.addAttribute("companyDeficitExcedent", (companyDeficitExcedent!=null && companyDeficitExcedent.length>0)?companyDeficitExcedent:null);
		}
		
		model.addAttribute("date", date);
		model.addAttribute("entity", entity);

		return "whizzer/cashflowForecastReport";
	}
	
	private CashFlowForecastGenData[] getCashFlowGenDataFilteredAndSorted(CashFlowForecastGenData[] cashFlowForecastGenData, String indicator) {
		CashFlowForecastGenData[] cashFlowForecastGenDataFiltered = Arrays.stream(cashFlowForecastGenData).filter(cashFlowForecast -> cashFlowForecast.getIndicator().contains(indicator)).toArray(CashFlowForecastGenData[]::new);
		if(cashFlowForecastGenDataFiltered.length > 1 && cashFlowForecastGenDataFiltered[0].getOperationiban() != null) {
			CashFlowForecastGenData[] cashFlowForecastGenDataSorted = Arrays.stream(cashFlowForecastGenDataFiltered).sorted(Comparator.comparing(CashFlowForecastGenData::getOperationiban)).toArray(CashFlowForecastGenData[]::new);
			return cashFlowForecastGenDataSorted;
		}

		return cashFlowForecastGenDataFiltered;
	}

}
