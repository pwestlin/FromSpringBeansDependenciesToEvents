package nu.westlin.fromspringbeansdependenciestoevents.common.eventhandling

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.modulith.events.CompletedEventPublications
import org.springframework.modulith.events.IncompleteEventPublications
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class CompletedEventPublicationsService(
    private val completedEventPublications: CompletedEventPublications
) {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    /**
     * Remove completed events.
     */
    @Scheduled(fixedDelay = 60_000, initialDelay = 15_000)
    fun removeCompletedEvents() {
        val noCompletedEvents = completedEventPublications.findAll().count()
        completedEventPublications.deletePublicationsOlderThan(Duration.ofMinutes(1))
        logger.debug("Deleted $noCompletedEvents completed events")
    }
}

/**
 * Handles incomplete events.
 */
@Service
class InCompleteEventPublicationsService(
    private val incompleteEventPublications: IncompleteEventPublications,
) {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    /**
     * Resubmits incomplete events.
     */
    //@Scheduled(fixedDelay = 60_000, initialDelay = 60_000)
    fun resubmitIncompleteEvents() {
        // TODO pwestlin: Make all event handlers idempotent
        incompleteEventPublications.resubmitIncompletePublicationsOlderThan(Duration.ofMinutes(1))
        logger.debug("Resubmitted incomplete events (if there were any)")
    }
}