package ro.allevo.whizzer.controllers;

import java.sql.Timestamp;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.QueryParam;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.multipart.MultipartFile;
import java.math.BigDecimal;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.controllers.TimeLimitsController;
import ro.allevo.fintpui.exception.NotAuthorizedException;
import ro.allevo.fintpui.service.InternalEntitiesService;
import ro.allevo.fintpui.utils.Filters;
import ro.allevo.fintpui.utils.JSONHelper;
import ro.allevo.fintpui.utils.Roles;
import ro.allevo.whizzer.model.BalanceSheetLoadHistory;
import ro.allevo.whizzer.model.ConfigBsandPl;
import ro.allevo.whizzer.model.ProfitLossLoadHistory;
import ro.allevo.whizzer.service.BalanceSheetLoadHistoryService;
import ro.allevo.whizzer.service.ConfigBsandPlService;
import ro.allevo.whizzer.service.ProfitLossLoadHistoryService;

@Controller
@RequestMapping("/whizzer")
public class WhizzerController {
	
	@Autowired
	private Config config;
	
	@Autowired
	private InternalEntitiesService internalEntitiesService;
	
	@Autowired
	private BalanceSheetLoadHistoryService balanceSheetService;
	
	@Autowired
	private ProfitLossLoadHistoryService profitLossService;
	
	@Autowired
	private ConfigBsandPlService configBsandPlService;
	
	@Autowired
	HttpServletRequest request;
	
	private static Logger logger = LogManager.getLogger(TimeLimitsController.class.getName());
	
	@GetMapping(value = "/inputIndicators")
	public String openInputIndicators(OAuth2Authentication auth, ModelMap model, @RequestParam(required = false) Integer year,@RequestParam(required = false) String entity) {
		logger.info("/connection required");
		if (!Roles.hasRoles(auth,Roles.BALANCE_SHEET_MODIFY))
			throw new NotAuthorizedException();
		
		model.addAttribute("hasModifyRole", Roles.hasRoles(auth,Roles.BALANCE_SHEET_MODIFY));
		model.addAttribute("apiUri", config.getAPIUrl());

		
		model.addAttribute("internalEntities", internalEntitiesService.getAllInternalEntities());
		model.addAttribute("year", year);
		model.addAttribute("entity", entity);
		return "whizzer/inputIndicators";
	}	
	
	@GetMapping(value = "/loadHistory")
	@ResponseBody
	public String getLoadHistory(OAuth2Authentication auth,ModelMap model, @QueryParam(value = "year") String year, @QueryParam(value = "entityName") String entityName) throws JsonProcessingException{		
		if (!Roles.hasRoles(auth,Roles.BALANCE_SHEET_MODIFY))
			throw new NotAuthorizedException();
		
		LinkedHashMap<String, List<String>> paramsLoadHistory = new LinkedHashMap<>();
		LinkedHashMap<String, List<String>> paramsConfigBsAndPl = new LinkedHashMap<>();
		
		if(!(year==null || entityName ==null)) {
			paramsLoadHistory.put("filter_year_exact", Filters.getFiltersParams(year));
			paramsLoadHistory.put("filter_entity_exact", Filters.getFiltersParams(entityName));
			
			BalanceSheetLoadHistory[] balanceSheet = balanceSheetService.getBalanceSheets(paramsLoadHistory);
			ProfitLossLoadHistory[] profitLoss = profitLossService.getProfitLosses(paramsLoadHistory);
			
			model.addAttribute("balanceSheetLoadHistory", balanceSheet.length == 1?balanceSheet[0]:new BalanceSheetLoadHistory());
			model.addAttribute("profitLossLoadHistory", profitLoss.length == 1?profitLoss[0]:new ProfitLossLoadHistory());
			
			HashMap<String,String> mandatoryOrOptionalFieldsBS = new HashMap<>();			
			paramsConfigBsAndPl.put("filter_reportingcategory_exact", Filters.getFiltersParams("Balance Sheet"));
			mandatoryOrOptionalFieldsBS.putAll(configBsandPlService.getMandatoryAndOptionalFields(paramsConfigBsAndPl));
			model.addAttribute("bsMandOptionFields", mandatoryOrOptionalFieldsBS);
			model.addAttribute("bsSameMandatoryType", pairSameMandatoryType(mandatoryOrOptionalFieldsBS)); 
			model.addAttribute("bsLabels", configBsandPlService.getLabels(paramsConfigBsAndPl));
			
			paramsConfigBsAndPl.clear();

			HashMap<String,String> mandatoryOrOptionalFieldsPL = new HashMap<>();
			paramsConfigBsAndPl.put("filter_reportingcategory_exact", Filters.getFiltersParams("Profit and Loss"));
			mandatoryOrOptionalFieldsPL.putAll(configBsandPlService.getMandatoryAndOptionalFields(paramsConfigBsAndPl));
			model.addAttribute("plMandOptionFields", mandatoryOrOptionalFieldsPL);
			model.addAttribute("plSameMandatoryType", pairSameMandatoryType(mandatoryOrOptionalFieldsPL)); 
			model.addAttribute("plLabels", configBsandPlService.getLabels(paramsConfigBsAndPl));
			
		}
		return JSONHelper.toString(model);
	}
	
	@PostMapping(value = "/updateInsertInputIndicators")
	@ResponseBody
	public String updateInsertIndicators(OAuth2Authentication auth, @ModelAttribute("balanceSheetLoadHistory") BalanceSheetLoadHistory balanceSheetLoadHistory, BindingResult bindingResultBS, @ModelAttribute("profitLossLoadHistory") ProfitLossLoadHistory profitLossLoadHistory, BindingResult bindingResultPL, 
			@QueryParam(value = "id") String id, @QueryParam(value = "inputIndicator") String inputIndicator, @QueryParam(value = "uiOrExcell") String uiOrExcell) {
		
		if (!Roles.hasRoles(auth,Roles.BALANCE_SHEET_MODIFY))
			throw new NotAuthorizedException();
		
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		HttpSession session = request.getSession();
		String userName = session.getAttribute("userName").toString();
				
		if(inputIndicator.equalsIgnoreCase("bs")) {
			balanceSheetLoadHistory.setInsertdate(timestamp);
			balanceSheetLoadHistory.setUsername(userName);		
			balanceSheetLoadHistory.setSource(uiOrExcell);
			if(id == null) {
				balanceSheetService.insertBalanceSheet(balanceSheetLoadHistory);
			}else {
				balanceSheetService.updateBalanceSheet(balanceSheetLoadHistory, id);
			}
			
		}else if(inputIndicator.equalsIgnoreCase("pl") || inputIndicator.equalsIgnoreCase("lp")) {
			profitLossLoadHistory.setInsertdate(timestamp);
			profitLossLoadHistory.setUsername(userName);
			profitLossLoadHistory.setSource(uiOrExcell);
			if(id == null) {
				profitLossService.insertProfitLoss(profitLossLoadHistory);
			}else {
				profitLossService.updateProfitLoss(profitLossLoadHistory, id);
			}			
		}				
		return "[]";
	}

	@PostMapping("/upload/{type}") 
	public ResponseEntity<?> handleFileUpload(OAuth2Authentication auth, @RequestParam("file") MultipartFile file, @PathVariable("type") String type ) {
		if (!Roles.hasRoles(auth,Roles.BALANCE_SHEET_MODIFY))
			throw new NotAuthorizedException();
		String message = "";
		Map<String,BigDecimal> xlsData = new HashMap<>();
		HSSFWorkbook wb = null;
		try {
			wb = new HSSFWorkbook(file.getInputStream());
			HSSFSheet sheet=wb.getSheetAt(0);  
			FormulaEvaluator formulaEvaluator=wb.getCreationHelper().createFormulaEvaluator();  
			for(Row row: sheet) {  
				Cell cellIndicator = row.getCell(1);
				if(cellIndicator!=null)
					switch(formulaEvaluator.evaluateInCell(cellIndicator).getCellType()){  
						case Cell.CELL_TYPE_NUMERIC: 
							Cell cellValue = row.getCell(4, Row.RETURN_BLANK_AS_NULL); 
							if(cellValue == null) {
								xlsData.put("omfp"+(int) Math.round(cellIndicator.getNumericCellValue()),null);
							}
							if(cellValue!=null && cellValue.getCellType()==Cell.CELL_TYPE_NUMERIC )
								xlsData.put("omfp"+(int) Math.round(cellIndicator.getNumericCellValue()), 
											cellValue!=null?new BigDecimal(cellValue.getNumericCellValue()):null);
						break; 
					}  
			}  
		
			List<ConfigBsandPl> labels = configBsandPlService.getLabelsList();
			labels = labels.stream().filter(c -> c.getReportingcategory().equals(type.equals("bsFileLoad")?"Balance Sheet":"Profit and Loss")).collect(Collectors.toList());
			for (String key : xlsData.keySet()) {
				if(xlsData.get(key)==null) {
					List<ConfigBsandPl> labelList = labels.stream().filter(c -> c.getName().equals(key)).collect(Collectors.toList());
					ConfigBsandPl label = (!labelList.isEmpty()?labelList.get(0):null);
				    boolean mandatory = (label!=null?label.getMandatory():"").equals("M");
				    message += mandatory?label.getLabel()+" is required! <br>":"";
				}
			}
			
			if(message.isEmpty()) {
				ObjectMapper mapper = new ObjectMapper(); 
				if(type.equals("bsFileLoad")) {
					return ResponseEntity.ok( mapper.convertValue(xlsData, BalanceSheetLoadHistory.class));
				}else {
					return ResponseEntity.ok( mapper.convertValue(xlsData, ProfitLossLoadHistory.class));
				}
			}
		}catch(java.lang.IllegalStateException e) {
			message += e.getMessage();
		}catch (IOException e) {
			message += e.getMessage();
			e.printStackTrace();
		}catch (Exception e) {
			message += "The file does not respect the data structure.";
			e.printStackTrace();
		}finally{
			try {
				if(wb!=null)
				wb.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return ResponseEntity.ok(message);
	}
	
	private Map<String, List<String>> pairSameMandatoryType(Map<String, String> mandatoryFields){
		LinkedHashMap<String, List<String>> result = new LinkedHashMap<>();
		for (Map.Entry<String, String> entry : mandatoryFields.entrySet()) {
			if(entry.getValue().contains("C")) {
				List<String> values = null;
				if(result.containsKey(entry.getValue())) {
					values = result.get(entry.getValue());					
				}else {
					values = new ArrayList<String>();
				}
				values.add(entry.getKey());
				result.put(entry.getValue(), values);
			}
		}
		
		return result;
	}

}
