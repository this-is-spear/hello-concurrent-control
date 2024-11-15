package tis.hello_concurrent_control.domain

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tis.hello_concurrent_control.application.internal.AggregateHistoryService
import tis.hello_concurrent_control.repository.PointTransactionRepository

@Service
class TransactionService(
    private val aggregateHistoryService: AggregateHistoryService,
    private val pointTransactionRepository: PointTransactionRepository,
) {
    @Transactional
    fun transaction(sourceAccount: AccountSequence, targetAccount: AccountSequence, amount: Point) {
        val pointHistories = aggregateHistoryService.findAccountHistories(sourceAccount)
        if (pointHistories.balance < amount) {
            throw IllegalArgumentException("${sourceAccount.sequence} -> ${targetAccount.sequence} 잔액이 부족합니다. ${pointHistories.balance} < $amount")
        }

        val pointTransaction = PointTransaction(sourceAccount, targetAccount, amount)
        pointTransactionRepository.save(pointTransaction)
    }
}
