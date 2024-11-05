package tis.hello_concurrent_control.application

import tis.hello_concurrent_control.application.internal.HistoryService
import org.springframework.stereotype.Service
import tis.hello_concurrent_control.domain.AccountSequence

@Service
class HistoryUseCase(
    private val historyService: HistoryService,
) {
    fun findAccountHistories(account: AccountSequence) = historyService.findAccountHistories(account)
}
