package nu.westlin.fromspringbeansdependenciestoevents

import com.ninjasquad.springmockk.MockkBean
import io.mockk.justRun
import io.mockk.verify
import nu.westlin.fromspringbeansdependenciestoevents.domain.Order
import nu.westlin.fromspringbeansdependenciestoevents.inventory.InventoryService
import nu.westlin.fromspringbeansdependenciestoevents.notification.NotificationsService
import nu.westlin.fromspringbeansdependenciestoevents.order.CompleteOrderService
import nu.westlin.fromspringbeansdependenciestoevents.reward.RewardsService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class CompleteOrderServiceTest(
    @Autowired private val service: CompleteOrderService
) {
    @MockkBean
    private lateinit var inventoryService: InventoryService

    @MockkBean
    private lateinit var rewardsService: RewardsService

    @MockkBean
    private lateinit var notificationsService: NotificationsService

    @Test
    fun `complete order`() {
        val order = Order(id = 42, data = "foo")
        justRun { inventoryService.updateStock(order) }
        justRun { rewardsService.registerRewards(order) }
        justRun { notificationsService.sendOrderConfirmation(order) }

        service.completeOrder(order)
        
        verify {
            inventoryService.updateStock(order)
            rewardsService.registerRewards(order)
            notificationsService.sendOrderConfirmation(order)
        }
    }
}