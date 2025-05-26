package nu.westlin.fromspringbeansdependenciestoevents

import org.junit.jupiter.api.Test
import org.springframework.modulith.core.ApplicationModules

class ModulesTests {

    private val modules = ApplicationModules.of(Application::class.java)

    @Test
    fun `print modules`() {
        modules.forEach { println(it) }
    }
    
    @Test
    fun `verify modules`() {
        modules.verify()
    }
}