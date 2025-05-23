package nu.westlin.fromspringbeansdependenciestoevents

import nu.westlin.fromspringbeansdependenciestoevents.domain.Order
import nu.westlin.fromspringbeansdependenciestoevents.order.CompleteOrderService
import org.springframework.beans.factory.getBean
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args).getBean<CompleteOrderService>().completeOrder(Order(
        id = 3096,
        data = "partiendo"
    ))
}

