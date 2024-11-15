package tis.hello_concurrent_control.transaction

import tis.hello_concurrent_control.domain.AccountSequence
import tis.hello_concurrent_control.domain.Point

class PointRequestMessage(
    val sourceAccount: AccountSequence,
    val targetAccount: AccountSequence,
    val amount: Point,
) {
    override fun toString(): String {
        return "PointTransactionMessage(sourceAccount=$sourceAccount, targetAccount=$targetAccount, amount=$amount)"
    }
}
