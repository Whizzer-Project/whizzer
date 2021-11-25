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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import ro.allevo.fintpui.exception.NotAuthorizedException;
import ro.allevo.fintpui.model.MessageType;
import ro.allevo.fintpui.model.Role;
import ro.allevo.fintpui.model.User;
import ro.allevo.fintpui.model.UserRole;
import ro.allevo.fintpui.model.UserRolesEntityMaps;
import ro.allevo.fintpui.service.InternalEntitiesService;
import ro.allevo.fintpui.service.MessageTypeService;
import ro.allevo.fintpui.service.RoleService;
import ro.allevo.fintpui.service.TemplateConfigOptionsService;
import ro.allevo.fintpui.service.UserService;
import ro.allevo.fintpui.utils.JSONHelper;
import ro.allevo.fintpui.utils.Roles;
import ro.allevo.fintpui.utils.Utils;
import ro.allevo.fintpui.utils.editors.UserEditor;

@Controller
@RequestMapping("/users")
public class UsersController {

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private MessageTypeService messageTypeService;

//	@Autowired
//	private InternalEntitiesService internalEntitiesService;
	
	@Autowired
	private TemplateConfigOptionsService templateConfigOptions;

	@Autowired
	HttpServletRequest request;

	@Value("${project.type:}")
	private String projectType;

	private static Logger logger = LogManager.getLogger(UsersController.class.getName());
	
	private static final String INTERNALENTITY = "internalentity";
	private static final String INTERNALENTITIESNAME = "name";

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		// custom user from json converter
		binder.registerCustomEditor(User.class, new UserEditor());
	}

	/*
	 * DISPLAY
	 */
	@GetMapping
	public String printUsers(ModelMap model, OAuth2Authentication auth) {
		logger.info("/users requested");

		if (!Roles.hasRoles(auth, Roles.USERS_MODIFY))
			throw new NotAuthorizedException();

		User[] users = userService.getAllUsers();
		Role[] userDefinedRoles = roleService.getUserDefinedRoles();
		Role[] applicationRoles = roleService.getApplicationRoles();

		model.addAttribute("users", users);
		model.addAttribute("userDefinedRoles", userDefinedRoles);
		model.addAttribute("applicationRoles", applicationRoles);
		model.addAttribute("project", true);
		if (projectType.equalsIgnoreCase("whizzer")) {
//			List<HashMap<String, Object>> internalEntities = internalEntitiesService.getAllInternalEntities();
			JSONObject entities= getEntities(new HashSet<>(Arrays.asList(INTERNALENTITY)));
			JSONArray internalEntity = Utils.getInternalEntities(entities, INTERNALENTITY, "id", INTERNALENTITIESNAME);
			UserRolesEntityMaps[] userRolesEntity = userService.getUserRolesEntityMaps(users[0].getUsername());
			model.addAttribute("internalEntities", internalEntity);
			model.addAttribute("project", false);
			model.addAttribute("entities", userRolesEntity);
		}

		return "users";
	}
	
	private JSONObject getEntities(Set<String> entities) {
		return templateConfigOptions.getAllConfingOptionsValues(entities);
	}

	@RequestMapping(value = "/sync/provider", method = RequestMethod.GET)
	@ResponseBody
	public String getProviderUsers() throws JsonProcessingException {
		return JSONHelper.toString(getNotMyUsers());
	}

	private List<ObjectNode> getNotMyUsers() {
		ObjectNode[] providerUsers = userService.getUsersFromProvider();
		User[] users = userService.getAllUsers();

		List<String> myUsers = new ArrayList<>();
		List<ObjectNode> notMyUsers = new ArrayList<ObjectNode>();

		for (User user : users)
			myUsers.add(user.getUsername());

		for (ObjectNode user : providerUsers)
			if (!myUsers.contains(user.get("username").asText()))
				notMyUsers.add(user);

		Collections.sort(notMyUsers, (a, b) -> a.get("username").asText().compareTo(b.get("username").asText()));

		return notMyUsers;
	}

	@GetMapping("/sync/deleted")
	@ResponseBody
	public String getDeleteUsers() throws JsonProcessingException {
		return JSONHelper.toString(getNotProviderUsers());
	}

	private List<User> getNotProviderUsers() {
		ObjectNode[] providerUsers = userService.getUsersFromProvider();
		User[] users = userService.getAllUsers();

		List<String> proUsers = new ArrayList<>();
		List<User> notProviderUsers = new ArrayList<>();

		for (ObjectNode user : providerUsers)
			proUsers.add(user.get("username").asText());

		for (User user : users)
			if (!proUsers.contains(user.getUsername()))
				notProviderUsers.add(user);

		Collections.sort(notProviderUsers, (a, b) -> a.getUsername().compareTo(b.getUsername()));

		return notProviderUsers;
	}

	@PostMapping(value = "/sync")
	@ResponseBody
	public String syncUsers() {
		List<ObjectNode> notMyUsers = getNotMyUsers();
		List<User> notProviderUsers = getNotProviderUsers();

		User[] importingUsers = new User[notMyUsers.size()];
		int[] removingUsers = new int[notProviderUsers.size()];

		int i = 0;
		for (ObjectNode u : notMyUsers) {
			User importingUser = new User();
			importingUser.setUsername(u.get("username").asText());
			importingUser.setEmail(u.get("email").asText());
			importingUsers[i++] =importingUser;
		}
		i = 0;
		for (User u : notProviderUsers)
			removingUsers[i++] = u.getId();

		if (importingUsers.length > 0)
			userService.insertUsers(importingUsers);

		if (removingUsers.length > 0)
			userService.deleteUsers(removingUsers);

		return "[]";
	}

	/*
	 * INSERT
	 */
	@GetMapping(value = "/add")
	public String addUser(ModelMap model, @ModelAttribute("user") User user) {
		logger.info("/addUser page requested");
		model.addAttribute("formAction", "users/insert");
		return "users_add";
	}

	@PostMapping(value = "/insert")
	@ResponseBody
	public String insertUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult)
			throws JsonProcessingException {
		logger.info("/insert user requested");

		if (bindingResult.hasErrors())
			return JSONHelper.toString(bindingResult.getAllErrors());

		userService.insertUser(user);
		// if the user chose to initialize schema with copies from other schema, do so
		return "[]";
	}

	/*
	 * EDIT
	 */
	@RequestMapping(value = "/{username}/edit")
	public String editUser(ModelMap model, @PathVariable String username) {
		logger.info("/editUser requested");
		User user = userService.getUser(username);
		model.addAttribute("user", user);
		model.addAttribute("formAction", "users/update");
		return "users_add";
	}

	// !!!EXACT PARAM ORDER !!!
	@PostMapping(value = "/update")
	@ResponseBody
	public String updateUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult)
			throws JsonProcessingException {
		logger.info("/update user requested");

		if (bindingResult.hasErrors())
			return JSONHelper.toString(bindingResult.getAllErrors());

		userService.updateUser(user.getId(), user);
		return "[]";
	}

	/*
	 * DELETE
	 */
	@RequestMapping(value = "/{userid}/delete")
	public String deleteUser(@PathVariable int userid) {
		logger.info("/delete user requested");
		userService.deleteUser(userid);
		return "redirect:/users";
	}

	@RequestMapping(value = "/roles/add")
	public String roles(ModelMap model, @ModelAttribute("role") Role role) {
		MessageType[] messageTypes = messageTypeService.getMessageTypes();
//		List<HashMap<String, Object>> internalEntities = internalEntitiesService.getAllInternalEntities();
		JSONObject entities= getEntities(new HashSet<>(Arrays.asList(INTERNALENTITY)));
		JSONArray internalEntity = Utils.getInternalEntities(entities, INTERNALENTITY, "id", INTERNALENTITIESNAME);
		messageTypes = Arrays.stream(messageTypes).filter(msgType -> !msgType.getMessageType().equalsIgnoreCase("Undefined")).toArray(MessageType[]::new);
		
		model.addAttribute("userDefined", 1);
		model.addAttribute("messageTypes", messageTypes);
		model.addAttribute("internalEntities", internalEntity);

		model.addAttribute("formAction", "users/roles/insert");

		return "roles_add";
	}

	@PostMapping(value = "/roles/insert")
	@ResponseBody
	public String insertRole(@Valid @ModelAttribute("role") Role role, BindingResult bindingResult)
			throws JsonProcessingException {
		logger.info("/insert role requested");

		if (bindingResult.hasErrors())
			return JSONHelper.toString(bindingResult.getAllErrors());

		roleService.insertRole(role);

		return "[]";
	}

	@GetMapping(value = "/roles/{roleid}/delete")
	public String deleteRole(@PathVariable int roleid) {
		roleService.deleteRole(roleid);

		return "redirect:/users";
	}

	@RequestMapping(value = "/roles/{roleid}/edit")
	public String editRole(ModelMap model, @PathVariable int roleid) {
		logger.info("/editRole requested");
		Role role = roleService.getRole(roleid);

		MessageType[] messageTypes = messageTypeService.getMessageTypes();
//		List<HashMap<String, Object>> internalEntities = internalEntitiesService.getAllInternalEntities();
		JSONObject entities= getEntities(new HashSet<>(Arrays.asList(INTERNALENTITY)));
		JSONArray internalEntity = Utils.getInternalEntities(entities, INTERNALENTITY, "id", INTERNALENTITIESNAME);
		messageTypes = Arrays.stream(messageTypes).filter(msgType -> !msgType.getMessageType().equalsIgnoreCase("Undefined")).toArray(MessageType[]::new);

		model.addAttribute("role", role);
		model.addAttribute("messageTypes", messageTypes);
		model.addAttribute("internalEntities", internalEntity);

		model.addAttribute("formAction", "users/roles/update");
		return "roles_add";
	}

	@PostMapping(value = "/roles/update")
	@ResponseBody
	public String updateRole(@Valid @ModelAttribute("role") Role role, BindingResult bindingResult,
			@RequestParam("id") int roleid) throws JsonProcessingException {
		if (bindingResult.hasErrors())
			return JSONHelper.toString(bindingResult.getAllErrors());

		roleService.updateRole(role, roleid);
		return "[]";
	}

	@RequestMapping(value = "/{username}/roles")
	@ResponseBody
	public String getUserRoles(@PathVariable String username) throws JsonProcessingException {
		UserRole[] userRoles = userService.getUserRoles(username);

		UserRolesEntityMaps[] userRolesEntity = userService.getUserRolesEntityMaps(username);
		Map<String, Object> userInfo = new HashMap<>();
		userInfo.put("entity", JSONHelper.toString(userRolesEntity));
		userInfo.put("roles", JSONHelper.toString(userRoles));
		return JSONHelper.toString(userInfo);
	}

	@PostMapping(value = "/{username}/update-roles")
	@ResponseStatus(value = HttpStatus.OK)
	public void updateUserRoles(@PathVariable String username, @RequestParam("roles") String roles,
			@RequestParam("entity") String entities) throws IOException {

		ObjectMapper objectMapper = new ObjectMapper();
		UserRole[] userRoles = objectMapper.readValue(roles, UserRole[].class);

		JSONArray entity = new JSONArray(entities);
//		List<HashMap<String, Object>> internalEntitiesListMap = internalEntitiesService.getAllInternalEntities();
		JSONObject entityInternal= getEntities(new HashSet<>(Arrays.asList(INTERNALENTITY)));
		JSONArray internalEntity = Utils.getInternalEntities(entityInternal, INTERNALENTITY, "id", INTERNALENTITIESNAME);
		List<UserRolesEntityMaps> newUserRolesEntityMaps = new ArrayList<>();
		User user = getUser(username);

//		UserRolesEntityMaps[] userRolesEntity = userService.getUserRolesEntityMaps(user.getUsername());

		for (int item = 0; item < entity.length(); item++) {
			JSONObject objEntity = entity.getJSONObject(item);
			String[] tenants = objEntity.getString("entity").split(",");
			Integer roleId = objEntity.getInt("roleid");
			for (String tenant : tenants) {
				Integer entityId = getEntityId(internalEntity, tenant);
				UserRolesEntityMaps userRolesEntityMaps = new UserRolesEntityMaps();
				userRolesEntityMaps.setEntityId(entityId);
				userRolesEntityMaps.setRoleId(roleId);
				userRolesEntityMaps.setUserId(user.getId());
				newUserRolesEntityMaps.add(userRolesEntityMaps);
			}
		}
		if (!newUserRolesEntityMaps.isEmpty()) {
			userService.deleteUserRolesEntityMaps(username);
			userService.insertUserRolesEntityMaps(username, newUserRolesEntityMaps);
		}

		userService.updateUserRoles(username, userRoles);
	}

	public Integer getEntityId(JSONArray internalEntity, String entityName) {
		for (int ind = 0; ind < internalEntity.length(); ind++) {
			JSONObject entity = internalEntity.getJSONObject(ind);
			if (entity.getString("name").trim().equals(entityName.trim())) {
				return (entity.getInt("id"));
			}
		}
		return -1;
	}

	private User getUser(String userName) {
		HttpSession session = request.getSession();
		User user = userService.getUser(userName);
		return user;
	}

}