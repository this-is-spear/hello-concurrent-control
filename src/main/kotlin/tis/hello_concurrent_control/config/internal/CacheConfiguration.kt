package tis.hello_concurrent_control.config.internal

import org.redisson.api.RedissonClient
import org.redisson.spring.cache.CacheConfig
import org.redisson.spring.cache.RedissonSpringCacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@EnableCaching
@Configuration
class CacheConfiguration {
    @Bean
    fun cacheManager(redissonClient: RedissonClient) = RedissonSpringCacheManager(redissonClient).apply {
        val config = HashMap<String, CacheConfig>()

        config["sourceAccountSequence"] = CacheConfig().apply {
            ttl = 30 * 60 * 1000 // 30분
            maxIdleTime = 10 * 60 * 1000 // 10분
        }

        config["targetAccountSequence"] = CacheConfig().apply {
            ttl = 30 * 60 * 1000 // 30분
            maxIdleTime = 10 * 60 * 1000 // 10분
        }

        setConfig(config)
    }
}
