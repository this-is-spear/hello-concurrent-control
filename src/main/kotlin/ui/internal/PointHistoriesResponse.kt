package ui.internal

data class PointHistoriesResponse(
    val account: String,
    val histories: List<PointTransactionResponse>,
    val balance: Int,
)
