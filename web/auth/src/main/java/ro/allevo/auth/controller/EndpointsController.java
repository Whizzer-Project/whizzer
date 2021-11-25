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
package ro.allevo.auth.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("endpoints")
public class EndpointsController {

	@Autowired
	DataSource dataSource;

	private boolean match = false;
	private String type = "";

	@Resource(name = "tokenStore")
	TokenStore tokenStore;

	private static final String SCHEMA = "finauth";

	private boolean checkToken(String token) {
		OAuth2Authentication auth = tokenStore.readAuthentication(token);
		// System.out.println("----------------"+auth.getOAuth2Request().getClientId());
		return (auth != null && auth.isAuthenticated());
	}

	@RequestMapping(method = RequestMethod.GET, value = "checkEndpoint", produces = "application/json")
	@ResponseBody
	public String check(@RequestParam String endpoint, @RequestParam String type,
			@RequestHeader("Authorization") String token) throws JsonProcessingException, IOException {
		this.type = type;
		if (!checkToken(token.replace("Bearer ", "")))
			return null;
		String genreJson = getDBRightsTree();
		genreJson = URLDecoder.decode(genreJson, "UTF-8");
		genreJson = genreJson.substring(1, genreJson.length() - 2);
		JsonNode node = new ObjectMapper().readTree(genreJson);
		match = false;
		checkEndpoint(node, endpoint.split("/"), 0, false);
		return String.valueOf(match);
	}

	@RequestMapping("")
	public ModelAndView endpoints(ModelMap model) {
		model.addAttribute("tree", getDBRightsTree());
		return new ModelAndView("rights", model);
	}

	@RequestMapping(method = RequestMethod.POST, value = "save")
	public String save(@RequestBody String tree) throws UnsupportedEncodingException {
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = dataSource.getConnection().prepareStatement("update " + SCHEMA
					+ ".oauth_client_details set" + " endpoint_rights = ? where client_id='SampleClientId' ");
			preparedStatement.setString(1, tree);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != preparedStatement) {
					preparedStatement.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return "rights";
	}

	private String getDBRightsTree() {
		try (Statement stmt = dataSource.getConnection().createStatement();
				ResultSet rs = stmt.executeQuery("SELECT endpoint_rights FROM " + SCHEMA + ".oauth_client_details")) {

			if (rs.next()) {
				return rs.getString("endpoint_rights");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}catch (Exception e1) {
			e1.printStackTrace();
		}
		return null;
	}

	private void checkEndpoint(JsonNode node, String[] endpoints, int index, boolean isMatch) throws IOException {
		Iterator<Map.Entry<String, JsonNode>> fieldsIterator = node.fields();
		JsonNode childrenVal = null;
		String text = "";
		JSONObject methods = new JSONObject();
		while (fieldsIterator.hasNext()) {
			Map.Entry<String, JsonNode> field = fieldsIterator.next();
			String key = field.getKey();
			JsonNode value = field.getValue();
			switch (key) {
			case "data":
				try {
					methods = new JSONObject(value.toString());
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			case "text":
				text = value.textValue();
				break;
			case "children":
				childrenVal = value;
				break;
			}
		}
		if (methods.optString("TOKEN").equals("1") || (methods.optString(type.toUpperCase()).equals("1")
				&& endpoints[index].equals(text.replaceAll("\"", "")))) {
			isMatch = true;
			if ((index == (endpoints.length - 1)) && (methods.optString(type).equals("1"))) {
				match = true;
			}
		}

		if (childrenVal != null) {
			if (isMatch && (endpoints.length > (index + 1))) {
				index++;
				isMatch = false;
				Iterator<JsonNode> elements = childrenVal.iterator();
				while (elements.hasNext()) {
					checkEndpoint(elements.next(), endpoints, index, isMatch);
				}
			}
		}
	}
}
