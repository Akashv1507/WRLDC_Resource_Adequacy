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
entityManagerFactoryRef = "misOracleEntityManager",
transactionManagerRef = "misOracleTransactionManager",
basePackages = "com.wrldc.resource.adequacy.repository.misOracleRepo")
@EnableTransactionManagement
public class MisOracleConfig {
	
	/**
	 * Oracle Reporting DB datasource definition.
	 * 
	 * @return datasource.
	 */
	@Bean
	@ConfigurationProperties(prefix = "spring.misoracle.datasource")
	public DataSource misOracleDataSource() {
	return DataSourceBuilder.create().build();
	}
	
	/**
	 * Entity manager definition. 
	 *  
	 * @param builder an EntityManagerFactoryBuilder.
	 * @return LocalContainerEntityManagerFactoryBean.
	 */
	
	@Bean(name = "misOracleEntityManager")
	public LocalContainerEntityManagerFactoryBean misOracleEntityManagerFactory (
	 EntityManagerFactoryBuilder builder) {
		return builder
				.dataSource(misOracleDataSource())
				
				.packages("com.wrldc.resource.adequacy.entity")
				.persistenceUnit("misOraclePU")
				.build();
	
	}
	/**
	 * @param entityManagerFactory
	 * @return
	 */
	@Bean(name = "misOracleTransactionManager")
	public PlatformTransactionManager misOracleTransactionManager(@Qualifier("misOracleEntityManager") EntityManagerFactory entityManagerFactory) {
	
	return new JpaTransactionManager(entityManagerFactory);
	}
}
