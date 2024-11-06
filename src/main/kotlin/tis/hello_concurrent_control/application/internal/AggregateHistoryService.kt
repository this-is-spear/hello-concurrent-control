package tis.hello_concurrent_control.application.internal

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tis.hello_concurrent_control.domain.AccountSequence
import tis.hello_concurrent_control.domain.PointHistories
import tis.hello_concurrent_control.domain.PointTransaction

private const val COUNT = 2

@Service
class AggregateHistoryService(
    private val historyService: HistoryService,
) {
    @Transactional(readOnly = true)
    fun findAccountHistories(account: AccountSequence): PointHistories {
        val histories = findSourceHistories(account) + findTargetHistories(account)
        return PointHistories(account, histories.sortedBy { it.createdAt })
    }

    private fun findSourceHistories(account: AccountSequence): List<PointTransaction> {
        val pageSize = historyService.getSourceAccountPageSize(account, COUNT)
        val histories = mutableListOf<PointTransaction>()
        for (i in 0 until pageSize) {
            histories += historyService.findSourceAccountSequence(
                account,
                PageRequest.of(i, COUNT, Sort.by("id"))
            )
        }
        return histories
    }

    private fun findTargetHistories(account: AccountSequence): List<PointTransaction> {
        val pageSize = historyService.getTargetAccountPageSize(account, COUNT)
        val histories = mutableListOf<PointTransaction>()
        for (i in 0 until pageSize) {
            histories += historyService.findTargetAccountSequence(
                account,
                PageRequest.of(i, COUNT, Sort.by("id"))
            )
        }
        return histories
    }
}
