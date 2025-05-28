package nu.westlin.fromspringbeansdependenciestoevents.notification

import nu.westlin.fromspringbeansdependenciestoevents.common.OrderCompletedEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.modulith.ApplicationModule
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@ApplicationModule(allowedDependencies = ["common"])
class NotificationsModuleMetadata

@Service
class NotificationsService(
    private val notificationsRepository: JdbcNotificationsRepository
) {
    private fun sendOrderConfirmation(orderId: Long, userId: Long) {
        logger.info("Sending notification for $orderId")
        notificationsRepository.save(
            Notification(
                orderId = orderId,
                userId = userId,
                message = "Order $orderId has been confirmed"
            )
        )
    }

    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    @EventListener(OrderCompletedEvent::class)
    @Transactional
    fun handleOrderCompletedEvent(event: OrderCompletedEvent) {
        logger.info("Handling $event")
        sendOrderConfirmation(event.orderId, event.userId)
    }
}

data class Notification(
    val orderId: Long,
    val userId: Long,
    val message: String
)

@Repository
class JdbcNotificationsRepository(
    private val jdbcClient: JdbcClient
) {

    @Suppress("JpaQueryApiInspection")
    // TODO pevest: typealias för Set<Notification>
    fun getNotificationsByOrderId(orderId: Long): Set<Notification> {
        return jdbcClient
            .sql("select * from notifications where order_id = :orderId")
            .param("orderId", orderId)
            // TODO pevest: Extension function
            .query(Notification::class.java)
            .set()
    }

    @Suppress("JpaQueryApiInspection")
    fun save(notification: Notification) {
        jdbcClient
            .sql("INSERT INTO notifications(order_id, user_id, message) VALUES (:orderId, :userId, :message)")
            .param("orderId", notification.orderId)
            .param("userId", notification.userId)
            .param("message", notification.message)
            .update()
    }
}