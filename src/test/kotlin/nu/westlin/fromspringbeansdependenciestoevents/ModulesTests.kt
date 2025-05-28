package nu.westlin.fromspringbeansdependenciestoevents

import org.junit.jupiter.api.Test
import org.springframework.modulith.core.ApplicationModules
import org.springframework.modulith.docs.Documenter

class ModulesTests {

    private val modules = ApplicationModules.of(Application::class.java)

    @Test
    fun `print modules`() {
        modules.forEach { println(it) }
    }

    @Test
    fun `write documentation snippets`() {
        Documenter(modules)
            .writeDocumentation()
    }

    @Test
    fun `verify modules`() {
        modules.verify()
    }
}