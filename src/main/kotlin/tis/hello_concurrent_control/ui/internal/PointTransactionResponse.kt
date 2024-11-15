package tis.hello_concurrent_control.ui.internal

import tis.hello_concurrent_control.transaction.PointTransactionStatus

class PointTransactionResponse(
    val status: PointTransactionStatus,
    val message: String
)
