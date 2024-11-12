package tis.hello_concurrent_control.transaction.request

import com.fasterxml.jackson.databind.ObjectMapper
import org.redisson.api.RStreamReactive
import org.redisson.api.RedissonReactiveClient
import org.redisson.api.stream.StreamReadGroupArgs
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.scheduler.Schedulers
import reactor.kotlin.core.publisher.toFlux
import tis.hello_concurrent_control.application.internal.IssuerService
import tis.hello_concurrent_control.application.internal.TransactionService
import tis.hello_concurrent_control.transaction.reply.PointResponseMessage
import tis.hello_concurrent_control.transaction.reply.PointTransactionStatus

const val CONSUMER = "point-transaction-consumer"
const val GROUP = "point-transaction-group"
const val STREAM = "point-transaction"

@Service
class ConsumerService(
    private val transactionService: TransactionService,
    private val issuerService: IssuerService,
    private val objectMapper: ObjectMapper,
    redissonReactiveClient: RedissonReactiveClient,
) {
    val stream: RStreamReactive<String, String> = redissonReactiveClient.getStream(STREAM)

    fun consumeTransactionRequest(): Flux<PointResponseMessage> {
        return stream.readGroup(GROUP, CONSUMER, StreamReadGroupArgs.neverDelivered().count(1))
            .flatMapMany { it.entries.toFlux() }
            .publishOn(Schedulers.boundedElastic())
            .mapNotNull {
                val pointTransactionMessage =
                    objectMapper.readValue(it.value["result"], PointTransactionMessage::class.java)
                val sourceAccount = pointTransactionMessage.sourceAccount
                val targetAccount = pointTransactionMessage.targetAccount
                val amount = pointTransactionMessage.amount
                try {
                    if (!issuerService.isPointIssuer(sourceAccount)) {
                        transactionService.transaction(sourceAccount, targetAccount, amount)
                    } else {
                        issuerService.issue(sourceAccount, targetAccount, amount)
                    }
                    PointResponseMessage(
                        pointTransactionMessage.transactionId,
                        PointTransactionStatus.SUCCESS,
                        "Success"
                    )
                } catch (e: Exception) {
                    PointResponseMessage(
                        pointTransactionMessage.transactionId,
                        PointTransactionStatus.FAIL,
                        e.message ?: "Fail"
                    )
                } finally {
                    stream.ack(GROUP, it.key).subscribe()
                }
            }
    }
}
