package nu.westlin.fromspringbeansdependenciestoevents.order

import nu.westlin.fromspringbeansdependenciestoevents.common.domain.Order
import nu.westlin.fromspringbeansdependenciestoevents.common.event.OrderCompletedEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CompleteOrderService(
    private val orderRepository: OrderRepository,
    private val eventPublisher: ApplicationEventPublisher
) {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    @Transactional
    fun completeOrder(order: Order) {
        orderRepository.save(order)
        eventPublisher.publishEvent(OrderCompletedEvent(order))

        logger.info("Order completed successfully")
    }
}

@Repository
class OrderRepository {

    @Transactional
    fun save(order: Order) {}
}