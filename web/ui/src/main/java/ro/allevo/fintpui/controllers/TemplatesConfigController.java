package ro.allevo.fintpui.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.exception.NotAuthorizedException;
import ro.allevo.fintpui.model.EditRules;
import ro.allevo.fintpui.model.EnrichRules;
import ro.allevo.fintpui.model.MessageType;
import ro.allevo.fintpui.model.TemplateConfig;
import ro.allevo.fintpui.model.TemplateConfigDetailed;
import ro.allevo.fintpui.model.TemplateConfigOptions;
import ro.allevo.fintpui.model.User;
import ro.allevo.fintpui.model.ValidationsRules;
import ro.allevo.fintpui.service.BankService;
import ro.allevo.fintpui.service.EditRulesService;
import ro.allevo.fintpui.service.EnrichService;
import ro.allevo.fintpui.service.ExternalAccountService;
import ro.allevo.fintpui.service.ExternalEntitiesService;
import ro.allevo.fintpui.service.InternalAccountService;
import ro.allevo.fintpui.service.InternalEntitiesService;
import ro.allevo.fintpui.service.MessageTypeService;
import ro.allevo.fintpui.service.TemplateConfigDetailedService;
import ro.allevo.fintpui.service.TemplateConfigOptionsService;
import ro.allevo.fintpui.service.TemplateConfigService;
import ro.allevo.fintpui.service.TemplatesService;
import ro.allevo.fintpui.service.UserService;
import ro.allevo.fintpui.service.ValidationsService;
import ro.allevo.fintpui.utils.Filters;
import ro.allevo.fintpui.utils.JSONHelper;
import ro.allevo.fintpui.utils.PagedCollection;
import ro.allevo.fintpui.utils.Roles;
import ro.allevo.fintpui.utils.datatables.DataTables;

import static java.lang.Math.toIntExact;

@Controller
@RequestMapping(value = "templates-config")
public class TemplatesConfigController {

	@Autowired
	Config config;
	
	@Autowired
	TemplatesService templatesService;

	@Autowired
	TemplateConfigService templateConfigService;

	@Autowired
	TemplateConfigOptionsService templateConfigOptionsService;

	@Autowired
	TemplateConfigDetailedService templateConfigDetailedService;

	@Autowired
	EnrichService enrichService;

	@Autowired
	MessageTypeService messageTypeService;

	@Autowired
	InternalAccountService internalAccountService;
	
	@Autowired
	ExternalAccountService externalAccountService;
	
	@Autowired
	InternalEntitiesService internalEntitiesService;
	
	@Autowired
	ExternalEntitiesService externalEntitiesService;
	
	@Autowired
	BankService bankService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	HttpServletRequest request;
	
	@Autowired
	ValidationsService validationsService;
	
	@Autowired
	EditRulesService editRulesService;

	private static Logger logger = LogManager.getLogger(TemplatesConfigController.class.getName());

	@GetMapping
	public String getTemplates(OAuth2Authentication auth, ModelMap model) {
		
		if (!Roles.hasRoles(auth, Roles.PAYMENT_TEMPLATE_CONFIGURATION_VIEW)) {
			throw new NotAuthorizedException();
		}

		TemplateConfig[] templateConfigs = templateConfigService.getAllTemplateConfig();
		MessageType[] messageTypes = messageTypeService.getMessageTypes();

		for (TemplateConfig templateConfig : templateConfigs) {
			for (MessageType messageType : messageTypes) {
				if (messageType.getMessageType().equals(templateConfig.getMessagetype())) {
					templateConfig.setMessagetype(messageType.getFriendlyName());
					break;
				}
			}
		}
		
		Boolean modifyRules = true;
		if (!Roles.hasRoles(auth,Roles.PAYMENT_TEMPLATE_CONFIGURATION_MODIFY)) {
			modifyRules = false;
		}
		model.addAttribute("templates", templateConfigs);
		model.addAttribute("title", "Template Config");
		model.addAttribute("apiUri", config.getAPIUrl());
		model.addAttribute("hasModifyRole", modifyRules);
		model.addAttribute("project", config.getProjectType());
		return "templateOpen";
	}

	/*
	 * DISPLAY
	 */
	/*
	 * @GetMapping(value = "/page") public @ResponseBody String
	 * getBanksJson(@RequestParam int draw) throws JsonProcessingException {
	 * logger.info("/templates-config required"); 
	 * PagedCollection<TemplateConfig> templatesConfig = templateConfigService.getPage();
	 * 
	 * DataTables dt = new DataTables(); if (null != templatesConfig) {
	 * dt.setData(templatesConfig.getItems());
	 * dt.setRecordsFiltered(templatesConfig.getTotal());
	 * dt.setRecordsTotal(templatesConfig.getTotal()); } dt.setDraw(draw); return
	 * JSONHelper.toString(dt); }
	 */

	@GetMapping(value = "/{id}/open")
	@Produces(MediaType.APPLICATION_JSON)
	public @ResponseBody TemplateConfig openTemplateConfig(@PathVariable String id){
//		model.addAttribute("hasModifyRole", true);
		return templateConfigService.getTemplateConfig(id);
	}

	@GetMapping(value = "{id}")
	public String getTemplateConfigById(OAuth2Authentication auth, ModelMap model, @PathVariable String id) throws NumberFormatException {
		TemplateConfig[] templateConfigs = templateConfigService.getAllTemplateConfig();
		MessageType[] messageTypes = messageTypeService.getMessageTypes();

		for (TemplateConfig templateConfig : templateConfigs) {
			for (MessageType messageType : messageTypes) {
				if (messageType.getMessageType().equals(templateConfig.getMessagetype())) {
					templateConfig.setMessagetype(messageType.getFriendlyName());
					break;
				}
			}
		}
		
		Boolean modifyRules = true;
		if (!Roles.hasRoles(auth,Roles.PAYMENT_TEMPLATE_CONFIGURATION_MODIFY)) {
			modifyRules = false;
		}
		model.addAttribute("addButtonPermision", modifyRules);
		model.addAttribute("templateId", id);
		model.addAttribute("templates", templateConfigs);
		model.addAttribute("template", templateConfigService.getTemplateConfigWithXsd(id));
		model.addAttribute("fields", templateConfigDetailedService.getAllTemplateConfigDetailed(id));
		model.addAttribute("hasModifyRole", true);
		model.addAttribute("project", config.getProjectType());
		return "templateOpen";
	}
	
	@GetMapping(value="view")
	public String getTemplate(OAuth2Authentication auth, ModelMap model) {
//		model.addAttribute("hasModifyRole", true);
		Boolean modifyRules = true;
		if (!Roles.hasRoles(auth,Roles.PAYMENT_TEMPLATE_CONFIGURATION_MODIFY)) {
			modifyRules = false;
		}
		model.addAttribute("templateAddButton", modifyRules);
//		model.addAttribute("project", config.getProjectType());
		return "templateConfigDetailed";
	}

	@GetMapping(value="page")
	public @ResponseBody String getTemplatesConfigDetailed(@RequestParam int draw, @RequestParam("templateId") String templateId) throws JsonProcessingException {
		logger.info("/Enrich Rules required");
		if (templateId.equals("undefined"))
			templateId = "1";
		
		LinkedHashMap<String, List<String>> filters = new LinkedHashMap<>();
		filters.put("filter_txtemplatesconfig.id_exact", Filters.getFiltersParams(templateId));
//		filters.put("sort_fieldlabel", Filters.getFiltersParams("asc"));
		
		PagedCollection<TemplateConfigDetailed> templateConfig =  templateConfigDetailedService.getTemplateConfigDetailedPage(templateId, filters);

		DataTables dt = new DataTables();
		if (null != templateConfig) {
			dt.setData(templateConfig.getItems());
			dt.setRecordsFiltered(templateConfig.getTotal());
			dt.setRecordsTotal(templateConfig.getTotal());
		}
		dt.setDraw(draw);
		return JSONHelper.toString(dt);
	}
	
	@GetMapping(value="add")
	public String addTemplatesConfigDetailed(ModelMap model, TemplateConfigDetailed detailed) {
		
		LinkedHashMap<String, List<String>> filter = new LinkedHashMap<>();
		filter.put("filter_category", Filters.getFiltersParams("create"));
		TemplateConfigOptions[] templateConfigOptions = templateConfigOptionsService.getAllTemplateConfig(filter);
		
		model.addAttribute("templateUpdButton", true);
		
		model.addAttribute("formAction", "insert");
		model.addAttribute("detailed", new TemplateConfigDetailed());
		model.addAttribute("options", templateConfigOptions);
		model.addAttribute("button", "templatesConfigDetailed.createRule");
		model.addAttribute("listsMetaData", enrichService.getListsMetadata());
		model.addAttribute("hasModifyRole", true);
		return "templateConfigDetailed_add";
	}
	
	@GetMapping(value="{id}/edit")
	public String editTemplatesConfigDetailed(OAuth2Authentication auth, ModelMap model, @PathVariable  Integer id) {
		TemplateConfig templateConfig = templateConfigService.getAllTemplateConfig()[0];
		TemplateConfigDetailed templateConfigDetailed = templateConfigDetailedService.getTemplateConfigDetailedByFieldId(templateConfig.getId().toString(), id);
		
		TemplateConfigOptions[] templateConfigOptions = templateConfigOptionsService.getAllTemplateConfig();
		
		Boolean modifyRules = true;
		if (!Roles.hasRoles(auth,Roles.PAYMENT_TEMPLATE_CONFIGURATION_MODIFY)) {
			modifyRules = false;
		}
		model.addAttribute("templateUpdButton", modifyRules);
		model.addAttribute("hasModifyRole", true);
		model.addAttribute("formAction", "update");
		model.addAttribute("detailed", templateConfigDetailed);
		model.addAttribute("options", templateConfigOptions);
		model.addAttribute("button", "templatesConfigDetailed.UpdateRule");
		model.addAttribute("listsMetaData", enrichService.getListsMetadata());
//		model.addAttribute("project", config.getProjectType());
		return "templateConfigDetailed_add";
	}
	
	@GetMapping(value="{id}/view")
	public String viewTemplatesConfigDetailed(ModelMap model, @PathVariable  Integer id) {
		TemplateConfig templateConfig = templateConfigService.getAllTemplateConfig()[0];
		TemplateConfigDetailed templateConfigDetailed = templateConfigDetailedService.getTemplateConfigDetailedByFieldId(templateConfig.getId().toString(), id);
		model.addAttribute("conditions", templateConfigDetailed.getConditions());
		model.addAttribute("listsMetaData", enrichService.getListsMetadata());
//		model.addAttribute("hasModifyRole", true);
//		model.addAttribute("project", config.getProjectType());
		return "conditionsQueryBuilderReadOnly_view";
	}

	@PostMapping(value = "update")
	@Consumes(MediaType.APPLICATION_JSON)
	@ResponseBody
	public String updateTemplateConfigDetailed(@RequestParam("detailed") String detailed)
			throws IOException {

		ObjectMapper objectMapper = new ObjectMapper();
		TemplateConfigDetailed templateConfigDetailed = objectMapper.readValue(detailed,
				TemplateConfigDetailed.class);
		
		templateConfigDetailedService.updateTemplateConfigDetailed(templateConfigDetailed);
		return "[]";
	}
	
	@PostMapping(value = "insert")
	@Consumes(MediaType.APPLICATION_JSON)
	@ResponseBody
	public String saveTemplateConfigDetailed(@RequestParam("detailed") String detailed)
			throws IOException {

		ObjectMapper objectMapper = new ObjectMapper();
		TemplateConfigDetailed templateConfigDetailed = objectMapper.readValue(detailed,
				TemplateConfigDetailed.class);
		
		TemplateConfigDetailed[] templatesConfigDetailed = new TemplateConfigDetailed[1];
		templatesConfigDetailed[0] = templateConfigDetailed;
		
		templateConfigDetailedService.insertTemplateConfigDetailed((int) (long)templateConfigDetailed.getTxtemplatesconfig().getId(), templatesConfigDetailed);

		return "[]";
	}
	
	@GetMapping(value="{id}/delete")
	public String deleteDetailed(OAuth2Authentication auth, @PathVariable("id") String id) {
		templateConfigDetailedService.deleteTemplateConfigDetailed(id);
		return getTemplate(auth, new ModelMap());
	}
	
	@GetMapping(value="enrich/page")
	public @ResponseBody String getEnrichJson(@RequestParam int draw, @RequestParam("templateId") String templateId) throws JsonProcessingException {
		logger.info("/Enrich Rules required");
		TemplateConfig template = templateConfigService.getTemplateConfig(templateId);
		
		LinkedHashMap<String, List<String>> filters = new LinkedHashMap<>();
		filters.put("filter_txType_exact", Filters.getFiltersParams(template.getMessagetype()));
//		filters.put("sort_name", Filters.getFiltersParams("asc"));
		
		PagedCollection<EnrichRules> enrichRules = enrichService.getPage(filters);

		DataTables dt = new DataTables();
		if (null != enrichRules) {
			dt.setData(enrichRules.getItems());
			dt.setRecordsFiltered(enrichRules.getTotal());
			dt.setRecordsTotal(enrichRules.getTotal());
		}
		dt.setDraw(draw);
		return JSONHelper.toString(dt);
	}
	
	@GetMapping(value ="enrich/view")
	public String viewEnrichTable(OAuth2Authentication auth, ModelMap model) {
		model.addAttribute("hasModifyRole", true);
		Boolean modifyRules = true;
		if (!Roles.hasRoles(auth,Roles.PAYMENT_TEMPLATE_CONFIGURATION_MODIFY)) {
			modifyRules = false;
		}
		model.addAttribute("enrichAddButton", modifyRules);
//		model.addAttribute("project", config.getProjectType());
		return "enrich";
	}
	
	@GetMapping(value="enrich/add")
	public String addEnrich(ModelMap model, @ModelAttribute("enrichRules") EnrichRules enrichRules, @RequestParam("templateId") String templateId) {
		logger.info("/addEnrichRules required");
		TemplateConfig template = templateConfigService.getTemplateConfig(templateId);
		
		LinkedHashMap<String, List<String>> filters = new LinkedHashMap<>();
		filters.put("filter_txType_exact", Filters.getFiltersParams(template.getMessagetype()));
		
		PagedCollection<EnrichRules> enrichs = enrichService.getPage(filters);
		
		filters.clear();
		filters.put("filter_category", Filters.getFiltersParams("enrich"));
		TemplateConfigOptions[] options = templateConfigOptionsService.getAllTemplateConfig(filters);
		
		model.addAttribute("enrichUpdButton", true);
		
		model.addAttribute("enrich", enrichRules);
		model.addAttribute("formAction", "enrich/insert");
		model.addAttribute("listsMetaData", enrichService.getListsMetadata());
		model.addAttribute("options", options);
		model.addAttribute("listEnrich", enrichs.getItems());
		model.addAttribute("button", "enrichRules.CreateRule");
		model.addAttribute("project", config.getProjectType());
		return "enrich_add";
	}
	
	@GetMapping(value="enrich/{id}/edit")
	public String getEnrichById(OAuth2Authentication auth, ModelMap model, @PathVariable("id") String id) {
		EnrichRules enrichRules = enrichService.getEnrich(id);
		
		LinkedHashMap<String, List<String>> filters = new LinkedHashMap<>();
		filters.put("filter_txType_exact", Filters.getFiltersParams(enrichRules.getTxType()));
		filters.put("filter_id_nexact", Filters.getFiltersParams(id));
		
		PagedCollection<EnrichRules> enrichs = enrichService.getPage(filters);
		
		filters.clear();
		filters.put("filter_category", Filters.getFiltersParams("enrich"));
		TemplateConfigOptions[] options = templateConfigOptionsService.getAllTemplateConfig(filters);
		
		Boolean modifyRules = true;
		
		if (!Roles.hasRoles(auth,Roles.PAYMENT_TEMPLATE_CONFIGURATION_MODIFY)) {
			modifyRules = false;
		}
		model.addAttribute("enrichUpdButton", modifyRules);
		
		model.addAttribute("enrich", enrichRules);
		model.addAttribute("formAction", "enrich/update");
		model.addAttribute("button", "enrichRules.UpdateRule");
		model.addAttribute("options", options);
		model.addAttribute("listsMetaData", enrichService.getListsMetadata());
		model.addAttribute("listEnrich", enrichs.getItems());
		model.addAttribute("hasModifyRole", true);
		model.addAttribute("project", config.getProjectType());
		return "enrich_add";
	}
	
	@GetMapping(value="enrich/{id}/view")
	public String viewEnrichById(ModelMap model, @PathVariable("id") String id) {
		EnrichRules enrichRules = enrichService.getEnrich(id);
		model.addAttribute("conditions", enrichRules.getConditions());
		model.addAttribute("listsMetaData", enrichService.getListsMetadata());
		model.addAttribute("hasModifyRole", true);
		model.addAttribute("project", config.getProjectType());
		return "conditionsQueryBuilderReadOnly_view";
	}
	
	@GetMapping(value="enrich/{id}/delete")
	public String deleteEnrich(OAuth2Authentication oauth, ModelMap model, @PathVariable("id") String id) {
		enrichService.deleteEnrichRules(id);
		model.addAttribute("hasModifyRole", true);
		return viewEnrichTable(oauth, model);
	}
	
	@PostMapping(value="enrich/update")
	@Consumes
	@ResponseBody
	public String updateEnrich(ModelMap model, @RequestParam("enrich") String enrich) throws IOException {
		
		ObjectMapper objectMapper = new ObjectMapper();
		EnrichRules enrichRules = objectMapper.readValue(enrich,
				EnrichRules.class);
		
		if (enrichRules.getName() == null || (enrichRules.getConditions() == null && enrichRules.getTxtemplatesconfigoption() == null)) {
			return JSONHelper.toString("enrich fail");
		}
		HttpSession session = request.getSession();
		String userName = session.getAttribute("userName").toString();
		User user = userService.getUser(userName);
		
		enrichRules.setUserId(user.getId());
		
		enrichService.updateEnrichRules(enrichRules, "" + enrichRules.getId());
//		model.addAttribute("hasModifyRole", true);
		return "[]";
	}
	
	@PostMapping(value="enrich/insert")
	@Consumes(MediaType.APPLICATION_JSON)
	@ResponseBody
	public String insertEnrich(@RequestParam("enrich") String enrich)
			throws IOException {
		logger.info("insert enrich requested");
		
		ObjectMapper objectMapper = new ObjectMapper();
		EnrichRules enrichRules = objectMapper.readValue(enrich,
				EnrichRules.class);
		
		if (enrichRules.getName() == null || (enrichRules.getConditions() == null && enrichRules.getTxtemplatesconfigoption() == null)) {
			return JSONHelper.toString("enrich fail");
		}
		HttpSession session = request.getSession();
		String userName = session.getAttribute("userName").toString();
		User user = userService.getUser(userName);
		
		enrichRules.setUserId(user.getId());
		
		enrichService.insertRules(enrichRules);
		return  "[]";
	}

	@GetMapping(value ="validations/view")
	public String viewValidationRulesTable(OAuth2Authentication auth, ModelMap model) {
		model.addAttribute("hasModifyRole", true);
		Boolean modifyRules = true;
		if (!Roles.hasRoles(auth,Roles.PAYMENT_TEMPLATE_CONFIGURATION_MODIFY)) {
			modifyRules = false;
		}
		model.addAttribute("validationsAddButton", modifyRules);
//		model.addAttribute("project", config.getProjectType());
		return "validations";
	}
	
	@GetMapping(value="validations/page")
	public @ResponseBody String getValidationsJson(@RequestParam int draw, @RequestParam("templateId") String templateId) throws JsonProcessingException {
		logger.info("/Validation Rules required");
		TemplateConfig template = templateConfigService.getTemplateConfig(templateId);
		
		LinkedHashMap<String, List<String>> filters = new LinkedHashMap<>();
		filters.put("filter_txType_exact", Filters.getFiltersParams(template.getMessagetype()));
		
		PagedCollection<ValidationsRules> validationsRules = validationsService.getPage(filters);

		DataTables dt = new DataTables();
		if (null != validationsRules) {
			dt.setData(validationsRules.getItems());
			dt.setRecordsFiltered(validationsRules.getTotal());
			dt.setRecordsTotal(validationsRules.getTotal());
		}
		dt.setDraw(draw);
		return JSONHelper.toString(dt);
	}
	
	@GetMapping(value="validations/{id}/view")
	public String viewValidationsById(ModelMap model, @PathVariable("id") String id) {
		ValidationsRules validationsRules = validationsService.getValidationRules(id);
		model.addAttribute("conditions", validationsRules.getConditions());
		model.addAttribute("listsMetaData", enrichService.getListsMetadata());
		model.addAttribute("hasModifyRole", true);
		model.addAttribute("project", config.getProjectType());
		return "conditionsQueryBuilderReadOnly_view";
	}
	
	@GetMapping(value="validations/{id}/delete")
	public String deleteValidations(OAuth2Authentication oauth, ModelMap model, @PathVariable("id") String id) {
		validationsService.deleteValidationRules(id);
		model.addAttribute("hasModifyRole", true);
		
		return viewValidationRulesTable(oauth, model);
	}
	
	private List<ValidationsRules> getListValidationRules(String messageType){
		ValidationsRules[] typeValidations = validationsService.getAllvalidationsRules();
		List<ValidationsRules> listValidationsRules = new ArrayList<>();
		for (ValidationsRules rules : typeValidations) {
			if (rules.getTxType().equals(messageType)) {
				listValidationsRules.add(rules);
			}
		}
		return listValidationsRules;
	}
	
	@GetMapping(value="validations/add")
	public String addValidationRules(ModelMap model, @ModelAttribute("validationsRules") ValidationsRules validationsRules, @RequestParam("txtype") String txtype) {
		logger.info("/addValidationRules required");
		
		TemplateConfig templateConfigs = templateConfigService.getTemplateConfig(txtype);
		List<ValidationsRules> listValidationsRules = getListValidationRules(templateConfigs.getMessagetype());
		
		model.addAttribute("validationUpdButton", true);
		model.addAttribute("validationsRules", validationsRules);
		model.addAttribute("validationsRulesData", listValidationsRules);
		model.addAttribute("formAction", "validations/insert");
		model.addAttribute("button", "validationRules.CreateRule");
		model.addAttribute("listsMetaData", enrichService.getListsMetadata());
		model.addAttribute("project", config.getProjectType());
		return "validations_add";
	}
	
	@PostMapping(value="validations/insert")
	@Consumes(MediaType.APPLICATION_JSON)
	@ResponseBody
	public String insertValidationRules( @RequestParam("validationsRules") String validationsRulesAsString)
			throws IOException {
		logger.info("insert validation requested");
		
		ObjectMapper objectMapper = new ObjectMapper();
		ValidationsRules validationsRules = objectMapper.readValue(validationsRulesAsString,
				ValidationsRules.class);
		
		if (validationsRules.getName() == null) {
			return JSONHelper.toString("validation fail");
		}
		completeValidationBuild(validationsRules);
		validationsService.insertValidationRules(validationsRules);
		return  "[]";
	}
	
	@GetMapping(value="validations/{id}/edit")
	public String getValidationRulesById(OAuth2Authentication auth,ModelMap model, @PathVariable("id") String id) {
		ValidationsRules validationsRules = validationsService.getValidationRules(id);
		
		List<ValidationsRules> listValidationsRules = getListValidationRules(validationsRules.getTxType());
		listValidationsRules = listValidationsRules.stream().filter(obj-> !obj.getName().equalsIgnoreCase(validationsRules.getName())).collect(Collectors.toList());

		Boolean modifyRules = true;
		
		if (!Roles.hasRoles(auth,Roles.PAYMENT_TEMPLATE_CONFIGURATION_MODIFY)) {
			modifyRules = false;
		}
		model.addAttribute("validationUpdButton", modifyRules);
//		model.addAttribute("project", config.getProjectType());
		model.addAttribute("validationsRules", validationsRules);
		model.addAttribute("validationsRulesData", listValidationsRules);
		model.addAttribute("formAction", "validations/update");
		model.addAttribute("button", "validationRules.UpdateRule");
		model.addAttribute("listsMetaData", enrichService.getListsMetadata());
		
		return "validations_add";
	}
	
	@PostMapping(value="validations/update")
	@Consumes
	@ResponseBody
	public String updateValidations(@RequestParam("validationsRules") String validationsRulesAsString) throws IOException {
		
		ObjectMapper objectMapper = new ObjectMapper();
		ValidationsRules validationsRules = objectMapper.readValue(validationsRulesAsString,
				ValidationsRules.class);
		
		if (validationsRules.getName() == null) {
			return JSONHelper.toString("validation fail");
		}

		completeValidationBuild(validationsRules);
		
		validationsService.updateValidationRules(validationsRules, "" + validationsRules.getId());
		return "[]";
	}
	
	private void completeValidationBuild(ValidationsRules validationsRules) {		
		validationsRules.setUserId(getUserId());
		validationsRules.setConfigId(getConfigId(validationsRules.getTxType()));
	}
	
	private int getUserId() {
		HttpSession session = request.getSession();
		String userName = session.getAttribute("userName").toString();
		User user = userService.getUser(userName);
		return user.getId();
	}
	
	private int getConfigId(String txType) {
		int configId = 0;
		for(TemplateConfig templateConfig : templateConfigService.getAllTemplateConfig()) {
			if(templateConfig.getMessagetype().equalsIgnoreCase(txType)) {
				try {
					configId = toIntExact(templateConfig.getId());
				} catch (ArithmeticException e) {
					e.printStackTrace();
				}
			}
		}
		return configId;
	}
	
	
	@GetMapping(value ="edit-rules/view")
	public String viewEditRulesTable(OAuth2Authentication auth, ModelMap model) {
		model.addAttribute("hasModifyRole", true);
		Boolean modifyRules = true;
		if (!Roles.hasRoles(auth,Roles.PAYMENT_TEMPLATE_CONFIGURATION_MODIFY)) {
			modifyRules = false;
		}
		model.addAttribute("editRulesAddButton", modifyRules);
		model.addAttribute("project", config.getProjectType());
		return "editRules";
	}
	
	@GetMapping(value="edit-rules/page")
	public @ResponseBody String getEditRulesJson(@RequestParam int draw, @RequestParam("templateId") String templateId) throws JsonProcessingException {
		logger.info("/Edit Rules required");
		TemplateConfig template = templateConfigService.getTemplateConfig(templateId);
		
		LinkedHashMap<String, List<String>> filters = new LinkedHashMap<>();
		filters.put("filter_txType_exact", Filters.getFiltersParams(template.getMessagetype()));
		
		PagedCollection<EditRules> editRules = editRulesService.getPage(filters);

		DataTables dt = new DataTables();
		if (null != editRules) {
			dt.setData(editRules.getItems());
			dt.setRecordsFiltered(editRules.getTotal());
			dt.setRecordsTotal(editRules.getTotal());
		}
		dt.setDraw(draw);
		return JSONHelper.toString(dt);
	}
	
	@GetMapping(value="edit-rules/{id}/view")
	public String viewEditRulesById(ModelMap model, @PathVariable("id") String id) {
		EditRules editRules = editRulesService.getEditRules(id);
		model.addAttribute("conditions", editRules.getConditions());
		model.addAttribute("listsMetaData", enrichService.getListsMetadata());
		model.addAttribute("hasModifyRole", true);
		model.addAttribute("project", config.getProjectType());
		return "conditionsQueryBuilderReadOnly_view";
	}
	
	@GetMapping(value="edit-rules/{id}/delete")
	public String deleteEditRules(OAuth2Authentication oauth, @PathVariable("id") String id) {
		editRulesService.deleteEditRules(id);
		return viewEditRulesTable(oauth, new ModelMap());
	}
	
	@GetMapping(value="edit-rules/add")
	public String addEditRules(ModelMap model, @ModelAttribute("editRules") EditRules editRules, @RequestParam("templateId") String templateId) {
		logger.info("/addEditRules required");
		
		TemplateConfig template = templateConfigService.getTemplateConfig(templateId);
		
		model.addAttribute("editRulesUpdButton", true);
		model.addAttribute("project", config.getProjectType());
		model.addAttribute("editRules", editRules);
		model.addAttribute("formAction", "edit-rules/insert");
		model.addAttribute("button", "global.create");
		model.addAttribute("listsMetaData", enrichService.getListsMetadata());
		model.addAttribute("listEditRules", editRulesService.getAllEditRulesByMessage(template.getMessagetype()));
		return "editRules_add";
	}
	
	@PostMapping(value="edit-rules/insert")
	@Consumes(MediaType.APPLICATION_JSON)
	@ResponseBody
	public String insertEditRules(@RequestParam("editRules") String editRulesAsString)
			throws IOException {
		logger.info("insert edit rules requested");
		
		ObjectMapper objectMapper = new ObjectMapper();
		EditRules editRules = objectMapper.readValue(editRulesAsString,
				EditRules.class);
		
		if (editRules.getName() == null) {
			return JSONHelper.toString("validation fail");
		}
		editRules.setUserId(getUserId());
		editRules.setConfigId(getConfigId(editRules.getTxType()));
		editRulesService.insertEditRules(editRules);
		return  "[]";
	}
	
	@GetMapping(value="edit-rules/{id}/edit")
	public String getEditRulesById(OAuth2Authentication auth, ModelMap model, @PathVariable("id") String id) {
		EditRules editRules = editRulesService.getEditRules(id);
		
		Boolean modifyRules = true;
		if (!Roles.hasRoles(auth,Roles.PAYMENT_TEMPLATE_CONFIGURATION_MODIFY)) {
			modifyRules = false;
		}
		model.addAttribute("editRulesUpdButton", modifyRules);
		model.addAttribute("project", config.getProjectType());
		model.addAttribute("editRules", editRules);
		model.addAttribute("formAction", "edit-rules/update");
		model.addAttribute("button", "global.update");
		model.addAttribute("listsMetaData", enrichService.getListsMetadata());
		model.addAttribute("listEditRules", editRulesService.getAllEditRulesByMessage(editRules.getTxType()));
		return "editRules_add";
	}
	
	@PostMapping(value="edit-rules/update")
	@Consumes
	@ResponseBody
	public String updateEditRules(@RequestParam("editRules") String editRulesAsString) throws IOException {
		
		ObjectMapper objectMapper = new ObjectMapper();
		EditRules editRules = objectMapper.readValue(editRulesAsString,
				EditRules.class);
		
		if (editRules.getName() == null) {
			return JSONHelper.toString("edit rule update fail");
		}

		editRules.setUserId(getUserId());
		editRules.setConfigId(getConfigId(editRules.getTxType()));		
		
		editRulesService.updateEditRules(editRules, "" + editRules.getId());
		return "[]";
	}
}
