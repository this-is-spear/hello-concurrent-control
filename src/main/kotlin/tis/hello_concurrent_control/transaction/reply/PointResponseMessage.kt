package tis.hello_concurrent_control.transaction.reply

import tis.hello_concurrent_control.transaction.PointTransactionId

class PointResponseMessage(
    val pointTransactionId: PointTransactionId,
    val responseStatus: PointTransactionStatus,
    val message: String,
)
