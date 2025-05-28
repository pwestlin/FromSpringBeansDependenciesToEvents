package nu.westlin.fromspringbeansdependenciestoevents

import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.images.PullPolicy
import org.testcontainers.utility.DockerImageName
import org.testcontainers.utility.TestcontainersConfiguration

/**
 * Postgres Stage-Docker container för Testcontainers.
 */
object PostgresContainer {
    val instance: PostgreSQLContainer<out PostgreSQLContainer<*>> by lazy { startContainer() }

    private fun startContainer(): PostgreSQLContainer<out PostgreSQLContainer<*>> {
        val reuse = System.getProperty("containers.reuse").toBoolean()
        TestcontainersConfiguration.getInstance().updateUserConfig("testcontainers.reuse.enable", reuse.toString())

        return PostgreSQLContainer(
            DockerImageName.parse("docker.lmv.lm.se/taco/postgres:16").asCompatibleSubstituteFor("postgres")
        ).apply {
            withImagePullPolicy(PullPolicy.alwaysPull())
            withDatabaseName("ditax_stad")
            withUsername("skrutt")
            withPassword("skrutt")
            withReuse(reuse)
            waitingFor(Wait.forLogMessage(".*databassystemet är redo att ta emot anslutningar.*", 2))
            start()
        }
    }
}