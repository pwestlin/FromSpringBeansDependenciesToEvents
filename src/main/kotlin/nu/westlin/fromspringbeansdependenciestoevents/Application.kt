package nu.westlin.fromspringbeansdependenciestoevents

import nu.westlin.fromspringbeansdependenciestoevents.order.CompleteOrderService
import nu.westlin.fromspringbeansdependenciestoevents.order.Order
import org.springframework.beans.factory.getBean
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args).getBean<CompleteOrderService>().completeOrder(
        order =
            Order(
                id = 2156,
                data = "repudiare"
            ), userId = 421235
    )
}

