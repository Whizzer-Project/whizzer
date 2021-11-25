///*
//* FinTP - Financial Transactions Processing Application
//* Copyright (C) 2013 Business Information Systems (Allevo) S.R.L.
//*
//* This program is free software: you can redistribute it and/or modify
//* it under the terms of the GNU General Public License as published by
//* the Free Software Foundation, either version 3 of the License, or
//* (at your option) any later version.
//*
//* This program is distributed in the hope that it will be useful,
//* but WITHOUT ANY WARRANTY; without even the implied warranty of
//* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
//* GNU General Public License for more details.
//*
//* You should have received a copy of the GNU General Public License
//* along with this program. If not, see <http://www.gnu.org/licenses/>
//* or contact Allevo at : 031281 Bucuresti, 23C Calea Vitan, Romania,
//* phone +40212554577, office@allevo.ro <mailto:office@allevo.ro>, www.allevo.ro.
//*/
//package ro.allevo.fintpws.config;
//
//import java.io.BufferedInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.core.io.Resource;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
//import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
//import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
//import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
//import org.springframework.security.oauth2.provider.token.TokenStore;
//import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
//import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
//
//@Configuration
//@EnableResourceServer
//public class OAuth2ResourceServerConfigJwt extends ResourceServerConfigurerAdapter {
//
//	@Autowired
//	private CustomAccessTokenConverter customAccessTokenConverter;
//
//	@Override
//	public void configure(final HttpSecurity http) throws Exception {
//		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED).
//		and().authorizeRequests().
//		and().exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());
//	}
//
//	@Override
//	public void configure(final ResourceServerSecurityConfigurer config) {
//		config.tokenServices(tokenServices());
//	}
//
//	@Bean
//	public TokenStore tokenStore() {
//		return new JwtTokenStore(accessTokenConverter());
//	}
//
//	@Bean
//	public JwtAccessTokenConverter accessTokenConverter() {
//		final JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
//		converter.setAccessTokenConverter(customAccessTokenConverter);
//		converter.setSigningKey("123");
//		final Resource resource = new ClassPathResource("public.txt");
//		String publicKey = null;
//
//		try {
//			publicKey = readInputStreamAsString(resource.getInputStream());
//		} catch (final IOException e) {
//			throw new RuntimeException(e);
//		}
//		converter.setVerifierKey(publicKey);
//		return converter;
//	}
//
//	@Bean
//	@Primary
//	public DefaultTokenServices tokenServices() {
//		final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
//		defaultTokenServices.setTokenStore(tokenStore());
//		return defaultTokenServices;
//	}
//
//	public static String readInputStreamAsString(InputStream in) throws IOException {
//		BufferedInputStream bis = new BufferedInputStream(in);
//		ByteArrayOutputStream buf = new ByteArrayOutputStream();
//		int result = bis.read();
//		while (result != -1) {
//			byte b = (byte) result;
//			buf.write(b);
//			result = bis.read();
//		}
//		return buf.toString();
//	}
//}