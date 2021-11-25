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
package ro.allevo.fintpws.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;

@Configuration
@EnableResourceServer
public class OAuth2ResourceServerConfigRemoteTokenService extends ResourceServerConfigurerAdapter {

	@Override
	public void configure(final HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/*").authenticated().and().csrf().disable();
	}

	@Bean
	public AccessTokenConverter accessTokenConverter() {
		return new CustomAccessTokenConverter();
	}

	@Primary
	@Bean
	public RemoteTokenServices tokenServices() {
		final RemoteTokenServices tokenService = new RemoteTokenServices();
		// tokenService.setCheckTokenEndpointUrl("http://193.1.4.51:8080/fintp_auth/oauth/check_token");//pentru
		// productie
		try (InputStream input = new ClassPathResource("application.properties").getInputStream()) {
			Properties prop = new Properties();
			prop.load(input);
			tokenService.setCheckTokenEndpointUrl(prop.getProperty("FinTP_Auth.url") + "/oauth/check_token");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		// localhost
		tokenService.setClientId("SampleClientId");
		tokenService.setClientSecret("secret");
		tokenService.setAccessTokenConverter(accessTokenConverter());
		return tokenService;
	}
}