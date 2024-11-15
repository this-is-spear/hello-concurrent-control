package tis.hello_concurrent_control.config.internal

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Exchange
import org.springframework.amqp.core.ExchangeBuilder
import org.springframework.amqp.core.Queue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitShardConfiguration {
    @Bean
    fun shardExchange(): Exchange {
        return ExchangeBuilder.fanoutExchange("shard-exchange")
            .durable(true)
            .build()
    }

    @Bean
    fun shardBindings(
        shardExchange: Exchange,
        shardQueues: List<Queue>
    ): List<Binding> {
        return shardQueues.map {
            BindingBuilder.bind(it)
                .to(shardExchange)
                .with("shard" + it.name)
                .noargs()
        }
    }
}
