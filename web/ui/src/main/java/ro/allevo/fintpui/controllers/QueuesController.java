/*
* FinTP - Financial Transactions Processing Application
* Copyright (C) 2013 Business Information Systems (Allevo) S.R.L.
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>
* or contact Allevo at : 031281 Bucuresti, 23C Calea Vitan, Romania,
* phone +40212554577, office@allevo.ro <mailto:office@allevo.ro>, www.allevo.ro.
*/

package ro.allevo.fintpui.controllers;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
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
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Document;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.exception.NotAuthorizedException;
import ro.allevo.fintpui.model.EditRules;
import ro.allevo.fintpui.model.FinTPLists;
import ro.allevo.fintpui.model.History;
import ro.allevo.fintpui.model.MessageType;
import ro.allevo.fintpui.model.MessagesGroup;
import ro.allevo.fintpui.model.Queue;
import ro.allevo.fintpui.model.QueueMoveMaps;
import ro.allevo.fintpui.model.QueuesCountEntity;
import ro.allevo.fintpui.model.QueuesDuplicate;
import ro.allevo.fintpui.model.RoutingJobParameters;
import ro.allevo.fintpui.model.TemplateConfigOptions;
import ro.allevo.fintpui.model.User;
import ro.allevo.fintpui.model.UserAction;
import ro.allevo.fintpui.model.UserRole;
import ro.allevo.fintpui.service.EditRulesService;
import ro.allevo.fintpui.service.ExternalEntitiesService;
import ro.allevo.fintpui.service.HistoriesServices;
import ro.allevo.fintpui.service.MessageService;
import ro.allevo.fintpui.service.MessageTypeService;
import ro.allevo.fintpui.service.QueueService;
import ro.allevo.fintpui.service.ReportService;
import ro.allevo.fintpui.service.ServiceMapService;
import ro.allevo.fintpui.service.TemplateConfigOptionsService;
import ro.allevo.fintpui.service.UserService;
import ro.allevo.fintpui.utils.Filters;
import ro.allevo.fintpui.utils.JSONHelper;
import ro.allevo.fintpui.utils.PagedCollection;
import ro.allevo.fintpui.utils.Pair;
import ro.allevo.fintpui.utils.PayloadHelper;
import ro.allevo.fintpui.utils.Roles;
import ro.allevo.fintpui.utils.Utils;
import ro.allevo.fintpui.utils.datatables.DataTables;

@Controller
@RequestMapping(value = "/queues")
public class QueuesController {

	@Autowired
	Config config;

	@Autowired
	private QueueService queueService;

	@Autowired
	private ReportService reportService;

	@Autowired
	private MessageService messageService;

	@Autowired
	private ServiceMapService serviceMapService;

	@Autowired
	private MessageTypeService messageTypeService;

	@Autowired
	private MessageService messagesService;

	@Autowired
	private TemplateConfigOptionsService templateConfigOptions;

	@Autowired
	private EditRulesService editRulesService;

	@Autowired
	private HistoriesServices historiesService;

	@Autowired
	HttpServletRequest request;

	@Autowired
	private UserService userService;

	@Autowired
	private ExternalEntitiesService externalEntitiesService;

	private static Logger logger = LogManager.getLogger(QueuesController.class.getName());
	private static final String QUEUES = "queues";

	/*
	 * DISPLAY Transactions
	 */
	@GetMapping
	public ModelAndView printMenu(OAuth2Authentication auth, ModelMap model) {

		logger.info("/queues requested");
		if (!Roles.hasRoles(auth, Roles.SUPERVISER))
			throw new NotAuthorizedException();

		return new ModelAndView(QUEUES, model);
	}

	/* Display Administration Queues */
	@RequestMapping(value = "/admin")
	public ModelAndView printMenuQueuesAdmin(OAuth2Authentication auth, ModelMap model) {

		logger.info("/queues admin requested");
		if (!Roles.hasRoles(auth, Roles.QUEUES_VIEW))
			throw new NotAuthorizedException();
		model.addAttribute("hasModifyRole", Roles.hasRoles(auth, Roles.QUEUES_MODIFY));

		return new ModelAndView("queuesAdmin", model);
	}

	@GetMapping(value = "/count")
	public @ResponseBody String getQueuesCount() throws JsonProcessingException {
		logger.info("/queues required");
		Map<Integer, Long> queuesCount = new HashMap<>();
		long item = 0;
		long sumNoOfTx = 0;
		for (QueuesCountEntity qCount : queueService.getQueuesCount()) {
			queuesCount.put(qCount.getId(), qCount.getNoOfTx());
			if (qCount.getNoOfTx() > 0)
				item++;
			sumNoOfTx += qCount.getNoOfTx();
		}
		queuesCount.put(0, item);
		queuesCount.put(-1, sumNoOfTx);
		return JSONHelper.toString(queuesCount);
	}

	@GetMapping(value = "/page")
	public @ResponseBody String getQueuesJson(@RequestParam int draw) throws JsonProcessingException {
		logger.info("/queues required");
		PagedCollection<Queue> queues = queueService.getPage();

		DataTables dt = new DataTables();
		if (null != queues) {
			dt.setData(queues.getItems());
			dt.setRecordsFiltered(queues.getTotal());
			dt.setRecordsTotal(queues.getTotal());
		}
		dt.setDraw(draw);
		return JSONHelper.toString(dt);
	}

	@GetMapping(value = "/page-finale")
	public @ResponseBody String getTransactionsFinaleJson(@RequestParam int draw) throws JsonProcessingException {
		logger.info("/queues required");
		List<HashMap<String, Object>> queues = queueService.getTransactionsFinale();

		DataTables dt = new DataTables();
		if (null != queues) {
			dt.setData(queues.toArray());
			dt.setRecordsFiltered(queues.toArray().length);
			dt.setRecordsTotal(queues.toArray().length);
		}
		dt.setDraw(draw);
		return JSONHelper.toString(dt);
	}

	@GetMapping(value = "/page-interm")
	public @ResponseBody String getTransactionsIntermJson(@RequestParam int draw) throws JsonProcessingException {
		logger.info("/queues required");
		List<HashMap<String, Object>> queues = queueService.getTransactionsInterm();

		DataTables dt = new DataTables();
		if (null != queues) {
			dt.setData(queues.toArray());
			dt.setRecordsFiltered(queues.toArray().length);
			dt.setRecordsTotal(queues.toArray().length);
		}
		dt.setDraw(draw);
		return JSONHelper.toString(dt);
	}

	/*
	 * INSERT
	 */

	@GetMapping(value = "/add")
	public String addQueue(ModelMap model, @ModelAttribute("queue") Queue queue) {
		logger.info("/addQueue required");
		model.addAttribute("queue", queue);
		model.addAttribute("types", getQueueTypes("edit"));
		model.addAttribute("formAction", "insert");
		model.addAttribute("connectors", serviceMapService.getAllServiceNamesAndId());
		model.addAttribute(QUEUES, queueService.getAllQueuesNamesAndId());
		return "queues_add";
	}

	@PostMapping(value = "insert")
	@ResponseBody
	public String insertBank(ModelMap model, @Valid @ModelAttribute("queue") Queue queue, BindingResult bindingResult)
			throws JsonProcessingException {
		logger.info("insert queue requested");
		if (bindingResult.hasErrors()) {
			return JSONHelper.toString(bindingResult.getAllErrors());
		}
		queue.setQueueMoveMapsEntity(queue.getQueueMoveMaps().stream().map(temp -> {
			QueueMoveMaps queueMoveMaps = new QueueMoveMaps();
			queueMoveMaps.setDestinationqueueid(temp);
			queueMoveMaps.setSourcequeueid(1);
			return queueMoveMaps;
		}).collect(Collectors.toList()));
		queueService.insertQueue(queue);
		if (queue.getQueueMoveMaps() != null) {
			queue = queueService.getQueue(queue.getName());
			Integer id = queue.getId();
			queue.getQueueMoveMapsEntity().forEach(c -> c.setSourcequeueid(id));
			queueService.updateQueue(queue.getName(), queue);
		}
		return "[]";
	}

	/*
	 * EDIT
	 */

	@GetMapping(value = "/{queueName}/edit")
	public String editBank(ModelMap model, @ModelAttribute("queue") Queue queue, @PathVariable String queueName) {
		logger.info("/editQueue requested");
		queue = queueService.getQueue(queueName);
		if (null != queue.getQueueMoveMapsEntity()) {
			queue.setQueueMoveMaps(queue.getQueueMoveMapsEntity().stream().map(QueueMoveMaps::getDestinationqueueid)
					.collect(Collectors.toList()));
		}
		model.addAttribute("queue", queue);
		model.addAttribute("types", getQueueTypes(""));
		model.addAttribute("formAction", "update");
		model.addAttribute("connectors", serviceMapService.getAllServiceNamesAndId());
		model.addAttribute(QUEUES, queueService.getAllQueuesNamesAndId());
		return "queues_add";
	}

	@PostMapping(value = "/update")
	@ResponseBody
	public String updateBank(ModelMap model, @ModelAttribute("queue") @Valid Queue queue, BindingResult bindingResult,
			@RequestParam("name") String name) throws JsonProcessingException {
		logger.info("update queue requested");
		if (bindingResult.hasErrors()) {
			return JSONHelper.toString(bindingResult.getAllErrors());
		}
		queue.setQueueMoveMapsEntity(queue.getQueueMoveMaps().stream().map(temp -> {
			QueueMoveMaps queueMoveMaps = new QueueMoveMaps();
			queueMoveMaps.setDestinationqueueid(temp);
			queueMoveMaps.setSourcequeueid(queue.getId());
			return queueMoveMaps;
		}).collect(Collectors.toList()));
		queueService.updateQueue(name, queue);
		return "[]";
	}

	/*
	 * DELETE
	 */

	@RequestMapping(value = "/{queueName}/delete")
	public String deleteQueue(@PathVariable String queueName) {
		logger.info("delete queue required");
		queueService.deleteQueue(queueName);
		return "redirect:/queues/admin";
	}

	/*
	 * @RequestMapping(value = "getQueueButtons", method = RequestMethod.GET)
	 * public @ResponseBody String getQueueButtons(@RequestParam Map<String,String>
	 * allRequestParams) { JSONObject r = new JSONObject(); return r.toString(); }
	 */

	// @RequestMapping(value = "/view-payload", method = RequestMethod.GET)
	@GetMapping(value = "/payload")
	public String viewPayload(ModelMap model, @RequestParam(value = "id", required = true) String id,
			@RequestParam(value = "type", required = true) String messageType,
			@RequestParam(value = "action", required = false, defaultValue = "view") String action,
			@RequestParam(value = "history", required = false, defaultValue = "true") Boolean history) throws Exception {

		String businessArea = messageTypeService.getMessageType(messageType).getBusinessArea();

		LinkedHashMap<String, List<String>> param = new LinkedHashMap<>();

		String correlationId = "";
		String payloadString = "";
		// what file load
		String tail = "";
		Boolean loadXMLParce = false;

		if (Boolean.TRUE.equals(history)) {
			ObjectNode entryQueueMessage = messageService.getEntryQueueMessage(id);
			correlationId = entryQueueMessage.get("correlationId").asText();
			payloadString = entryQueueMessage.get("payload").asText();
			param.put("filter_correlationid_exact", Filters.getFiltersParams(correlationId));
			model.addAttribute("entryQueueMessage", entryQueueMessage);
		} else {
			param.put("filter_guid_exact", Filters.getFiltersParams(id));
			param.put("sort_guid", Filters.getFiltersParams("asc"));
			History entryQueueMessageHistory = historiesService.getHistory(id);
			byte[] decodedBytes = Base64.getDecoder().decode(entryQueueMessageHistory.getPayload());
			correlationId = entryQueueMessageHistory.getCorrelationid();
			payloadString = new String(decodedBytes);
			tail = "Original";
			if (config.getProjectType().equalsIgnoreCase("adpharma"))
				loadXMLParce = true;
		}

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		Document payload = null;
		try {
			factory.newDocumentBuilder();
			payload = PayloadHelper.parsePayload(payloadString);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("payloadString", StringEscapeUtils.escapeHtml(payloadString));
		}

		ObjectNode message = messageService.getMessage(businessArea, correlationId, "live");// correlationid
		model.addAttribute("message", message);
		model.addAttribute("messageType", messageType);

		// friendly tags
		if (action.equals("view")) {
			if (null != payload)
				PayloadHelper.friendlyPayload(payload);
		} else {
			EditRules[] editRules = editRulesService.getAllEditRulesByMessage(messageType);

			Set<String> listEntity = new HashSet<>();
			List<FinTPLists> finTPLists = config.getProjectType().equalsIgnoreCase("adpharma")?Utils.adpharmaLists:Utils.finTPLists;
			for (FinTPLists finLists : finTPLists) {
				listEntity.add(finLists.getName());
			}

			JSONObject jsonObject =  templateConfigOptions.getAllConfingOptionsValues(listEntity);
			model.addAttribute("dropDowns", jsonObject.toString());
			Map<Integer, String> options = new HashMap<>();
			for (TemplateConfigOptions option : templateConfigOptions.getAllTemplateConfig()) {
				options.put(option.getId(), option.getDatasource());
			}
			model.addAttribute("templateOptions", options);

			if (null != payload) {
				String addStringNode = config.getXML();
				addStringNode = addStringNode.replace("CstmrCdtTrfInitnOthr", messageType);
				if ( null == PayloadHelper.getElementByXpath(payload, addStringNode)) {
					String[] segments = addStringNode.split("/");
					segments = (String[]) ArrayUtils.remove(segments, 0);
					PayloadHelper.createElement(payload, payload.getDocumentElement(), segments, 1, "[[#{remove}]]");
				}
				PayloadHelper.editPayload(payload, editRules);
			}
		}
		model.addAttribute("id", id);
		model.addAttribute("project", config.getProjectType());
		model.addAttribute("payload", payload);
		model.addAttribute("action", action);
		model.addAttribute("xmlParse", loadXMLParce);
		return "viewMessage" + tail;
	}

	@PostMapping(value = "{id}/save-payload")
	@ResponseBody
	public String savePayload(ModelMap model, @PathVariable(value = "id", required = true) String id,
			@RequestParam() Map<String, String> map) throws Exception {

		ObjectNode entryQueueMessage = messageService.getEntryQueueMessage(id);
		Document payload = PayloadHelper.parsePayload(entryQueueMessage.get("payload").asText());
		String savePayload = PayloadHelper.savePayload(payload, map, true, true);
		queueService.updatePayload(id, entryQueueMessage.get("correlationId").asText(), savePayload);
		return "";
	}

	private User getUserFromSession() {
		HttpSession session = request.getSession();
		String userName = session.getAttribute("userName").toString();
		return userService.getUser(userName);
	}

	private UserRole[] getDefinedUserRoles(Integer userId, boolean allRoles) {
		LinkedHashMap<String, List<String>> filter = new LinkedHashMap<>();

		filter.put("filter_userId_exact", Arrays.asList(String.valueOf(userId)));
		if (Boolean.FALSE.equals(allRoles)) {
			filter.put("filter_roleEntity.userDefined_exact", Arrays.asList("1"));
		}

		return userService.getUserRolesByUserDefined(userId, filter);
	}

	@GetMapping(value = "/{queueName}")
	public String viewQueue(@PathVariable String queueName, ModelMap model) {
		logger.info("/queues/%s  requested", queueName);

		// add queue name attribute
		model.addAttribute("queueName", queueName);

		List<MessageType> messageTypes = new ArrayList<>();

		// build hashmap of headers (message type is the key, array
		// containing table headers is the value)
		// get groups for current message type
		// if there are no groups, then treat as a non-batchalbe page

		//
		// headers Map is a hash in which key: messageType (s.a. 103)
		// value: array of strings which represent the table headers
		// name (s.a. Sender, Receiver, Reference ...)
		//
		// HashMap<String, List<String>> headersMap = new HashMap<>();
		//
		// columns Map is a hash in which key: messageType (s.a. 103)
		// value: array of strings which represent the fields contained
		// by the json object returned by fintp API (s.a. sender,
		// receiver, trn ...) they correspond in a 1-1 relantionship
		// with headersMap
		//
		HashMap<String, ObjectNode[]> columnsMap = new HashMap<>();

		//
		// gropsMap is a hash table in which key: message type value :
		// list of groups belonging to that message type
		//
		HashMap<String, List<MessagesGroup>> groupsMap = new HashMap<>();

		//
		// gropsFieldsMap is a hash table in which key: message type
		// (that admits groups/batches) value : "grouped by" fields
		//
		HashMap<String, ObjectNode[]> groupFieldsMap = new HashMap<>();
		try {
			messageTypes.addAll(Arrays.asList(queueService.getMessageTypesInQueue(queueName)));
		} catch (Exception e) {
			e.printStackTrace();
		}

		User user = getUserFromSession();
		Integer userId = user.getId();
		UserRole[] userRoles = getDefinedUserRoles(userId, true);
		List<String> sortEntities;
		HashMap<String, String> bussinesAreas = new HashMap<>();

		List<UserRole> sourceList = Arrays.asList(userRoles);

		boolean isSuperviser = sourceList.stream().filter(f -> f.getRoleEntity().getName().contains(Roles.SUPERVISER))
				.collect(Collectors.toList()).isEmpty();// is it OK check by 1653???
		Set<String> setOfMessage = null;
		if (!isSuperviser) {
			setOfMessage = Roles.getMessageTypeByRoles(userRoles);
			sortEntities = new ArrayList<>(Roles.getEntityByRoles(userRoles));
			Collections.sort(sortEntities);
		}

		sourceList = sourceList.stream().filter(f -> userId == f.getUserId() && 1 == f.getRoleEntity().getUserDefined())
				.collect(Collectors.toList());
		if (sourceList.isEmpty() && !isSuperviser) {
			throw new NotAuthorizedException();
		}

		for (int i = 0; i < messageTypes.size(); i++) {
			if (messageTypes.get(i) == null) {
				MessageType m = new MessageType();
				m.setBusinessArea("Outstanding");
				m.setFriendlyName("Undefined");
				m.setMessageType("Undefined");
				m.setReportingStorage("MTUndefinedView");
				m.setParentMessageType("n/a");
				messageTypes.set(i, m);
			}
			bussinesAreas.put(messageTypes.get(i).getMessageType(), messageTypes.get(i).getBusinessArea());
			String messageType = messageTypes.get(i).getMessageType();
			if (!isSuperviser && !setOfMessage.contains(messageType))
				continue;
			columnsMap.put(messageType, reportService.getTableHeaders(messageType));
			if (messageType.equals("Undefined")) {
				ObjectNode[] objs = reportService.getGroupHeaders(messageType);
				if (objs != null && objs.length > 0) {
					ObjectNode[] objs1 = { objs[0] };
					groupFieldsMap.put(messageType, objs1);
				} else {
					groupFieldsMap.put(messageType, objs);
				}
			}
			Map<String, Object> map = filteredMessageGroup(queueName, messageType, isSuperviser, sourceList);
			if (map.containsKey("groupFieldNames"))
				groupFieldsMap.put(messageType, (ObjectNode[]) map.get("groupFieldNames"));
			if (map.containsKey("groupsMap"))
				groupsMap.put(messageType, (List<MessagesGroup>) map.get("groupsMap"));
		}

		model.addAttribute("messageTypes", messageTypes);
		model.addAttribute("columns", columnsMap);
		model.addAttribute("groupsMap", groupsMap);
		model.addAttribute("groupFieldNames", groupFieldsMap);
		model.addAttribute("groupFilters", queueService.getQueueMessageGroupsFilter());
		model.addAttribute("groupFields", queueService.getQueueMessageGroups());
		model.addAttribute("dropDowns","");

		//if (bussinesAreas.containsValue("Payments")) 
		{
			Set<String> listEntity = new HashSet<>();
			List<FinTPLists> finTPLists = config.getProjectType().equalsIgnoreCase("adpharma") ? Utils.adpharmaLists
					: Utils.finTPLists;
			for (FinTPLists finLists : finTPLists) {
				listEntity.add(finLists.getName());
			}
			model.addAttribute("dropDowns", templateConfigOptions.getAllConfingOptionsValues(listEntity).toString());
		}

		Map<String, UserAction[]> selectionActions = new HashMap<>();
		Map<String, UserAction[]> groupActions = new HashMap<>();

		for (MessageType messageType : messageTypes) {
			selectionActions.put(messageType.getMessageType(),
					queueService.getSelectionActions(queueName, messageType.getMessageType()));
			groupActions.put(messageType.getMessageType(),
					queueService.getGroupActions(queueName, messageType.getMessageType()));
		}
		List<QueueMoveMaps> queueMoveMaps = queueService.getQueueByTransaction(queueName).getQueueMoveMapsEntity();
		if (null != queueMoveMaps) {
			List<Integer> queueDestinationIds = queueMoveMaps.stream().map(QueueMoveMaps::getDestinationqueueid)
					.collect(Collectors.toList());
			Map<String, String> queuesDestination = queueService.getTransactions(null).stream()
					.filter(map -> queueDestinationIds.contains(map.get("id")))
					.collect(Collectors.toMap(map -> map.get("label").toString(), map -> map.get("name").toString()));
			Map<String, String> sortedMap = queuesDestination.entrySet().stream()
                    .sorted(Entry.comparingByValue())
                    .collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
			model.addAttribute("queuesDestination", sortedMap);
		}
		boolean ruleOperate = Arrays.stream(userRoles).anyMatch(userRole -> userRole.getAction().contains("operate"));
		model.addAttribute("viewOperateButton", ruleOperate);
		model.addAttribute("selActions", selectionActions);
		model.addAttribute("grpActions", groupActions);
		model.addAttribute("externalEntitiesName", externalEntitiesService.getExternalEntitiesWithOutRules());
		model.addAttribute("bussinesAreas", bussinesAreas);
		return "queue";
	}

	private Map<String, Object> filteredMessageGroup(String queueName, String messageType, Boolean isSuperviser,
			List<UserRole> sourceList) {
		Map<String, Object> model = new HashMap<>();
		List<MessagesGroup> msgsGroup = reportService.getTransactionsInGroup(queueName, messageType);
		if (messageType.equals("Undefined"))
			msgsGroup.subList(1, msgsGroup.size()).clear();
		if (Boolean.TRUE.equals(isSuperviser)) {
			for (MessagesGroup msg : msgsGroup) {
				msg.setHasOperate(true);
			}
			model.put("groupsMap", msgsGroup);
		} else {
			List<MessagesGroup> filteredMsgGroups = new ArrayList<>();
			for (MessagesGroup msg : msgsGroup) {
				msg.setHasOperate(false);
				List<UserRole> filteredListUndefined = sourceList.stream()
						.filter(f -> f.getRoleEntity().getMessageTypes().contains(messageType))
						.collect(Collectors.toList());
				if (!filteredListUndefined.isEmpty()) {
					if (!filteredListUndefined.stream().filter(f -> "operate".equalsIgnoreCase(f.getAction()))
							.collect(Collectors.toList()).isEmpty()) {
						msg.setHasOperate(true);
					}
					if (!filteredListUndefined.stream().filter(f -> "view".equalsIgnoreCase(f.getAction()))
							.collect(Collectors.toList()).isEmpty()) {
						filteredMsgGroups.add(msg);
					}
//					filteredMsgGroups.add(msg);
				}
			}
			model.put("groupsMap", filteredMsgGroups);
		}

		if (!messageType.equals("Undefined")) {
			model.put("groupFieldNames", reportService.getGroupHeaders(messageType));
		}
		return model;
	}

	@GetMapping(value = "/page-messages")
	public @ResponseBody String getMessagesDynamicJson(@RequestParam int draw, @RequestParam String queueName,
			@RequestParam String messageType) throws JsonProcessingException {

		PagedCollection<ObjectNode> messages;

		User user = getUserFromSession();
		Integer userId = user.getId();
		UserRole[] userRoles = getDefinedUserRoles(userId, true);
		List<String> sortEntities;

		List<UserRole> sourceList = Arrays.asList(userRoles);

		boolean isSuperviser = sourceList.stream().filter(f -> f.getRoleEntity().getName().contains(Roles.SUPERVISER))
				.collect(Collectors.toList()).isEmpty();// is it OK check by 1653???
		LinkedHashMap<String, List<String>> params = new LinkedHashMap<>();
		if (!isSuperviser) {
			sortEntities = new ArrayList<>(Roles.getEntityByRoles(userRoles));
			if (!sortEntities.isEmpty()) {
//			Collections.sort(sortEntities);
				params.put("filter_payload", sortEntities);
			}
		}
		messages = messagesService.getMessagesInQueue(queueName, messageType, params);

		DataTables dt = new DataTables();
		if (null != messages) {
			dt.setData(messages.getItems());
			dt.setRecordsFiltered(messages.getTotal());
			dt.setRecordsTotal(messages.getTotal());
		} else {
			dt.setData(new ObjectNode[0]);
			dt.setRecordsFiltered(0);
			dt.setRecordsTotal(0);
		}

		dt.setDraw(draw);
		return JSONHelper.toString(dt);
	}

	@GetMapping(value = "/page-duplicate")
	public @ResponseBody String getDuplicateMSGDetails(@RequestParam int draw, @RequestParam String id,
			@RequestParam String queue, @RequestParam Map<String, String> allRequestParams) throws IOException {

		final String DATA = "columns[%s][%s]";
		final String SEARCHVALUE = "columns[%s][%s][%s]";
		QueuesDuplicate[] duplicates = queueService.getDuplicateMSG(id, queue);
		// NavigableMap<String, String[]> item = new TreeMap<>(allRequestParams);
		Map<String, String> map = new HashMap(allRequestParams);
		Map<String, String> paramsMap = new TreeMap(map);
		List<QueuesDuplicate> listDuplicates = Arrays.asList(duplicates);
//		
		Integer index = 0;
//		
		while (paramsMap.containsKey(String.format(DATA, index, "data"))) {
			String paramKey = String.format(SEARCHVALUE, index, "search", "value");
			String paramData = String.format(DATA, index, "data");
			if (paramsMap.get(paramData).length() > 0 && paramsMap.containsKey(paramKey)
					&& paramsMap.get(paramKey).length() > 0) {
				String getMethode = getFirstUpperString(paramsMap.get(paramData));
				String searchValue = paramsMap.get(paramKey).trim().toUpperCase();
				Method method;
				try {
					method = QueuesDuplicate.class.getMethod("get" + getMethode, null);
					listDuplicates = listDuplicates.stream().filter(dupl -> {
						try {
							return method.invoke(dupl, null).toString().toUpperCase().contains(searchValue);
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						}
						return false;
					}).collect(Collectors.toList());
				} catch (NoSuchMethodException | SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			index++;
		}

		DataTables dt = new DataTables();
		if (null != listDuplicates) {
			dt.setData(listDuplicates.toArray());
			dt.setRecordsFiltered(listDuplicates.size());
			dt.setRecordsTotal(listDuplicates.size());
		}
		dt.setDraw(draw);
		return JSONHelper.toString(dt);
	}

	private String getFirstUpperString(String strings) {
		String firstLetter = strings.substring(0, 1).toUpperCase();
		return firstLetter + strings.substring(1);
	}

	@PostMapping(value = "/message-routing-jobs")
	@ResponseBody
	public String messageRoutingJobs(HttpServletRequest request, @RequestParam Map<String, String> allRequestParams)
			throws IOException {
		final String messageId = "messageId";
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode item = objectMapper.readValue(allRequestParams.get("item"), ObjectNode.class);
		String[] msgIds = new String[item.get(messageId).size()];

		for (int i = 0; i < item.get(messageId).size(); i++) {
			msgIds[i] = item.get(messageId).get(i).asText();
		}

		String reason = "";

		if (null != item.get("reason"))
			reason = item.get("reason").asText();

		String details = "";
		if (null != item.get("details"))
			details = item.get("details").asText();

		RoutingJobParameters params = new RoutingJobParameters(item.get("action").asText(), reason, details,
				item.get("messageType").asText(), msgIds);
		queueService.sendRoutingJobs(item.get("queueName").asText(), params);

		return item.get("action").asText();
	}

	@PostMapping(value = "/group-routing-jobs")
	@ResponseBody
	public String groupRoutingJobs(HttpServletRequest request, @RequestParam Map<String, String> allRequestParams)
			throws IOException {
		final String GROUPKEYS = "groupKeys";
		final String FIELDVALUES = "fieldValues";
		final String TIMEKEYS = "timeKeys";
		final String messageId = "messageId";
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode item = objectMapper.readValue(allRequestParams.get("item"), ObjectNode.class);

		String[] grpKeys = new String[item.get(GROUPKEYS).size()];
		
		String[] msgIds = new String[item.get(messageId).size()];

		for (int i = 0; i < item.get(messageId).size(); i++) {
			msgIds[i] = item.get(messageId).get(i).asText();
		}

		for (int i = 0; i < item.get(GROUPKEYS).size(); i++) {
			grpKeys[i] = item.get(GROUPKEYS).get(i).asText();
		}

		String[] timeKeys = new String[item.get(TIMEKEYS).size()];

		for (int i = 0; i < item.get(TIMEKEYS).size(); i++) {
			timeKeys[i] = item.get(TIMEKEYS).get(i).asText();
		}
		int max = 3;
		for (int i = 0; i < item.get(FIELDVALUES).size(); i++) {
			max = max<item.get(FIELDVALUES).get(i).size()?item.get(FIELDVALUES).get(i).size():max;
		}
			

		String[][] fieldValues = new String[item.get(FIELDVALUES).size()][max];
		for (int i = 0; i < item.get(FIELDVALUES).size(); i++)
			for (int j = 0; j < item.get(FIELDVALUES).get(i).size(); j++) {
				fieldValues[i][j] = item.get(FIELDVALUES).get(i).get(j).asText();
			}

		String reason = "";

		if (null != item.get("reason"))
			reason = item.get("reason").asText();

		String details = "";
		if (null != item.get("details"))
			details = item.get("details").asText();

		RoutingJobParameters params = new RoutingJobParameters(item.get("action").asText(), reason, details,
				item.get("messageType").asText(), grpKeys, timeKeys, fieldValues, msgIds);
		queueService.sendRoutingJobs(item.get("queueName").asText(), params);

		return item.get("action").asText();
	}

	private List<Pair<String, String>> getQueueTypes(String... omitType) {
		List<Pair<String, String>> result = new ArrayList<>();
		List<Pair<String, String>> listToVerify = queueService.getAllQueueTypes();
		for (Pair<String, String> type : listToVerify) {
			if (Arrays.stream(omitType).noneMatch(ot -> ot.equalsIgnoreCase(type.getSecond()))) {
//			if (!type.getSecond().equalsIgnoreCase(omitType)) {
				result.add(type);
			}
		}
		return result;
	}

	@GetMapping(value = "/page-history")
	public @ResponseBody String viewHistory(@RequestParam int draw, @RequestParam String id)
			throws JsonProcessingException {
		LinkedHashMap<String, List<String>> params = new LinkedHashMap<>();
		params.put("filter_correlationid_exact", Filters.getFiltersParams(id));

		PagedCollection<History> histories = historiesService.getAllHistoriesPaged(params);

		DataTables dt = new DataTables();
		if (null != histories) {
			dt.setData(histories.getItems());
			dt.setRecordsFiltered(histories.getTotal());
			dt.setRecordsTotal(histories.getTotal());
		}
		dt.setDraw(draw);
		return JSONHelper.toString(dt);
	}
}
