package tis.hello_concurrent_control.application.internal

import com.fasterxml.jackson.databind.ObjectMapper
import org.redisson.api.RStream
import org.redisson.api.RedissonClient
import org.redisson.api.stream.StreamAddArgs
import org.springframework.stereotype.Service
import tis.hello_concurrent_control.application.PointTransactionMessage
import tis.hello_concurrent_control.domain.AccountSequence
import tis.hello_concurrent_control.domain.Point

@Service
class ProduceService(
    private val redissonClient: RedissonClient,
    private val objectMapper: ObjectMapper,
) {
    fun produceTransactionRequest(
        sourceAccount: AccountSequence,
        targetAccount: AccountSequence,
        amount: Point
    ): String {
        val message = PointTransactionMessage(sourceAccount, targetAccount, amount)
        val messageData: Map<String, String> = extractMessageData(message)
        val stream: RStream<String, String> = redissonClient.getStream(STREAM)
        val messageId = stream.add(StreamAddArgs.entries(messageData))
        return "${messageId.id0}-${messageId.id1}"
    }

    private fun extractMessageData(result: PointTransactionMessage) = mutableMapOf<String, String>()
        .apply {
            put("result", objectMapper.writeValueAsString(result))
        }
}
