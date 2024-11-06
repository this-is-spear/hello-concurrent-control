package tis.hello_concurrent_control.application

import org.springframework.stereotype.Service
import tis.hello_concurrent_control.application.internal.AggregateHistoryService
import tis.hello_concurrent_control.application.internal.IssuerService
import tis.hello_concurrent_control.application.internal.TransactionService
import tis.hello_concurrent_control.concurrent.PointTransactionLock
import tis.hello_concurrent_control.domain.AccountSequence
import tis.hello_concurrent_control.domain.Point
import tis.hello_concurrent_control.domain.PointTransaction

@Service
class TransactionUseCase(
    private val transactionService: TransactionService,
    private val aggregateHistoryService: AggregateHistoryService,
    private val issuerService: IssuerService,
) {
    /**
     * 전송하는 계좌와 받는 계좌를 입력받아 금액을 전송한다.
     * 1. 출발지는 계좌에 잔액이 존재해야 한다.
     * 2. 출발지는 계좌가 발급하는 계좌라면 잔액 조회를 진행하지 않는다.
     * 3. 출발지는 계좌의 잔액이 전송 금액보다 많아야 한다.
     * 4. 목적지는 잔액 조회를 하지 않는다.
     *
     * @param sourceAccount 전송하는 계좌
     * @param targetAccount 받는 계좌
     */
    @PointTransactionLock(source = "#sourceAccount.sequence", target = "#targetAccount.sequence")
    fun transaction(sourceAccount: AccountSequence, targetAccount: AccountSequence, amount: Point) {
        if (!issuerService.isIssuer(sourceAccount)) {
            val pointHistories = aggregateHistoryService.findAccountHistories(sourceAccount)
            if (pointHistories.balance < amount) {
                throw IllegalArgumentException("잔액이 부족합니다.")
            }
        }

        val pointTransaction = PointTransaction(sourceAccount, targetAccount, amount)
        transactionService.saveTransaction(pointTransaction)
    }
}
