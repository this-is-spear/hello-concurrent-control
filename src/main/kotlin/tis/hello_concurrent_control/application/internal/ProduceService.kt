package tis.hello_concurrent_control.application.internal

import com.fasterxml.jackson.databind.ObjectMapper
import org.redisson.api.RStreamReactive
import org.redisson.api.RedissonReactiveClient
import org.redisson.api.stream.StreamAddArgs
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import tis.hello_concurrent_control.application.PointTransactionMessage
import tis.hello_concurrent_control.domain.AccountSequence
import tis.hello_concurrent_control.domain.Point

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
    ): Mono<Void> {
        return Mono.just(PointTransactionMessage(sourceAccount, targetAccount, amount))
            .map { extractMessageData(it) }
            .flatMap { stream.add(StreamAddArgs.entries(it)) }
            .then()
    }

    private fun extractMessageData(result: PointTransactionMessage) = mutableMapOf<String, String>()
        .apply {
            put("result", objectMapper.writeValueAsString(result))
        }
}
