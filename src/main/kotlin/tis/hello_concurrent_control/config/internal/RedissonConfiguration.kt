package tis.hello_concurrent_control.config.internal

import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RedissonConfiguration {
    @Bean
    fun redissonClient(): RedissonClient {
        val config = Config().apply {
            useSingleServer().address = "redis://localhost:6379"
        }
        return Redisson.create(config)
    }
}
