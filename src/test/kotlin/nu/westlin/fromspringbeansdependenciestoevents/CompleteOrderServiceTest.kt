package nu.westlin.fromspringbeansdependenciestoevents

import com.ninjasquad.springmockk.MockkBean
import io.mockk.justRun
import io.mockk.verify
import nu.westlin.fromspringbeansdependenciestoevents.domain.Order
import nu.westlin.fromspringbeansdependenciestoevents.inventory.InventoryService
import nu.westlin.fromspringbeansdependenciestoevents.notification.NotificationsService
import nu.westlin.fromspringbeansdependenciestoevents.order.CompleteOrderService
import nu.westlin.fromspringbeansdependenciestoevents.order.OrderRepository
import nu.westlin.fromspringbeansdependenciestoevents.reward.RewardsService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest
@Testcontainers
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
    fun `complete order`() {
        val order = Order(id = 42, data = "foo")

        service.completeOrder(order)

        assertThat(orderRepository.getOrder(order.id)).isEqualTo(order)

        // Men restev sakerna som blivit anropad då?
    }
}

@JdbcTest
@Import(CompleteOrderService::class, OrderRepository::class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class CompleteOrderService2Test(
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

    @MockkBean
    private lateinit var inventoryService: InventoryService

    @MockkBean
    private lateinit var rewardsService: RewardsService

    @MockkBean
    private lateinit var notificationsService: NotificationsService

    @Test
    fun `complete order`() {
        val order = Order(id = 42, data = "foo")
        justRun { inventoryService.updateStock(order) }
        justRun { rewardsService.registerRewards(order) }
        justRun { notificationsService.sendOrderConfirmation(order) }

        service.completeOrder(order)

        assertThat(orderRepository.getOrder(order.id)).isEqualTo(order)

        verify {
            inventoryService.updateStock(order)
            rewardsService.registerRewards(order)
            notificationsService.sendOrderConfirmation(order)
        }
    }
}
