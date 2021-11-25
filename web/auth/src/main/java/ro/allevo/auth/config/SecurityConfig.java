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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;

@Configuration
@Order(2)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	/*
	 * @Autowired public void configureGlobal(AuthenticationManagerBuilder auth)
	 * throws Exception { auth.inMemoryAuthentication() .wi }
	 */

	@Override
    protected void configure(HttpSecurity http) throws Exception {
    	   http.requestMatchers()
    	   
    	    .antMatchers("/login", "/auth/oauth/authorize", "/oauth/authorize", 
						    	   "/endpoints", "/endpoints/*",  "/rights.js", "/libs/*/*","/images/*/*", 
						    	   "/libs/dist/themes/default/*","/oauth/checkEndpoint","/users")
    	    .and().authorizeRequests()
    	    .antMatchers("/endpoints/checkEndpoint").permitAll()
    	    //.anyRequest().authenticated()
            .and()
            .formLogin()		
            	.loginPage("/login")
            	.failureUrl("/login?error")
            	.usernameParameter("username").passwordParameter("password")
            .permitAll() 
            .and() 
            	.logout().logoutSuccessUrl("/login?logout")
            .permitAll()
		 ;
         http.csrf().disable();
    }
	@Value("${db.schema}")
    private String schema;
	
	@Value("${ldap.urls}")
    private String ldapUrls;

    @Value("${ldap.base.dn}")
    private String ldapBaseDn;

    @Value("${ldap.username}")
    private String ldapSecurityPrincipal;

    @Value("${ldap.password}")
    private String ldapPrincipalPassword;

    @Value("${ldap.user.dn.pattern}")
    private String ldapUserDnPattern;

    @Value("${ldap.domain}")
    private String activeDirectoryDomain;
    
    @Value("${ldap.type}")
    private String ldapType;
    
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
    	if(ldapType.equalsIgnoreCase("ad")) {
    		auth.authenticationProvider(activeDirectoryAuthProvider());
    	}else {
        	auth.ldapAuthentication().userSearchFilter(ldapUserDnPattern)
            .rolePrefix("ROLE_").userDetailsContextMapper(new CustomUserDetailsMapper())
            .contextSource(contextSource());
    	}
    }
    
    @Bean
    public AuthenticationProvider activeDirectoryAuthProvider() {
        ActiveDirectoryLdapAuthenticationProvider authProvider = new ActiveDirectoryLdapAuthenticationProvider(this.activeDirectoryDomain, ldapUrls);
        authProvider.setConvertSubErrorCodesToExceptions(true);
        authProvider.setUseAuthenticationRequestCredentials(true);
        authProvider.setSearchFilter(ldapUserDnPattern);
        authProvider.setUserDetailsContextMapper(new CustomUserDetailsMapper());
        return authProvider;
    }
    
    @Bean
    public LdapContextSource contextSource(){
        LdapContextSource contextSource = new LdapContextSource();
        contextSource.setUrl(ldapUrls);
        contextSource.setBase(ldapBaseDn);
        contextSource.setUserDn(ldapSecurityPrincipal);
        contextSource.setPassword(ldapPrincipalPassword);
        contextSource.afterPropertiesSet();
        return contextSource;
    }

    @Bean
    public LdapTemplate ldapTemplate(){
        LdapTemplate template = new LdapTemplate(contextSource());
        return template;
    }
    
    @Bean
    public String schema() {
    	return schema;
    }
    
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
       return super.authenticationManagerBean();
    }

}