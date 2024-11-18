package tis.hello_concurrent_control.config.internal

import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.AsyncRabbitTemplate
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.rabbit.listener.DirectReplyToMessageListenerContainer
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.amqp.support.converter.MessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * rabbitmq 설정
 *
 * @see <a href="https://docs.spring.io/spring-amqp/reference/amqp/request-reply.html">Request-Reply</a>
 */
@Configuration
class RabbitmqConfiguration {
    @Bean
    fun rabbitTemplate(
        connectionFactory: ConnectionFactory,
        replyQueue: Queue,
        myMessageConverter: MessageConverter,
    ): RabbitTemplate = RabbitTemplate(connectionFactory).apply {
        setReplyAddress(replyQueue.name)
        setUseDirectReplyToContainer(false)
        messageConverter = myMessageConverter
    }

    @Bean
    fun queue1(): Queue = Queue("queue-1")

    @Bean
    fun queue2(): Queue = Queue("queue-2")

    @Bean
    fun queue3(): Queue = Queue("queue-3")

    @Bean
    fun replyQueue(): Queue = Queue("reply-queue")

    @Bean
    fun replyMessageListenerContainer(
        connectionFactory: ConnectionFactory,
        rabbitTemplate: RabbitTemplate,
        replyQueue: Queue,
    ): DirectReplyToMessageListenerContainer = DirectReplyToMessageListenerContainer(connectionFactory).apply {
        setQueues(replyQueue)
        setExclusive(true)
        setIdleEventInterval(10_000)
        setMessageListener(rabbitTemplate)
    }

    @Bean
    fun asyncRabbitTemplate(
        rabbitTemplate: RabbitTemplate,
    ): AsyncRabbitTemplate = AsyncRabbitTemplate(rabbitTemplate)

    @Bean
    fun myMessageConverter(): MessageConverter = Jackson2JsonMessageConverter()
}
