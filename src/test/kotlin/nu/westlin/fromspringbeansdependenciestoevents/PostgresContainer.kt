package nu.westlin.fromspringbeansdependenciestoevents

import org.testcontainers.containers.PostgreSQLContainer
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
            DockerImageName.parse("postgres:16-alpine")
        ).apply {
            withImagePullPolicy(PullPolicy.alwaysPull())
            withDatabaseName("mydb")
            withUsername("myuser")
            withPassword("mypassword")
            withReuse(reuse)
            start()
        }
    }
}