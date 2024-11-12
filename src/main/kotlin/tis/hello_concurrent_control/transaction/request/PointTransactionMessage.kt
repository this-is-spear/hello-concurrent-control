package tis.hello_concurrent_control.transaction.request

import tis.hello_concurrent_control.domain.AccountSequence
import tis.hello_concurrent_control.domain.Point
import tis.hello_concurrent_control.transaction.PointTransactionId

class PointTransactionMessage(
    val sourceAccount: AccountSequence,
    val targetAccount: AccountSequence,
    val amount: Point,
    val transactionId: PointTransactionId,
) {
    override fun toString(): String {
        return "PointTransactionMessage(sourceAccount=$sourceAccount, targetAccount=$targetAccount, amount=$amount, transactionId=$transactionId)"
    }
}
