package tis.hello_concurrent_control.application.internal

import org.springframework.stereotype.Service
import tis.hello_concurrent_control.domain.AccountSequence
import tis.hello_concurrent_control.domain.PointHistories
import tis.hello_concurrent_control.repository.PointTransactionRepository

@Service
class HistoryService(
    private val pointTransactionRepository: PointTransactionRepository,
) {
    fun findAccountHistories(account: AccountSequence) = PointHistories(
        account,
        pointTransactionRepository.findBySourceAccountSequence(account) +
                pointTransactionRepository.findByTargetAccountSequence(account),
    )
}
