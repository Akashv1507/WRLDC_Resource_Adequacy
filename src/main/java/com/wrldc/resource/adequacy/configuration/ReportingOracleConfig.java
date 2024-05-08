package com.wrldc.resource.adequacy.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableJpaRepositories(
entityManagerFactoryRef = "oracleEntityManager",
transactionManagerRef = "oracleTransactionManager",
basePackages = "com.wrldc.resource.adequacy.repository.oracleRepo")
@EnableTransactionManagement
public class ReportingOracleConfig {
	
	/**
	 * Oracle Reporting DB datasource definition.
	 * 
	 * @return datasource.
	 */
	@Bean
	@ConfigurationProperties(prefix = "spring.oracle.datasource")
	public DataSource oracleDataSource() {
	return DataSourceBuilder.create().build();
	}
	
	/**
	 * Entity manager definition. 
	 *  
	 * @param builder an EntityManagerFactoryBuilder.
	 * @return LocalContainerEntityManagerFactoryBean.
	 */
	
	@Bean(name = "oracleEntityManager")
	public LocalContainerEntityManagerFactoryBean oracleEntityManagerFactory (
	 EntityManagerFactoryBuilder builder) {
		return builder
				.dataSource(oracleDataSource())
				
				.packages("com.wrldc.resource.adequacy.entity")
				.persistenceUnit("oraclePU")
				.build();
	
	}
	/**
	 * @param entityManagerFactory
	 * @return
	 */
	@Bean(name = "oracleTransactionManager")
	public PlatformTransactionManager oracleTransactionManager(@Qualifier("oracleEntityManager") EntityManagerFactory entityManagerFactory) {
	
	return new JpaTransactionManager(entityManagerFactory);
	}
}
