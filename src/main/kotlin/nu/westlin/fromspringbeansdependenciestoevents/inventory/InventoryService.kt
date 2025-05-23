package nu.westlin.fromspringbeansdependenciestoevents.inventory

import nu.westlin.fromspringbeansdependenciestoevents.domain.Order
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class InventoryService {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    fun updateStock(order: Order) {
        logger.info("updated stock for ${order.id}")
    }
}