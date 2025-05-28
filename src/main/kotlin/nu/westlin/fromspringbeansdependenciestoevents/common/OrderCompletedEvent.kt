package nu.westlin.fromspringbeansdependenciestoevents.common

import org.springframework.modulith.ApplicationModule

@ApplicationModule(allowedDependencies = [""])
class InventoryModuleMetadata

data class OrderCompletedEvent(
    val orderId: Long,
    val userId: Long
)