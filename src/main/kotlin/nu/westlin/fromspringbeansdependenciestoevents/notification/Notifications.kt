package nu.westlin.fromspringbeansdependenciestoevents.notification

import nu.westlin.fromspringbeansdependenciestoevents.common.domain.Order
import nu.westlin.fromspringbeansdependenciestoevents.common.event.OrderCompletedEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class NotificationsService {
    private fun sendOrderConfirmation(order: Order) {
        logger.info("Sending notification for ${order.id}")
    }

    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    @EventListener(OrderCompletedEvent::class)
    @Transactional
    fun handleOrderCompletedEvent(event: OrderCompletedEvent) {
        logger.info("Handling $event")
        sendOrderConfirmation(event.order)
    }
}
