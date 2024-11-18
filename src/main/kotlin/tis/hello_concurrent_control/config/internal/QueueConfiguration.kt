package tis.hello_concurrent_control.config.internal

import org.springframework.amqp.core.Queue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import tis.hello_concurrent_control.config.QUEUE_1
import tis.hello_concurrent_control.config.QUEUE_2
import tis.hello_concurrent_control.config.QUEUE_3
import tis.hello_concurrent_control.config.REPLY_QUEUE_1
import tis.hello_concurrent_control.config.REPLY_QUEUE_2
import tis.hello_concurrent_control.config.REPLY_QUEUE_3

@Configuration
class QueueConfiguration {
    @Bean
    fun queue1() = Queue(QUEUE_1)

    @Bean
    fun queue2() = Queue(QUEUE_2)

    @Bean
    fun queue3() = Queue(QUEUE_3)

    @Bean
    fun replyQueue1() = Queue(REPLY_QUEUE_1)

    @Bean
    fun replyQueue2() = Queue(REPLY_QUEUE_2)

    @Bean
    fun replyQueue3() = Queue(REPLY_QUEUE_3)
}
