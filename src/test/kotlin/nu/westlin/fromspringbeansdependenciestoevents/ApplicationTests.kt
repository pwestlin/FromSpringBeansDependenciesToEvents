package nu.westlin.fromspringbeansdependenciestoevents

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import

@SpringBootTest
@Import(TestPostgresContainerConfig::class)
class ApplicationTests {

    @Test
    fun contextLoads() {
        // Foo
    }
}

