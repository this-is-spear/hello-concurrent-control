package tis.hello_concurrent_control.application.internal

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tis.hello_concurrent_control.domain.AccountSequence
import tis.hello_concurrent_control.domain.PointHistories
import tis.hello_concurrent_control.repository.PointTransactionRepository

private const val COUNT = 2

@Service
class AggregateHistoryService(
    private val pointTransactionRepository: PointTransactionRepository,
) {
    @Transactional(readOnly = true)
    fun findAccountHistories(account: AccountSequence): PointHistories {
        val histories = pointTransactionRepository.findBySourceAccountSequence(account) +
                pointTransactionRepository.findByTargetAccountSequence(account)
        return PointHistories(account, histories.sortedBy { it.createdAt })
    }
}
