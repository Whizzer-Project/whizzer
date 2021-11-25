package ro.allevo.connect.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.QueryParam;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ro.allevo.connect.model.Account;
import ro.allevo.connect.model.Connect;
import ro.allevo.connect.service.AccountBalancesResponseService;
import ro.allevo.connect.service.ConnectService;
import ro.allevo.connect.service.SynchronizeService;
import ro.allevo.fintpui.exception.NotAuthorizedException;
import ro.allevo.fintpui.model.InternalAccount;
import ro.allevo.fintpui.model.InternalEntity;
import ro.allevo.fintpui.model.TemplateConfigDetailed;
import ro.allevo.fintpui.model.UserRolesEntityMaps;
import ro.allevo.fintpui.service.BankService;
import ro.allevo.fintpui.service.InternalAccountService;
import ro.allevo.fintpui.service.InternalEntitiesService;
import ro.allevo.fintpui.service.TemplateConfigOptionsService;
import ro.allevo.fintpui.service.UserService;
import ro.allevo.fintpui.utils.Filters;
import ro.allevo.fintpui.utils.JSONHelper;
import ro.allevo.fintpui.utils.PagedCollection;
import ro.allevo.fintpui.utils.Roles;
import ro.allevo.fintpui.utils.Utils;

@Controller
@RequestMapping("synchronized")
public class SynchronizeController {

	@Autowired
	BankService bankService;

	@Autowired
	InternalAccountService internalAccountService;

	@Autowired
	SynchronizeService synchronizeService;

	@Autowired
	AccountBalancesResponseService accountResponseService;
	
	@Autowired
	private TemplateConfigOptionsService templateConfigOptions;

	@Autowired
	UserService userService;

	@Autowired
	InternalEntitiesService internalEntitiesService;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private ConnectService connectService;

	@Value("${project.type}")
	private String projectType;

	private final String RESOURCE = "resource";
	private final String CHECKED = "checked";
	private final String BANK = "bank";
	private final String INTERNALACCOUNT = "internalaccount";

	@GetMapping(value = "synchronize")
	public String getSynchronize(OAuth2Authentication auth, ModelMap model) {
		
		if (!Roles.hasRoles(auth,Roles.API_INTERFACE_MODIFY))
			throw new NotAuthorizedException();
		
		JSONObject entities= getEntities(new HashSet<>(Arrays.asList(BANK)));
		JSONArray bankEntities =  Utils.getInternalEntities(entities, BANK, "bic", "name");
		

		if (projectType.equalsIgnoreCase("whizzer")) {
			String userName = request.getSession().getAttribute("userName").toString();
			UserRolesEntityMaps[] userRolesEntityMaps = getEntityByUserName(userName);
			List<String> entitiesId = new ArrayList<>();
			for (UserRolesEntityMaps entityMaps : userRolesEntityMaps) {
				entitiesId.add(String.valueOf(entityMaps.getEntityId()));
			}

			LinkedHashMap<String, List<String>> params = new LinkedHashMap<>();
			params.put("filter_internalEntityId_exact", new ArrayList<>(entitiesId));

			PagedCollection<Connect> connectsPaged = connectService.getPage(params);
			List<String> connectBanks = new ArrayList<>();
			for (Connect connect : connectsPaged.getItems()) {
				connectBanks.add(connect.getBic());
			}
			JSONArray banks = new JSONArray();
			for (int indx = 0; indx < bankEntities.length(); indx++) {
				JSONObject object = bankEntities.getJSONObject(indx);
				if (connectBanks.contains(object.getString("bic"))) {
					banks.put(object);
				}
			}
			bankEntities = banks;
		}
 		model.addAttribute("banks",  Utils.getMapInternalEntities(bankEntities, "bic", "name"));
		model.addAttribute("title", "Synchronize banks accounts");
		return "connect/synchronize";
	}
	
	private JSONObject getEntities(Set<String> entities) {
		return templateConfigOptions.getAllConfingOptionsValues(entities);
	}

	@GetMapping(value = "page/{bic}")
	@ResponseBody
	public String getInternalEntity(ModelMap model, @PathVariable String bic) throws IOException {
		JSONObject internalAccount = getEntities(new HashSet<>(Arrays.asList(INTERNALACCOUNT)));
		JSONArray internalEntity = Utils.getInternalEntities(internalAccount, INTERNALACCOUNT);
		
		JSONArray listInternalEntity = Utils.filteredInternalEntities(internalEntity, "bic", bic);
		
		
		Map<String, Map<String, String>> listInternal = null;
		if (null != internalAccount) {
			listInternal = formatInternalAccounts(listInternalEntity, false);
		}

		Account[] externalAccount = synchronizeService.getAllAccountByAccounts(bic);
		if (null != externalAccount && null != listInternal) {
			Map<String, Map<String, String>> listExtern = formatExternalAccounts(externalAccount, listInternal);
			model.addAttribute("external", listExtern);
		}

		model.addAttribute("internal", listInternal);
		return JSONHelper.toString(model);
	}

	private UserRolesEntityMaps[] getEntityByUserName(String userName) {
		LinkedHashMap<String, List<String>> params = new LinkedHashMap<>();
		List<String> paramsValue = new ArrayList<>();
		paramsValue.add(String.valueOf(13));
		params.put("filter_roleId_exact", paramsValue);

		return userService.getUserRolesEntityMaps(userName, params);
	}

	private Map<String, Map<String, String>> formatInternalAccounts(JSONArray internalAccounts,
			boolean fromMock) {
		Map<String, Map<String, String>> listInternal = new HashMap<>();
		for (int ind = 0; ind < internalAccounts.length(); ind++) {
			JSONObject account = internalAccounts.getJSONObject(ind);
			Map<String, String> internal = new HashMap<>();
			internal.put("iban", account.getString("accountNumber"));
			internal.put(RESOURCE, account.getString("resourceid"));
			internal.put(CHECKED, account.getString("resourceid").trim().length() == 0 ? "false" : "true");

			StringBuilder amount = new StringBuilder();
			String amountFromMockServer = accountResponseService.getBalanceAmount(account.getString("bic"),
					"" + account.getString("id"));
			if (fromMock && null != amountFromMockServer) {
				amount.append(amountFromMockServer);
			} else {
				amount.append(account.getString("balance"));
			}

			internal.put("balance", amount.toString());
			listInternal.put(account.getString("accountNumber"), internal);
		}
		return listInternal;
	}

	private Map<String, Map<String, String>> formatExternalAccounts(Account[] pageExternal,
			Map<String, Map<String, String>> listInternal) {
		Map<String, Map<String, String>> listExtern = new HashMap<>();
		for (Account account : pageExternal) {
			Map<String, String> external = new HashMap<>();
			external.put("iban", account.getIban());
			external.put(RESOURCE, account.getResourceId());
			if (listInternal.containsKey(account.getIban())) {
				if (account.getResourceId().equals(listInternal.get(account.getIban()).get(RESOURCE))) {
					external.put(CHECKED, listInternal.get(account.getIban()).get(CHECKED));
				} else {
					external.put(CHECKED, "false");
					listInternal.get(account.getIban()).put(CHECKED, "false");
					listInternal.get(account.getIban()).put(RESOURCE, account.getResourceId());
				}
			}
			listExtern.put(account.getIban(), external);
		}
		return listExtern;
	}

	@GetMapping(value = "sync")
	@ResponseBody
	public String saveSyncAccounts(ModelMap model, @QueryParam(value = "bic") String bic,
			@QueryParam(value = "intern") String[] intern, @QueryParam(value = RESOURCE) String[] resource)
			throws JsonProcessingException {

		JSONObject internalAccount = getEntities(new HashSet<>(Arrays.asList(INTERNALACCOUNT)));
		JSONArray internalEntity = Utils.getInternalEntities(internalAccount, INTERNALACCOUNT);
		
		JSONArray listInternalEntity = Utils.filteredInternalEntities(internalEntity, "bic", bic);
		if (null != listInternalEntity) {
			Map<String, Map<String, String>> listInternal = formatInternalAccounts(listInternalEntity, true);
//			model.addAttribute("internal", listInternal);

//			List<InternalAccount> listInternalAccount = Arrays.asList(internalAccount.getItems());

			for (int ind = 0; ind < listInternalEntity.length(); ind++) {
				Boolean execute = false;
				JSONObject acount = listInternalEntity.getJSONObject(ind);
				String accountNumber = acount.getString("accountNumber").trim(); 
				if (0 != accountNumber.length()) {
					for (int i = 0; i < intern.length; i++) {
						if (accountNumber.equals(intern[i])) {
							execute = true;
							acount.put("resourceid", resource[i]);

							BigDecimal amount = null;
							try {
								amount = new BigDecimal(listInternal.get(accountNumber).get("balance"));
							} catch (NumberFormatException e) {
								e.printStackTrace();
							}
							if (null != amount) {
								acount.put("balance", String.valueOf(amount));
							}
//TODO - ask how to do update for internalAccount with rule API Interface
							//internalAccountService.updateInternalAccount(acount, acount.getString("id"));
							break;
						}
					}
					if (Boolean.FALSE.equals(execute)) {
						acount.put("resourceid", "");
						acount.put("balance", "");
					//	internalAccountService.updateInternalAccount(acount, acount.getString("id"));
					}
				}
			}

			Account[] externalAccount = synchronizeService.getAllAccountByAccounts(bic);
			listInternal = formatInternalAccounts(listInternalEntity, false);
			Map<String, Map<String, String>> listExtern = formatExternalAccounts(externalAccount, listInternal);
			model.addAttribute("external", listExtern);
			model.addAttribute("internal", listInternal);
		}
		return JSONHelper.toString(model);
	}
}
