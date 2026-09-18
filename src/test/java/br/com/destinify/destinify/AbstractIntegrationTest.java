package br.com.destinify.destinify;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.RestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractIntegrationTest {

    @LocalServerPort
    protected int port;

    static final PostgreSQLContainer<?> postgres;
    static final RabbitMQContainer rabbitmq;

    static {
        if (System.getProperty("api.version") == null) {
            System.setProperty("api.version", "1.44");
        }
        postgres = new PostgreSQLContainer<>("postgres:16-alpine")
                .withDatabaseName("destinify_test")
                .withUsername("test")
                .withPassword("test");
        postgres.start();

        rabbitmq = new RabbitMQContainer("rabbitmq:3.13-management-alpine");
        rabbitmq.start();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        // PostgreSQL
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");

        // RabbitMQ
        registry.add("spring.rabbitmq.host", rabbitmq::getHost);
        registry.add("spring.rabbitmq.port", rabbitmq::getAmqpPort);
        registry.add("spring.rabbitmq.username", rabbitmq::getAdminUsername);
        registry.add("spring.rabbitmq.password", rabbitmq::getAdminPassword);

        // TTL curto para testes de expiração (1000ms = 1 segundo)
        registry.add("destinify.reservations.ttl-milliseconds", () -> "1000");
    }

    protected RestClient createRestClient() {
        return RestClient.builder()
                .baseUrl("http://localhost:" + port)
                .build();
    }
}
