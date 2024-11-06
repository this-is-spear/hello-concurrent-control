package tis.hello_concurrent_control.application.internal

import org.springframework.stereotype.Service
import tis.hello_concurrent_control.domain.AccountSequence
import tis.hello_concurrent_control.domain.Issuer

@Service
class IssuerService {
    val pointIssuers = listOf(
        Issuer(AccountSequence("1234")),
        Issuer(AccountSequence("2345")),
    )

    fun isPointIssuer(accountSequence: AccountSequence): Boolean {
        return pointIssuers.map { it.accountSequence }
            .contains(accountSequence)
    }
}
