package nu.westlin.fromspringbeansdependenciestoevents.notification

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
class NotificationModuleTests(
    @Autowired private val notificationsRepository: JdbcNotificationsRepository,
    @Autowired private val jdbcClient: JdbcClient
) {

    @Test
    fun `handle order completed`(scenario: Scenario) {
        val event = OrderCompletedEvent(orderId = 123, userId = 456)

        val scenarioResult = scenario
            // Exekvera nåt
            .publish(event)
            .andCleanup { _ ->
                rensaDatabasen(jdbcClient)
            }

        // Vänta på att systemet hamnar i ett viss tillstånd
        scenarioResult
            .andWaitForStateChange { notificationsRepository.getNotificationsByOrderId(event.orderId) }
            // Verifiera
            .andVerify { notifications ->
                assertThat(notifications).containsExactly(
                    Notification(
                        orderId = event.orderId,
                        userId = event.userId,
                        message = "Order ${event.orderId} has been confirmed"
                    )
                )
            }
    }
}