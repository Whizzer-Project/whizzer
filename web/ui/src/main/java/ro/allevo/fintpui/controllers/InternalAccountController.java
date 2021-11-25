package ro.allevo.fintpui.controllers;

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

import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.exception.NotAuthorizedException;
import ro.allevo.fintpui.model.InternalAccount;
import ro.allevo.fintpui.service.BankService;
import ro.allevo.fintpui.service.InternalAccountService;
import ro.allevo.fintpui.service.InternalEntitiesService;
import ro.allevo.fintpui.utils.Filters;
import ro.allevo.fintpui.utils.JSONHelper;
import ro.allevo.fintpui.utils.PagedCollection;
import ro.allevo.fintpui.utils.Roles;
import ro.allevo.fintpui.utils.datatables.DataTables;

@Controller
@RequestMapping(value = "/internal-accounts")
public class InternalAccountController {
	@Autowired
	Config config;

	@Autowired
	private InternalAccountService intAccountService;

	@Autowired
	private BankService banksService;

	@Autowired
	private InternalEntitiesService internalEntitiesService;

	private static Logger logger = LogManager.getLogger(InternalAccountController.class.getName());

	/*
	 * DISPLAY
	 */
	@GetMapping
	public String printInternalAccounts(OAuth2Authentication auth, ModelMap model) throws JsonProcessingException {
		logger.info("/internalAccounts required");
		if (!Roles.hasRoles(auth, Roles.INTERNAL_ENTITIES_LIST_VIEW))
			throw new NotAuthorizedException();
		model.addAttribute("bics", JSONHelper.toString(banksService.getAllListBankBics()));
		model.addAttribute("entityNames", JSONHelper.toString(internalEntitiesService.getAllInternalEntityNames()));
		model.addAttribute("showNumberId", config.getProjectType().equalsIgnoreCase("adpharma"));
		System.out.println("internal accounts" + auth.getAuthorities());

		model.addAttribute("hasModifyRole", Roles.hasRoles(auth, Roles.INTERNAL_ENTITIES_LIST_MODIFY));

		return "internalAccounts";
	}

	@GetMapping(value = "/page")
	public @ResponseBody String getInternalAccountsJson(@RequestParam int draw) throws JsonProcessingException {
		logger.info("/internalAccounts required");
		PagedCollection<InternalAccount> internalAccounts = intAccountService.getPage();

		DataTables dt = new DataTables();
		if (null != internalAccounts) {
			dt.setData(internalAccounts.getItems());
			dt.setRecordsFiltered(internalAccounts.getTotal());
			dt.setRecordsTotal(internalAccounts.getTotal());
		}
		dt.setDraw(draw);
		return JSONHelper.toString(dt);
	}

	/*
	 * INSERT
	 */
	@GetMapping(value = "/add")
	public String addinternalAccount(ModelMap model, @ModelAttribute("internalAccount") InternalAccount internalAccount) {
		logger.info("/addinternalAccount required");
		model.addAttribute("internalAccount", internalAccount);

		model.addAttribute("bics", banksService.getAllBankBics());
		model.addAttribute("entityNames", internalEntitiesService.getAllInternalEntityNames());
		model.addAttribute("formAction", "internal-accounts/insert");
		model.addAttribute("showNumberId", config.getProjectType().equalsIgnoreCase("adpharma"));
		return "internalAccount_add";
	}

	@PostMapping(value = "/insert")
	@ResponseBody
	public String insertInternalAccount(ModelMap model,
			@ModelAttribute("internalAccount") @Valid InternalAccount internalAccount, BindingResult bindingResult)
			throws JsonProcessingException {
		logger.info("insert internalAccount requested");
		if (bindingResult.hasErrors()) {
			return JSONHelper.toString(bindingResult.getAllErrors());
		} else {
			intAccountService.insertInternalAccount(internalAccount);
			return "[]";
		}
	}

	private Integer getDefaultId(String name, Integer idEntity) {
		LinkedHashMap<String, List<String>> params = new LinkedHashMap<>();
		params.put("filter_entityName_exact", Filters.getFiltersParams(name));
		params.put("filter_defaultAccount_exact", Filters.getFiltersParams("Y"));
//		if (idEntity>0)
//		{
//			params.put("filter_id_exact", Filters.getFiltersParams(String.valueOf(idEntity)));
//		}
		InternalAccount[] internalAccount = intAccountService.getAllInternalAccounts(params);
		Integer id = 0;
		if (internalAccount.length > 0) {
			id = (int) internalAccount[0].getId();
			if (idEntity > 0 && id.compareTo(idEntity) == 0) {
				id = 0;
			}
		}
		return id;
	}

	@GetMapping(value = "/getDefaultAccount")
	public @ResponseBody String getDefaultAccount(@RequestParam("name") String name) throws Exception {
		return JSONHelper.toString(getDefaultId(name, 0));
	}

	/*
	 * EDIT
	 */
	@GetMapping(value = "/{id}/edit")
	public String editInternalAccount(ModelMap model,
			@ModelAttribute("internalAccount") InternalAccount internalAccount, @PathVariable String id) {
		logger.info("/editInternalAccount requested");
		
		internalAccount = intAccountService.getInternalAccount(id);
		
		model.addAttribute("internalAccount", intAccountService.getInternalAccount(id));

		model.addAttribute("bics", banksService.getAllBankBics());
		model.addAttribute("entityNames", internalEntitiesService.getAllInternalEntityNames());
		model.addAttribute("init_id", id);
		model.addAttribute("formAction", "internal-accounts/update");
		model.addAttribute("showNumberId", config.getProjectType().equalsIgnoreCase("adpharma"));
		model.addAttribute("defaultId", getDefaultId(internalAccount.getEntityName(), (int) internalAccount.getId()));
		return "internalAccount_add";
	}

	@PostMapping(value = "/update")
	@ResponseBody
	public String updateInternalAccount(ModelMap model,
			@ModelAttribute("internalAccount") @Valid InternalAccount internalAccount, BindingResult bindingResult,
			@RequestParam("id") String id) throws JsonProcessingException {
		logger.info("update internalAccount requested");
		if (bindingResult.hasErrors()) {
			return JSONHelper.toString(bindingResult.getAllErrors());
		}

		intAccountService.updateInternalAccount(internalAccount, id);
		return "[]";
	}

	/*
	 * DELETE
	 */
	@RequestMapping(value = "/{id}/delete")
	public String deleteInternalAccountk(@PathVariable String id) {
		logger.info("delete internalAccounts required");
		intAccountService.deleteInternalAccount(id);
		return "redirect:/internal-accounts";
	}

}
