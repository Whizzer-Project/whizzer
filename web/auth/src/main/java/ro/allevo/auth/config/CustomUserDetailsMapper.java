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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.LdapUserDetailsMapper;
import org.springframework.web.client.RestTemplate;

import ro.allevo.auth.model.BaseModel;
import ro.allevo.auth.model.UserRoleEntity;

@Configuration
public class CustomUserDetailsMapper extends LdapUserDetailsMapper {
 
	private static String apiUrl;

	@Value("${fintp.api.url}")
	public void setApiUrl(String apiUrl) {
		CustomUserDetailsMapper.apiUrl = apiUrl;
	} 
	
    protected static Logger logger = Logger.getLogger("CustomUserDetailsMapper");
   
    @Override
    public UserDetails mapUserFromContext(DirContextOperations ctx, String username, Collection< ? extends GrantedAuthority> authorities) {
    	/**
         * at this point, the user has permissions based on groups in LDAP/AD. If
         * we used AD for permissions, this would be sufficient, but we want permissions
         * from elsewhere so we need to modify the permissions/authorities
         */
        UserDetails user = super.mapUserFromContext( ctx, username, getPermissions(username));
        return user;
    }
 
    /**
     * gets permissions
     *
     * @param username the username to get permissions for
     * @return a Set of permissions for the user
     */
    private Set<GrantedAuthority> getPermissions(String username) {
    	 Set<GrantedAuthority> permissions = new HashSet<GrantedAuthority>();
        ResponseEntity<BaseModel<UserRoleEntity>> responseEntity =
     			new RestTemplate().exchange( apiUrl +"/users/"+username+"/user-roles",
     	            HttpMethod.GET, null, new ParameterizedTypeReference<BaseModel<UserRoleEntity>>() {});
    	for(UserRoleEntity userRoleEntity:responseEntity.getBody().getItems()){
     		permissions.add(new SimpleGrantedAuthority("ROLE_"+userRoleEntity.getRoleEntity().getName()+"_"+userRoleEntity.getAction()));
 		}
        return permissions;
    }
}