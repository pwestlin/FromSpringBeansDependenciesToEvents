package nu.westlin.fromspringbeansdependenciestoevents.order

import nu.westlin.fromspringbeansdependenciestoevents.domain.Order
import org.intellij.lang.annotations.Language
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.stereotype.Repository

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