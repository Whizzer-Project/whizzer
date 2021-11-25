package ro.allevo.fintpui.controllers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;

import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.service.MessageService;
import ro.allevo.fintpui.utils.Filters;
import ro.allevo.fintpui.utils.JSONHelper;
import ro.allevo.fintpui.utils.PagedCollection;

@Controller
@RequestMapping(value = "/home")
public class HomeController {

	@Autowired
	MessageService messageService;
	
	@Autowired
	Config config;

	@GetMapping
	public String viewHome(OAuth2Authentication auth, ModelMap model){
		model.addAttribute("title", "home");
		model.addAttribute("apiUri", config.getAPIUrl());
		return "home";
	}
	
	@GetMapping(value = {"/transactions", "/transactions/statements"})
	public @ResponseBody String getMessagesType(ModelMap model, @RequestParam(name = "msgType") String param) throws JsonProcessingException {
		
		SimpleDateFormat sdfStart = new SimpleDateFormat("yyyy-MM-dd'T'00:00:00");
		SimpleDateFormat sdfEnd = new SimpleDateFormat("yyyy-MM-dd'T'23:59:59");
		Calendar cal = Calendar.getInstance();
		
		LinkedHashMap<String, List<String>> filters = new LinkedHashMap<>();
		filters.put("filter_insertdate_ldate", Filters.getFiltersParams(sdfStart.format(cal.getTime())));
		filters.put("filter_insertdate_udate", Filters.getFiltersParams(sdfEnd.format(cal.getTime())));
		filters.put("filter_messageType_exact", Filters.getFiltersParams(param));
		filters.put("total", Filters.getFiltersParams("1"));
		PagedCollection<ObjectNode> getMessageType = messageService.getMessagesType(filters);
		if (null != getMessageType) {
			return JSONHelper.toString(getMessageType.getTotal());
		}
		return null;
	}
}
