package tis.hello_concurrent_control.application

import tis.hello_concurrent_control.domain.PointTransactionStatus

class PointTransactionResponse(
    val status: PointTransactionStatus,
    val message: String
)
