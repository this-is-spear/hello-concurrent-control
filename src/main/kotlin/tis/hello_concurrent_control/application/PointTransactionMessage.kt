package tis.hello_concurrent_control.application

import tis.hello_concurrent_control.domain.AccountSequence
import tis.hello_concurrent_control.domain.Point

class PointTransactionMessage(
    val sourceAccount: AccountSequence,
    val targetAccount: AccountSequence,
    val amount: Point,
) {
    override fun toString(): String {
        return "PointTransactionMessage(sourceAccount=$sourceAccount, targetAccount=$targetAccount, amount=$amount)"
    }
}
