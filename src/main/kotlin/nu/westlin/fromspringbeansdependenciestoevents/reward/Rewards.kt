package nu.westlin.fromspringbeansdependenciestoevents.reward

import nu.westlin.fromspringbeansdependenciestoevents.common.OrderCompletedEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.modulith.ApplicationModule
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@ApplicationModule(allowedDependencies = ["common"])
class RewardsModuleMetadata

@Service
class RewardsService {

    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    private fun registerRewards(orderId: Long) {
        logger.info("Register rewards for $orderId")
    }

    @EventListener(OrderCompletedEvent::class)
    @Transactional
    fun handleOrderCompletedEvent(event: OrderCompletedEvent) {
        logger.info("Handling $event")

        registerRewards(event.orderId)
    }
}
