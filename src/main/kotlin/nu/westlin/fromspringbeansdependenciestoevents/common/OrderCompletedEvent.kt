package nu.westlin.fromspringbeansdependenciestoevents.common

data class OrderCompletedEvent(
    val orderId: Long,
    val userId: Long
)