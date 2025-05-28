package nu.westlin.fromspringbeansdependenciestoevents.order

import nu.westlin.fromspringbeansdependenciestoevents.TestPostgresContainerConfig
import nu.westlin.fromspringbeansdependenciestoevents.common.OrderCompletedEvent
import nu.westlin.fromspringbeansdependenciestoevents.rensaDatabasen
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.modulith.test.ApplicationModuleTest
import org.springframework.modulith.test.Scenario

@Import(TestPostgresContainerConfig::class)
@ApplicationModuleTest
class OrderModuleTests(
    @Autowired private val service: CompleteOrderService,
    @Autowired private val orderRepository: OrderRepository,
    @Autowired private val jdbcClient: JdbcClient
) {

    @Test
    fun `complete order`(scenario: Scenario) {
        val order = Order(id = 42, data = "foo")
        val userId = 11L

        val scenarioResult = scenario
            // Exekvera nåt
            .stimulate { _ ->
                service.completeOrder(order = order, userId)
            }
            .andCleanup { _ ->
                // Eftersom detta är av formen "integrationstest" kommer det du skickar in lagras i databasen och därför måste man rensa upp efter sig.
                rensaDatabasen(jdbcClient)
            }

        // Vänta på att systemet hamnar i ett viss tillstånd
        scenarioResult
            .andWaitForStateChange { orderRepository.getOrder(order.id) }
            .andVerify { orderResult ->
                assertThat(orderResult).isEqualTo(order)
            }

        // Vänta på att ett event skickats
        scenarioResult.andWaitForEventOfType(OrderCompletedEvent::class.java)
            .matching { event ->
                event == OrderCompletedEvent(
                    orderId = order.id,
                    userId = userId
                )
            }
            .toArrive()
    }
}
