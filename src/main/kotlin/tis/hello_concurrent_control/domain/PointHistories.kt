package tis.hello_concurrent_control.domain

class PointHistories(
    val account: AccountSequence,
    val histories: List<PointTransaction>,
) {
    val balance: Point
        get() = histories.fold(Point()) { acc, transaction ->
            when (transaction.targetAccountSequence == account) {
                true -> acc + transaction.amount
                false -> acc - transaction.amount
            }
        }
}
