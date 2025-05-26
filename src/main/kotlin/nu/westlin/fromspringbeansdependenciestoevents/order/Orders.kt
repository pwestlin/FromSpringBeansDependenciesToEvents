package nu.westlin.fromspringbeansdependenciestoevents.order

import nu.westlin.fromspringbeansdependenciestoevents.common.OrderCompletedEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.jdbc.core.simple.JdbcClient
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
    fun completeOrder(order: Order, userId: Long) {
        orderRepository.save(order)
        eventPublisher.publishEvent(OrderCompletedEvent(orderId = order.id, userId = userId))

        logger.info("Order completed successfully")
    }
}

@Repository
class OrderRepository(
    private val jdbcClient: JdbcClient
) {
    fun save(order: Order) {
        val rows = jdbcClient
            .sql("insert into orders(id,data) values (:id, :data)")
            .param("id", order.id)
            .param("data", order.data)
            .update()
        check(rows == 1) { "1 row should've affected but $rows were" }
    }

    fun getOrder(id: Long): Order? {
        return jdbcClient
            .sql("select * from orders where id = :id")
            .param("id", id)
            .query { rs, rowNum ->
                Order(id = rs.getLong("id"), data = rs.getString("data"))
            }
            .optional()
            .orElse(null)
    }
}
