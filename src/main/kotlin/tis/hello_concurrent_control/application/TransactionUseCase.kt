package tis.hello_concurrent_control.application

import org.springframework.stereotype.Service
import tis.hello_concurrent_control.application.internal.IssuerService
import tis.hello_concurrent_control.application.internal.ProduceService
import tis.hello_concurrent_control.domain.AccountSequence
import tis.hello_concurrent_control.domain.Point

@Service
class TransactionUseCase(
    private val produceService: ProduceService,
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
    fun transaction(sourceAccount: AccountSequence, targetAccount: AccountSequence, amount: Point) {
        require(!issuerService.isPointIssuer(sourceAccount) || !issuerService.isPointIssuer(targetAccount)) { "출발지와 목적지는 모두 발급 계좌일 수 없습니다." }
        produceService.produceTransactionRequest(sourceAccount, targetAccount, amount)
    }
}
