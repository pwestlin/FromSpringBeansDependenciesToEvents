package nu.westlin.fromspringbeansdependenciestoevents

import nu.westlin.fromspringbeansdependenciestoevents.order.CompleteOrderService
import nu.westlin.fromspringbeansdependenciestoevents.order.Order
import org.springframework.beans.factory.getBean
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.modulith.Modulith
import kotlin.math.abs
import kotlin.random.Random

@SpringBootApplication
@Modulith
class Application

fun main(args: Array<String>) {
    val ctx = runApplication<Application>(*args)
/*
    ctx.getBean<CompleteOrderService>().completeOrder(
        order =
            Order(
                id = abs(Random.nextLong()),
                data = "färdtjänst"
            ), userId = abs(Random.nextLong())
    )
*/
}

