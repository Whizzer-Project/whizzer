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


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationServerConfigJwt extends AuthorizationServerConfigurerAdapter {

    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;
    
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Value("${FinTP_UI.url}")
	private String url;
    
    @Override
    public void configure(final AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
    }
    
//    There are four different grant types defined by OAuth2. These grant types define interactions between the client and the auth/resource server. More detailed information can be found here.
//    Authorisation code - redirection-based flow for confidential client, the client communicates with the server via user-agent (web browser etc.), typical for web servers
//    Implicit - typically used with public clients running in a web browser using a scripting language, does not contain refresh tokens, this grant does not contain authentication and relies on redirection URI specified during client registration to the auth server
//    Resource owner password credentials - used with trusted clients (e.g. clients written by the same company that owns the auth server), user credentials are passed to the client and then to the auth server and exchanged for access and refresh tokens
//    Client credentials - used when the client itself is the resource owner (one client does not operate with multiple users), client credentials are exchanged directly for the tokens

    //ToDo clients.jdbc
	@Override
	public void configure(final ClientDetailsServiceConfigurer clients) throws Exception {
		PreparedStatement preparedStatement = null;
		try (Statement stmt = jdbcTemplate.getDataSource().getConnection().createStatement();
			ResultSet rs = stmt.executeQuery("SELECT web_server_redirect_uri FROM finauth.oauth_client_details")) {
			if (rs.next()) {
				List<String> uris = Arrays.asList(rs.getString("web_server_redirect_uri").split(","));
				if(!uris.contains(url + "/login")) {
					preparedStatement = jdbcTemplate.getDataSource().getConnection()
							.prepareStatement("update finauth.oauth_client_details set web_server_redirect_uri = ? where client_id='SampleClientId' ");
					preparedStatement.setString(1, StringUtils.join(uris, ',')+ "," + url + "/login" );
					preparedStatement.executeUpdate();
				}
			}
			if(null!=preparedStatement)
				preparedStatement.close();
		} catch (SQLException e) {
			if(null!=preparedStatement)
				preparedStatement.close();
			e.printStackTrace();
		}catch (Exception e1) {
			if(null!=preparedStatement)
				preparedStatement.close();
			e1.printStackTrace();
		}
		
		clients.jdbc(jdbcTemplate.getDataSource());         
	}

	@Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setSupportRefreshToken(true);
        defaultTokenServices.setReuseRefreshToken(true);
        return defaultTokenServices;
    }

    @Override
    public void configure(final AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        final TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), accessTokenConverter()));
        endpoints.tokenStore(tokenStore()).tokenEnhancer(tokenEnhancerChain).authenticationManager(authenticationManager);
    }
      
    @Bean
    public TokenStore tokenStore() { 
        return new JdbcTokenStore(jdbcTemplate.getDataSource()); 
    }
     
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        final JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("123");
        final KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(
       		new ClassPathResource("mytest.jks"), "mypass".toCharArray());
        converter.setKeyPair(keyStoreKeyFactory.getKeyPair("mytest"));
        return converter;
    }

    @Bean
    public TokenEnhancer tokenEnhancer() {
        return new CustomTokenEnhancer();
    }
}
