package tis.hello_concurrent_control.ui.internal

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import tis.hello_concurrent_control.application.HistoryUseCase
import tis.hello_concurrent_control.application.PointTransactionResponse
import tis.hello_concurrent_control.application.TransactionUseCase
import tis.hello_concurrent_control.domain.AccountSequence
import tis.hello_concurrent_control.domain.Point

@RestController
class TransactionController(
    private val transactionUseCase: TransactionUseCase,
    private val historyUseCase: HistoryUseCase,
) {
    @PostMapping("/transaction")
    fun transaction(@RequestBody transactionRequest: TransactionRequest): Mono<PointTransactionResponse> {
        return Mono.just(transactionRequest)
            .filter {
                require(it.sourceAccount != it.targetAccount) { "출발지와 목적지가 같을 수 없습니다." }
                require(it.amount > 0) { "금액은 0보다 커야 합니다." }
                true
            }.flatMap {
                transactionUseCase.transaction(
                    AccountSequence(it.sourceAccount),
                    AccountSequence(it.targetAccount),
                    Point(it.amount)
                )
            }
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
