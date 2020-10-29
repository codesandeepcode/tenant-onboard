package com.nic.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

@SuppressWarnings("rawtypes")
@ContextConfiguration(initializers = PostgresqlDockerSetup.Initializer.class)
abstract class PostgresqlDockerSetup {

	static Logger log = LoggerFactory.getLogger(PostgresqlDockerSetup.class);

	@Container
	static final PostgreSQLContainer DATABASE = new PostgreSQLContainer<>("postgres:10.14");

	static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

		@Override
		public void initialize(ConfigurableApplicationContext applicationContext) {
			log.info("The configured PostgreSQL Docker container :- URL - {}, username - {}", DATABASE.getJdbcUrl(),
					DATABASE.getUsername());

			TestPropertyValues
					.of("spring.datasource.url=" + DATABASE.getJdbcUrl(),
							"spring.datasource.username=" + DATABASE.getUsername(),
							"spring.datasource.password=" + DATABASE.getPassword())
					.applyTo(applicationContext.getEnvironment());
		}

	}

}
