package nu.westlin.fromspringbeansdependenciestoevents

import org.springframework.boot.test.context.TestConfiguration

// Utan denna fungerar testerna i IntelliJ men inte i Gradle...?
@TestConfiguration
class TestPostgresContainerConfig {

    companion object {
        @Suppress("unused")
        val postgreSQLContainer = PostgresContainer.instance.also { postgreSQLContainer ->
            System.setProperty("spring.datasource.url", postgreSQLContainer.getJdbcUrl())
            System.setProperty("spring.datasource.username", postgreSQLContainer.username)
            System.setProperty("spring.datasource.password", postgreSQLContainer.password)
        }
    }
}