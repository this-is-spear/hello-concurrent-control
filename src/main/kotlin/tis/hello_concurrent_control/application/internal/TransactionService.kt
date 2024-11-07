package tis.hello_concurrent_control.application.internal

import org.springframework.stereotype.Service
import tis.hello_concurrent_control.concurrent.PointTransactionLock
import tis.hello_concurrent_control.domain.AccountSequence
import tis.hello_concurrent_control.domain.Point
import tis.hello_concurrent_control.domain.PointTransaction
import tis.hello_concurrent_control.repository.PointTransactionRepository

@Service
class TransactionService(
    private val historyService: HistoryService,
    private val pointTransactionRepository: PointTransactionRepository,
) {
    @PointTransactionLock(source = "#sourceAccount.sequence", target = "#targetAccount.sequence")
    fun transaction(sourceAccount: AccountSequence, targetAccount: AccountSequence, amount: Point) {
        val pointHistories = historyService.findAccountHistories(sourceAccount)
        if (pointHistories.balance < amount) {
            throw IllegalArgumentException("잔액이 부족합니다.")
        }

        val pointTransaction = PointTransaction(sourceAccount, targetAccount, amount)
        pointTransactionRepository.save(pointTransaction)
    }
}
