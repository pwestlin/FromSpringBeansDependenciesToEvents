package nu.westlin.fromspringbeansdependenciestoevents.order

import nu.westlin.fromspringbeansdependenciestoevents.domain.Order
import org.springframework.stereotype.Repository

@Repository
class OrderRepository {
    fun save(order: Order) {}
}