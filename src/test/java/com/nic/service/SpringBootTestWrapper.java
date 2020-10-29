package com.nic.service;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Because new containers are started for every classes that extends this class
 * (due to presence of @Testcontainers), hence it will cause JDBC connection
 * failed error as Application Contexts are cached and reused. To avoid it,
 * using @DirtiesContext annotation to force Spring Test to recreate new
 * contexts for every classes that extend this abstract class.
 * 
 * <br />
 * <br />
 * That said, you may remove this @DirtiesContext if you are going to use single
 * database for all extending classes (achieved by removing @Testcontainers and
 * starting DATABASE located in superclass manually (via start() method))
 * 
 */
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers(disabledWithoutDocker = true)
@DirtiesContext
@ActiveProfiles("test")
public abstract class SpringBootTestWrapper extends PostgresqlDockerSetup {

}
