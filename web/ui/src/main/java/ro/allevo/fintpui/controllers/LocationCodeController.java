package ro.allevo.fintpui.controllers;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Base64.Decoder;
import java.util.LinkedHashMap;
import java.util.List;

import javax.validation.Valid;
import javax.ws.rs.core.Response;

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
import ro.allevo.fintpui.model.LocationCode;
import ro.allevo.fintpui.service.InternalEntitiesService;
import ro.allevo.fintpui.service.LocationCodeService;
import ro.allevo.fintpui.utils.Filters;
import ro.allevo.fintpui.utils.JSONHelper;
import ro.allevo.fintpui.utils.PagedCollection;
import ro.allevo.fintpui.utils.Roles;
import ro.allevo.fintpui.utils.datatables.DataTables;

@Controller
@RequestMapping(value = "/location-codes")
public class LocationCodeController {

	@Autowired
	Config config;

	@Autowired
	private  LocationCodeService locationCodeService;
	
	@Autowired
	private InternalEntitiesService internalEntitiesService;

	private static Logger logger = LogManager.getLogger(TimeLimitsController.class.getName());

	/*
	 * DISPLAY
	 */
	@GetMapping
	public String printLocationCodes(OAuth2Authentication auth, ModelMap model) throws JsonProcessingException {
		logger.info("/locationCodes required");
		model.addAttribute("apiUri", config.getAPIUrl());

		model.addAttribute("entityNames", JSONHelper.toString(internalEntitiesService.getAllInternalEntityNames()));

		if (!auth.getAuthorities().contains(new SimpleGrantedAuthority(Roles.INTERNAL_ENTITIES_LIST_VIEW)))
			throw new NotAuthorizedException();

		model.addAttribute("hasModifyRole",
				auth.getAuthorities().contains(new SimpleGrantedAuthority(Roles.INTERNAL_ENTITIES_LIST_MODIFY)));

		return "locationCodes";
	}

	@GetMapping(value = "/page")
	public @ResponseBody String getLocationCodesJson(@RequestParam int draw) throws JsonProcessingException {
		logger.info("/locationCodes required");
		PagedCollection<LocationCode> locationCodes = locationCodeService.getPage();
		DataTables dt = new DataTables();
		if (null != locationCodes) {
			dt.setData(locationCodes.getItems());
			dt.setRecordsFiltered(locationCodes.getTotal());
			dt.setRecordsTotal(locationCodes.getTotal());
		}
		dt.setDraw(draw);
		return JSONHelper.toString(dt);
	}

	/*
	 * INSERT
	 */
	@GetMapping(value = "/add")
	public String addLocationCode(ModelMap model, @ModelAttribute("locationCode") LocationCode locationCode)
			throws JsonProcessingException {
		logger.info("/addlocationCode required");
		model.addAttribute("locationCode", locationCode);
		model.addAttribute("entityNames", internalEntitiesService.getAllInternalEntityNames());
		model.addAttribute("formAction", "location-codes/insert");
		model.addAttribute("defaultCodeLocation", false);
		model.addAttribute("project", config.getProjectType());
		return "locationCode_add";
	}

	@PostMapping(value = "/insert")
	@ResponseBody
	public String insertLocationCode(ModelMap model,
			@ModelAttribute("locationCode") @Valid LocationCode locationCode, BindingResult bindingResult)
			throws JsonProcessingException {
		logger.info("insert locationCode requested");
		if (bindingResult.hasErrors()) {
			return JSONHelper.toString(bindingResult.getAllErrors());
		} else {
			locationCodeService.insertLocationCode(locationCode);
			return "[]";
		}
	}

	/*
	 * DELETE
	 */
	@RequestMapping(value = "/{id}/delete")
	public String deleteLocationCode(@PathVariable String id) {
		logger.info("delete locationCode required");
		locationCodeService.deleteLocationCode(id);
		return "redirect:/location-codes";
	}

	/*
	 * EDIT
	 */
	@GetMapping(value = "/{id}/edit")
	public String editLocationCode(ModelMap model,
			@ModelAttribute("locationCode") LocationCode locationCode, @PathVariable String id)
			throws JsonProcessingException {
		logger.info("/editLocationCode requested");
		locationCode = locationCodeService.getLocationCode(id);
		model.addAttribute("locationCode", locationCode);
		model.addAttribute("entityNames", internalEntitiesService.getAllInternalEntityNames());
		model.addAttribute("init_id", id);
		model.addAttribute("formAction", "location-codes/update");
		model.addAttribute("project", config.getProjectType());
		model.addAttribute("defaultCodeLocation", getLocationCodeId(locationCode.getEntityName(), locationCode.getId()));
		return "locationCode_add";
	}

	@PostMapping(value = "/update")
	@ResponseBody
	public String updateLocationCode(ModelMap model,
			@ModelAttribute("locationCode") @Valid LocationCode locationCode, BindingResult bindingResult,
			@RequestParam("id") String id) throws JsonProcessingException {
		logger.info("update locationCode requested");
		if (bindingResult.hasErrors()) {
			return JSONHelper.toString(bindingResult.getAllErrors());
		}
		locationCodeService.updateLocationCode(locationCode, id);
		return "[]";
	}
	
	@GetMapping(value="/getDefaultEntity")
	public @ResponseBody String getEntitySetDefault(@RequestParam("entity") String entity) throws JsonProcessingException, UnsupportedEncodingException {
		Integer value = getLocationCodeId(URLDecoder.decode(entity,"UTF-8"), 0L);
		return JSONHelper.toString(value);
	}
	
	private Integer getLocationCodeId(String entity, Long id) {
		LinkedHashMap<String, List<String>> param = new LinkedHashMap<String, List<String>>();
		param.put("filter_entityName_exact", Filters.getFiltersParams(entity));
		param.put("filter_defaultValue_exact", Filters.getFiltersParams("Y"));
		param.put("filter_id_nexact", Filters.getFiltersParams(id.toString()));
		LocationCode[] locationCodes = locationCodeService.getLocationCode(param); 
		if (null == locationCodes || locationCodes.length < 1) {
			return 0;
		}
		return (int) locationCodes[0].getId();
	}

}
