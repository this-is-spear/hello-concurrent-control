package tis.hello_concurrent_control.application

import org.springframework.stereotype.Service
import tis.hello_concurrent_control.application.internal.AggregateHistoryService
import tis.hello_concurrent_control.domain.AccountSequence

@Service
class HistoryUseCase(
    private val aggregateHistoryService: AggregateHistoryService,
) {
    fun findAccountHistories(account: AccountSequence) = aggregateHistoryService.findAccountHistories(account)
}
