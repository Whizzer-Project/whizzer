package ro.allevo.fintpui.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import ro.allevo.fintpui.model.RepreconInvVsStatement;
import ro.allevo.fintpui.service.InternalAccountService;
import ro.allevo.fintpui.service.InternalEntitiesService;
import ro.allevo.fintpui.service.RepreconInvSuppVsStatementService;
import ro.allevo.fintpui.utils.JSONHelper;
import ro.allevo.fintpui.utils.PagedCollection;
import ro.allevo.fintpui.utils.Roles;
import ro.allevo.fintpui.utils.datatables.DataTables;



@Controller
@RequestMapping(value = "/repreconInvSuppVsStatement")
public class RepreconInvSuppVsStatementController {
	
	@Autowired
	Config config;

	@Autowired
	private RepreconInvSuppVsStatementService repreconInvSuppVsStatementService;
	
	@Autowired
	private InternalEntitiesService internalEntitiesService;
	
	@Autowired
	InternalAccountService internalAccountService;
	
	private static Logger logger = LogManager.getLogger(TimeLimitsController.class.getName());

	/*
	 * DISPLAY
	 */
	@GetMapping
	public String printRepreconInvSuppVsStatement(OAuth2Authentication auth, ModelMap model)
			throws JsonProcessingException {
		logger.info("/repreconInvSuppVsStatement required");
		if (!Roles.hasRoles(auth,Roles.RECONCILIATION_VIEW))
			throw new NotAuthorizedException();
		model.addAttribute("hasModifyRole",Roles.hasRoles(auth,Roles.RECONCILIATION_MODIFY));

		model.addAttribute("repreconInvSuppVsStatement", JSONHelper.toString(repreconInvSuppVsStatementService.getAllRepreconInvSuppVsStatement()));
		model.addAttribute("internalEntities", JSONHelper.toString(internalEntitiesService.getAllInternalEntityNames()));
		model.addAttribute("internalAccounts", JSONHelper.toString(internalAccountService.getAllInternalAccountsCurrency()));
		model.addAttribute("stmtNumber", JSONHelper.toString(repreconInvSuppVsStatementService.getAllReconciliationStmtNumber()));
		model.addAttribute("apiUri", config.getAPIUrl());
		return "repreconInvSuppVsStatement";
	}
	

	
	/*
	 * DISPLAY
	 */
	@GetMapping(value = "/page")
	public @ResponseBody String getRepreconInvSuppVsStatementJson(@RequestParam int draw) throws JsonProcessingException {
		logger.info("/repreconInvSuppVsStatement required");
		PagedCollection<RepreconInvVsStatement> repreconInvSuppVsStatement = repreconInvSuppVsStatementService.getPage();

		DataTables dt = new DataTables();
		if (null != repreconInvSuppVsStatement) {
			dt.setData(repreconInvSuppVsStatement.getItems());
			dt.setRecordsFiltered(repreconInvSuppVsStatement.getTotal());
			dt.setRecordsTotal(repreconInvSuppVsStatement.getTotal());
		}
		dt.setDraw(draw);
		return JSONHelper.toString(dt);
	}
	

	
	
}
