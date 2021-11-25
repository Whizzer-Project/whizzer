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

import java.security.Principal;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ro.allevo.auth.exception.NotAuthorizedException;
import ro.allevo.auth.model.User;
import ro.allevo.auth.util.UserAttributesMapper;

@RestController
public class UserController {
	@Autowired
	private LdapTemplate ldapTemplate;
	@Autowired
	DataSource dataSource;
	@Resource(name = "tokenStore")
	TokenStore tokenStore;

	private boolean checkToken(String token) {
		OAuth2Authentication auth = tokenStore.readAuthentication(token);
		return (auth != null && auth.isAuthenticated());
	}

	@RequestMapping("/user/me")
	public Principal user(Principal principal) {
		return principal;
	}

	@RequestMapping(method = RequestMethod.GET, value = "users", produces = "application/json")
	public List<User> getAllPersons(Principal principal, @RequestHeader("Authorization") String token) {
		if (!checkToken(token.replace("Bearer ", "").replace("bearer ", "")))
			throw new NotAuthorizedException();
		List<User> users = new LinkedList<User>();
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", "person"));
		LinkedList<Map<String, Object>> list = (LinkedList<Map<String, Object>>) ldapTemplate.search("",
				filter.encode(), new UserAttributesMapper());
		User user = new User();
		for (Map<String, Object> m : list) {
			user = new User();
			Object sAMAccountName = m.get("sAMAccountName");
			if (null != sAMAccountName)
				user.setUsername(sAMAccountName.toString());
			Object email = m.get("email");
			if (null != email)
				user.setEmail(email.toString());
			users.add(user);
		}
		return users;
	}

}