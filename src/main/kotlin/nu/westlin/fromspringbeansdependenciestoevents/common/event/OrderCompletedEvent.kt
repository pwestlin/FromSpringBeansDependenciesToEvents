package nu.westlin.fromspringbeansdependenciestoevents.common.event

import nu.westlin.fromspringbeansdependenciestoevents.common.domain.Order

data class OrderCompletedEvent(val order: Order)