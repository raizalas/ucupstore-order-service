package com.ucupstore.orderservice.order.domain;

import com.ucupstore.orderservice.config.DataConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import reactor.test.StepVerifier;


@DataR2dbcTest
@Import(DataConfig.class)
@Testcontainers
class OrderRepositoryR2dbcTest {

    @Container
    static PostgreSQLContainer<?> postgresql = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:16.4-alpine"));

    @Autowired
    private OrderRepository orderRepository;

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.r2dbc.url", OrderRepositoryR2dbcTest::getR2dbcUrl);
        registry.add("spring.r2dbc.username", postgresql::getUsername);
        registry.add("spring.r2dbc.password", postgresql::getPassword);
        registry.add("spring.flyway.url", postgresql::getJdbcUrl);
    }

    private static String getR2dbcUrl() {
        return String.format("r2dbc:postgresql://%s:%s/%s",
                postgresql.getHost(),
                postgresql.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT),
                postgresql.getDatabaseName());
    }

    @Test
    void createRejectedOrder() {
        Order order = OrderService.buildRejectedOrder("1234567890", 3);
        StepVerifier
                .create(orderRepository.save(order))
                .expectNextMatches(
                        order1 -> order.status().equals(OrderStatus.REJECTED)
                )
                .verifyComplete();
    }
}