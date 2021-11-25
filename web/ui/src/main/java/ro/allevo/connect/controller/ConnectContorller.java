package ro.allevo.connect.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.UriBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
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

import ro.allevo.connect.model.Connect;
import ro.allevo.connect.model.Consent;
import ro.allevo.connect.model.Job;
import ro.allevo.connect.model.Trigger;
import ro.allevo.connect.service.ConnectService;
import ro.allevo.connect.service.ConsentService;
import ro.allevo.connect.service.JobService;
import ro.allevo.connect.service.TriggerService;
import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.exception.NotAuthorizedException;
import ro.allevo.fintpui.model.UserRolesEntityMaps;
import ro.allevo.fintpui.service.BankService;
import ro.allevo.fintpui.service.TemplateConfigOptionsService;
import ro.allevo.fintpui.service.UserService;
import ro.allevo.fintpui.utils.JSONHelper;
import ro.allevo.fintpui.utils.PagedCollection;
import ro.allevo.fintpui.utils.Roles;
import ro.allevo.fintpui.utils.datatables.DataTables;
import ro.allevo.fintpui.utils.Utils;

@Controller
@RequestMapping("connects")
public class ConnectContorller {

	@Autowired
	Config config;

	@Autowired
	ConnectService connectService;

	@Autowired
	ConsentService consentService;

	@Autowired
	BankService bankService;

	@Autowired
	JobService jobService;

	@Autowired
	TriggerService triggerService;

	@Autowired
	private TemplateConfigOptionsService templateConfigOptions;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private UserService userService;

	@Value("${project.type}")
	private String projectType;

	private static Logger logger = LogManager.getLogger(ConnectContorller.class.getName());

	private static String jobGroup = "PSD2";
	private static final String BANK = "bank";
	private static final String INTERNALENTITY = "internalentity"; 
	private static final String CONECTHTMLPAGE = "connect/connect";
	private static final String INTERNALENTITIES ="internalEntities";
	private static final String FORMACTION = "formAction";
	private static final String CONNECT = "connect";
	private static final String INTERNALENTITIESNAME = "name";

	@GetMapping(value = CONNECT)
	public String printBanks(OAuth2Authentication auth, ModelMap model) {
		logger.info("/connection required");
		if (!Roles.hasRoles(auth, Roles.API_INTERFACE_MODIFY))
			throw new NotAuthorizedException();
		model.addAttribute("hasModifyRole", true);
		JSONObject entities= getEntities(new HashSet<>(Arrays.asList(INTERNALENTITY)));
		JSONArray internalEntity = Utils.getInternalEntities(entities, INTERNALENTITY, "id", INTERNALENTITIESNAME);
		model.addAttribute(INTERNALENTITIES, Utils.getKeyValueFromEntity(internalEntity, "id", INTERNALENTITIESNAME));
		model.addAttribute("apiUri", config.getConnectUrl());
		return CONECTHTMLPAGE;
	}

	private JSONObject getEntities(Set<String> entities) {
		return templateConfigOptions.getAllConfingOptionsValues(entities);
	}

	@GetMapping(value = "page")
	public @ResponseBody String getBanksJson(@RequestParam int draw) throws JsonProcessingException {
		logger.info("/connection required");
		

		LinkedHashMap<String, List<String>> params = null;

		if (projectType.equalsIgnoreCase("whizzer")) {
			String userName = request.getSession().getAttribute("userName").toString();
			params = new LinkedHashMap<>();
			UserRolesEntityMaps[] userRolesEntityMaps = getEntityByUserName(userName);
			Set<String> entities = getEntityIdFromUserRolesEntityMaps(userRolesEntityMaps);

			params.put("filter_internalEntityId_exact", new ArrayList<>(entities));
		}

		PagedCollection<Connect> connects = connectService.getPage(params);

		DataTables dt = new DataTables();
		if (null != connects) {
			dt.setData(connects.getItems());
			dt.setRecordsFiltered(connects.getTotal());
			dt.setRecordsTotal(connects.getTotal());
		}
		dt.setDraw(draw);
		return JSONHelper.toString(dt);
	}

	private UserRolesEntityMaps[] getEntityByUserName(String userName) {
		LinkedHashMap<String, List<String>> params = new LinkedHashMap<>();
		List<String> paramsValue = new ArrayList<>();
		paramsValue.add(String.valueOf(13));
		params.put("filter_roleId_exact", paramsValue);

		return userService.getUserRolesEntityMaps(userName, params);
	}

	private Set<String> getEntityIdFromUserRolesEntityMaps(UserRolesEntityMaps... userRolesEntityMaps) {
		Set<String> entities = new HashSet<>();
		for (UserRolesEntityMaps userEntity : userRolesEntityMaps) {
			entities.add(String.valueOf(userEntity.getEntityId()));
		}
		return entities;
	}

	/*
	 * INSERT
	 */
	@GetMapping(value = "add")
	public String addConnect(ModelMap model, @ModelAttribute(CONNECT) Connect connect,
			@ModelAttribute("consent") Consent consent) {
		logger.info("/addConnection required");
		connect.setConsentEntity(consent);
		model.addAttribute(CONNECT, connect);
		model.addAttribute(FORMACTION, "insert");
		model.addAttribute("id", 0);
		
		JSONObject entities= getEntities(new HashSet<>(Arrays.asList(INTERNALENTITY, BANK)));
		JSONArray bankEntities =  Utils.getInternalEntities(entities, BANK, "bic", "name");
		model.addAttribute("banks",  Utils.getMapInternalEntities(bankEntities, "bic", "name"));

		JSONArray internalEntities =  Utils.getInternalEntities(entities, INTERNALENTITY, "id", INTERNALENTITIESNAME);
		if (projectType.equalsIgnoreCase("whizzer")) {
			String userName = request.getSession().getAttribute("userName").toString();
			UserRolesEntityMaps[] userRolesEntityMaps = getEntityByUserName(userName);
			Set<String> entitiesSet = getEntityIdFromUserRolesEntityMaps(userRolesEntityMaps);

			internalEntities = Utils.filteredInternalEntities(internalEntities, entitiesSet);
		}
		
		
		model.addAttribute(INTERNALENTITIES,  Utils.getMapInternalEntities(internalEntities, "id", INTERNALENTITIESNAME));
		return "connect/connect_add";
	}

	@PostMapping(value = "insert")
	@ResponseBody
	public String insertConnect(ModelMap model, @Valid @ModelAttribute(CONNECT) Connect connect,
			BindingResult bindingResult) throws JsonProcessingException {
		logger.info("insert connection requested");
		if (bindingResult.hasErrors()) {
			return JSONHelper.toString(bindingResult.getAllErrors());
		}

//		connect.getConsentEntity().setConnect(connect);
		connect.getConsentEntity().setBic(connect.getBic());
		connectService.insertConnect(connect);
		return "[]";
	}

	/*
	 * DELETE
	 */
	@GetMapping(value = "/{id}/delete")
	public String deleteConnect(@PathVariable Long id) {
		logger.info("delete connection required");
		connectService.deleteConnect(id);
		return "redirect:/connects/connect";
	}

	/*
	 * EDIT
	 */
	@GetMapping(value = "/{id}/edit")
	public String editConnect(ModelMap model, @ModelAttribute(CONNECT) Connect connect, @PathVariable Long id) {
		logger.info("/editConnection requested");
		connect = connectService.getConnect(id);
		model.addAttribute(CONNECT, connect);
		model.addAttribute(FORMACTION, "update");
		JSONObject entities= getEntities(new HashSet<>(Arrays.asList(INTERNALENTITY, BANK)));
		JSONArray bankEntities =  Utils.getInternalEntities(entities, BANK, "bic", "name");
		model.addAttribute("banks",  Utils.getMapInternalEntities(bankEntities, "bic", "name"));
//		List<String> internalEntityName = new ArrayList<>();
//		for (Map<String, String> map : getInternalEntities(entities, INTERNALENTITY, "id", INTERNALENTITIESNAME)) {
//			internalEntityName.add(map.get(INTERNALENTITIESNAME));
//		}
		JSONArray internalEntities =  Utils.getInternalEntities(entities, INTERNALENTITY, "id", INTERNALENTITIESNAME);
		model.addAttribute(INTERNALENTITIES,  Utils.getMapInternalEntities(internalEntities, "id", INTERNALENTITIESNAME));
		return "connect/connect_add";
	}

	@PostMapping(value = "/update")
	@ResponseBody
	public String updateConnect(ModelMap model, @ModelAttribute(CONNECT) @Valid Connect connect,
			BindingResult bindingResult, @RequestParam("id") Long id) throws JsonProcessingException {
		logger.info("update connection requested");
		if (bindingResult.hasErrors()) {
			return JSONHelper.toString(bindingResult.getAllErrors());
		}
		connectService.updateConnect(connect);
		return "[]";
	}

	/*
	 * @GetMapping(value = "/token/{id}/update") public String newToken(ModelMap
	 * model, @PathVariable long id) throws Exception {
	 */
	/*
	 * Connect connect = connectService.getConnect(id);
	 * ResourceOwnerPasswordResourceDetails resource = new
	 * ResourceOwnerPasswordResourceDetails();
	 * resource.setAccessTokenUri(connect.getAccessTokenUri());
	 * resource.setClientId(connect.getClientId());
	 * resource.setClientSecret(connect.getClientSecret());
	 * //resource.setScope(Arrays.asList("foo read write"));
	 * resource.setScope(Arrays.asList("trust"));
	 * resource.setUsername(connect.getUserId());
	 * resource.setPassword(connect.getUserSecret());
	 * resource.setId(String.valueOf(connect.getId()));
	 * 
	 * OAuth2RestTemplate template = new OAuth2RestTemplate(resource);
	 * OAuth2AccessToken existingToken = template.getAccessToken();
	 * //((DefaultOAuth2AccessToken) existingToken).setExpiration(new Date(0L));
	 * 
	 * 
	 * connectService.getToken(connect, id); return connect.getToken();
	 */

	/*
	 * ResponseEntity<String> connects = connectService.newToken(id);
	 * 
	 * return JSONHelper.toString(connects);
	 */
	/* } */

	@GetMapping(value = "/token/{id}/delete")
	@ResponseBody
	public ResponseEntity<Integer> deleteToken(ModelMap model, @PathVariable long id) {
		Connect connect = connectService.getConnect(id);
		connect.setToken("");
		connect.setExpirationDate(null);
		ResponseEntity<String> resp = connectService.updateConnect(connect);
		model.addAttribute("code", resp.getStatusCodeValue());

		return ResponseEntity.ok(resp.getStatusCodeValue());// JSONHelper.toString();
	}

	@SuppressWarnings("finally")
	@GetMapping(value = "/consent/{id}/update")
	@ResponseBody
	public String newConsent(ModelMap model, @PathVariable long id) throws JsonProcessingException {
		Connect connect = new Connect();
		try {
			connect = connectService.getConnect(id);
			Consent consent = connect.getConsentEntity();
			consentService.insertConsent(consent);
		} finally {
			connect = connectService.getConnect(id);
			return JSONHelper.toString(connect.getConsentEntity());
		}
	}

	@PostMapping(value = "/consent/{id}/delete")
	@ResponseBody
	public ResponseEntity<Integer> deleteConsent(ModelMap model, @PathVariable long id) {
		Connect connect = connectService.getConnect(id);
		Consent consent = connect.getConsentEntity();
		ResponseEntity<String> resp = consentService.deleteConsent(consent.getBic());
// (consent, consent.getBic());

		return ResponseEntity.ok(resp.getStatusCodeValue());
	}

	@GetMapping(value = "/{id}/ViewToken")
	public String getViewToken(ModelMap model, @PathVariable long id) {
		Connect connect = connectService.getConnect(id);

		model.addAttribute(CONNECT, connect);
		model.addAttribute("title", "Edit consent");

		return "connect/connect_upd";
	}

	@GetMapping(value = "/get_redirect")
	@ResponseBody
	public String getRedirectUrl(@QueryParam(value = "{id}") Long id) {
		String code = "U1C0I6sHBtIHHFzZJmoHpeOfaiNoA5sIURzrnNspkfIhvmRKHfqXZiJcoxHcQvBturMupLBhu6Ym5x52EAFWbUsDg0a6hvhvUeNbf5KbhlJ4Wodm7iqVs6HMybYLapCm"/*
																																						 * UUID
																																						 * .
																																						 * randomUUID
																																						 * (
																																						 * )
																																						 * .
																																						 * toString
																																						 * (
																																						 * )
																																						 */;

		String shaCode = "cKrQnB7uO_7axnHLZr84--CrfCRDEnK5naYaeGKONqw";// DigestUtils.sha256Hex(code);
		request.getSession().setAttribute("code_token", code);

		Connect connect = connectService.getConnect(id);

		String redirect = "response_type=code&client_id=" + connect.getClientId() + "&";
		redirect += "code_challenge_method=S256&state=statetest&code_challenge=" + shaCode;
		redirect += "&redirect_uri=https://allevo.ro";

		String consentId = connect.getConsentEntity().getConsentId();
		String redirectUrl = connect.getUserAuthorizationUri().replace("://",
				"://" + connect.getUserId() + ":" + connect.getUserSecret() + "@");

		return redirectUrl + "?" + "scope=AIS:" + consentId + "&" + redirect;
	}

	private List<String> getFiltersParams(String... value) {
		List<String> filterParams = new ArrayList<String>();
		for (String val : value) {
			filterParams.add(val);
		}
		return filterParams;
	}

	@GetMapping(value = "newtoken")
	@ResponseBody
	public String newToken(@DefaultValue("") @QueryParam("code") String code,
			@DefaultValue("") @QueryParam("bic") String bic) {

		LinkedHashMap<String, List<String>> filters = new LinkedHashMap<>();
		filters.put("filter_bic_exact", getFiltersParams(bic.toUpperCase()));

		PagedCollection<Connect> connect = connectService.getPage(filters);
		Connect conn = connect.getItems()[0];

		String shaCode = "U1C0I6sHBtIHHFzZJmoHpeOfaiNoA5sIURzrnNspkfIhvmRKHfqXZiJcoxHcQvBturMupLBhu6Ym5x52EAFWbUsDg0a6hvhvUeNbf5KbhlJ4Wodm7iqVs6HMybYLapCm";// DigestUtils.sha256Hex(code_token);
		String response = connectService.newToken(new Long(conn.getId()), code, shaCode);

		return response;
	}

	@GetMapping(value = "tokenformtest")
	public String getTokenFormTest() {
		return "tokenFormTest";
	}

	@GetMapping(value = "{rowid}/add-job")
	public String addJob(ModelMap model, @PathVariable Long rowid) {

		Connect connect = connectService.getConnect(rowid);
		String bic = connect.getBic();
		int internalEntityId = connect.getInternalEntityId();
		String internalEntityName = "";
		JSONArray internalEntityList =  Utils.getInternalEntities(getEntities(new HashSet<>(Arrays.asList(INTERNALENTITY))), INTERNALENTITY, "id", INTERNALENTITIESNAME);
		for (int ind = 0; ind< internalEntityList.length(); ind++) {
			JSONObject object = internalEntityList.getJSONObject(ind);
			int id = Integer.parseInt(object.getString("id"));
			if (internalEntityId == id) {
				internalEntityName = object.getString(INTERNALENTITIESNAME);
				break;
			}
		}
//				internalEntitiesService.getAllInternalEntityIdAndName().get(internalEntityId);

		String time = connect.getTimeTrigger();
		boolean isJobCreated = time != null && !time.equals("");
		model.addAttribute("isJobCreated", isJobCreated);
		model.addAttribute("rowid", rowid);
		model.addAttribute("bicAndName", bic + "-" + internalEntityName);
		model.addAttribute("time", time);
		model.addAttribute(FORMACTION, "insert-trigger");
		return "connect/job_add";
	}

	@PostMapping(value = "insert-trigger")
	public String insertTrigger(Long rowid, boolean isJobCreated, String bicAndName, String timePicker) { // timePicker
																											// = 08:15

		if (!timePicker.equals("")) {
			Connect connect = connectService.getConnect(rowid);
			String[] timeSplit = timePicker.split(":");
			String hour = timeSplit[0];
			String minute = timeSplit[1];

			if (!isJobCreated) {
				insertJob(bicAndName);
				insertTrigger(minute, hour, bicAndName);
			} else {
				updatetrigger(minute, hour, bicAndName);
			}

			connect.setTimeTrigger(timePicker);
			connectService.updateConnect(connect);
			return CONECTHTMLPAGE;
		}
		return "[]";
	}

	private void insertJob(String bicAndName) {
		Job job = new Job();
		job.setName(bicAndName);
		job.setGroup(jobGroup);
		job.setParams(new HashMap<>());
		job.setAction("start");
		jobService.insertJob(job);
	}

	private void insertTrigger(String minute, String hour, String bicAndName) {

		Trigger trigger = new Trigger();
		trigger.setName(bicAndName + "trigger");
		trigger.setGroup(bicAndName + "groupTrigger");
		trigger.setJobName(bicAndName);
		trigger.setCronExpression("0 " + minute + " " + hour + " ? * MON,TUE,WED,THU,FRI *");
		trigger.setAction("start");

		URI uri = UriBuilder.fromUri(config.getConnectUrl()).path("jobs/" + jobGroup + "/" + bicAndName + "/triggers")
				.build();
		triggerService.insertTrigger(uri, trigger);
	}

	private void updatetrigger(String minute, String hour, String bicAndName) {
		String id = bicAndName + "groupTrigger/" + bicAndName + "trigger";
		Trigger trigger = triggerService.getTrigger(id);
		trigger.setCronExpression("0 " + minute + " " + hour + " ? * MON,TUE,WED,THU,FRI *");
		triggerService.updateTrigger(trigger, id);
	}

	@PostMapping(value = "delete-trigger")
	public String deleteTrigger(Long rowid, String bicAndName) {

		Connect connect = connectService.getConnect(rowid);
		connect.setTimeTrigger("");
		connectService.updateConnect(connect);
		jobService.deleteJob(jobGroup + "/" + bicAndName);
		return CONECTHTMLPAGE;
	}
}
