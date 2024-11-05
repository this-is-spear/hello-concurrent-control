package tis.hello_concurrent_control

import org.junit.jupiter.api.Test
import org.springframework.modulith.core.ApplicationModules
import org.springframework.modulith.docs.Documenter


class HelloConcurrentControlApplicationModuleTests {
    @Test
    fun verifyModularity() {
        val modules = ApplicationModules.of(HelloConcurrentControlApplication::class.java)
        modules.verify()
    }

    /**
     * build/spring-modulith-docs contains the generated documentation.
     */
    @Test
    fun createModuleDocumentation() {
        val modules = ApplicationModules.of(HelloConcurrentControlApplication::class.java)
        Documenter(modules)
            .writeDocumentation()
            .writeIndividualModulesAsPlantUml()
    }
}
