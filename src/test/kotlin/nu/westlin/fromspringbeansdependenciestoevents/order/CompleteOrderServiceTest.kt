package nu.westlin.fromspringbeansdependenciestoevents.order

import nu.westlin.fromspringbeansdependenciestoevents.common.OrderCompletedEvent
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.event.ApplicationEvents
import org.springframework.test.context.event.RecordApplicationEvents
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.util.stream.Collectors

@JdbcTest
@Import(CompleteOrderService::class, OrderRepository::class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@RecordApplicationEvents
class CompleteOrderServiceTest(
    @Autowired private val service: CompleteOrderService,
    @Autowired private val orderRepository: OrderRepository,
) {

    companion object {
        @Container
        val postgresContainer = PostgreSQLContainer<Nothing>("postgres:latest").apply {
            withDatabaseName("testdb")
            withUsername("user")
            withPassword("password")
        }

        @JvmStatic
        @DynamicPropertySource
        fun datasourceConfig(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgresContainer::getJdbcUrl)
            registry.add("spring.datasource.username", postgresContainer::getUsername)
            registry.add("spring.datasource.password", postgresContainer::getPassword)
        }
    }

    @Test
    fun `complete order`(applicationEvents: ApplicationEvents) {
        val order = Order(id = 42, data = "foo")
        val userId = 11L
        service.completeOrder(order = order, userId)

        assertThat(orderRepository.getOrder(order.id)).isEqualTo(order)
        assertThat(applicationEvents.list<OrderCompletedEvent>()).containsExactly(OrderCompletedEvent(orderId = order.id, userId = userId))
    }
}

inline fun <reified T:Any> ApplicationEvents.list(): List<T> = this.stream(T::class.java).collect(Collectors.toList())