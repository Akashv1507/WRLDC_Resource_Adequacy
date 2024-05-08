package com.wrldc.resource.adequacy.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
entityManagerFactoryRef = "postgresqlEntityManager",
transactionManagerRef = "postgresqlTransactionManager",
basePackages = "com.wrldc.resource.adequacy.repository.postgresRepo")

public class PostgresConfig {
	/**
	 * PostgreSQL datasource definition.
	 * 
	 * @return datasource.
	 */
	@Primary
	@Bean
	@ConfigurationProperties(prefix = "spring.postgresql.datasource")
	public DataSource postgresqlDataSource() {
	return DataSourceBuilder.create().build();
	}
	/**
	 * Entity manager definition. 
	 *  
	 * @param builder an EntityManagerFactoryBuilder.
	 * @return LocalContainerEntityManagerFactoryBean.
	 */
	@Primary
	@Bean(name = "postgresqlEntityManager")
	public LocalContainerEntityManagerFactoryBean postgresqlEntityManagerFactory(
	 EntityManagerFactoryBuilder builder) {
		return builder
				.dataSource(postgresqlDataSource())
				.packages("com.wrldc.resource.adequacy.entity")
				.persistenceUnit("postgresqlPU")
				.build();
	}
	
	@Primary
	@Bean(name = "postgresqlTransactionManager")
	public PlatformTransactionManager postgresqlTransactionManager(@Qualifier("postgresqlEntityManager") EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}}
