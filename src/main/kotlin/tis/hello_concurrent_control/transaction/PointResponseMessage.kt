package tis.hello_concurrent_control.transaction

class PointResponseMessage(
    val responseStatus: PointTransactionStatus,
    val message: String,
)
