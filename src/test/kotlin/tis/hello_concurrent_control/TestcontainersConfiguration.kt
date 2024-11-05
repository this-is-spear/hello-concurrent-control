package tis.hello_concurrent_control

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.springframework.test.context.DynamicPropertyRegistry
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.utility.DockerImageName

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {
    @Bean
    @ServiceConnection
    fun mysqlContainer(registry: DynamicPropertyRegistry): MySQLContainer<*> {
        return MySQLContainer(DockerImageName.parse("mysql:latest"))
            .withUsername("hello")
            .withPassword("world")
            .apply {
                registry.add("spring.datasource.url", this::getJdbcUrl)
                registry.add("spring.datasource.username", this::getUsername)
                registry.add("spring.datasource.password", this::getPassword)
            }
    }

    @Bean
    @ServiceConnection(name = "redis")
    fun redisContainer(): GenericContainer<*> {
        return GenericContainer(DockerImageName.parse("redis:latest"))
            .withExposedPorts(6379)
    }
}
