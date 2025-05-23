package nu.westlin.fromspringbeansdependenciestoevents.notification

import nu.westlin.fromspringbeansdependenciestoevents.domain.Order
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class NotificationsService {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    fun sendOrderConfirmation(order: Order) {
        logger.info("Sent confirmation for order ${order.id}")
    }
}