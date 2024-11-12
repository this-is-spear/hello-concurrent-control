package tis.hello_concurrent_control.ui.internal

data class PointHistoriesResponse(
    val account: String,
    val histories: List<PointHistoryResponse>,
    val balance: Int,
)
