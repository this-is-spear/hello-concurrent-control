package tis.hello_concurrent_control.application.internal

import org.springframework.stereotype.Service
import tis.hello_concurrent_control.domain.AccountSequence
import tis.hello_concurrent_control.domain.Issuer

@Service
class IssuerService {
    fun isIssuer(accountSequence: AccountSequence): Boolean {
        return listOf(Issuer(AccountSequence("1234")))
            .map { it.accountSequence }
            .contains(accountSequence)
    }
}
