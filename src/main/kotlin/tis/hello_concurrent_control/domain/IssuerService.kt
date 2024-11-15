package tis.hello_concurrent_control.domain

import org.springframework.stereotype.Service
import tis.hello_concurrent_control.repository.PointTransactionRepository

@Service
class IssuerService(
    private val pointTransactionRepository: PointTransactionRepository,
) {
    val pointIssuers = listOf(
        Issuer(AccountSequence("1234")),
        Issuer(AccountSequence("2345")),
    )

    fun isPointIssuer(accountSequence: AccountSequence): Boolean {
        return pointIssuers.map { it.accountSequence }
            .contains(accountSequence)
    }

    fun issue(sourceAccount: AccountSequence, targetAccount: AccountSequence, amount: Point) {
        val pointTransaction = PointTransaction(sourceAccount, targetAccount, amount)
        pointTransactionRepository.save(pointTransaction)
    }
}
