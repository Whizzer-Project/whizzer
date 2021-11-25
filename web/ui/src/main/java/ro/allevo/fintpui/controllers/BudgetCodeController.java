package ro.allevo.fintpui.controllers;

import javax.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
import ro.allevo.fintpui.model.BudgetCode;
import ro.allevo.fintpui.service.BudgetCodeService;
import ro.allevo.fintpui.utils.JSONHelper;
import ro.allevo.fintpui.utils.PagedCollection;
import ro.allevo.fintpui.utils.Roles;
import ro.allevo.fintpui.utils.datatables.DataTables;

@Controller
@RequestMapping(value = "/budget-codes")
public class BudgetCodeController {

	@Autowired
	Config config;
	
	@Autowired
	private BudgetCodeService budgetCodeService;

	private static Logger logger = LogManager.getLogger(TimeLimitsController.class.getName());

	/*
	 * DISPLAY
	 */
	@GetMapping
	public String printBudgetCode(OAuth2Authentication auth, ModelMap model)
			throws JsonProcessingException {
		logger.info("/budgetCode required");
		if (!auth.getAuthorities().contains(new SimpleGrantedAuthority(Roles.INTERNAL_ENTITIES_LIST_VIEW)))
			throw new NotAuthorizedException();
		model.addAttribute("hasModifyRole",
				auth.getAuthorities().contains(new SimpleGrantedAuthority(Roles.INTERNAL_ENTITIES_LIST_MODIFY)));
		model.addAttribute("apiUri", config.getAPIUrl());
		return "budgetCodes";
	}

	/*
	 * DISPLAY
	 */
	@GetMapping(value = "/page")
	public @ResponseBody String getBudgetCodesJson(@RequestParam int draw) throws JsonProcessingException {
		logger.info("/budgetCode required");
		PagedCollection<BudgetCode> budgetCode = budgetCodeService.getPage();

		DataTables dt = new DataTables();
		if (null != budgetCode) {
			dt.setData(budgetCode.getItems());
			dt.setRecordsFiltered(budgetCode.getTotal());
			dt.setRecordsTotal(budgetCode.getTotal());
		}
		dt.setDraw(draw);
		return JSONHelper.toString(dt);
	}

	/*
	 * INSERT
	 */
	@GetMapping(value = "/add")
	public String addBudgetCode(ModelMap model, @ModelAttribute("budgetCode") BudgetCode budgetCode) {
		logger.info("/addBudgetCode required");
		model.addAttribute("budgetCode", budgetCode);
		model.addAttribute("formAction", "budget-codes/insert");
		return "budgetCode_add";
	}

	@PostMapping(value = "/insert")
	@ResponseBody
	public String insertBudgetCode(ModelMap model, @Valid @ModelAttribute("budgetCode") BudgetCode budgetCode, BindingResult bindingResult)
			throws JsonProcessingException {
		logger.info("insert BudgetCode requested");
		if (bindingResult.hasErrors()) {
			return JSONHelper.toString(bindingResult.getAllErrors());
		}
		budgetCodeService.insertBudgetCode(budgetCode);
		return "[]";
	}

	/*
	 * DELETE
	 */
	@RequestMapping(value = "/{id}/delete")
	public String deleteAccountingCode(@PathVariable String id) {
		logger.info("delete budgetCode required");
		budgetCodeService.deleteBudgetCode(id);
		return "redirect:/budget-codes";
	}


	/*
	 * EDIT
	 */
	@GetMapping(value = "/{id}/edit")
	public String editBudgetCode(ModelMap model, @ModelAttribute("budgetCode") BudgetCode budgetCode, @PathVariable String id) {
		logger.info("/editBudgetCode requested");
		budgetCode = budgetCodeService.getBudgetCode(id);
		model.addAttribute("budgetCode", budgetCode);
		model.addAttribute("formAction", "budget-codes/update");
		return "budgetCode_add";
	}

	@PostMapping(value = "/update")
	@ResponseBody
	public String updateBudgetCode(ModelMap model, @ModelAttribute("budgetCode") @Valid BudgetCode budgetCode, BindingResult bindingResult,
			@RequestParam("id") String id) throws JsonProcessingException {
		logger.info("update budgetCode requested");
		if (bindingResult.hasErrors()) {
			return JSONHelper.toString(bindingResult.getAllErrors());
		}
		budgetCodeService.updateBudgetCode(budgetCode, id);
		return "[]";
	}

}
