package ro.allevo.fintpui.controllers;

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
import ro.allevo.fintpui.model.Params;
import ro.allevo.fintpui.service.ParamService;
import ro.allevo.fintpui.utils.JSONHelper;
import ro.allevo.fintpui.utils.PagedCollection;
import ro.allevo.fintpui.utils.Roles;
import ro.allevo.fintpui.utils.datatables.DataTables;

@Controller
@RequestMapping("/params")
public class ParamsController {
	
	@Autowired
	Config config;
	
	@Autowired
	private ParamService paramService;

	private Params[] params;
	
	private static Logger logger = LogManager.getLogger(ParamsController.class.getName());
	
	/*
	 * DISPLAY
	 */
	@GetMapping
	public String displayParams(OAuth2Authentication auth, ModelMap model) {
		logger.info("/params required");
		if (!Roles.hasRoles(auth,Roles.CONFIGURATION_LISTS_VIEW))
			throw new NotAuthorizedException();
		
		model.addAttribute("hasModifyRole",Roles.hasRoles(auth,Roles.CONFIGURATION_LISTS_MODIFY));
		params = paramService.getAllParams();
		model.addAttribute("params" , params);		
		model.addAttribute("apiUri", config.getAPIUrl());
		model.addAttribute("title", "Params");
		return "params";
	}
	
	/*
	 * DISPLAY
	 */
	@GetMapping(value = "/page")
	public @ResponseBody String getParamsJson(@RequestParam int draw) throws JsonProcessingException {
		logger.info("/params required");
		PagedCollection<Params> params = paramService.getPage();

		DataTables dt = new DataTables();
		if (null != params) {
			dt.setData(params.getItems());
			dt.setRecordsFiltered(params.getTotal());
			dt.setRecordsTotal(params.getTotal());
		}
		dt.setDraw(draw);
		return JSONHelper.toString(dt);
	}
	
	/*
	 * EDIT
	 */
	@GetMapping(value = "/{code}/edit")
	public String editParams(ModelMap model, @ModelAttribute("params") Params params, @PathVariable String code) {
		logger.info("/editParams requested");
		params = paramService.getParam(code);
		model.addAttribute("params", params);
		model.addAttribute("formAction", "params/update");
		return "params_add";
	}
	
	@PostMapping(value = "/update")
	@ResponseBody
	public String updateParams(ModelMap model, @ModelAttribute("params") Params params, BindingResult bindingResult,
			@RequestParam("code") String code) throws JsonProcessingException {
		
		logger.info("update params requested");
		if (bindingResult.hasErrors()) {
			return JSONHelper.toString(bindingResult.getAllErrors());
		}
		
		paramService.updateParam(params, code);
		return "[]";
	}
}
