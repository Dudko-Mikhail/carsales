package by.dudko.carsales.integration;

import by.dudko.carsales.integration.IntegrationTest;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;

@IntegrationTest
@Sql("classpath:/data.sql")
public abstract class BaseIntegrationTest {
    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:15.0");

    @BeforeAll
    static void configureContainer() {
        container.withUsername("test")
                .withPassword("test")
                .start();
    }

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);

    }
}
