package tis.hello_concurrent_control.config.internal

import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer
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
        setReplyTimeout(60000)
        setReplyAddress(replyQueue.name)
        setUseDirectReplyToContainer(false)
        messageConverter = myMessageConverter
    }

    @Bean
    fun replyQueue(): Queue = Queue("reply-queue")

    @Bean
    fun myMessageConverter(): MessageConverter = Jackson2JsonMessageConverter()

    @Bean
    fun replyMessageListenerContainer(
        connectionFactory: ConnectionFactory,
        rabbitTemplate: RabbitTemplate,
        replyQueue: Queue,
    ): SimpleMessageListenerContainer = SimpleMessageListenerContainer(connectionFactory).apply {
        setQueues(replyQueue)
        setMessageListener(rabbitTemplate)
    }
}
