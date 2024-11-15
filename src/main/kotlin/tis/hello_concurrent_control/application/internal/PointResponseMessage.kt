package tis.hello_concurrent_control.application.internal

import tis.hello_concurrent_control.domain.PointTransactionStatus

class PointResponseMessage(
    val responseStatus: PointTransactionStatus,
    val message: String,
)
