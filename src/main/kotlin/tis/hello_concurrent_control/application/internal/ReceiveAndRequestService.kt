package tis.hello_concurrent_control.application.internal

import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Service
import tis.hello_concurrent_control.domain.IssuerService
import tis.hello_concurrent_control.domain.PointTransactionStatus
import tis.hello_concurrent_control.domain.TransactionService

@Service
class ReceiveAndRequestService(
    private val transactionService: TransactionService,
    private val issuerService: IssuerService,
) {
    @RabbitListener(queues = ["queue-1", "queue-2", "queue-3"])
    @SendTo("reply-queue")
    fun subscribe(pointRequestMessage: PointRequestMessage): PointResponseMessage {
        return try {
            val sourceAccount = pointRequestMessage.sourceAccount
            val targetAccount = pointRequestMessage.targetAccount
            val amount = pointRequestMessage.amount

            if (!issuerService.isPointIssuer(sourceAccount)) {
                transactionService.transaction(sourceAccount, targetAccount, amount)
            } else {
                issuerService.issue(sourceAccount, targetAccount, amount)
            }
            PointResponseMessage(
                PointTransactionStatus.SUCCESS,
                "Success"
            )
        } catch (e: Exception) {
            PointResponseMessage(
                PointTransactionStatus.FAIL,
                e.message ?: "Fail"
            )
        }
    }
}
