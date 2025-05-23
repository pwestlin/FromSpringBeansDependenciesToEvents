package nu.westlin.fromspringbeansdependenciestoevents

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@SpringBootApplication
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}

data class Order(
    val id: Long,
    val data: String
)

data class OrderCompletedEvent(val order: Order)

@Service
class CompleteOrderService(
    private val orderRepository: OrderRepository,
    private val eventPublisher: ApplicationEventPublisher
) {

    @Transactional
    fun completeOrder(order: Order) {
        orderRepository.save(order)
        eventPublisher.publishEvent(OrderCompletedEvent(order))
    }
}

@Service
class OrderCompletedEventHandler(
    private val inventoryService: InventoryService,
    private val rewardsService: RewardsService,
    private val notificationsService: NotificationsService
) {

    @EventListener(OrderCompletedEvent::class)
    fun handle(event: OrderCompletedEvent) {
        println("Handling $event")
        val order = event.order
        inventoryService.updateStock(order)
        rewardsService.registerRewards(order)
        notificationsService.sendOrderConfirmation(order)
    }
}

@Service
class NotificationsService {
    fun sendOrderConfirmation(order: Order) {}
}

@Service
class InventoryService {
    fun updateStock(order: Order) {}
}

@Service
class RewardsService {
    fun registerRewards(order: Order) {}

}

@Repository
class OrderRepository {
    fun save(order: Order) {}
}
