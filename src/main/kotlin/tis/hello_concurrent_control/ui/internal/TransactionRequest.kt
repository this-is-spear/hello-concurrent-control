package tis.hello_concurrent_control.ui.internal

class TransactionRequest(
    val sourceAccount: String,
    val targetAccount: String,
    val amount: Int,
) {
    override fun toString(): String {
        return "TransactionRequest(sourceAccount='$sourceAccount', targetAccount='$targetAccount', amount=$amount)"
    }
}
