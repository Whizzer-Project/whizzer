package ro.allevo.fintpui.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import ro.allevo.fintpui.model.MessageType;
import ro.allevo.fintpui.model.Template;
import ro.allevo.fintpui.model.TemplateConfig;
import ro.allevo.fintpui.model.TemplateConfigDetailed;
import ro.allevo.fintpui.model.TemplateDetailed;
import ro.allevo.fintpui.model.TemplateGroup;
import ro.allevo.fintpui.model.User;
import ro.allevo.fintpui.model.UserRole;
import ro.allevo.fintpui.service.InternalEntitiesService;
import ro.allevo.fintpui.service.MessageTypeService;
import ro.allevo.fintpui.service.TemplateConfigDetailedService;
import ro.allevo.fintpui.service.TemplateConfigOptionsService;
import ro.allevo.fintpui.service.TemplateConfigService;
import ro.allevo.fintpui.service.TemplateDetailedService;
import ro.allevo.fintpui.service.TemplateGroupService;
import ro.allevo.fintpui.service.TemplatesService;
import ro.allevo.fintpui.service.UserService;
import ro.allevo.fintpui.utils.Filters;
import ro.allevo.fintpui.utils.JSONHelper;
import ro.allevo.fintpui.utils.PagedCollection;
import ro.allevo.fintpui.utils.Roles;
import ro.allevo.fintpui.utils.datatables.DataTables;

@Controller
@RequestMapping(value = "/templates")
public class TemplatesController {

	@Autowired
	Config config;

	@Autowired
	private TemplatesService templatesService;

	@Autowired
	private TemplateDetailedService templateDetailedService;

	@Autowired
	private TemplateConfigOptionsService templateConfigOptions;

	@Autowired
	private TemplateConfigService templateConfigService;

	@Autowired
	private TemplateConfigDetailedService templateConfigDetailedService;

	@Autowired
	private TemplateGroupService templateGroupService;

	@Autowired
	HttpServletRequest request;

	@Autowired
	private UserService userService;

	@Autowired
	private MessageTypeService messageTypeService;

	@Autowired
	private InternalEntitiesService internalEntitiesService;

	private static Logger logger = LogManager.getLogger(TemplatesController.class.getName());

	/*
	 * DISPLAY
	 */
	@GetMapping
	public String printTemplates(OAuth2Authentication auth, ModelMap model) {
		logger.info("/templates required");

		UserRole[] userRoles = getDefinedUserRoles();
		if (0 == userRoles.length) {
			throw new NotAuthorizedException();
		}

		boolean ruleCreate = Arrays.stream(userRoles).anyMatch(userRole -> userRole.getAction().contains("create")||userRole.getAction().contains(Roles.SUPERVISER));

		model.addAttribute("addButtonView", false);
		if (Boolean.TRUE.equals(ruleCreate)) {
			model.addAttribute("addButtonView", true);
		}
		model.addAttribute("hasModifyRole", true);
		return "templates";
	}

	private UserRole[] getDefinedUserRoles() {
		LinkedHashMap<String, List<String>> filter = new LinkedHashMap<>();

		Integer userId = getUserIdFromSession();

		filter.put("filter_userId_exact", Filters.getFiltersParams(String.valueOf(userId)));
		filter.put("filter_roleEntity.userDefined_exact", Filters.getFiltersParams("1"));

		return userService.getUserRolesByUserDefined(userId, filter);
	}

	/*
	 * DISPLAY
	 */
	@GetMapping(value = "/page")
	public @ResponseBody String getTemplatesJson(OAuth2Authentication auth, @RequestParam int draw)
			throws JsonProcessingException {
		logger.info("/templates required");

		UserRole[] userRoles = getDefinedUserRoles();
		List<String> sortEntities;
		LinkedHashMap<String, List<String>> messageTypeFilters = new LinkedHashMap<>();
		PagedCollection<Template> templates;
		if (!Roles.hasRoles(auth, Roles.SUPERVISER)) {
			messageTypeFilters.put("filter_txtemplatesconfig.messagetype_exact",
					new ArrayList<>(Roles.getMessageTypeByRoles(userRoles)));
			sortEntities = new ArrayList<>(Roles.getEntityByRoles(userRoles));
			Collections.sort(sortEntities);
			messageTypeFilters.put("filter_txtemplatesdetaileds.value_exact",sortEntities); 
		}
		
		templates = templatesService.getPage(messageTypeFilters);

		DataTables dt = new DataTables();
		if (null != templates) {
			dt.setData(templates.getItems());
			dt.setRecordsFiltered(templates.getTotal());
			dt.setRecordsTotal(templates.getTotal());
		}
		dt.setDraw(draw);
		return JSONHelper.toString(dt);
	}

	/*
	 * INSERT
	 */
	@GetMapping(value = "/add")
	public String addTemplate(OAuth2Authentication auth, ModelMap model,
			@ModelAttribute("template") Template template) throws JsonProcessingException {
		logger.info("/addTemplate required");

		UserRole[] userRoles = getDefinedUserRoles();

		List<String> sortEntities = new ArrayList<>();
		LinkedHashMap<String, List<String>> messageTypeFilters = new LinkedHashMap<>();
		Boolean superviser = true;
		if (!Roles.hasRoles(auth, Roles.SUPERVISER)) {
			messageTypeFilters.put("filter_messageType_exact", new ArrayList<>(Roles.getMessageTypeByRoles(userRoles)));
			sortEntities = new ArrayList<>(Roles.getEntityByRoles(userRoles));
			Collections.sort(sortEntities);
			superviser = false;
		}

		template.setType(0);
		model.addAttribute("template", template);

		LinkedHashMap<String, List<String>> filter = new LinkedHashMap<>();
		filter.put("filter_type_exact", Arrays.asList("0"));
		filter.put("sort_name", Arrays.asList("asc"));

		MessageType[] messageTypes;
		if (Boolean.TRUE.equals(superviser)) {
			messageTypes = messageTypeService.getMessageTypes("Payments");
		} else {
			messageTypes = messageTypeService.getMessageTypes(messageTypeFilters);
		}

		List<String> messageTypesField = Arrays.stream(messageTypes).map(elem -> elem.getMessageType())
				.collect(Collectors.toList());

		LinkedHashMap<String, List<String>> params = new LinkedHashMap<>();
		params.put("filter_messagetype_exact", messageTypesField);
		params.put("sort_messagetype", Filters.getFiltersParams("asc"));

		TemplateConfig[] templateConfigs = templateConfigService.getAllTemplateConfig(params);

		for (TemplateConfig templateConfig : templateConfigs) {
			for (MessageType messageType : messageTypes) {
				if (messageType.getMessageType().equals(templateConfig.getMessagetype())) {
					templateConfig.setMessagetype(messageType.getFriendlyName());
					break;
				}
			}
		}

		JSONObject entities = templateConfigOptions
				.getAllConfingOptionsValues(getSetEntity(templateConfigs));
		if (Boolean.FALSE.equals(superviser)) {
			JSONArray internalAccounts = new JSONArray();
			if (!sortEntities.isEmpty()) {
//				JSONHelper.toList(entities.get("internalentity"));
				internalAccounts = Roles.getInternalEntityFromRules( entities.getJSONArray("internalentity"), sortEntities);
			}
			entities.put("internalentity", internalAccounts);
		}

		model.addAttribute("simpleTemplates", templatesService.getAllTemplates(filter));
		model.addAttribute("templatesConfig", templateConfigs);
		model.addAttribute("dropDowns", (entities.toString()));
//		System.out.println(entities.toString());
		model.addAttribute("formAction", "templates/insert");
		return "templates_add";
	}

	private Set<String> getSetEntity(TemplateConfig[] templateConfigs) {
		Set<String> setEntities = new HashSet<>();
		for (int item = 0; item < templateConfigs.length; item++) {
			for (TemplateConfigDetailed tcDetailed : templateConfigs[item].getTxtemplatesconfigdetaileds()) {
				if (tcDetailed.getBusslist() != null && tcDetailed.getBusslist().trim().length() > 0) {
					setEntities.add(tcDetailed.getBusslist());
				}
			}
		}
		return setEntities;
	}

	@GetMapping(value = "{configid}/fields")
	@ResponseBody
	public Map<String, Object> addXPaths(@PathVariable String configid) {
		Map<String, Object> map = new HashMap<>();

		List<TemplateConfigDetailed> listTemplateConfigDetailed = Arrays
				.asList(templateConfigDetailedService.getAllTemplateConfigDetailed(configid));
//		listTemplateConfigDetailed = listTemplateConfigDetailed.stream().filter(tCDetailed -> tCDetailed.getVisible()).collect(Collectors.toList());

		Collections.sort(listTemplateConfigDetailed, new Comparator<TemplateConfigDetailed>() {

			@Override
			public int compare(TemplateConfigDetailed o1, TemplateConfigDetailed o2) {
				return o1.getId() - o2.getId();
			}
		});
		if (null != listTemplateConfigDetailed) {
			map.put("fields", listTemplateConfigDetailed);
		}
		map.put("internal", internalEntitiesService
				.getAllInternalEntitiesByRights(templateConfigService.getTemplateConfig(configid).getMessagetype()));
		return map;
	}

	@GetMapping(value = "{configid}/groups")
	@ResponseBody
	public Template[] getSimpleTemplate(@PathVariable String configid) {

		LinkedHashMap<String, List<String>> filter = new LinkedHashMap<>();
		filter.put("filter_type_exact", Filters.getFiltersParams("0"));
		filter.put("filter_txtemplatesconfig.id_exact", Filters.getFiltersParams(configid));
		filter.put("sort_name", Filters.getFiltersParams("asc"));

		return templatesService.getAllTemplates(filter);
	}

	private Integer getUserIdFromSession() {
		HttpSession session = request.getSession();
		String userName = session.getAttribute("userName").toString();
		User user = userService.getUser(userName);
		return user.getId();
	}

	@PostMapping(value = "insert")
	@ResponseBody
	public String insertTemplate(@RequestParam("template") String templateAsString) throws IOException, JSONException {
		logger.info("insert template requested");
		ObjectMapper objectMapper = new ObjectMapper();
		Template template = objectMapper.readValue(templateAsString, Template.class);

//		TemplateConfig templateConfig = templateConfigService
//				.getTemplateConfig(template.getTxtemplatesconfig().getId().toString());
//		for (TemplateDetailed templateDetailed : template.getTxtemplatesdetaileds()) {
//			if (null != templateDetailed.getTxtemplatesconfigdetailed().getTxtemplatesconfigoption() &&
//			/*
//			 * templateDetailed.getTxtemplatesconfigdetailed().getTxtemplatesconfigoption().
//			 * getDatasource().equals("internal-entities.name") &&
////			 */ templateDetailed.getTxtemplatesconfigdetailed().getFieldxpath().equals(templateConfig.getType())) {
//				template.setEntity(templateDetailed.getValue());
//				break;
//			}
//		}

		insertTemplate(template);
		return "[]";
	}

	private Integer insertTemplate(Template template) {
		Integer userId = getUserIdFromSession();
		template.setUserid(userId);
//		TemplateDetailed[] templateDetaileds = template.getTxtemplatesdetaileds();
//		TemplateGroup[] templateGroups = template.getTxtemplatesgroups();
//		template.setTxtemplatesdetaileds(null);
//		template.setTxtemplatesgroups(null);
		ResponseEntity<String> responseInsertTemplate = templatesService.insertTemplate(template);
		Integer id = 0;
		if (responseInsertTemplate.getStatusCodeValue() == 201) {
			JSONObject json = new JSONObject(responseInsertTemplate.getBody());
			String[] uri = json.get("uri").toString().split("/");
			id = Integer.parseInt(uri[uri.length - 1]);
		}

		Template templateNew = templatesService.getTemplate(id);
		// insert template detailed
//		List<TemplateDetailed> listTemplateDetailed = new ArrayList<>();
//		boolean insertTemplateDetailed = false;
//		for (TemplateDetailed templateDetailed : templateDetaileds) {
//			templateDetailed.setTxtemplate(templateNew);
//			templateDetailed.setId(null);
//			listTemplateDetailed.add(templateDetailed);
//			templateDetailedService.insertTemplateDetailed(templateDetailed);
//			insertTemplateDetailed = true;
//		}
//		if (!listTemplateDetailed.isEmpty()) {
//			templateDetailedService.insertListOfTemplateDetailed(listTemplateDetailed);
//		}
//		if (Boolean.TRUE.valueOf(insertTemplateDetailed)) {
//			eventService.insertEvent();
//		}
		// insert template group
//		insertTemplateDetailed = false;
//		for (TemplateGroup templateGroup : templateGroups) {
//			templateGroup.setTxtemplate(templateNew);
//			templateGroup.setUserid(userId);
//			templateGroup.setId(null);
//			templateGroupService.insertTemplateGroup(templateGroup);
//		}
		return id;
	}

	/*
	 * DELETE
	 */
	@RequestMapping(value = "/{id}/delete")
	public String deleteTemplate(@PathVariable Integer id) {
		logger.info("delete template required");
		templatesService.deleteTemplate(id);
		return "redirect:/templates";
	}

	@GetMapping(value = "/{id}/copy")
	public String getCopyTemplate(ModelMap model, @PathVariable Integer id) {
		Template template = templatesService.getTemplate(id);
		model.addAttribute("template", template);
		return "template_copy";
	}

	@PostMapping(value = "/copy")
	@ResponseBody
	public String copyTemplate(@RequestParam("id") String id, @RequestParam("name") String name) {
		Template template = templatesService.getTemplate(Integer.valueOf(id));
		template.setName(name);
		template.setId(null);
		TemplateConfig templateConfig = new TemplateConfig();
		templateConfig.setId(template.getTxtemplatesconfig().getId());
		template.setTxtemplatesconfig(templateConfig);
		for (TemplateDetailed templateDetailed : template.getTxtemplatesdetaileds()) {
			templateDetailed.setId(null);
			templateDetailed.getTxtemplatesconfigdetailed().setTxtemplatesconfig(null);
			templateDetailed.getTxtemplatesconfigdetailed().setTxtemplatesconfigoption(null);
		}
		insertTemplate(template);
		return "[]";
	}

	/*
	 * EDIT
	 */
	@GetMapping(value = "/{id}/edit")
	public String editTemplate(OAuth2Authentication auth, ModelMap model, @PathVariable Integer id) {
		logger.info("/editTemplate requested");

		List<String> sortEntities = new ArrayList<>();
		LinkedHashMap<String, List<String>> messageTypeFilters = new LinkedHashMap<>();
		UserRole[] userRoles = getDefinedUserRoles();

		if (!Roles.hasRoles(auth, Roles.SUPERVISER)) {
			Set<String> messageTypeByRole = Roles.getMessageTypeByRoles(userRoles);
			messageTypeFilters.put("filter_messageType_exact", new ArrayList<>(messageTypeByRole));
			messageTypeFilters.put("filter_messagetype_exact", new ArrayList<>(messageTypeByRole));
			sortEntities = new ArrayList<>(Roles.getEntityByRoles(userRoles));
			Collections.sort(sortEntities);
		}

		Template template = templatesService.getTemplate(id);

		LinkedHashMap<String, List<String>> filter = new LinkedHashMap<>();
		filter.put("filter_type_exact", Filters.getFiltersParams("0"));
		filter.put("filter_txtemplatesconfig.id_exact",
				Filters.getFiltersParams(String.valueOf(template.getTxtemplatesconfig().getId())));
		filter.put("sort_name", Filters.getFiltersParams("asc"));

		Template[] simpleTemplates = templatesService.getAllTemplates(filter);

		for (TemplateGroup templateGroup : template.getTxtemplatesgroups()) {
			Integer templateId = templateGroup.getGroupid();
			for (Template simpleTemplate : simpleTemplates) {
				if (simpleTemplate.getId().equals(templateId)) {
					templateGroup.setName(simpleTemplate.getName());
					break;
				}
			}
		}

		MessageType[] messageTypes = messageTypeService.getMessageTypes(messageTypeFilters);
		TemplateConfig[] templateConfigs = templateConfigService.getAllTemplateConfig(messageTypeFilters);

		for (TemplateConfig templatesConfig : templateConfigs) {
			for (MessageType messageType : messageTypes) {
				if (messageType.getMessageType().equals(templatesConfig.getMessagetype())) {
					templatesConfig.setMessagetype(messageType.getFriendlyName());
					break;
				}
			}
		}

		JSONObject  entities = templateConfigOptions
				.getAllConfingOptionsValues(getSetEntity(templateConfigs));
		JSONArray internalAccounts;
		if (!sortEntities.isEmpty()) {
			internalAccounts = Roles.getInternalEntityFromRules(entities.getJSONArray("internalentity"), sortEntities);
		}
		else {
			internalAccounts = entities.getJSONArray("internalentity");
		}
		entities.put("internalentity", internalAccounts.toString());
		model.addAttribute("simpleTemplates", simpleTemplates);
		model.addAttribute("templatesConfig", templateConfigs);
		model.addAttribute("dropDowns", entities);
		model.addAttribute("template", template);

		model.addAttribute("formAction", "templates/update");

		return "templates_add";
	}

	@PostMapping(value = "/update")
	@ResponseBody
	public String updateTemplate(@RequestParam("template") String templateAsString) throws IOException {
		logger.info("update template requested");
		ObjectMapper objectMapper = new ObjectMapper();
		Template template = objectMapper.readValue(templateAsString, Template.class);
		LinkedHashMap<String, List<String>> filter = new LinkedHashMap<>();
		filter.put("filter_id_exact", Filters.getFiltersParams(String.valueOf(template.getId())));
		filter.put("sort_id", Filters.getFiltersParams("asc"));
		Template oldTemplate = templatesService.getAllTemplates(filter)[0];

//		if (!template.getTxtemplatesconfig().getId().equals(oldTemplate.getTxtemplatesconfig().getId())) {
//			templateDetailedService.deleteTemplateDetailedByTemplateId(template.getId());
//		}

//		TemplateConfig templateConfig = templateConfigService
//				.getTemplateConfig(template.getTxtemplatesconfig().getId().toString());
//		for (TemplateDetailed templateDetailed : template.getTxtemplatesdetaileds()) {
//			if (null != templateDetailed.getTxtemplatesconfigdetailed().getTxtemplatesconfigoption()
//					&& templateDetailed.getTxtemplatesconfigdetailed().getTxtemplatesconfigoption().getDatasource()
//							.equals("internal-entities.name")
//					&& templateDetailed.getTxtemplatesconfigdetailed().getFieldxpath()
//							.equals(templateConfig.getType())) {
//				template.setEntity(templateDetailed.getValue());
//				break;
//			}
//		}

		Integer userId = getUserIdFromSession();
		template.setUserid(userId);

		TemplateDetailed[] templatesDetailed = template.getTxtemplatesdetaileds();
		template.setTxtemplatesdetaileds(null);
		TemplateDetailed[] oldTemplatesDetailed = oldTemplate.getTxtemplatesdetaileds();
		TemplateGroup[] templateGroups = template.getTxtemplatesgroups();
		template.setTxtemplatesgroups(null);
//		templatesService.updateTemplate(template, template.getId());

		// insert template detailed
		List<TemplateDetailed> insertTemplatesDetailed = new ArrayList<>();
		for (TemplateDetailed templateDetailed : templatesDetailed) {
			templateDetailed.setTxtemplate(template);
			if (null == templateDetailed.getId()) {
				insertTemplatesDetailed.add(templateDetailed);
			} else if (null != templateDetailed.getId()) {
				for (TemplateDetailed oldTemplateDetailed : oldTemplatesDetailed) {
					if (oldTemplateDetailed.getId().equals(templateDetailed.getId())
							&& !oldTemplateDetailed.getValue().equalsIgnoreCase(templateDetailed.getValue())) {
						templateDetailedService.updateTemplateDetailed(templateDetailed);
						break;
					}
				}

			}
		}
		if (!insertTemplatesDetailed.isEmpty()) {
			templateDetailedService.insertListOfTemplateDetailed(insertTemplatesDetailed);
		}

		TemplateGroup[] templatesGroups = templateGroupService.getAllTemplateGroups(template.getId());
		List<TemplateGroup> insertTemplatesGroup = new ArrayList<>();
		// insert template group
		for (TemplateGroup templateGroup : templateGroups) {
			templateGroup.setTxtemplate(template);
			if (null != templateGroup.getId()) {
				int i;
				for (i = 0; i < templatesGroups.length; i++) {
					if (templatesGroups[i].getId().equals(templateGroup.getId())) {
						break;
					}
				}
				if (i < templatesGroups.length) {
					templatesGroups = (TemplateGroup[]) ArrayUtils.remove(templatesGroups, i);
				}
//				templateGroupService.updateTemplateGroup(templateGroup);
			} else {
				insertTemplatesGroup.add(templateGroup);
//				templateGroupService.insertTemplateGroup(templateGroup);
			}
		}
		if (insertTemplatesGroup.size() > 0) {
			templateGroupService.insertTemplateGroup(insertTemplatesGroup);
		}
		for (TemplateGroup templateGroup : templatesGroups) {
			templateGroupService.deleteTemplateGroup(templateGroup.getId());
		}
		return "[]";
	}

}
