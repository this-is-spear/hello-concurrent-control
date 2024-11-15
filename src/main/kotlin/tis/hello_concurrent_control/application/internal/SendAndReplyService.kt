package tis.hello_concurrent_control.application.internal

import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import tis.hello_concurrent_control.domain.AccountSequence
import tis.hello_concurrent_control.domain.Point

@Service
class SendAndReplyService(
    private val rabbitTemplate: RabbitTemplate,
) {
    fun produceTransactionRequest(
        sourceAccount: AccountSequence,
        targetAccount: AccountSequence,
        amount: Point,
    ): Mono<PointResponseMessage> {
        return Mono.just(PointRequestMessage(sourceAccount, targetAccount, amount))
            .map {
                val message = rabbitTemplate.convertSendAndReceive("request-queue", it)
                check(message != null) { "Failed to send message" }
                check(message is PointResponseMessage) { "Failed to send message" }
                message
            }
    }
}
