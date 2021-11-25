package ro.allevo.auth.controller;
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

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TokenController {

	@Resource(name = "tokenStore")
	TokenStore tokenStore;

	@DeleteMapping(value = "/token")
	@ResponseBody
	public void getTokens(OAuth2Authentication auth, HttpServletRequest request) {
		OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) auth.getDetails();
		OAuth2AccessToken accessToken = tokenStore.readAccessToken(details.getTokenValue());

		if (null != tokenStore.readAccessToken(details.getTokenValue())) {
			OAuth2RefreshToken oAuth2RefreshToken = tokenStore
					.readAccessToken(details.getTokenValue()).getRefreshToken();
			if (null != oAuth2RefreshToken) {
				System.out.println(oAuth2RefreshToken);
				tokenStore.removeAccessToken(tokenStore.readAccessToken(details.getTokenValue()));
				tokenStore.removeRefreshToken(oAuth2RefreshToken);
			}
		}

		tokenStore.removeAccessToken(accessToken);
		new SecurityContextLogoutHandler().logout(request, null, null);
		request.getSession().invalidate();
	}

}
