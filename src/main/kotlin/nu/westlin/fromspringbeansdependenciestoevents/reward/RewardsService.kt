package nu.westlin.fromspringbeansdependenciestoevents.reward

import nu.westlin.fromspringbeansdependenciestoevents.domain.Order
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class RewardsService {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    fun registerRewards(order: Order) {
        logger.info("Registered rewards for order ${order.id}")
    }

}