package tis.hello_concurrent_control.config

import org.springframework.amqp.rabbit.AsyncRabbitTemplate
import org.springframework.stereotype.Component
import tis.hello_concurrent_control.application.internal.PointRequestMessage
import tis.hello_concurrent_control.application.internal.PointResponseMessage
import java.util.concurrent.CompletableFuture
import kotlin.math.abs


@Component
class ShardedMessageService(
    private val asyncRabbitTemplate: AsyncRabbitTemplate,
    private val queueNames: List<String>,
) {

    fun sendWithConsistentHashing(key: String, message: PointRequestMessage): CompletableFuture<PointResponseMessage> {
        val queueParameterising = queueNames.filter { it.startsWith("reply-") }
        val shardIndex = abs(key.hashCode() % queueParameterising.size)
        val queueName = queueParameterising[shardIndex]
        return asyncRabbitTemplate.convertSendAndReceive(queueName, message)
    }
}
