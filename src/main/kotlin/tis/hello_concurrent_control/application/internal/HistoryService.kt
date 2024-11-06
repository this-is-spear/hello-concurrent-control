package tis.hello_concurrent_control.application.internal

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tis.hello_concurrent_control.domain.AccountSequence
import tis.hello_concurrent_control.domain.PointHistories
import tis.hello_concurrent_control.domain.PointTransaction
import tis.hello_concurrent_control.repository.PointTransactionRepository

private const val COUNT = 2

@Service
class HistoryService(
    private val pointTransactionRepository: PointTransactionRepository,
) {
    @Transactional(readOnly = true)
    fun findAccountHistories(account: AccountSequence): PointHistories {
        val histories = findSourceHistories(account) + findTargetHistories(account)
        return PointHistories(account, histories.sortedBy { it.createdAt })
    }

    private fun findSourceHistories(account: AccountSequence): List<PointTransaction> {
        val historyCount = pointTransactionRepository.countBySourceAccountSequence(account).toInt()
        val pageSize = getPageSize(historyCount)
        val histories = mutableListOf<PointTransaction>()
        for (i in 0 until pageSize) {
            histories += pointTransactionRepository.findBySourceAccountSequence(
                account,
                PageRequest.of(i, COUNT, Sort.by("id"))
            )
        }
        return histories
    }

    private fun findTargetHistories(account: AccountSequence): List<PointTransaction> {
        val historyCount = pointTransactionRepository.countByTargetAccountSequence(account).toInt()
        val pageSize = getPageSize(historyCount)
        val histories = mutableListOf<PointTransaction>()
        for (i in 0 until pageSize) {
            histories += pointTransactionRepository.findByTargetAccountSequence(
                account,
                PageRequest.of(i, COUNT, Sort.by("id"))
            )
        }
        return histories
    }

    private fun getPageSize(historyCount: Int): Int {
        val pageSize = if (historyCount % COUNT == 0) {
            historyCount / COUNT
        } else {
            historyCount / COUNT + 1
        }
        return pageSize
    }
}
