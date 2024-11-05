package tis.hello_concurrent_control.ui.internal

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import tis.hello_concurrent_control.application.HistoryUseCase
import tis.hello_concurrent_control.application.TransactionUseCase
import tis.hello_concurrent_control.domain.AccountSequence
import tis.hello_concurrent_control.domain.Point

@RestController
class TransactionController(
    private val transactionUseCase: TransactionUseCase,
    private val historyUseCase: HistoryUseCase,
) {
    @PostMapping("/transaction")
    fun transaction(@RequestBody transactionRequest: TransactionRequest) {
        val sourceAccount = AccountSequence(transactionRequest.sourceAccount)
        val targetAccount = AccountSequence(transactionRequest.targetAccount)
        val amount = Point(transactionRequest.amount)
        transactionUseCase.transaction(sourceAccount, targetAccount, amount)
    }

    @GetMapping("/transaction/{account}")
    fun findAccountHistories(@PathVariable account: String): PointHistoriesResponse {
        val accountHistories = historyUseCase.findAccountHistories(AccountSequence(account))
        return PointHistoriesResponse(
            accountHistories.account.sequence,
            accountHistories.histories.map { history ->
                PointTransactionResponse(
                    history.sourceAccountSequence.sequence,
                    history.targetAccountSequence.sequence,
                    history.amount.amount,
                )
            },
            accountHistories.balance.amount,
        )
    }
}
