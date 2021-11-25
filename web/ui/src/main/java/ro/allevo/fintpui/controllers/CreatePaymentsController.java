package ro.allevo.fintpui.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.QueryParam;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.ArrayUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.exception.NotAuthorizedException;
import ro.allevo.fintpui.model.FinTPLists;
import ro.allevo.fintpui.model.Manualtransactions;
import ro.allevo.fintpui.model.MessageType;
import ro.allevo.fintpui.model.Template;
import ro.allevo.fintpui.model.TemplateConfig;
import ro.allevo.fintpui.model.TemplateConfigDetailed;
import ro.allevo.fintpui.model.TemplateDetailed;
import ro.allevo.fintpui.model.TemplateGroup;
import ro.allevo.fintpui.model.User;
import ro.allevo.fintpui.model.UserRole;
import ro.allevo.fintpui.service.ManualtransactionsService;
import ro.allevo.fintpui.service.MessageTypeService;
import ro.allevo.fintpui.service.TemplateConfigOptionsService;
import ro.allevo.fintpui.service.TemplateConfigService;
import ro.allevo.fintpui.service.TemplatesService;
import ro.allevo.fintpui.service.UserService;
import ro.allevo.fintpui.utils.Filters;
import ro.allevo.fintpui.utils.PayloadHelper;
import ro.allevo.fintpui.utils.Roles;
import ro.allevo.fintpui.utils.SortTemplateConfigDetailedById;
import ro.allevo.fintpui.utils.SortTemplateDetailedById;
import ro.allevo.fintpui.utils.Utils;

@Controller
@RequestMapping(value = "payment-create")
public class CreatePaymentsController {

	@Autowired
	TemplatesService templateService;

	@Autowired
	TemplateConfigService templateConfigService;

	@Autowired
	TemplateConfigOptionsService templateConfigOptions;

	@Autowired
	ManualtransactionsService manualtransactionsService;

	@Autowired
	HttpServletRequest request;

	@Autowired
	private UserService userService;

	@Autowired
	MessageTypeService messageTypeService;
	
	@Autowired
	Config config;

	public static final String PAYMENTCREATE = "paymentCreate";
	public static final String FIELDXPATH = "fieldxpath";
	public static final String FIELDVALUE = "fieldvalue";
	public static final String XMLFILENAME = "xsd/xsd.cast.to.xml.xml";
	public static final String XMLFILENAME_ADPHARMA = "xsd/xsd.cast.to.xml_adpharma.xml";
	public static final String ADPHARMA = "adpharma";

	@GetMapping
	public String getPayments(OAuth2Authentication auth, ModelMap model) {
		Boolean superviser = false;
		List<String> sortEntities = new ArrayList<>();
		if (!Roles.hasRoles(auth, Roles.SUPERVISER)) {
			UserRole[] userRoles = getDefinedUserRoles();

			boolean ruleOperate = Arrays.stream(userRoles).anyMatch(userRole -> userRole.getAction().contains("create"));
			if (Boolean.FALSE.equals(ruleOperate)) {
				throw new NotAuthorizedException(); 
			}
			sortEntities = new ArrayList<>(Roles.getEntityByRoles(userRoles));
			Collections.sort(sortEntities);
			superviser = false;

		}
		else {
			superviser = true;
		}
		model.addAttribute("title", "Create payments");
		Set<String> setEntity = new HashSet<>();
		List<FinTPLists> finTPLists = config.getProjectType().equalsIgnoreCase(ADPHARMA)?Utils.adpharmaLists:Utils.finTPLists;
		for (FinTPLists finLists : finTPLists) {
			setEntity.add(finLists.getName());
		}
		
		JSONObject entities = templateConfigOptions.getAllConfingOptionsValues(setEntity);
		if (Boolean.FALSE.equals(superviser)) {
			JSONArray internalAccounts = new JSONArray();
			if (!sortEntities.isEmpty()) {
				internalAccounts = Roles.getInternalEntityFromRules(entities.getJSONArray("internalentity"), sortEntities);
			}
			entities.put("internalentity", internalAccounts);
		}
		
		model.addAttribute("dropDowns", entities.toString());
		model.addAttribute("project", config.getProjectType());
		if (config.getProjectType().equalsIgnoreCase(ADPHARMA))
			model.addAttribute("adpharma", true);
		return PAYMENTCREATE;
	}
	
	private Integer getUserIdFromSession() {
		HttpSession session = request.getSession();
		String userName = session.getAttribute("userName").toString();
		User user = userService.getUser(userName);
		return user.getId();
	}
	
	private UserRole[] getDefinedUserRoles() {
		LinkedHashMap<String, List<String>> filter = new LinkedHashMap<>();

		Integer userId = getUserIdFromSession();

		filter.put("filter_userId_exact", Filters.getFiltersParams(String.valueOf(userId)));
		filter.put("filter_roleEntity.userDefined_exact", Filters.getFiltersParams("1"));

		return userService.getUserRolesByUserDefined(userId, filter);
	}

	@GetMapping(value = "/{id}/sample")
	@ResponseBody
	public Template[] getSample(OAuth2Authentication auth, @PathVariable Integer id) {
		
		UserRole[] userRoles = getDefinedUserRoles();
		
		List<String> sortEntities = new ArrayList<>();
		LinkedHashMap<String, List<String>> filter = new LinkedHashMap<>();
//		Boolean superviser = false;
		if (!Roles.hasRoles(auth, Roles.SUPERVISER)) {
			filter.put("filter_txtemplatesconfig.messagetype_exact",
					new ArrayList<>(Roles.getMessageTypeByRoles(userRoles)));
			sortEntities = new ArrayList<>(Roles.getEntityByRoles(userRoles));
			Collections.sort(sortEntities);
			filter.put("filter_txtemplatesdetaileds.value_exact", sortEntities);
		}
//		else {
//			superviser = true;
//		}

		
		filter.put("filter_type_exact", Filters.getFiltersParams(String.valueOf(id - 2)));
		filter.put("sort_name", Filters.getFiltersParams("asc"));
		
//		Template[] templates = templateService.getAllTemplates(filter);
//		List<Template> newTemplates = new ArrayList<>();
//		if (Boolean.FALSE.equals(superviser)) {
//			for (int ind = 0; ind < templates.length; ind++) {
//				boolean findEntity = false;
//				for (TemplateDetailed templateDetailed : templates[ind].getTxtemplatesdetaileds()) {
//					Optional<String> checkEntity = sortEntities.stream()
//							.filter(entity -> entity.equalsIgnoreCase(templateDetailed.getValue())).findFirst();
//					if (checkEntity.isPresent()) {
//						findEntity = true;
//						break;
//					}
//				}
//				if (findEntity) {
//					newTemplates.add(templates[ind]);
//				}
//			}
//		}else
//		{
//			newTemplates = new ArrayList<>(Arrays.asList(templates));
//		}
		

		return templateService.getAllTemplates(filter);
	}

	@GetMapping(value = "/{id}/config")
	@ResponseBody
	public TemplateConfigDetailed[] getConfigDetailed(@PathVariable String id) {
		TemplateConfig tc = templateConfigService.getTemplateConfig(id);
		List<TemplateConfigDetailed> listTemplateConfigDetaileds = new ArrayList<>();
		for (TemplateConfigDetailed templateConfig : tc.getTxtemplatesconfigdetaileds()) {
			listTemplateConfigDetaileds.add(templateConfig);
		}
//		Comparator<TemplateConfigDetailed> compareTemplateConfig = (o1, o2)-> Integer.compare(o1.getId(), o2.getId());
		Collections.sort(listTemplateConfigDetaileds, new SortTemplateConfigDetailedById());
		return listTemplateConfigDetaileds.toArray(new TemplateConfigDetailed[0]);
	}

	@GetMapping(value = "/{id}/template")
	@ResponseBody
	public List<Template> getTemplate(@PathVariable Integer id) {
		
		List<Template> listTemplate = new ArrayList<>();
		Template template = templateService.getTemplate(id);
		if (template.getTxtemplatesgroups().length > 0) {
			for (TemplateGroup group : template.getTxtemplatesgroups()) {
				listTemplate.add(templateService.getTemplate(group.getGroupid()));
			}
		} else {
			listTemplate.add(template);
		}
		for (Template otherTemplate : listTemplate) {
			List<TemplateDetailed> templateDetailed = Arrays.asList(otherTemplate.getTxtemplatesdetaileds());
			Collections.sort(templateDetailed, new SortTemplateDetailedById());
			otherTemplate.setTxtemplatesdetaileds(templateDetailed.toArray(new TemplateDetailed[0]));
		}
		
		return listTemplate;
	}

	@GetMapping(value = "/templates")
	@ResponseBody
	public List<Map<String, String>> getTemplatesConfigs(OAuth2Authentication auth) {
		
		UserRole[] userRoles = getDefinedUserRoles();
		if (0 == userRoles.length) {
			throw new NotAuthorizedException();
		}
		
//		List<String> sortEntities;
		LinkedHashMap<String, List<String>> messageTypeFilters = new LinkedHashMap<>();
		if (!Roles.hasRoles(auth, Roles.SUPERVISER)) {
			Set<String> roles = Roles.getMessageTypeByRoles(userRoles);
			messageTypeFilters.put("filter_messageType_exact", new ArrayList<>(roles));
			messageTypeFilters.put("filter_messagetype_exact", new ArrayList<>(roles));
//			sortEntities = new ArrayList<>(Roles.getEntityByRoles(userRoles));
//			Collections.sort(sortEntities);
		}

		TemplateConfig[] templateConfigs = templateConfigService.getAllTemplateConfig(messageTypeFilters);
		messageTypeFilters.put("filter_businessArea_exact", Arrays.asList("Payments"));
		MessageType[] messageTypes = messageTypeService.getMessageTypes(messageTypeFilters);
		
		List<Map<String, String>> template = new ArrayList<>();

		for (TemplateConfig templateConfig : templateConfigs) {
			for (MessageType messageType : messageTypes) {
				if (messageType.getMessageType().equals(templateConfig.getMessagetype())) {
					Map<String, String> mapTemplate = new HashMap<>();
					mapTemplate.put("id", String.valueOf(templateConfig.getId()));
					mapTemplate.put("label", messageType.getFriendlyName());
					template.add(mapTemplate);
					break;
				}
			}
		}

		return template;
	}

	@GetMapping(value = "{id}")
	public String getTemplatesConfig(ModelMap model, @PathVariable String id,
			@QueryParam(value = "payments") Integer payments, @QueryParam(value = "sample") Integer sample) {
		model.addAttribute("validationXsd", templateConfigService.getTemplateConfigWithXsd(id));
		model.addAttribute("configs", templateConfigService.getAllTemplateConfig());
		model.addAttribute("dropDowns", templateConfigOptions.getAllConfingOptionsValues());
		model.addAttribute("payments", payments);
		model.addAttribute("sample", sample);
		return PAYMENTCREATE;
	}

	@GetMapping(value = "field")
	@ResponseBody
	public Object[] getField(@QueryParam(value = "condition") String condition,
			@QueryParam(value = "queryValue") String queryValue) throws IOException, JSONException {
		return templateConfigOptions.getConfigOption(condition, queryValue);
	}

	private JSONArray sortPayload(JSONArray payload) {
		for (int item=0; item < payload.length(); item++) {
			for (int ind = 1 + item; ind < payload.length(); ind++) {
				JSONObject objCompare = payload.getJSONObject(item);
				JSONObject objComparable = payload.getJSONObject(ind);
				if (objCompare.getString("fieldxpath").compareTo(objComparable.getString("fieldxpath"))>0) {
					payload.put(item, objComparable);
					payload.put(ind, objCompare);
				}
			}
		}
		return payload;
	}

	@PostMapping(value = "/create-payload")
	@ResponseBody
	public String savePayload(@RequestParam("payload") String payload) throws Exception {
		JSONArray payloadMap = sortPayload(new JSONArray(payload));
		String fileName = XMLFILENAME;
		if (config.getProjectType().equalsIgnoreCase("adpharma")) {
			fileName = XMLFILENAME_ADPHARMA;
		}
		Document templateDoc = parseXMLFromFile(fileName);
		if (null == templateDoc)
			return PAYMENTCREATE;
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = dbf.newDocumentBuilder();
		Document doc = builder.newDocument();

		Element element = doc.createElement("Document");
		element.setAttribute("xmlns", "urn:iso:std:iso:20022:tech:xsd:pain.001.001.08Alv");
		doc.appendChild(element);
		
		NodeList nodes = templateDoc.getElementsByTagName("*");
		for (int i = 2; i < nodes.getLength(); i++) {
			String templatexPath = PayloadHelper.getXPath(nodes.item(i));
			for (int item = 0; item < payloadMap.length(); item++) {
				JSONObject obj = payloadMap.getJSONObject(item);
				String valuexPath = obj.getString(FIELDVALUE);
				if (0 == valuexPath.replace("-","").trim().length())
					continue;
				String fieldxPath = obj.getString(FIELDXPATH);
				templatexPath = replaceSegment(templatexPath, fieldxPath);
				if (templatexPath.equals(fieldxPath)) {
					addNodeByDoc(doc, fieldxPath, valuexPath);
				}else if ((fieldxPath.matches("(.*)@(.*)"))) {
					String[] attributes = fieldxPath.split("/@");
					if (templatexPath.equals(attributes[0])) {
						addNodeByDoc(doc, fieldxPath, valuexPath);
					}
				}
				
			}
		}
		Manualtransactions payment = new Manualtransactions();

		HttpSession session = request.getSession();
		String userName = session.getAttribute("userName").toString();
		User user = userService.getUser(userName);

		payment.setPayload(PayloadHelper.getStringFromDoc(doc));
		payment.setStatus(0);
		payment.setUserid(user.getId());
		manualtransactionsService.insertManualtransactions(payment);
		return PayloadHelper.getStringFromDoc(doc);

	}
	
	private String replaceSegment(String templatexPath, String fieldxPath) {
		String[] segmentTemplate = templatexPath.split("/");
		String[] segmentField = fieldxPath.split("/");
		segmentTemplate[2] = segmentField[2];
		return String.join("/", segmentTemplate);
		
	}

	private Document parseXMLFromFile(String fileName) {
		InputStream is = getFileFromResourceAsStream(fileName);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		try {
			db = dbf.newDocumentBuilder();
			return db.parse(is);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	private InputStream getFileFromResourceAsStream(String fileName) {

		// The class loader that loaded the class
		ClassLoader classLoader = getClass().getClassLoader();
		InputStream inputStream = classLoader.getResourceAsStream(fileName);

		// the stream holding the file content
		if (inputStream == null) {
			throw new IllegalArgumentException("file not found! " + fileName);
		} else {
			return inputStream;
		}

	}

	private Document addNodeByDoc(Document doc, String fieldXPath, String fieldValue) throws Exception {
		String[] segments = fieldXPath.split("/");
		segments = (String[]) ArrayUtils.remove(segments, 0);
		PayloadHelper.createElement(doc, doc.getDocumentElement(), segments, 1, fieldValue);
		return doc;
	}
	
}

