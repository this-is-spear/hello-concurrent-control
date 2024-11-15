package tis.hello_concurrent_control.ui.internal

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import tis.hello_concurrent_control.application.HistoryUseCase
import tis.hello_concurrent_control.application.TransactionUseCase
import tis.hello_concurrent_control.domain.AccountSequence
import tis.hello_concurrent_control.domain.Point
import tis.hello_concurrent_control.application.PointTransactionResponse

@RestController
class TransactionController(
    private val transactionUseCase: TransactionUseCase,
    private val historyUseCase: HistoryUseCase,
) {
    @PostMapping("/transaction")
    fun transaction(@RequestBody transactionRequest: TransactionRequest): Mono<PointTransactionResponse> {
        require(transactionRequest.sourceAccount != transactionRequest.targetAccount) { "출발지와 목적지가 같을 수 없습니다." }
        require(transactionRequest.amount > 0) { "금액은 0보다 커야 합니다." }
        val sourceAccount = AccountSequence(transactionRequest.sourceAccount)
        val targetAccount = AccountSequence(transactionRequest.targetAccount)
        val amount = Point(transactionRequest.amount)
        return transactionUseCase.transaction(sourceAccount, targetAccount, amount)
    }

    @GetMapping("/transaction/{account}")
    fun findAccountHistories(@PathVariable account: String): PointHistoriesResponse {
        val accountHistories = historyUseCase.findAccountHistories(AccountSequence(account))
        return PointHistoriesResponse(
            accountHistories.account.sequence,
            accountHistories.histories.map { history ->
                PointHistoryResponse(
                    history.sourceAccountSequence.sequence,
                    history.targetAccountSequence.sequence,
                    history.amount.amount,
                )
            },
            accountHistories.balance.amount,
        )
    }
}
