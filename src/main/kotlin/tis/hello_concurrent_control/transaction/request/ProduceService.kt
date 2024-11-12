package tis.hello_concurrent_control.transaction.request

import com.fasterxml.jackson.databind.ObjectMapper
import java.util.UUID
import org.redisson.api.RStreamReactive
import org.redisson.api.RedissonReactiveClient
import org.redisson.api.stream.StreamAddArgs
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import tis.hello_concurrent_control.domain.AccountSequence
import tis.hello_concurrent_control.domain.Point
import tis.hello_concurrent_control.transaction.PointTransactionId

@Service
class ProduceService(
    private val objectMapper: ObjectMapper,
    redissonReactiveClient: RedissonReactiveClient,
) {
    val stream: RStreamReactive<String, String> = redissonReactiveClient.getStream(STREAM)

    fun produceTransactionRequest(
        sourceAccount: AccountSequence,
        targetAccount: AccountSequence,
        amount: Point,
    ): Mono<PointTransactionId> {
        val message = PointTransactionMessage(sourceAccount, targetAccount, amount, PointTransactionId())
        return Mono.just(message)
            .map { extractMessageData(it) }
            .flatMap { stream.add(StreamAddArgs.entries(it)) }
            .map { message.transactionId }
    }

    private fun extractMessageData(result: PointTransactionMessage) = mutableMapOf<String, String>()
        .apply {
            put("result", objectMapper.writeValueAsString(result))
        }
}
