package com.example.msccspringtesting.infrastructure.adapters.output.persistence;

import com.example.msccspringtesting.infrastructure.adapters.config.TestConfigPersistenceComponent;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@ActiveProfiles(value = "mysql")
@ContextConfiguration(classes = TestConfigPersistenceComponent.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public abstract class AbstractPersistenceAdapterTest {

    @Container
    private static final MySQLContainer<?> MY_SQL_CONTAINER = new MySQLContainer<>("mysql:5.7.22")
            .withInitScript("init_scripts.sql");

    @DynamicPropertySource
    static void mySqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> MY_SQL_CONTAINER.getJdbcUrl() + "?verifyServerCertificate=false&useSSL=false&requireSSL=false");
        registry.add("spring.datasource.password", MY_SQL_CONTAINER::getPassword);
        registry.add("spring.datasource.username", MY_SQL_CONTAINER::getUsername);
    }

}