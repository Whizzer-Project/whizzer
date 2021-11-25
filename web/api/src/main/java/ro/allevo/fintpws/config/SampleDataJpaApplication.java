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

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
// @EnableTransactionManagement
public class SampleDataJpaApplication {
	// pentru a folosi jndi doar atunci cand aplicatia nu ruleaza in mod standalone

	@Value("${app.standaloneMode}")
	private String standalone;

	@Value("${data.datasource.url:null}")
	private String dbUrl;

	@Value("${data.datasource.username:null}")
	private String dbUsername;

	@Value("${data.datasource.password:null}")
	private String dbPassword;

	@Value("${cfg.datasource.url:null}")
	private String cfgDbUrl;

	@Value("${cfg.datasource.username:null}")
	private String cfgDbUsername;

	@Value("${cfg.datasource.password:null}")
	private String cfgDbPassword;

	@Value("${list.datasource.url:null}")
	private String listDbUrl;

	@Value("${list.datasource.username:null}")
	private String listDbUsername;

	@Value("${list.datasource.password:null}")
	private String listDbPassword;
	
	@Value("${track.datasource.url:null}")
	private String trackDbUrl;

	@Value("${track.datasource.username:null}")
	private String trackDbUsername;

	@Value("${track.datasource.password:null}")
	private String trackDbPassword;

	@Value("${cfg.datasource.jndi-name}")
	private String jndiCfg;

	@Value("${data.datasource.jndi-name}")
	private String jndiData;

	@Value("${list.datasource.jndi-name}")
	private String jndiList;
	
	@Value("${track.datasource.jndi-name}")
	private String jndiTrack;
	
	@Value("${datasource.driver-class-name:null}")
	private String driverClassName;
	
	@Value("${connect.datasource.url:null}")
	private String connectDbUrl;

	@Value("${connect.datasource.username:null}")
	private String connectDbUsername;

	@Value("${connect.datasource.password:null}")
	private String connectDbPassword;
	
	@Value("${connect.datasource.jndi-name}")
	private String jndiConnect;
	
	@Value("${at.datasource.url:null}")
	private String atDbUrl;

	@Value("${at.datasource.username:null}")
	private String atDbUsername;

	@Value("${at.datasource.password:null}")
	private String atDbPassword;
	
	@Value("${at.datasource.jndi-name}")
	private String jndiAt;
	
	@Value("${bo.datasource.url:null}")
	private String boDbUrl;

	@Value("${bo.datasource.username:null}")
	private String boDbUsername;

	@Value("${bo.datasource.password:null}")
	private String boDbPassword;
	
	@Value("${bo.datasource.jndi-name}")
	private String jndiBO;

	@Primary
	@Bean
	@PersistenceContext(unitName = "fintpDATA", type = PersistenceContextType.TRANSACTION)
	public LocalContainerEntityManagerFactoryBean dataEntityManagerFactory() {

		final LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
		emf.setDataSource(dataSourceData());
		emf.setPersistenceUnitName("fintpDATA");
		emf.setJpaPropertyMap(jpaPropertyMap());
		emf.setPackagesToScan("ro.allevo.fintpws.model", "ro.allevo.fintpuiws.model", "ro.allevo.whizzer.model");
		final JpaVendorAdapter vendorAdapter = new org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter();
		emf.setJpaVendorAdapter(vendorAdapter);
		emf.setJpaProperties(jpaProperties());

		return emf;
	}

	@Primary
	@Bean
	public DataSource dataSourceData() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		if (standalone.equals("true")) {
			JndiDataSourceLookup dataSourceLookup = new JndiDataSourceLookup();
			return dataSourceLookup.getDataSource(jndiData);
//			return dataSource1;
		} else {
			dataSource.setDriverClassName(driverClassName);
			dataSource.setUrl(dbUrl);
			dataSource.setUsername(dbUsername);
			dataSource.setPassword(dbPassword);
		}
		return dataSource;
	}

	@Primary
	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
		final JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(emf);

		return transactionManager;
	}

	@Bean
	@PersistenceContext(unitName = "fintpCFG", type = PersistenceContextType.TRANSACTION)
	public LocalContainerEntityManagerFactoryBean cfgEntityManagerFactory(){

		final LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
		emf.setDataSource(cfgDataSource());

		emf.setPersistenceUnitName("fintpCFG");
		emf.setJpaPropertyMap(jpaPropertyMap());
		emf.setPackagesToScan("ro.allevo.fintpws.model", "ro.allevo.fintpuiws.model", "ro.allevo.fintpws.converters", "ro.allevo.whizzer.model");
		final JpaVendorAdapter vendorAdapter = new org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter();
		emf.setJpaVendorAdapter(vendorAdapter);
		emf.setJpaProperties(jpaProperties());

		return emf;
	}

	@Bean
	public DataSource cfgDataSource(){
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		if (standalone.equals("true")) {

			JndiDataSourceLookup dataSourceLookup = new JndiDataSourceLookup();
			return dataSourceLookup.getDataSource(jndiCfg);
//			return dataSource1;
		} else {
			dataSource.setDriverClassName(driverClassName);
			dataSource.setUrl(cfgDbUrl);
			dataSource.setUsername(cfgDbUsername);
			dataSource.setPassword(cfgDbPassword);
		}

		return dataSource;
	}

	@Bean
	public PlatformTransactionManager cfgTransactionManager(EntityManagerFactory emf) {
		final JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(emf);

		return transactionManager;
	}

	@Bean
	@PersistenceContext(unitName = "fintpLIST", type = PersistenceContextType.TRANSACTION)
	public LocalContainerEntityManagerFactoryBean listEntityManagerFactory()
			throws NamingException, SQLException {

		final LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
		emf.setDataSource(listDataSource());

		emf.setPersistenceUnitName("fintpLIST");
		emf.setJpaPropertyMap(jpaPropertyMap());
		emf.setPackagesToScan("ro.allevo.fintpws.model", "ro.allevo.fintpuiws.model", "ro.allevo.fintpws.converters");
		final JpaVendorAdapter vendorAdapter = new org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter();
		emf.setJpaVendorAdapter(vendorAdapter);
		emf.setJpaProperties(jpaProperties());

		return emf;
	}

	@Bean
	public DataSource listDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		if (standalone.equals("true")) {

			JndiDataSourceLookup dataSourceLookup = new JndiDataSourceLookup();
			return dataSourceLookup.getDataSource(jndiList);
//			return dataSource1;
		} else {
			dataSource.setDriverClassName(driverClassName);
			dataSource.setUrl(listDbUrl);
			dataSource.setUsername(listDbUsername);
			dataSource.setPassword(listDbPassword);
		}

		return dataSource;
	}

	@Bean
	public PlatformTransactionManager listTransactionManager(EntityManagerFactory emf) {
		final JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(emf);

		return transactionManager;
	}
	
	@Bean
	@PersistenceContext(unitName = "fintpTRACK", type = PersistenceContextType.TRANSACTION)
	public LocalContainerEntityManagerFactoryBean trackEntityManagerFactory()
			throws NamingException, SQLException {

		final LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
		emf.setDataSource(trackDataSource());

		emf.setPersistenceUnitName("fintpTRACK");
		emf.setJpaPropertyMap(jpaPropertyMap());
		emf.setPackagesToScan("ro.allevo.tracker.model");
		final JpaVendorAdapter vendorAdapter = new org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter();
		emf.setJpaVendorAdapter(vendorAdapter);
		emf.setJpaProperties(jpaProperties());

		return emf;
	}

	@Bean
	public DataSource trackDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		if (standalone.equals("true")) {

			JndiDataSourceLookup dataSourceLookup = new JndiDataSourceLookup();
			return dataSourceLookup.getDataSource(jndiTrack);
//			return dataSource1;
		} else {
			dataSource.setDriverClassName(driverClassName);
			dataSource.setUrl(trackDbUrl);
			dataSource.setUsername(trackDbUsername);
			dataSource.setPassword(trackDbPassword);
		}

		return dataSource;
	}

	@Bean
	public PlatformTransactionManager trackTransactionManager(EntityManagerFactory emf) {
		final JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(emf);

		return transactionManager;
	}
	
	@Bean
	@PersistenceContext(unitName = "fintpCONNECT", type = PersistenceContextType.TRANSACTION)
	public LocalContainerEntityManagerFactoryBean connectEntityManagerFactory()
			throws NamingException {

		final LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
		emf.setDataSource(connectDataSource());
		
		emf.setPersistenceUnitName("fintpCONNECT");
		emf.setJpaPropertyMap(jpaPropertyMap());
		emf.setPackagesToScan("ro.allevo.connect.model", "ro.allevo.connect.resources", "ro.allevo.fintpws.model");
		final JpaVendorAdapter vendorAdapter = new org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter();
		emf.setJpaVendorAdapter(vendorAdapter);
		emf.setJpaProperties(jpaProperties());

		return emf;
	}

	@Bean
	public DataSource connectDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		if (standalone.equals("true")) {
			JndiDataSourceLookup dataSourceLookup = new JndiDataSourceLookup();
			return dataSourceLookup.getDataSource(jndiConnect);
//			return dataSourceConnect;
		} else {
			dataSource.setDriverClassName(driverClassName);
			dataSource.setUrl(connectDbUrl);
			dataSource.setUsername(connectDbUsername);
			dataSource.setPassword(connectDbPassword);
		}
		return dataSource;
	}

	@Bean
	public PlatformTransactionManager connectTransactionManager(EntityManagerFactory emf) {
		final JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(emf);

		return transactionManager;
	}
	
	@Bean
	@PersistenceContext(unitName = "fintpAT", type = PersistenceContextType.TRANSACTION)
	public LocalContainerEntityManagerFactoryBean atEntityManagerFactory()
			throws NamingException {

		final LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
		emf.setDataSource(atDataSource());
		
		emf.setPersistenceUnitName("fintpAT");
		emf.setJpaPropertyMap(jpaPropertyMap());
		emf.setPackagesToScan("ro.allevo.at.model", "ro.allevo.at.resources", "ro.allevo.fintpws.model"); //, "ro.allevo.at.resources"
		final JpaVendorAdapter vendorAdapter = new org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter();
		emf.setJpaVendorAdapter(vendorAdapter);
		emf.setJpaProperties(jpaProperties());

		return emf;
	}

	@Bean
	public DataSource atDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		if (standalone.equals("true")) {
			JndiDataSourceLookup dataSourceLookup = new JndiDataSourceLookup();
			return dataSourceLookup.getDataSource(jndiAt);
//			return dataSourceAt;
		} else {
			dataSource.setDriverClassName(driverClassName);
			dataSource.setUrl(atDbUrl);
			dataSource.setUsername(atDbUsername);
			dataSource.setPassword(atDbPassword);
		}
		return dataSource;
	}
	
	@Bean
	public PlatformTransactionManager atTransactionManager(EntityManagerFactory emf) {
		final JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(emf);
		
		return transactionManager;
	}
	
	@Bean
	@PersistenceContext(unitName = "fintpBO", type = PersistenceContextType.TRANSACTION)
	public LocalContainerEntityManagerFactoryBean boEntityManagerFactory()
			throws NamingException {

		final LocalContainerEntityManagerFactoryBean emb = new LocalContainerEntityManagerFactoryBean();
		emb.setDataSource(boDataSource());
		
		emb.setPersistenceUnitName("fintpBO");
		emb.setJpaPropertyMap(jpaPropertyMap());
		emb.setPackagesToScan("ro.allevo.fintpws.model", "ro.allevo.fintpws.resources");
		final JpaVendorAdapter vendorAdapter = new org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter();
		emb.setJpaVendorAdapter(vendorAdapter);
		emb.setJpaProperties(jpaProperties());

		return emb;
	}

	@Bean
	public DataSource boDataSource() throws IllegalArgumentException, NamingException {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		if (standalone.equals("true")) {
			JndiDataSourceLookup dataSourceLookup = new JndiDataSourceLookup();
			return dataSourceLookup.getDataSource(jndiBO);
//			return dataSourceBO;
		} else {
			dataSource.setDriverClassName(driverClassName);
			dataSource.setUrl(boDbUrl);
			dataSource.setUsername(boDbUsername);
			dataSource.setPassword(boDbPassword);
		}
		return dataSource;
	}
	
	@Bean
	public PlatformTransactionManager boTransactionManager(EntityManagerFactory emb) {
		final JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(emb);

		return transactionManager;
	}

	public Properties jpaProperties() {
		final Properties properties = new Properties();
		properties.setProperty("databasePlatform", "org.eclipse.persistence.platform.database.H2Platform");
		properties.setProperty("generateDdl", "true");
		properties.setProperty("showSql", "false");
		properties.setProperty("eclipselink.logging.level", "FINE");

		return properties;
	}

	public Map<String, Object> jpaPropertyMap() {
		final Map<String, Object> jpaPropertyMap = new HashMap<>();

		jpaPropertyMap.put("eclipselink.weaving", "false");
		jpaPropertyMap.put("eclipselink.logging.level.sql", "ALL");
		jpaPropertyMap.put("eclipselink.cache.shared.default", "true");

		return jpaPropertyMap;
	}

}
