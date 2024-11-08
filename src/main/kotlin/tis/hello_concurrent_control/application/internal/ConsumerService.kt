package tis.hello_concurrent_control.application.internal

import com.fasterxml.jackson.databind.ObjectMapper
import org.redisson.api.RStream
import org.redisson.api.RedissonClient
import org.redisson.api.stream.StreamReadGroupArgs
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import tis.hello_concurrent_control.application.PointTransactionMessage

const val CONSUMER = "point-transaction-consumer"
const val GROUP = "point-transaction-group"
const val STREAM = "point-transaction"

@Service
class ConsumerService(
    private val transactionService: TransactionService,
    private val issuerService: IssuerService,
    private val redissonClient: RedissonClient,
    private val objectMapper: ObjectMapper,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun consumeTransactionRequest() {
        val stream: RStream<String, String> = redissonClient.getStream(STREAM)
        val message = stream.readGroup(
            GROUP,
            CONSUMER,
            StreamReadGroupArgs.neverDelivered().count(1)
        ).entries.stream()
            .findFirst()
            .orElse(null)

        if (message != null) {
            val pointTransactionMessage =
                objectMapper.readValue(message.value["result"], PointTransactionMessage::class.java)
            val sourceAccount = pointTransactionMessage.sourceAccount
            val targetAccount = pointTransactionMessage.targetAccount
            val amount = pointTransactionMessage.amount
            if (!issuerService.isPointIssuer(sourceAccount)) {
                transactionService.transaction(sourceAccount, targetAccount, amount)
            } else {
                issuerService.issue(sourceAccount, targetAccount, amount)
            }
            stream.ack(GROUP, message.key)
        }
    }
}
