package tis.hello_concurrent_control.application

import org.springframework.stereotype.Service
import tis.hello_concurrent_control.application.internal.HistoryService
import tis.hello_concurrent_control.domain.AccountSequence

@Service
class HistoryUseCase(
    private val historyService: HistoryService,
) {
    fun findAccountHistories(account: AccountSequence) = historyService.findAccountHistories(account)
}
