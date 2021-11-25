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
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.exception.NotAuthorizedException;
import ro.allevo.fintpui.model.Event;
import ro.allevo.fintpui.model.EventsProcedureCallCursor;
import ro.allevo.fintpui.model.MessageType;
import ro.allevo.fintpui.model.ReportFilter;
import ro.allevo.fintpui.model.Status;
import ro.allevo.fintpui.model.User;
import ro.allevo.fintpui.model.UserRole;
import ro.allevo.fintpui.service.EventService;
import ro.allevo.fintpui.service.EventsProcedureCallService;
import ro.allevo.fintpui.service.FilterService;
import ro.allevo.fintpui.service.MessageService;
import ro.allevo.fintpui.service.MessageTypeService;
import ro.allevo.fintpui.service.ReportService;
import ro.allevo.fintpui.service.UserService;
import ro.allevo.fintpui.utils.Filters;
import ro.allevo.fintpui.utils.JSONHelper;
import ro.allevo.fintpui.utils.PagedCollection;
import ro.allevo.fintpui.utils.PayloadHelper;
import ro.allevo.fintpui.utils.ReportsExportHelper;
import ro.allevo.fintpui.utils.Roles;
import ro.allevo.fintpui.utils.datatables.DataTables;

@Controller
@RequestMapping(value = "/reports")
public class ReportsController {

	private static Logger logger = LogManager.getLogger(ReportsController.class.getName());

	@Autowired
	private MessageService messageService;

	@Autowired
	private ReportService reportService;

	@Autowired
	private MessageTypeService messageTypeService;

	@Autowired
	private UserService userService;

	@Autowired
	private FilterService filterService;

	@Autowired
	private EventsProcedureCallService eventsProcedureCallService;

	@Autowired
	HttpServletRequest request;

	@Autowired
	private EventService eventService;
	
	@Autowired
	Config config;

	static final String PARAMS = "params";
	static final String BUSINESSAREA = "businessArea";
	
	private enum day {
		START,
		END,
		NONE
	}

	@GetMapping(value = "/general/toPDF")
	public @ResponseBody ResponseEntity<byte[]> exportToPDF(@RequestParam Map<String, String> allRequestParams)
			throws IOException {
		logger.info("/reports/toPDF requested");

		return exportTo("toPDF", allRequestParams); 
	}

	@RequestMapping(value = "/general/toExcel")
	public @ResponseBody ResponseEntity<byte[]> exportToExcel(@RequestParam Map<String, String> allRequestParams)
			throws IOException {
		logger.info("/reports/toExcel requested");

		return exportTo("toExcel", allRequestParams);
	}

	private ResponseEntity<byte[]> exportTo(String exportTo, Map<String, String> allRequestParams) throws IOException{
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String fileName = "FinTPMessages" + sdf.format(now) + ".pdf";

		ObjectMapper mapper = new ObjectMapper();
		LinkedHashMap<String, List<String>> params = mapper.readValue(allRequestParams.get(PARAMS), LinkedHashMap.class);

		String businessArea = params.get(BUSINESSAREA).get(0);
		params.put("pagination", Arrays.asList(new String[]{"pagination"}));

		List<String> selectedColumns =  params.get("columnsSel");
		List<String> columnHeaders = getSelectedHeaders(businessArea, selectedColumns);
		params = getFormattingParams(params);
		List<List<String>> messages = getSelectedMessages(businessArea, prepareFilters(params), selectedColumns);
		if (exportTo.equalsIgnoreCase("toExcel")) {
			fileName = fileName.replace("pdf", "xlsx");
			return ReportsExportHelper.ExportToExcel(fileName, columnHeaders, messages);
		}
		return ReportsExportHelper.ExportToPDF(fileName, columnHeaders, messages);
	}
	
	private List<String> getSelectedHeaders(String businessArea, List<String> selectedColumns) {
		ObjectNode[] columns = reportService.getMessageResults(businessArea);

		List<String> columnHeaders = new ArrayList<>();
		columnHeaders.add("No");
		for (ObjectNode node : columns) {
			if (selectedColumns.contains(node.get("field").asText()))
				columnHeaders.add(node.get("label").asText());
		}
		return columnHeaders;
	}

	private List<List<String>> getSelectedMessages(String businessArea, LinkedHashMap<String, List<String>> params,
			List<String> selectedColumns) {
		PagedCollection<ObjectNode> paged = reportService.getPagedMessageResults(businessArea, params);
		List<List<String>> data = new ArrayList<>();

		ObjectNode[] messages = paged.getItems();
		long i = 0;

		for (ObjectNode message : messages) {
			i++;

			List<String> row = new ArrayList<>();

			row.add(0, i + "");

			for (String column : selectedColumns)
				if (null != message.get(column))
					row.add(message.get(column).asText());
				else
					row.add("");

			data.add(row);
		}

		return data;
	}

	private LinkedHashMap<String, List<String>> prepareFilters(LinkedHashMap<String, List<String>> params) {
		LinkedHashMap<String, List<String>> filters = new LinkedHashMap<>();
		List<String> removeKeys = new ArrayList<>(Arrays. asList("columns", "columnsSel"));
		
		for (String removeKey : removeKeys ) {
			params.remove(removeKey);
		}
		
		removeKeys.clear();
		Iterator<String> iter = params.keySet().iterator();
		
		while (iter.hasNext()) {
			String key = iter.next();

			if (key.startsWith("filter_")) {
				List<String> nodes = params.get(key);
				String node = "";
				if (!nodes.isEmpty())
					node = params.get(key).get(0);

				if (key.endsWith("_idate") && 10 <= node.length()) { // split date interval
					String[] dates;
					dates = node.split(" - ");
					String baseKey = key.replace("_idate", "");
					String dateBegin = dates[0];
					String dateEnd = dates[0];
					if (dates.length > 1) {
						dateEnd = dates[1];
					}
					filters.put(baseKey + "_ldate", Filters.getFiltersParams(getDateTimeFormat(dateBegin, day.START)));
					filters.put(baseKey + "_udate", Filters.getFiltersParams(getDateTimeFormat(dateEnd, day.END)));
					removeKeys.add(key);
				}
				else {
					if (0 == node.length()) {
						removeKeys.add(key);
					}
				}
			}
			
		}

		for (String removeKey : removeKeys ) {
			params.remove(removeKey);
		}
		
		params.putAll(filters);
		
		return params;
	}
	
	private String getDateTimeFormat(String value, day timeOfTheDay) {
		if (value.length() > 10) {
			return value;
		}
		LocalDate localDate = LocalDate.parse(value.substring(0, 10));
	
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss");
		switch(timeOfTheDay) {
		case START:
			return localDate.atStartOfDay().format(formatter);
		case END:
			return LocalDateTime.of(localDate, LocalTime.MAX).format(formatter);
		case NONE:
			return localDate.format(formatter);
		}
		return localDate.format(formatter);
	}
	
	@RequestMapping(value = "/general/page", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody String dataTable(@RequestParam int draw, @RequestParam Map<String, String> allRequestParams)
			throws IOException {

		logger.info("/reports/get requested");

		ObjectMapper mapper = new ObjectMapper();
		LinkedHashMap<String, List<String>> params = mapper.readValue(allRequestParams.get(PARAMS), LinkedHashMap.class);

		String businessArea = params.get(BUSINESSAREA).get(0);
		params = getFormattingParams(params);

//		LinkedHashMap<String, List<String>> parameters = prepareFilters(formatParams);
		String type = "live";
		if (params.containsKey("type")) {
			type = params.get("type").get(0);
		}
		params.put("type", Filters.getFiltersParams(type));
		
		PagedCollection<ObjectNode> messages = reportService.getPagedMessageResults(businessArea, prepareFilters(params));
		
		DataTables dt = new DataTables();
		if (null != messages) {
			dt.setData(messages.getItems());
			dt.setRecordsFiltered(messages.getTotal());
			dt.setRecordsTotal(messages.getTotal());
		}
		else {
			ObjectNode[] objetc = new ObjectNode[0];
			dt.setData(objetc);
			dt.setRecordsFiltered(0);
			dt.setRecordsTotal(0);
		}
		dt.setDraw(draw);
		return JSONHelper.toString(dt);
	}
	
//	private User getUserFromSession() {
//		HttpSession session = request.getSession();
//		String userName = session.getAttribute("userName").toString();
//		return userService.getUser(userName);
//	}
//
//	private UserRole[] getDefinedUserRoles(Integer userId) {
//		LinkedHashMap<String, List<String>> filter = new LinkedHashMap<>();
//
//		filter.put("filter_userId_exact", Arrays.asList(String.valueOf(userId)));
//		filter.put("filter_roleEntity.userDefined_exact", Arrays.asList("1"));
//
//		return userService.getUserRolesByUserDefined(userId, filter);
//	}

	private LinkedHashMap<String, List<String>> getFormattingParams(LinkedHashMap<String, List<String>> params) {
		final String INSERTDATE = "filter_insertdate_idate";
		final String EVENTDATE = "filter_eventdate_idate";
		String tempParamValue = "";
		String businessArea = params.get(BUSINESSAREA).get(0);
		Set<String> keys = params.keySet();
		for (String key : keys) {
			if (key.contains("idate")) {
				params.put(key, Arrays.asList(new String[] {getiDateFromParams(params.get(key))}));
			}
			
		}
		if (businessArea.equalsIgnoreCase("events") && !params.containsKey(EVENTDATE)) {
			params.put(EVENTDATE, params.get(INSERTDATE));
			params.remove(INSERTDATE);
		}
		return params;
	}
	
	private String getiDateFromParams(List<String> listNode) {
		String tempParamValue = listNode.get(0);
		if ((tempParamValue.split(" - ")).length == 1) {
			if (tempParamValue.trim().isEmpty()) {
				return tempParamValue;
			}
			LocalDate localDate = LocalDate.parse(tempParamValue);

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss");
			String start = localDate.atStartOfDay().format(formatter);
			String end = LocalDateTime.of(localDate, LocalTime.MAX).format(formatter);
			return start + " - " + end;
		}
		return tempParamValue;
	}

	private String getiDateFromParams(ObjectNode params, String parameter) {
		String tempParamValue = "";
		JsonNode node = params.get(parameter);
		if (node.isArray()) {
			tempParamValue = node.get(0).asText();
		} else {
			tempParamValue = node.asText();
		}
		if ((tempParamValue.split(" - ")).length == 1) {
			if (tempParamValue.trim().isEmpty()) {
				return tempParamValue;
			}
			LocalDate localDate = LocalDate.parse(tempParamValue);

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss");
			String start = localDate.atStartOfDay().format(formatter);
			String end = LocalDateTime.of(localDate, LocalTime.MAX).format(formatter);
			return start + " - " + end;
		}
		return tempParamValue;
	}

//	@GetMapping(value = "/general")
//	public String printForm(ModelMap model) {
//		logger.info("/reportsForm requested");
//
//		model.addAttribute("businessareas", messageTypeService.getBusinessAreas());
//
//		model.addAttribute("states", reportService.getTransactionStatuses());
//		model.addAttribute("userNames", userService.getUserList());
//
//		model.addAttribute("queues", queueService.getQueueList());
//
//		return "reportsForm";
//	}

	@GetMapping(value = "general/search")
	public String printResults(ModelMap model, OAuth2Authentication auth,
			@RequestParam Map<String, String> allRequestParams) {
		logger.info("/reports requested");
		String businessArea = allRequestParams.get(BUSINESSAREA);
		if (businessArea.equalsIgnoreCase("EVENTS") && !Roles.hasRoles(auth, Roles.EVENTS_VIEW)) {
			throw new NotAuthorizedException();
		}
		if (businessArea.equalsIgnoreCase("Outstanding") && !Roles.hasRoles(auth, Roles.SUPERVISER)) {
			throw new NotAuthorizedException();
		}
		if (businessArea.equalsIgnoreCase("Statements") || businessArea.equalsIgnoreCase("Invoices") || businessArea.equalsIgnoreCase("Payments")) {
			User user = getUserFromSession();
			Integer userId = user.getId();
			UserRole[] userRoles = getDefinedUserRoles(userId);
			List<UserRole> sourceList = Arrays.asList(userRoles).stream().filter(f -> null != f.getRoleEntity().getInternalEntities()).collect(Collectors.toList());
			if (sourceList.isEmpty() && !Roles.hasRoles(auth, Roles.SUPERVISER))
				throw new NotAuthorizedException();
		}
		model.addAttribute("businessareas", messageTypeService.getBusinessAreas());
		model.addAttribute("type", allRequestParams.get("type"));

		model.addAllAttributes(allRequestParams);

		model.addAttribute("states", reportService.getTransactionStatuses());

//		model.addAttribute("queues", queueService.getQueueList());
		model.addAttribute("businessArea", businessArea.toLowerCase());
		model.addAttribute("project", config.getProjectType());

		String userName = auth.getName();
		User user = userService.getUser(userName);

		ReportFilter[] filters = filterService.getFilters(businessArea, user.getId());
		Set<String> optionFilter = new HashSet<>();

		for (ReportFilter filter : filters) {
			optionFilter.add(filter.getTemplateName());
		}

		model.addAttribute("filterNames", new LinkedList<String>(optionFilter));

		return "reports";
	}
	
	private UserRole[] getDefinedUserRoles(Integer userId) {
		LinkedHashMap<String, List<String>> filter = new LinkedHashMap<>();

		filter.put("filter_userId_exact", Arrays.asList(String.valueOf(userId)));
		filter.put("filter_roleEntity.userDefined_exact", Arrays.asList("1"));

		return userService.getUserRolesByUserDefined(userId, filter);
	}
	
	private User getUserFromSession() {
		HttpSession session = request.getSession();
		String userName = session.getAttribute("userName").toString();
		return userService.getUser(userName);
	}

	@GetMapping(value = "/view-message")
	public String printMessage(ModelMap model, @RequestParam(value = "id", required = true) String id, // correlationid
			@RequestParam(value = "businessArea", required = true) String businessArea,
			@RequestParam(value = "type", required = true) String type) {

		ObjectNode message = messageService.getMessage(businessArea, id, type);
		model.addAttribute("message", message);
		model.addAttribute("correlationid", message.get("correlationid").asText());
		String messagePayload = messageService.getFeedbackPayload(id, type);
		MessageType[] messagesType = messageTypeService.getMessageTypes(businessArea);
		for (MessageType messageType : messagesType) {
			if (messageType.getFriendlyName().equalsIgnoreCase(message.get("messagetype").asText())) {
				model.addAttribute("messageType", messageType.getMessageType());
				break;
			}
		}

		try {
			model.addAttribute("entryQueueMessage", messageService.getEntryQueueMessage(message.get("id").asText()));

			Document payload = PayloadHelper.parsePayload(messagePayload);

			// friendly tags
			PayloadHelper.friendlyPayload(payload);

			model.addAttribute("payload", payload);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("payloadString", StringEscapeUtils.escapeHtml(messagePayload));
		}
		model.addAttribute("id", message.get("id").asText());
		return "viewMessage";
	}

	@GetMapping(value = "/view-message-outstanding")
	public String getOutstandingMessage(ModelMap model, @RequestParam(value = "id", required = true) String id, // correlationid
			@RequestParam(value = "businessArea", required = true) String businessArea) {

		ObjectNode message = messageService.getMessage(businessArea, id, "live");
		model.addAttribute("message", message);
		model.addAttribute("correlationid", message.get("correlationid").asText());
		model.addAttribute("outstanding", true);

		try {
			model.addAttribute("entryQueueMessage", messageService.getEntryQueueMessage(message.get("id").asText()));

			Document payload = PayloadHelper.parsePayload(message.get("payload").asText());

			// friendly tags
			PayloadHelper.friendlyPayload(payload);

			model.addAttribute("payload", payload);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "viewMessage";
	}

	@GetMapping(value = "/message-status")
	@ResponseBody
	public String status(ModelMap model, @RequestParam(value = "id", required = true) String id,
			@RequestParam(value = "type", required = true) String type,
			@RequestParam(value = "businessArea", required = true) String businessArea) throws DOMException, Exception {
		ObjectNode message = messageService.getMessage(id, type);

		String messagePayload = messageService.getFeedbackPayload(id, type);
		Document xmlDocument = PayloadHelper.parsePayload(messagePayload);

		Node payment = PayloadHelper.getElementByXpath(xmlDocument, "//PaymentId");
		String paymentId;

		if (null == payment) {
			return "Cannot get status code!";
		} else {
			paymentId = payment.getTextContent();
		}
		if (messageService.getMessageStatus(message.findValue("orderingbank").textValue(), paymentId)
				.findValue("statusCode").textValue().equals("OK")) {
			return "Sent to beneficiary bank";
		}
		return "Status code : "
				+ messageService.getMessageStatus(message.findValue("orderingbank").textValue(), paymentId)
						.findValue("statusCode").textValue();
	}

	@GetMapping(value = "/message-types")
	@ResponseBody
	public String getMessageTypes(ModelMap model,
			@RequestParam(value = "businessArea", required = true) String businessArea) throws JsonProcessingException {
		return JSONHelper.toString(messageTypeService.getMessageTypes(businessArea));
	}

	@GetMapping(value = "/message-criteria")
	@ResponseBody
	public String getFilters(ModelMap model, @RequestParam(value = "businessArea", required = true) String businessArea)
			throws JsonProcessingException {
		return JSONHelper.toString(reportService.getMessageCriteria(businessArea));
	}

	@GetMapping(value = "/message-results")
	@ResponseBody
	public String getColumns(ModelMap model, @RequestParam(value = "businessArea", required = true) String businessArea)
			throws JsonProcessingException {
		return JSONHelper.toString(reportService.getMessageResults(businessArea));
	}

	@GetMapping(value = "/message-filter")
	@ResponseBody
	public ReportFilter[] getFilter(ModelMap model, OAuth2Authentication auth,
			@RequestParam(value = "report") String businessArea,
			@RequestParam(value = "templateName") String template) {
		String userName = auth.getName();
		User user = userService.getUser(userName);

		return filterService.getFilters(businessArea, user.getId(), template);
	}

	@DeleteMapping(value = "/message-filter")
	@ResponseBody
	public int deleteFilter(@RequestParam("report") String businessArea,
			@RequestParam("templateName") String templateName) throws ParseException, UnknownHostException {
		HttpSession session = request.getSession();
		String userName = session.getAttribute("userName").toString();
		User user = userService.getUser(userName);

		ResponseEntity<String> responseEntity = filterService.deleteFilter(businessArea, user.getId(), templateName);
		String message = "Delete transaction report template.";
		if (204 == responseEntity.getStatusCodeValue()) {
			Status status = getStatus(message, user.getId(),
					filterService.getFilters(businessArea, user.getId(), templateName));
			eventService.insertStatusData(status);
		}
		return responseEntity.getStatusCodeValue();
	}

	@PostMapping(value = "/message-filter")
	@ResponseBody
	public int saveFilter(@RequestParam("filter") String filters, @RequestParam("report") String businessArea,
			@RequestParam("templateName") String templateName) throws IOException, ParseException {

		HttpSession session = request.getSession();
		String userName = session.getAttribute("userName").toString();
		User user = userService.getUser(userName);

		ObjectMapper objectMapper = new ObjectMapper();
		ReportFilter[] allFilter = objectMapper.readValue(filters, ReportFilter[].class);

		for (ReportFilter filter : allFilter) {
			filter.setUserid(user.getId());
		}

		// delete old templateName
		ResponseEntity<String> delFilter = filterService.deleteFilter(businessArea, user.getId(), templateName);
		String message = "Modify transaction report template.";
		if (200 == delFilter.getStatusCodeValue()) {
			message = "Add new transaction report template.";
		}

		// create new templateName
		ResponseEntity<String> stringResponseEntity = filterService.insertFilter(allFilter);
		if (201 == stringResponseEntity.getStatusCodeValue()) {
			Status status = getStatus(message, user.getId(), allFilter);
			eventService.insertStatusData(status);
		}
		return stringResponseEntity.getStatusCodeValue();
	}

	public Status getStatus(String message, Integer userId, ReportFilter[] objs)
			throws ParseException {
		Status status = new Status();
		status.setService(new BigDecimal(-1));
		status.setCorrelationid("00000000-00000000-00000000");
		status.setType("Info");
		Timestamp time = new Timestamp(
				new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse(LocalDateTime.now().toString()).getTime());
		status.setEventdate(time);
		status.setInsertdate(time);
		status.setMessage(message);
		status.setClasS("Config.Manage");
		try {
			String host = InetAddress.getLocalHost().getHostName();
			status.setMachine(host);
		}
		catch(UnknownHostException uHE) {
			status.setMachine("localhost");
		}
		
		String addit = "";
		for (ReportFilter obj : objs) {
			addit += " " + obj.toString();
		}
		status.setAdditionalinfo(addit);
		status.setUserid(userId);
		return status;
	}

	@GetMapping(value = "/view-event")
	public String displayEvent(ModelMap model, @RequestParam(value = "id", required = true) String id, // correlationid
			@RequestParam(value = "businessArea", required = true) String businessArea,
			@RequestParam(value = "type", required = true) String type) {

		Event event = eventService.getEvent(id, type);

		model.addAttribute("event", event);
		return "viewEvent";
	}

	@GetMapping(value = "/page-related")
	public @ResponseBody String displayRelated(@RequestParam int draw,
			@RequestParam(value = "correlationid") String correlationid) throws JsonProcessingException {

		LinkedHashMap<String, List<String>> params = new LinkedHashMap<>();
		params.put("filter_correlationid_exact", Filters.getFiltersParams(correlationid));
		params.put("sort_eventdate", Filters.getFiltersParams("desk"));
		PagedCollection<EventsProcedureCallCursor> related = eventsProcedureCallService.getCallProcedureResult(params);

		DataTables dt = new DataTables();
		if (null != related) {
			dt.setData(related.getItems());
			dt.setRecordsFiltered(related.getTotal());
			dt.setRecordsTotal(related.getTotal());
		}
		dt.setDraw(draw);
		return JSONHelper.toString(dt);
	}
}
