package ro.allevo.fintpui.controllers;

import javax.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
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
import ro.allevo.fintpui.model.AccountingCode;
import ro.allevo.fintpui.service.AccountingCodeService;
import ro.allevo.fintpui.service.MessageTypeService;
import ro.allevo.fintpui.utils.JSONHelper;
import ro.allevo.fintpui.utils.PagedCollection;
import ro.allevo.fintpui.utils.Roles;
import ro.allevo.fintpui.utils.datatables.DataTables;

@Controller
@RequestMapping(value = "/accounting-codes")
public class AccountingCodeController {

	@Autowired
	Config config;
	
	@Autowired
	MessageTypeService messageTypeService;

	@Autowired
	private AccountingCodeService accountingCodeService;

	private static Logger logger = LogManager.getLogger(TimeLimitsController.class.getName());

	/*
	 * DISPLAY
	 */
	@GetMapping
	public String printAccountingCode(OAuth2Authentication auth, ModelMap model)
			throws JsonProcessingException {
		logger.info("/accountingCodes required");
		if (!auth.getAuthorities().contains(new SimpleGrantedAuthority(Roles.INTERNAL_ENTITIES_LIST_VIEW)))
			throw new NotAuthorizedException();
		model.addAttribute("hasModifyRole",
				auth.getAuthorities().contains(new SimpleGrantedAuthority(Roles.INTERNAL_ENTITIES_LIST_MODIFY)));
		model.addAttribute("messageType", JSONHelper.toString(messageTypeService.getAllMessageTypeByArea()));
		model.addAttribute("apiUri", config.getAPIUrl());
		return "accountingCodes";
	}

	/*
	 * DISPLAY
	 */
	@GetMapping(value = "/page")
	public @ResponseBody String getAccountingCodesJson(@RequestParam int draw) throws JsonProcessingException {
		logger.info("/accountingCode required");
		PagedCollection<AccountingCode> accountingCode = accountingCodeService.getPage();

		DataTables dt = new DataTables();
		if (null != accountingCode) {
			dt.setData(accountingCode.getItems());
			dt.setRecordsFiltered(accountingCode.getTotal());
			dt.setRecordsTotal(accountingCode.getTotal());
		}
		dt.setDraw(draw);
		return JSONHelper.toString(dt);
	}

	/*
	 * INSERT
	 */
	@GetMapping(value = "/add")
	public String addAccountingCode(ModelMap model, @ModelAttribute("accountingCode") AccountingCode accountingCode) {
		logger.info("/addAccountingCode required");
		model.addAttribute("accountingCode", accountingCode);
		model.addAttribute("formAction", "accounting-codes/insert");
		model.addAttribute("messageType", messageTypeService.getAllMessageTypeByArea());
		return "accountingCode_add";
	}

	@PostMapping(value = "/insert")
	@ResponseBody
	public String insertAccountingCode(ModelMap model, @Valid @ModelAttribute("accountingCode") AccountingCode accountingCode, BindingResult bindingResult)
			throws JsonProcessingException {
		logger.info("insert accountingCode requested");
		if (bindingResult.hasErrors()) {
			return JSONHelper.toString(bindingResult.getAllErrors());
		}
		accountingCodeService.insertAccountingCode(accountingCode);
		return "[]";
	}

	/*
	 * DELETE
	 */
	@RequestMapping(value = "/{id}/delete")
	public String deleteAccountingCode(@PathVariable String id) {
		logger.info("delete accountingCode required");
		accountingCodeService.deleteAccountingCode(id);
		return "redirect:/accounting-codes";
	}


	/*
	 * EDIT
	 */
	@GetMapping(value = "/{id}/edit")
	public String editAccountingCode(ModelMap model, @ModelAttribute("accountingCode") AccountingCode accountingCode, @PathVariable String id) {
		logger.info("/editAccountingCode requested");
		accountingCode = accountingCodeService.getAccountingCode(id);
		model.addAttribute("accountingCode", accountingCode);
		model.addAttribute("formAction", "accounting-codes/update");
		model.addAttribute("messageType", messageTypeService.getAllMessageTypeByArea());
		return "accountingCode_add";
	}

	@PostMapping(value = "/update")
	@ResponseBody
	public String updateAccountingCode(ModelMap model, @ModelAttribute("accountingCode") @Valid AccountingCode accountingCode, BindingResult bindingResult,
			@RequestParam("id") String id) throws JsonProcessingException {
		logger.info("update accountingCode requested");
		if (bindingResult.hasErrors()) {
			return JSONHelper.toString(bindingResult.getAllErrors());
		}
		accountingCodeService.updateAccountingCode(accountingCode, id);
		return "[]";
	}

}
