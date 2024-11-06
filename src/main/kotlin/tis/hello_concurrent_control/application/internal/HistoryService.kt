package tis.hello_concurrent_control.application.internal

import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import tis.hello_concurrent_control.domain.AccountSequence
import tis.hello_concurrent_control.repository.PointTransactionRepository

@Service
class HistoryService(
    private val pointTransactionRepository: PointTransactionRepository,
) {
    @Cacheable(
        value = ["sourceAccountSequence"],
        key = "#accountSequence.sequence+#pageable.pageNumber+#pageable.pageSize",
        condition = "#result?.size() == #pageable.pageSize"
    )
    fun findSourceAccountSequence(accountSequence: AccountSequence, pageable: Pageable) =
        pointTransactionRepository.findBySourceAccountSequence(accountSequence, pageable)

    @Cacheable(
        value = ["targetAccountSequence"],
        key = "#accountSequence.sequence+#pageable.pageNumber+#pageable.pageSize",
        unless = "#result?.size() == #pageable.pageSize"
    )
    fun findTargetAccountSequence(accountSequence: AccountSequence, pageable: Pageable) =
        pointTransactionRepository.findByTargetAccountSequence(accountSequence, pageable)

    fun getSourceAccountPageSize(accountSequence: AccountSequence, pageSize: Int): Int {
        val countHistory = pointTransactionRepository.countBySourceAccountSequence(accountSequence)
        return getPageSize(countHistory.toInt(), pageSize)
    }

    fun getTargetAccountPageSize(accountSequence: AccountSequence, pageSize: Int): Int {
        val countHistory = pointTransactionRepository.countByTargetAccountSequence(accountSequence)
        return getPageSize(countHistory.toInt(), pageSize)
    }

    private fun getPageSize(historyCount: Int, pageSize: Int) = if (historyCount % pageSize == 0) {
        historyCount / pageSize
    } else {
        historyCount / pageSize + 1
    }
}
