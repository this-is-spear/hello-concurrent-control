package tis.hello_concurrent_control

import org.junit.jupiter.api.Test
import org.springframework.modulith.core.ApplicationModules


class HelloConcurrentControlApplicationModuleTests {
    @Test
    fun verifyModularity() {
        val modules = ApplicationModules.of(HelloConcurrentControlApplication::class.java)
        modules.verify()
    }
}
