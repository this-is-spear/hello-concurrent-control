package tis.hello_concurrent_control.config.internal

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.Exchange
import org.springframework.amqp.core.ExchangeBuilder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import tis.hello_concurrent_control.config.EXCHANGE_1
import tis.hello_concurrent_control.config.EXCHANGE_2
import tis.hello_concurrent_control.config.EXCHANGE_3
import tis.hello_concurrent_control.config.ROUTING_KEY_1
import tis.hello_concurrent_control.config.ROUTING_KEY_2
import tis.hello_concurrent_control.config.ROUTING_KEY_3

@Configuration
class RabbitShardConfiguration {
    @Bean
    fun exchange1(): DirectExchange = DirectExchange(EXCHANGE_1)

    @Bean
    fun exchange2(): DirectExchange = DirectExchange(EXCHANGE_2)

    @Bean
    fun exchange3(): DirectExchange = DirectExchange(EXCHANGE_3)

    @Bean
    fun binding1(
        queue1: Queue,
        exchange1: Exchange
    ): Binding = BindingBuilder.bind(queue1).to(exchange1).with(ROUTING_KEY_1).noargs()

    @Bean
    fun binding2(
        queue2: Queue,
        exchange2: Exchange
    ): Binding = BindingBuilder.bind(queue2).to(exchange2).with(ROUTING_KEY_2).noargs()

    @Bean
    fun binding3(
        queue3: Queue,
        exchange3: Exchange
    ): Binding = BindingBuilder.bind(queue3).to(exchange3).with(ROUTING_KEY_3).noargs()

    @Bean
    fun replyContainer1(
        connectionFactory: ConnectionFactory,
        rabbitTemplate: RabbitTemplate,
        replyQueue1: Queue,
    ): SimpleMessageListenerContainer = SimpleMessageListenerContainer(connectionFactory).apply {
        setQueues(replyQueue1)
//        setMessageListener(rabbitTemplate)
    }

    @Bean
    fun replyContainer2(
        connectionFactory: ConnectionFactory,
        rabbitTemplate: RabbitTemplate,
        replyQueue2: Queue,
    ): SimpleMessageListenerContainer = SimpleMessageListenerContainer(connectionFactory).apply {
        setQueues(replyQueue2)
//        setMessageListener(rabbitTemplate)
    }

    @Bean
    fun replyContainer3(
        connectionFactory: ConnectionFactory,
        rabbitTemplate: RabbitTemplate,
        replyQueue3: Queue,
    ): SimpleMessageListenerContainer = SimpleMessageListenerContainer(connectionFactory).apply {
        setQueues(replyQueue3)
//        setMessageListener(rabbitTemplate)
    }
}
