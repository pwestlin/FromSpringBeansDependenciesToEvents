package nu.westlin.fromspringbeansdependenciestoevents.order

import nu.westlin.fromspringbeansdependenciestoevents.domain.Order
import nu.westlin.fromspringbeansdependenciestoevents.inventory.InventoryService
import nu.westlin.fromspringbeansdependenciestoevents.notification.NotificationsService
import nu.westlin.fromspringbeansdependenciestoevents.reward.RewardsService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CompleteOrderService(
    private val orderRepository: OrderRepository,
    private val inventoryService: InventoryService,
    private val rewardsService: RewardsService,
    private val notificationsService: NotificationsService,
) {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    @Transactional
    fun completeOrder(order: Order) {
        orderRepository.save(order)

        inventoryService.updateStock(order)
        rewardsService.registerRewards(order)
        notificationsService.sendOrderConfirmation(order)

        logger.info("Order ${order.id} completed successfully")
    }
}