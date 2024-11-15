package tis.hello_concurrent_control.transaction.reply

import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Service
import tis.hello_concurrent_control.application.internal.IssuerService
import tis.hello_concurrent_control.application.internal.TransactionService
import tis.hello_concurrent_control.transaction.PointRequestMessage
import tis.hello_concurrent_control.transaction.PointResponseMessage
import tis.hello_concurrent_control.transaction.PointTransactionStatus

@Service
class ReceiveAndRequestService(
    private val transactionService: TransactionService,
    private val issuerService: IssuerService,
) {
    @RabbitListener(queues = ["request-queue"])
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