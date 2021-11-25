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
package ro.allevo.auth.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.web.client.RestTemplate;

import ro.allevo.auth.model.UserEntity;

public class CustomTokenEnhancer implements TokenEnhancer {

	@Value("${fintp.api.url}")
	private String apiUrl;
	 
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        final Map<String, Object> additionalInfo = new HashMap<String, Object>();
    	ResponseEntity<UserEntity> responseUserEntity =
    			new RestTemplate().exchange(apiUrl + "/users/" + authentication.getName(),
    	            HttpMethod.GET, null, new ParameterizedTypeReference<UserEntity>() {});
    	UserEntity user = responseUserEntity.getBody();
    	additionalInfo.put("user_id", user.getId());
    	((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
    	return accessToken;
    }
}
