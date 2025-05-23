package nu.westlin.fromspringbeansdependenciestoevents

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.event.ApplicationEvents
import org.springframework.test.context.event.RecordApplicationEvents
import java.util.stream.Collectors

@SpringBootTest(classes = [CompleteOrderService::class, OrderRepository::class])
@RecordApplicationEvents
class CompleteOrderServiceTest(
    @Autowired private val service: CompleteOrderService,
) {

    @Test
    fun `complete order`(applicationEvents: ApplicationEvents) {
        val order = Order(id = 42, data = "foo")
        service.completeOrder(order)

        assertThat(applicationEvents.list<OrderCompletedEvent>()).containsExactly(OrderCompletedEvent(order = order))
    }
}

inline fun <reified T:Any> ApplicationEvents.list(): List<T> = this.stream(T::class.java).collect(Collectors.toList())