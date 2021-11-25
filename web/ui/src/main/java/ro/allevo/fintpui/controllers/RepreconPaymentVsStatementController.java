package ro.allevo.fintpui.controllers;


import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;

import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.exception.NotAuthorizedException;
import ro.allevo.fintpui.model.RepreconPaymentVsStatement;
import ro.allevo.fintpui.service.InternalAccountService;
//import ro.allevo.fintpui.service.InternalEntitiesService;
import ro.allevo.fintpui.service.RepreconPaymentVsStatementService;
import ro.allevo.fintpui.service.TemplateConfigOptionsService;
import ro.allevo.fintpui.utils.JSONHelper;
import ro.allevo.fintpui.utils.PagedCollection;
import ro.allevo.fintpui.utils.Roles;
import ro.allevo.fintpui.utils.Utils;
import ro.allevo.fintpui.utils.datatables.DataTables;



@Controller
@RequestMapping(value = "/repreconPaymentVsStatement")
public class RepreconPaymentVsStatementController {
	
	@Autowired
	Config config;

	@Autowired
	private RepreconPaymentVsStatementService repreconPaymentVsStatementService;
	
//	@Autowired
//	private InternalEntitiesService internalEntitiesService;
	
	@Autowired
	private TemplateConfigOptionsService templateConfigOptions;
	
	@Autowired
	InternalAccountService internalAccountService;
	
	private static Logger logger = LogManager.getLogger(RepreconPaymentVsStatementController.class.getName());
	private static final String INTERNALENTITY = "internalentity";
	private static final String INTERNALENTITIES = "internalEntities";
	private static final String INTERNALENTITIESNAME = "name";
	private static final String INTERNALACCOUNT = "internalaccount";
	private static final String INTERNALACCOUNTS = "internalAccounts";
	

	/*
	 * DISPLAY
	 */
	@GetMapping
	public String printRepreconPaymentVsStatement(OAuth2Authentication auth, ModelMap model)
			throws JsonProcessingException {
		logger.info("/repreconPaymentVsStatement required");
		if (!Roles.hasRoles(auth,Roles.RECONCILIATION_VIEW))
			throw new NotAuthorizedException();
		model.addAttribute("hasModifyRole",Roles.hasRoles(auth,Roles.RECONCILIATION_MODIFY));
		
		JSONObject entities= getEntities(new HashSet<>(Arrays.asList(INTERNALENTITY, INTERNALACCOUNT)));
		JSONArray internalEntity = Utils.getInternalEntities(entities, INTERNALENTITY, "id", INTERNALENTITIESNAME);
		JSONArray internalAccount = Utils.getInternalEntities(entities, INTERNALACCOUNT, "id", "currency");

		model.addAttribute("repreconPaymentVsStatement", JSONHelper.toString(repreconPaymentVsStatementService.getAllRepreconPaymentVsStatement()));
		model.addAttribute(INTERNALENTITIES, Utils.getValueFromEntity(internalEntity, INTERNALENTITIESNAME));
		model.addAttribute(INTERNALACCOUNTS, Utils.getValueFromEntity(internalAccount, "currency"));
		model.addAttribute("stmtNumber", JSONHelper.toString(repreconPaymentVsStatementService.getAllReconciliationStmtNumber()));
		model.addAttribute("apiUri", config.getAPIUrl());
		return "repreconPaymentVsStatement";
	}
	
	private JSONObject getEntities(Set<String> entities) {
		return templateConfigOptions.getAllConfingOptionsValues(entities);
	}
	
	/*
	 * DISPLAY
	 */
	@GetMapping(value = "/page")
	public @ResponseBody String getBanksJson(@RequestParam int draw) throws JsonProcessingException {
		logger.info("/repreconPaymentVsStatement required");
		PagedCollection<RepreconPaymentVsStatement> repreconPaymentVsStatement = repreconPaymentVsStatementService.getPage();

		DataTables dt = new DataTables();
		if (null != repreconPaymentVsStatement) {
			dt.setData(repreconPaymentVsStatement.getItems());
			dt.setRecordsFiltered(repreconPaymentVsStatement.getTotal());
			dt.setRecordsTotal(repreconPaymentVsStatement.getTotal());
		}
		dt.setDraw(draw);
		return JSONHelper.toString(dt);
	}
	

	
	
}
