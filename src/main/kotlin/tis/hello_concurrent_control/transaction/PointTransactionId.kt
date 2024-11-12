package tis.hello_concurrent_control.transaction

import java.util.UUID

data class PointTransactionId(
    val id: String = UUID.randomUUID().toString(),
) {
    override fun toString(): String {
        return id
    }
}
