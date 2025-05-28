package nu.westlin.fromspringbeansdependenciestoevents.inventory

import nu.westlin.fromspringbeansdependenciestoevents.order.Order
import nu.westlin.fromspringbeansdependenciestoevents.common.OrderCompletedEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.modulith.ApplicationModule
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@ApplicationModule(allowedDependencies = ["common"])
class InventoryModuleMetadata

@Service
class InventoryService {

    private fun updateStock(orderId: Long) {
        logger.info("Updating stock rewards for $orderId")
    }

    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    @EventListener(OrderCompletedEvent::class)
    @Transactional
    fun handleOrderCompletedEvent(event: OrderCompletedEvent) {
        logger.info("Handling $event")
        updateStock(event.orderId)
    }
}

