package tis.hello_concurrent_control

import org.junit.jupiter.api.Test
import org.springframework.modulith.core.ApplicationModules
import org.springframework.modulith.docs.Documenter


class HelloConcurrentControlApplicationModuleTests {
    private val modules = ApplicationModules.of(HelloConcurrentControlApplication::class.java)

    @Test
    fun verifyModularity() {
        modules.verify()
    }

    /**
     * build/spring-modulith-docs contains the generated documentation.
     */
    @Test
    fun createModuleDocumentation() {
        Documenter(modules)
            .writeDocumentation()
            .writeIndividualModulesAsPlantUml()
    }
}
